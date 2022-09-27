package com.coursenet.springbasic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfiguration {
	@Bean
	public KeyConfig setupKey() {
		return new KeyConfig();
	}
}
