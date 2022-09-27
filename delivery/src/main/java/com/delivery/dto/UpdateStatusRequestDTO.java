package com.delivery.dto;

import lombok.Data;

@Data
public class UpdateStatusRequestDTO {
  private Long orderId;
  private String invoice;
  private String status;
}
