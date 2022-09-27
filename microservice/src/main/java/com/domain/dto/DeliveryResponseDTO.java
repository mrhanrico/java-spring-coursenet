package com.domain.dto;

import java.time.LocalDateTime;

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

}
