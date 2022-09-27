package com.coursenet.springbasic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.coursenet.springbasic.controller.OrderController;
import com.coursenet.springbasic.dto.OrderRequestDTO;
import com.coursenet.springbasic.dto.OrderResponseDTO;
import com.coursenet.springbasic.entity.Orders;
import com.coursenet.springbasic.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	public ResponseEntity<OrderResponseDTO> createOrder(OrderRequestDTO orderRequest) {
		// 1. Ngambil dari request DTO
		Orders order = new Orders();

		// 2. Mapping ke ORM
		order.setGoodsName(orderRequest.getGoodsName());
		order.setReceiverName(orderRequest.getReceiverName());
		order.setReceiverAddress(orderRequest.getReceiverAddress());

		// 3. Save
		order = orderRepository.save(order);

		OrderResponseDTO orderResponse = new OrderResponseDTO(order);
		
		log.info("Create Order Controller Finished: OrderRequest "+orderRequest+"OrderResponse"+ orderResponse);
		return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
	}

	public ResponseEntity<List<OrderResponseDTO>> getOrder(Long id) {
		// Get All
		List<OrderResponseDTO> listOrderResponseDTO = new ArrayList<>();

		if (id == null) {
			List<Orders> listOrders = orderRepository.findAll();

			for (int i = 0; i < listOrders.size(); i++) {
				OrderResponseDTO responseDTO = new OrderResponseDTO(listOrders.get(i));
				listOrderResponseDTO.add(responseDTO);
			}

			return new ResponseEntity<>(listOrderResponseDTO, HttpStatus.OK);
		}

		// Get by ID
		Optional<Orders> order = orderRepository.findById(id);

		// Cek Data Jika ID tidak ada di Database
		if (!order.isPresent()) {
			return new ResponseEntity<>(listOrderResponseDTO, HttpStatus.NOT_FOUND);
		}

		OrderResponseDTO responseDTO = new OrderResponseDTO(order.get());
		listOrderResponseDTO.add(responseDTO);

		return new ResponseEntity<>(listOrderResponseDTO, HttpStatus.OK);

	}

	public ResponseEntity<OrderResponseDTO> putOrder(Long id, OrderRequestDTO orderRequest) {
		// Cari ID
		Optional<Orders> order = orderRepository.findById(id);

		// Apabila datanya tidak ada maka create baru
		if (!order.isPresent()) {
			Orders newOrder = new Orders();
			newOrder.setGoodsName(orderRequest.getGoodsName());
			newOrder.setReceiverName(orderRequest.getReceiverName());
			newOrder.setReceiverAddress(orderRequest.getReceiverAddress());
			newOrder = orderRepository.save(newOrder);
			OrderResponseDTO orderResponse = new OrderResponseDTO(newOrder);

			return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
		}

		// Tapi jika ada maka edit yang sudah ada
		Orders newOrder = order.get();
		newOrder.setGoodsName(orderRequest.getGoodsName());
		newOrder.setReceiverName(orderRequest.getReceiverName());
		newOrder.setReceiverAddress(orderRequest.getReceiverAddress());

		newOrder = orderRepository.save(newOrder);
		OrderResponseDTO orderResponse = new OrderResponseDTO(newOrder);

		return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);

	}

	public ResponseEntity<OrderResponseDTO> patchOrderReceiverAddress(Long id, String receiverAddress) {
		// Cari ID
		Optional<Orders> order = orderRepository.findById(id);

		OrderResponseDTO orderResponse;

		// Apabila datanya tidak ada
		if (!order.isPresent()) {
			orderResponse = new OrderResponseDTO();
			return new ResponseEntity<>(orderResponse, HttpStatus.NOT_FOUND);
		}

		Orders newOrder = order.get();
		newOrder.setReceiverAddress(receiverAddress);
		newOrder = orderRepository.save(newOrder);

		orderResponse = new OrderResponseDTO(newOrder);

		return new ResponseEntity<>(orderResponse, HttpStatus.OK);

	}

	public ResponseEntity<OrderResponseDTO> deleteOrder(Long id) {
		// Cari ID
		Optional<Orders> order = orderRepository.findById(id);

		// Apabila datanya tidak ada
		if (!order.isPresent()) {
			return new ResponseEntity<>(new OrderResponseDTO(), HttpStatus.NOT_FOUND);
		}

		orderRepository.deleteById(id);
		return new ResponseEntity<>(new OrderResponseDTO(), HttpStatus.NO_CONTENT);
	}

	public ResponseEntity<OrderResponseDTO> getOrderByGoodsName(String goodsName) {
		Optional<Orders> order = orderRepository.findByGoodsName(goodsName);

		// Apabila datanya tidak ada
		if (!order.isPresent()) {
			return new ResponseEntity<>(new OrderResponseDTO(), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(new OrderResponseDTO(order.get()), HttpStatus.OK);
	}

}
