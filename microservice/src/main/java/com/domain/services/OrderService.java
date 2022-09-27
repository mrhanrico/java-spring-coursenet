package com.domain.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.domain.client.DeliveryServiceClient;
import com.domain.constants.OrderStatusConstant;
import com.domain.dto.DeliveryRequestDTO;
import com.domain.dto.OrderRequestDTO;
import com.domain.dto.OrderResponseDTO;
import com.domain.dto.OrderStatusRequestDTO;
import com.domain.models.entities.OrderEntity;
import com.domain.models.repositories.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {
  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private DeliveryServiceClient deliveryServiceClient;

  public ResponseEntity<OrderResponseDTO> createOrder(OrderRequestDTO orderRequest) {
    DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
    String stringDate = dateFormat.format(new Date());

    // 1. Ngambil dari request DTO
    OrderEntity order = new OrderEntity();

    // 2. Mapping ke ORM
    order.setGoodsName(orderRequest.getGoodsName());
    order.setReceiverName(orderRequest.getReceiverName());
    order.setReceiverAddress(orderRequest.getReceiverAddress());
    order.setInvoice("INV/" + stringDate);
    order.setStatus(OrderStatusConstant.ORDER_CREATED);
    order.setShipperId(orderRequest.getShipperId());

    // 3. Save
    order = orderRepository.save(order);

    OrderResponseDTO orderResponse = new OrderResponseDTO(order);

    log.info("Create Order Controller Finished: OrderRequest " + orderRequest + "OrderResponse" + orderResponse);
    return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
  }

  public ResponseEntity<List<OrderResponseDTO>> getOrder(Long id) {
    // Get All
    List<OrderResponseDTO> listOrderResponseDTO = new ArrayList<>();

    if (id == null) {
      List<OrderEntity> listOrders = orderRepository.findAll();

      for (int i = 0; i < listOrders.size(); i++) {
        OrderResponseDTO responseDTO = new OrderResponseDTO(listOrders.get(i));
        listOrderResponseDTO.add(responseDTO);
      }

      return new ResponseEntity<>(listOrderResponseDTO, HttpStatus.OK);
    }

    // Get by ID
    Optional<OrderEntity> order = orderRepository.findById(id);

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
    Optional<OrderEntity> order = orderRepository.findById(id);

    // Apabila datanya tidak ada maka create baru
    if (!order.isPresent()) {
      OrderEntity newOrder = new OrderEntity();
      newOrder.setGoodsName(orderRequest.getGoodsName());
      newOrder.setReceiverName(orderRequest.getReceiverName());
      newOrder.setReceiverAddress(orderRequest.getReceiverAddress());
      newOrder = orderRepository.save(newOrder);
      OrderResponseDTO orderResponse = new OrderResponseDTO(newOrder);

      return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    // Tapi jika ada maka edit yang sudah ada
    OrderEntity newOrder = order.get();
    newOrder.setGoodsName(orderRequest.getGoodsName());
    newOrder.setReceiverName(orderRequest.getReceiverName());
    newOrder.setReceiverAddress(orderRequest.getReceiverAddress());

    newOrder = orderRepository.save(newOrder);
    OrderResponseDTO orderResponse = new OrderResponseDTO(newOrder);

    return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);

  }

  public ResponseEntity<OrderResponseDTO> patchOrderReceiverAddress(Long id, String receiverAddress) {
    // Cari ID
    Optional<OrderEntity> order = orderRepository.findById(id);

    OrderResponseDTO orderResponse;

    // Apabila datanya tidak ada
    if (!order.isPresent()) {
      orderResponse = new OrderResponseDTO();
      return new ResponseEntity<>(orderResponse, HttpStatus.NOT_FOUND);
    }

    OrderEntity newOrder = order.get();
    newOrder.setReceiverAddress(receiverAddress);
    newOrder = orderRepository.save(newOrder);

    orderResponse = new OrderResponseDTO(newOrder);

    return new ResponseEntity<>(orderResponse, HttpStatus.OK);

  }

  public ResponseEntity<OrderResponseDTO> deleteOrder(Long id) {
    // Cari ID
    Optional<OrderEntity> order = orderRepository.findById(id);

    // Apabila datanya tidak ada
    if (!order.isPresent()) {
      return new ResponseEntity<>(new OrderResponseDTO(), HttpStatus.NOT_FOUND);
    }

    orderRepository.deleteById(id);
    return new ResponseEntity<>(new OrderResponseDTO(), HttpStatus.NO_CONTENT);
  }

  public ResponseEntity<OrderResponseDTO> getOrderByGoodsName(String goodsName) {
    Optional<OrderEntity> order = orderRepository.findByGoodsName(goodsName);

    // Apabila datanya tidak ada
    if (!order.isPresent()) {
      return new ResponseEntity<>(new OrderResponseDTO(), HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(new OrderResponseDTO(order.get()), HttpStatus.OK);
  }

  public ResponseEntity<OrderResponseDTO> updateOrderStatus(OrderStatusRequestDTO orderStatusRequestDTO) {
    // Find Dulu Order yang ada
    Optional<OrderEntity> order = orderRepository.findById(orderStatusRequestDTO.getId());
    if (!order.isPresent()) {
      return new ResponseEntity<>(new OrderResponseDTO(), HttpStatus.NOT_FOUND);
    }

    // Update Status
    // Ada 3 Status
    OrderResponseDTO orderResponseDTO;
    switch (orderStatusRequestDTO.getStatus()) {
      case OrderStatusConstant.ORDER_INPROCESS:
        orderResponseDTO = processOrder(order.get());
        break;
      case OrderStatusConstant.ORDER_FINISH:
        orderResponseDTO = finishOrder(order.get());
        break;
      case OrderStatusConstant.ORDER_CANCELED:
        orderResponseDTO = cancelOrder(order.get());
        break;
      default:
        orderResponseDTO = null;
        break;
    }

    // Jika data Null
    if (orderResponseDTO == null) {
      return new ResponseEntity<>(new OrderResponseDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(orderResponseDTO, HttpStatus.OK);

  }

  private OrderResponseDTO cancelOrder(OrderEntity orderEntity) {
    // Bisa dua arah

    // Update Status Order
    orderEntity.setStatus(OrderStatusConstant.ORDER_CANCELED);
    orderEntity = orderRepository.save(orderEntity);
    return new OrderResponseDTO(orderEntity);
  }

  private OrderResponseDTO finishOrder(OrderEntity orderEntity) {
    // Di hit Delivery Service

    // Update Status Order
    orderEntity.setStatus(OrderStatusConstant.ORDER_FINISH);
    orderEntity = orderRepository.save(orderEntity);
    return new OrderResponseDTO(orderEntity);
  }

  private OrderResponseDTO processOrder(OrderEntity orderEntity) {
    // Hit Delivery Service
    DeliveryRequestDTO deliveryRequestDTO = new DeliveryRequestDTO();
    deliveryRequestDTO.setOrderId(orderEntity.getId());
    deliveryRequestDTO.setInvoice(orderEntity.getInvoice());
    deliveryRequestDTO.setReceiverAddress(orderEntity.getReceiverAddress());
    deliveryRequestDTO.setShipperId(orderEntity.getShipperId());

    deliveryServiceClient.createDelivery(deliveryRequestDTO);

    // Update Status Order
    orderEntity.setStatus(OrderStatusConstant.ORDER_INPROCESS);
    orderEntity = orderRepository.save(orderEntity);
    return new OrderResponseDTO(orderEntity);
  }

}
