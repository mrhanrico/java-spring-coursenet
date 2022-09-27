package com.delivery.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.delivery.dto.GojekRequestDTO;
import com.delivery.dto.GojekResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GojekClient implements ShipperClient {
  private RestTemplate restTemplate = new RestTemplate();

  @Value("${gojek.base.url}")
  private String gojekBaseUrl;

  @Value("${gojek.requestPickup.endpoint}")
  private String requestPickupEndpoint;

  @Override
  public void requestPickup(ShipperRequestDTO shipperRequestDTO) {
    log.info("DeliveryServiceClient Started, Request: " + shipperRequestDTO.toString());

    GojekRequestDTO gojekRequestDTO = new GojekRequestDTO();
    gojekRequestDTO.setInvoice(shipperRequestDTO.getInvoice());

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<GojekRequestDTO> request = new HttpEntity<>(gojekRequestDTO, httpHeaders);
    try {
      restTemplate.postForObject(String.format("%s%s", gojekBaseUrl, requestPickupEndpoint), request,
          GojekResponseDTO.class);
    } catch (Exception e) {
      throw e;
    }

    log.info("Gojek Service Client Finished, Request: " + gojekRequestDTO.toString());
  }
}
