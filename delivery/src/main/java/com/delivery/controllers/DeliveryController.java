package com.delivery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.dto.DeliveryRequestDTO;
import com.delivery.dto.DeliveryResponseDTO;
import com.delivery.dto.UpdateStatusRequestDTO;
import com.delivery.services.DeliveryServices;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DeliveryController {
  @Autowired
  private DeliveryServices deliveryServices;

  @PostMapping("/deliveries")
  public ResponseEntity<DeliveryResponseDTO> createDelivery(@RequestBody DeliveryRequestDTO deliveryRequest) {
    log.info("Create Delivery Controller Started: Deliver Request DTO" + deliveryRequest.toString());
    return deliveryServices.createDelivery(deliveryRequest);
  }

  @PostMapping("/deliveries/update-status")
  public ResponseEntity<DeliveryResponseDTO> updateDeliveryStatus(@RequestBody UpdateStatusRequestDTO updateStatus) {
    log.info("Create Delivery Controller Started: Deliver Request DTO" + updateStatus.toString());
    return deliveryServices.updateDeliveryStatus(updateStatus);
  }

}
