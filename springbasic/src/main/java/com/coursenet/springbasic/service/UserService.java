package com.coursenet.springbasic.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.coursenet.springbasic.constants.TokenConstants;
import com.coursenet.springbasic.dto.LoginUserResponseDTO;
import com.coursenet.springbasic.dto.UserResponseDTO;
import com.coursenet.springbasic.dto.UserRequestDTO;
import com.coursenet.springbasic.entity.Users;
import com.coursenet.springbasic.repository.UserRepository;
import com.coursenet.springbasic.security.JWTUtil;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JWTUtil jwtUtil;

	@Value("${salt.hash.password}")
	private String salt;

	public ResponseEntity<UserResponseDTO> userRegistration(UserRequestDTO userRequestDTO) {
		Users user = new Users();
		user.setUserName(userRequestDTO.getUserName());
		user.setPassword(hashPassword(userRequestDTO.getPassword()));

		user = userRepository.save(user);

		UserResponseDTO userResponseDTO = new UserResponseDTO();
		userResponseDTO.setUserName(user.getUserName());

		return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
	}

	private String hashPassword(String password) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	public ResponseEntity<LoginUserResponseDTO> userLogin(@Valid UserRequestDTO userRequestDTO) {

		Optional<Users> user = userRepository.findByUserNameAndPassword(userRequestDTO.getUserName(),
				hashPassword(userRequestDTO.getPassword()));

		if (!user.isPresent()) {
			return new ResponseEntity<>(new LoginUserResponseDTO(), HttpStatus.FORBIDDEN);
		}

		String accessToken = jwtUtil.generatedJWTToken(user.get().getUserName(), TokenConstants.TOKEN_ACCESS);
		String refreshToken = jwtUtil.generatedJWTToken(user.get().getUserName(), TokenConstants.TOKEN_REFRESH);

		LoginUserResponseDTO loginUserResponseDTO = new LoginUserResponseDTO();
		loginUserResponseDTO.setAccessToken(accessToken);
		loginUserResponseDTO.setRefreshToken(refreshToken);

		return new ResponseEntity<>(loginUserResponseDTO, HttpStatus.OK);
	}

	public ResponseEntity<LoginUserResponseDTO> refreshToken(String token) {
		DecodedJWT decodedJWT = jwtUtil.decodeJWTToken(token);
		if (decodedJWT.getClaim("type").asString().equals(TokenConstants.TOKEN_REFRESH)) {
			String accessToken = jwtUtil.generatedJWTToken(decodedJWT.getSubject(), TokenConstants.TOKEN_ACCESS);
			String refreshToken = token.replace("Bearer ", "");
			
			LoginUserResponseDTO loginUserResponseDTO = new LoginUserResponseDTO();
			loginUserResponseDTO.setAccessToken(accessToken);
			loginUserResponseDTO.setRefreshToken(refreshToken);
			
			return new ResponseEntity<>(loginUserResponseDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(new LoginUserResponseDTO(), HttpStatus.FORBIDDEN);
	}

}
