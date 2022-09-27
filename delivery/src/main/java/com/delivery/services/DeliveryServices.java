package com.delivery.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.delivery.clients.ShipperClient;
import com.delivery.clients.ShipperRequestDTO;
import com.delivery.constants.DeliveryStatusConstants;
import com.delivery.dto.DeliveryRequestDTO;
import com.delivery.dto.DeliveryResponseDTO;
import com.delivery.dto.UpdateStatusRequestDTO;
import com.delivery.models.entities.DeliveryEntity;
import com.delivery.models.repositories.DeliveryRepository;
import com.delivery.utilities.ShipperMapperUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeliveryServices {
  @Autowired
  private DeliveryRepository deliveryRepository;

  @Autowired
  private ShipperMapperUtility shipperMapperUtility;

  public ResponseEntity<DeliveryResponseDTO> createDelivery(DeliveryRequestDTO deliveryRequest) {
    DeliveryEntity deliveryEntity = new DeliveryEntity();
    deliveryEntity.setOrderId(deliveryRequest.getOrderId());
    deliveryEntity.setInvoice(deliveryRequest.getInvoice());
    deliveryEntity.setReceiverAddress(deliveryRequest.getReceiverAddress());
    deliveryEntity.setShipperId(deliveryRequest.getShipperId());

    deliveryEntity.setStatus(DeliveryStatusConstants.DELIVERY_CREATED);

    deliveryEntity = deliveryRepository.save(deliveryEntity);

    DeliveryResponseDTO deliveryResponse = new DeliveryResponseDTO(deliveryEntity);

    log.info("Create Delivery Controller Finished: Delivery Request " + deliveryRequest.toString()
        + " Delivery Response " + deliveryResponse.toString());

    return new ResponseEntity<>(deliveryResponse, HttpStatus.CREATED);

  }

  public ResponseEntity<DeliveryResponseDTO> updateDeliveryStatus(UpdateStatusRequestDTO updateStatus) {
    Optional<DeliveryEntity> delivery = deliveryRepository.findByOrderId(updateStatus.getOrderId());

    if (!delivery.isPresent()) {
      delivery = deliveryRepository.findByInvoice(updateStatus.getInvoice());

      if (!delivery.isPresent()) {
        return new ResponseEntity<>(new DeliveryResponseDTO(), HttpStatus.NOT_FOUND);
      }
    }

    DeliveryResponseDTO deliveryResponseDTO;
    switch (updateStatus.getStatus()) {
      case DeliveryStatusConstants.DELIVERY_IN_DELIVERY:
        deliveryResponseDTO = inDelivery(delivery.get());
        break;

      // case DeliveryStatusConstants.DELIVERY_DELIVERED:
      // deliveryResponseDTO = delivered(delivery.get());
      // break;

      // case DeliveryStatusConstants.DELIVERY_CANCELED:
      // deliveryResponseDTO = cancelDelivery(delivery.get());
      // break;

      default:
        deliveryResponseDTO = null;
        break;

    }

    if (deliveryResponseDTO == null) {
      return new ResponseEntity<>(new DeliveryResponseDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    log.info("Update Delivery Status Controller Finished: Delivery Status Request " + updateStatus.toString()
        + "Delivery Response " + deliveryResponseDTO.toString());
    return new ResponseEntity<>(deliveryResponseDTO, HttpStatus.OK);
  }

  private DeliveryResponseDTO inDelivery(DeliveryEntity deliveryEntity) {
    // Shipper Mapper
    ShipperClient shipperClient = shipperMapperUtility.mapShipperClient(deliveryEntity.getShipperId());

    shipperClient.requestPickup(ShipperRequestDTO.builder().invoice(deliveryEntity.getInvoice()).build());

    deliveryEntity.setStatus(DeliveryStatusConstants.DELIVERY_IN_DELIVERY);
    deliveryEntity = deliveryRepository.save(deliveryEntity);

    return new DeliveryResponseDTO(deliveryEntity);
  }

}
