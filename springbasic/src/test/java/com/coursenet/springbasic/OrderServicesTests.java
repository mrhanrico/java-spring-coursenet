package com.coursenet.springbasic;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.coursenet.springbasic.dto.OrderRequestDTO;
import com.coursenet.springbasic.dto.OrderResponseDTO;
import com.coursenet.springbasic.entity.Orders;
import com.coursenet.springbasic.repository.OrderRepository;
import com.coursenet.springbasic.service.OrderService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class OrderServicesTests {
	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrderRepository orderRepository;

	@Test
	void test_GetOrderServiceById_Success() throws Exception {
		// Arrange
		Orders mockOrder = new Orders();
		mockOrder.setId(1L);
		mockOrder.setGoodsName("Laptop Kantor");
		mockOrder.setReceiverName("Haris");
		mockOrder.setReceiverAddress("Tangsel");

		when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(mockOrder));

		// Act
		ResponseEntity<List<OrderResponseDTO>> actualResponse = orderService.getOrder(1L);

		// Assert
		assertEquals(mockOrder.getGoodsName(), actualResponse.getBody().get(0).getGoodsName());
		assertEquals(mockOrder.getReceiverName(), actualResponse.getBody().get(0).getReceiverName());
		assertEquals(mockOrder.getReceiverAddress(), actualResponse.getBody().get(0).getReceiverAddress());

	}

	@Test
	void test_GetOrderServiceById_NotFound() throws Exception {
		// Arrange
		when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

		// Act
		ResponseEntity<List<OrderResponseDTO>> actualResponse = orderService.getOrder(1L);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
	}

	@Test
	void test_GetOrderServiceAll_Success() throws Exception {
		// Arrange
		Orders mockOrder = new Orders();
		mockOrder.setId(1L);
		mockOrder.setGoodsName("Laptop Kantor");
		mockOrder.setReceiverName("Haris");
		mockOrder.setReceiverAddress("Tangsel");

		List<Orders> mockListOrder = new ArrayList();
		mockListOrder.add(mockOrder);

		when(orderRepository.findAll()).thenReturn(mockListOrder);

		// Act
		ResponseEntity<List<OrderResponseDTO>> actualResponse = orderService.getOrder(null);

		// Assert
		assertEquals(mockOrder.getGoodsName(), actualResponse.getBody().get(0).getGoodsName());
		assertEquals(mockOrder.getReceiverName(), actualResponse.getBody().get(0).getReceiverName());
		assertEquals(mockOrder.getReceiverAddress(), actualResponse.getBody().get(0).getReceiverAddress());
	}
	
	@Test
	void test_PostOrderService_Success() throws Exception {
		// Arrange
		Orders mockOrder = new Orders();
		mockOrder.setId(1L);
		mockOrder.setGoodsName("Laptop Kantor");
		mockOrder.setReceiverName("Haris");
		mockOrder.setReceiverAddress("Tangsel");
		
		OrderRequestDTO mockOrderRequest = new OrderRequestDTO();
		mockOrderRequest.setGoodsName("Laptop Kantor");
		mockOrderRequest.setReceiverName("Haris");
		mockOrderRequest.setReceiverAddress("Tangsel");

		when(orderRepository.save(Mockito.any())).thenReturn(mockOrder);

		// Act
		ResponseEntity<OrderResponseDTO> actualResponse = orderService.createOrder(mockOrderRequest);

		// Assert
		assertEquals(mockOrder.getGoodsName(), actualResponse.getBody().getGoodsName());
		assertEquals(mockOrder.getReceiverName(), actualResponse.getBody().getReceiverName());
		assertEquals(mockOrder.getReceiverAddress(), actualResponse.getBody().getReceiverAddress());
		assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());

	}
	
	@Test
	@Disabled
	void test_DeleteOrderService_Success() throws Exception {
		// Arrange
		Orders mockOrder = new Orders();
		mockOrder.setId(1L);
		mockOrder.setGoodsName("Laptop Kantor");
		mockOrder.setReceiverName("Haris");
		mockOrder.setReceiverAddress("Tangsel");

		when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(mockOrder));
//		when(orderRepository.deleteById(1L)).thenReturn(Optional.ofNullable(mockOrder));

		// Act
		ResponseEntity<OrderResponseDTO> actualResponse = orderService.deleteOrder(1L);

		// Assert
		assertEquals(HttpStatus.NO_CONTENT, actualResponse.getStatusCode());

	}
}
