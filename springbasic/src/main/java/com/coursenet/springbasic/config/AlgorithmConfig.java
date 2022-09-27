package com.coursenet.springbasic.config;

import org.springframework.stereotype.Component;

@Component
public class AlgorithmConfig {
	private String algorithmName;
	
	public AlgorithmConfig() {
		algorithmName = "SHA-1";
	}
	
	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}
	
}
