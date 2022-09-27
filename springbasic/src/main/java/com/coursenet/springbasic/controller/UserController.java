package com.coursenet.springbasic.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.coursenet.springbasic.dto.LoginUserResponseDTO;
import com.coursenet.springbasic.dto.UserResponseDTO;
import com.coursenet.springbasic.service.UserService;
import com.coursenet.springbasic.dto.UserRequestDTO;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/users/registration")
	public ResponseEntity<UserResponseDTO> userRegistration(@RequestBody @Valid UserRequestDTO userRequestDTO){
		return userService.userRegistration(userRequestDTO);
	}
	
	@PostMapping("/users/login")
	public ResponseEntity<LoginUserResponseDTO> userLogin(@RequestBody @Valid UserRequestDTO userRequestDTO){
		return userService.userLogin(userRequestDTO);
	}
	
	@PostMapping("/users/refresh-token")
	public ResponseEntity<LoginUserResponseDTO> refreshLogin(@RequestHeader("Authorization") String token) {
		return userService.refreshToken(token);
	}

}
