package com.domain.dto;

import lombok.Data;

@Data
public class DeliveryRequestDTO {
  private long orderId;
  private String receiverAddress;
  private String invoice;
  private int shipperId;
}
