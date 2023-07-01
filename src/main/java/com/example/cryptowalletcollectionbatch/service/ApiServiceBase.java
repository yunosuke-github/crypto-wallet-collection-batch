package com.example.cryptowalletcollectionbatch.service;

import com.example.cryptowalletcollectionbatch.dto.AssetHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public abstract class ApiServiceBase {

  private final RestTemplate restTemplate;

  @Autowired
  public ApiServiceBase(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public abstract List<AssetHistoryDto> get();

  public abstract Float getPrice(String assetName);

  public <RQ, RS> RS apiCall(HttpMethod method, String url, HttpHeaders headers, RQ requestClass, Class<RS> responseClass) {
    HttpEntity<RQ> request = new HttpEntity<>(requestClass, headers);
    ResponseEntity<RS> response = restTemplate.exchange(url, method, request, responseClass);
    return response.getBody();
  }

}
