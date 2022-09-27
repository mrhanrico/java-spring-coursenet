package com.domain.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.domain.dto.DeliveryRequestDTO;
import com.domain.dto.DeliveryResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DeliveryServiceClient {
  private RestTemplate restTemplate = new RestTemplate();

  @Value("${delivery.base.url}")
  private String deliveryBaseURL;

  @Value("${delivery.createDelivery.endpoint}")
  private String createDeliveryEndpoint;

  public void createDelivery(DeliveryRequestDTO deliveryRequestDTO) {
    log.info("DeliveryServiceClient Started, Request: " + deliveryRequestDTO.toString());

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<DeliveryRequestDTO> request = new HttpEntity<>(deliveryRequestDTO, httpHeaders);
    try {
      restTemplate.postForObject(String.format("%s%s", deliveryBaseURL, createDeliveryEndpoint), request,
          DeliveryResponseDTO.class);
    } catch (Exception e) {
      throw e;
    }

    log.info("DeliveryServiceClient Finished, Request: " + deliveryRequestDTO.toString());
  }
}
