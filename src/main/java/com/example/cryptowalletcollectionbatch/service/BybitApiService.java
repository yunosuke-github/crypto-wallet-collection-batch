package com.example.cryptowalletcollectionbatch.service;

import com.example.cryptowalletcollectionbatch.dto.AssetHistoryDto;
import com.example.cryptowalletcollectionbatch.entity.bybit.BybitApiResponse;
import com.example.cryptowalletcollectionbatch.entity.bybit.WalletBalanceCoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.*;


@Service
public class BybitApiService extends ApiServiceBase {

  public static final String EXCHANGE_NAME = "BYBIT";
  private final String recvWindow = "5000";

  @Value("${bybit.endpoint-url}")
  private String endpointUrl;

  @Value("${bybit.api-version}")
  private String apiVersion;

  @Value("${bybit.api-key}")
  private String apiKey;

  @Value("${bybit.api-secret}")
  private String apiSecret;

  @Autowired
  MoneyOrderApiService moneyOrderApiService;

  @Autowired
  public BybitApiService(RestTemplate restTemplate) {
    super(restTemplate);
  }

  /**
   * https://bybit-exchange.github.io/docs/v5/account/wallet-balance
   *
   * @return
   */
  public List<AssetHistoryDto> get() {
    String path = "/unified/" + apiVersion + "/private/account/wallet/balance";
    String url = endpointUrl + path;
    HttpHeaders headers = makeHeader();
    BybitApiResponse response = apiCall(HttpMethod.GET, url, headers, null, BybitApiResponse.class);
    if (!response.getRetMsg().equals("Success")) {
      return null;
    }
    return makeAssetHistoryDto(response.getResult().getCoin());
  }

  public Float getPrice(String assetName) {
    String url = endpointUrl + "/spot/" + apiVersion + "/public/quote/ticker/price?symbol=" + assetName + "USDT";
    BybitApiResponse response = apiCall(HttpMethod.GET, url, null, null, BybitApiResponse.class);
    if (!response.getRetMsg().equals("OK")) {
      return Float.valueOf("0");
    }

    // TODO: Enumにする
    Float usdJpyPrice = moneyOrderApiService.getPrice("usd-jpy");
    return Float.valueOf(response.getResult().getPrice()) * usdJpyPrice;
  }

  private List<AssetHistoryDto> makeAssetHistoryDto(List<WalletBalanceCoin> coins) {
    List<AssetHistoryDto> assetHistoryDtos = new ArrayList<>();
    coins.stream().forEach(coin -> {
      AssetHistoryDto assetHistoryDto = new AssetHistoryDto();
      assetHistoryDto.setAssetName(coin.getCurrencyCoin());
      assetHistoryDto.setExchangeName(EXCHANGE_NAME);
      assetHistoryDto.setHoldingAmount(Float.valueOf(coin.getWalletBalance()));
      Float price;
      if (coin.getCurrencyCoin().equals("USDT") || coin.getCurrencyCoin().equals("USDC") || coin.getCurrencyCoin().equals("BUSD")) {
        price = Float.valueOf(coin.getWalletBalance()) * moneyOrderApiService.getPrice("usd-jpy");
      } else {
        price = getPrice(coin.getCurrencyCoin());
      }
      assetHistoryDto.setJpy(price * assetHistoryDto.getHoldingAmount());
      assetHistoryDtos.add(assetHistoryDto);
    });
    return assetHistoryDtos;
  }

  private HttpHeaders makeHeader() {
    String timestamp = Long.toString(ZonedDateTime.now().toInstant().toEpochMilli());
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-BAPI-API-KEY", apiKey);
    headers.add("X-BAPI-SIGN", genGetSign(timestamp));
    headers.add("X-BAPI-TIMESTAMP", timestamp);
    headers.add("X-BAPI-RECV-WINDOW", recvWindow);
    return headers;
  }

  private String genGetSign(String timestamp) {
    try {
      String queryStr = timestamp + apiKey + recvWindow;

      Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secret_key = new SecretKeySpec(apiSecret.getBytes(), "HmacSHA256");
      sha256_HMAC.init(secret_key);
      return bytesToHex(sha256_HMAC.doFinal(queryStr.getBytes()));
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  private String bytesToHex(byte[] hash) {
    StringBuilder hexString = new StringBuilder();
    for (byte b : hash) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) hexString.append('0');
      hexString.append(hex);
    }
    return hexString.toString();
  }

  private StringBuilder genQueryStr(Map<String, Object> map) {
    Set<String> keySet = map.keySet();
    Iterator<String> iter = keySet.iterator();
    StringBuilder sb = new StringBuilder();
    while (iter.hasNext()) {
      String key = iter.next();
      sb.append(key)
          .append("=")
          .append(map.get(key))
          .append("&");
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb;
  }

}
