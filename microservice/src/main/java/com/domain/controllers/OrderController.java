package com.domain.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.domain.dto.OrderRequestDTO;
import com.domain.dto.OrderResponseDTO;
import com.domain.dto.OrderStatusRequestDTO;
import com.domain.services.OrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderController {
  @Autowired
  private OrderService orderService;

  @PostMapping("/orders")
  public ResponseEntity<OrderResponseDTO> createOrder(
      @Valid @RequestBody OrderRequestDTO orderRequest) {
    log.info("Create Order Controller Started: OrderRequestDTO" + orderRequest);
    return orderService.createOrder(orderRequest);
  }

  @GetMapping("/orders")
  public ResponseEntity<List<OrderResponseDTO>> getOrder(@RequestParam(value = "id", required = false) Long id) {
    return orderService.getOrder(id);
  }

  @GetMapping("/ordersByGoodsName")
  public ResponseEntity<OrderResponseDTO> getOrderByGoodsName(
      @RequestParam(value = "name", required = false) String name) {
    return orderService.getOrderByGoodsName(name);
  }

  @PutMapping("/orders/{id}")
  public ResponseEntity<OrderResponseDTO> putOrder(
      @PathVariable(value = "id") Long id,
      @RequestBody OrderRequestDTO orderRequest) {
    return orderService.putOrder(id, orderRequest);
  }

  @PatchMapping("/orders/{id}/{receiverAddress}")
  public ResponseEntity<OrderResponseDTO> patchOrderReceiverAddress(
      @PathVariable(value = "id") Long id,
      @PathVariable(value = "receiverAddress") String receiverAddress) {
    return orderService.patchOrderReceiverAddress(id, receiverAddress);
  }

  @DeleteMapping("/orders/{id}")
  public ResponseEntity<OrderResponseDTO> deleteOrder(@PathVariable(value = "id") Long id) {
    return orderService.deleteOrder(id);
  }

  @PostMapping("/orders/update-status")
  public ResponseEntity<OrderResponseDTO> updateStatusOrder(
      @Valid @RequestBody OrderStatusRequestDTO orderStatusRequestDTO) {
    log.info("Update Order Status Controller Started: OrderStatusRequestDTO " + orderStatusRequestDTO.toString());
    return orderService.updateOrderStatus(orderStatusRequestDTO);
  }
}
