package com.delivery.dto;

import java.time.LocalDateTime;

import com.delivery.models.entities.DeliveryEntity;

import lombok.Data;

@Data
public class DeliveryResponseDTO {
  private long id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String receiverAddress;
  private String invoice;
  private String status;
  private long orderId;
  private int shipperId;

  public DeliveryResponseDTO(DeliveryEntity delivery) {
    super();
    this.id = delivery.getId();
    this.createdAt = delivery.getCreatedAt();
    this.updatedAt = delivery.getUpdatedAt();
    this.receiverAddress = delivery.getReceiverAddress();
    this.invoice = delivery.getInvoice();
    this.status = delivery.getStatus();
    this.orderId = delivery.getOrderId();
    this.shipperId = delivery.getShipperId();
  }

  public DeliveryResponseDTO() {
    super();
  }

}
