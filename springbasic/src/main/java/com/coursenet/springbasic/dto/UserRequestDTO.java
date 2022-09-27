package com.coursenet.springbasic.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserRequestDTO {
	@NotNull
	private String userName;
	
	@NotNull
	private String password;
}
