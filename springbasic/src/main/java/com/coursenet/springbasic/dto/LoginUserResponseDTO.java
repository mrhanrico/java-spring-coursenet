package com.coursenet.springbasic.dto;

import lombok.Data;

@Data
public class LoginUserResponseDTO {
	private String accessToken;
	private String refreshToken;
}
