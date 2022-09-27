package com.delivery.clients;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShipperRequestDTO {
  private String invoice;
}
