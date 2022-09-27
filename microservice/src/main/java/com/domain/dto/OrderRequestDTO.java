package com.domain.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class OrderRequestDTO {
  @NotNull
  private String goodsName;

  @NotNull
  private String receiverName;

  @NotNull
  private String receiverAddress;

  @NotNull
  private Integer shipperId;
}
