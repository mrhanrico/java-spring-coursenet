package com.coursenet.springbasic.dto;

import lombok.Data;

@Data
public class OrderRequestDTO {
	private String goodsName;
	private String receiverName;
	private String receiverAddress;
}
