package com.example.cryptowalletcollectionbatch.service;

import com.example.cryptowalletcollectionbatch.dto.AssetHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MoneyOrderApiService extends ApiServiceBase {

  @Value("${bybit.endpoint-url}")
  private String endpointUrl;

  @Autowired
  public MoneyOrderApiService(RestTemplate restTemplate) {
    super(restTemplate);
  }

  public List<AssetHistoryDto> get() {
    return null;
  }

  /**
   * https://excelapi.org/docs/currency/rate/
   *
   * @param assetName
   * @return
   */
  public Float getPrice(String assetName) {
    String url = endpointUrl + "/currency/rate?pair=" + assetName;
    String price =  apiCall(HttpMethod.GET, url, null, null, String.class);
    return Float.valueOf(price);
  }

}
