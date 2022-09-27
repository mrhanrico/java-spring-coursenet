package com.coursenet.springbasic.dto;

import java.time.LocalDateTime;

import com.coursenet.springbasic.entity.Orders;

import lombok.Data;

@Data
public class OrderResponseDTO {
	private long id;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String goodsName;
	private String receiverName;
	private String receiverAddress;
	
	public OrderResponseDTO(Orders order) {
		super();
		this.id = order.getId();
		this.createdAt = order.getCreatedAt();
		this.updatedAt = order.getUpdatedAt();
		this.goodsName = order.getGoodsName();
		this.receiverName = order.getReceiverName();
		this.receiverAddress = order.getReceiverAddress();
	}
	
	public OrderResponseDTO() {
		super();
	}
	
}
