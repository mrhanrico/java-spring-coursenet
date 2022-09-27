package com.delivery.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.delivery.dto.GrabRequestDTO;
import com.delivery.dto.GrabResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GrabClient implements ShipperClient {
  private RestTemplate restTemplate = new RestTemplate();

  @Value("${grab.base.url}")
  private String grabBaseUrl;

  @Value("${grab.requestPickup.endpoint}")
  private String requestPickupEndpoint;

  @Override
  public void requestPickup(ShipperRequestDTO shipperRequestDTO) {
    log.info("DeliveryServiceClient Started, Request: " + shipperRequestDTO.toString());

    // Convert from general Shipper Request DTO to Specific shipperDTO
    GrabRequestDTO grabRequestDTO = new GrabRequestDTO();
    grabRequestDTO.setInvoice(shipperRequestDTO.getInvoice());

    // Set HTTP Header
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);

    // Create Request
    HttpEntity<GrabRequestDTO> request = new HttpEntity<>(grabRequestDTO, httpHeaders);

    // Hit Grab Request pickup API
    try {
      restTemplate.postForObject(String.format("%s%s", grabBaseUrl, requestPickupEndpoint), request,
          GrabResponseDTO.class);
    } catch (Exception e) {
      throw e;
    }

    log.info("Grab Service Client Finished, Request: " + grabRequestDTO.toString());
  }
}
