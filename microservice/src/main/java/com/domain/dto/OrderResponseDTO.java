package com.domain.dto;

import java.time.LocalDateTime;

import com.domain.models.entities.OrderEntity;

import lombok.Data;

@Data
public class OrderResponseDTO {
  private long id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String goodsName;
  private String receiverName;
  private String receiverAddress;
  private String status;
  private int shipperId;
  private String invoice;

  public OrderResponseDTO(OrderEntity order) {
    super();
    this.id = order.getId();
    this.createdAt = order.getCreatedAt();
    this.updatedAt = order.getUpdatedAt();
    this.goodsName = order.getGoodsName();
    this.receiverName = order.getReceiverName();
    this.receiverAddress = order.getReceiverAddress();
    this.status = order.getStatus();
    this.shipperId = order.getShipperId();
    this.invoice = order.getInvoice();
  }

  public OrderResponseDTO() {
    super();
  }

}
