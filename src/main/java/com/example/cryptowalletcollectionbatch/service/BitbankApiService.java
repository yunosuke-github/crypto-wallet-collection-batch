package com.example.cryptowalletcollectionbatch.service;

import com.example.cryptowalletcollectionbatch.dto.AssetHistoryDto;
import com.example.cryptowalletcollectionbatch.entity.bitbank.ApiResponse;
import com.example.cryptowalletcollectionbatch.entity.bitbank.Asset;
import org.apache.commons.codec.binary.Hex;
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
import java.util.*;

@Service
public class BitbankApiService extends ApiServiceBase {

  // TODO: ファイルとかに切り出す
  private final Map<String, String> assetMap = Map.of(
      "jpy", "JPY",
      "btc", "BTC",
      "xrp", "XRP",
      "ltc", "LTC",
      "eth", "ETH",
      "xym", "XYM",
      "dot", "DOT",
      "doge", "DOGE",
      "astr", "ASTR",
      "flr", "FLR"
  );

  public static final String EXCHANGE_NAME = "BITBANK";

  @Value("${bitbank.public-api-endpoint-url}")
  private String publicApiEndpointUrl;

  @Value("${bitbank.endpoint-url}")
  private String endpointUrl;

  @Value("${bitbank.api-version}")
  private String apiVersion;

  @Value("${bitbank.api-key}")
  private String apiKey;

  @Value("${bitbank.api-secret}")
  private String apiSecret;

  @Autowired
  public BitbankApiService(RestTemplate restTemplate) {
    super(restTemplate);
  }

  /**
   * https://github.com/bitbankinc/bitbank-api-docs/blob/master/rest-api_JP.md#%E8%AA%8D%E8%A8%BC
   * @return
   */
  public List<AssetHistoryDto> get() {
    String path = "/" + apiVersion + "/user/assets";
    String url = endpointUrl + path;
    HttpHeaders headers = makeHeader(path, "");
    ApiResponse response = apiCall(HttpMethod.GET, url, headers, null, ApiResponse.class);
    if (response.getSuccess() == 0) {
      return null;
    }
    return makeAssetHistoryDto(response.getData().getAssets());
  }

  public Float getPrice(String assetName) {
    String url = publicApiEndpointUrl + "/" + assetName.toLowerCase() + "_jpy/ticker";
    ApiResponse response = apiCall(HttpMethod.GET, url, null, null, ApiResponse.class);
    if (response.getSuccess() == 0) {
      return null;
    }
    return response.getData().getLast();
  }

  private List<AssetHistoryDto> makeAssetHistoryDto(List<Asset> assets) {
    List<AssetHistoryDto> assetHistoryDtos = new ArrayList<>();
    assets.stream().forEach(asset -> {
      String assetName = assetMap.get(asset.getAsset());
      if (assetName == null) return;
      AssetHistoryDto assetHistoryDto = new AssetHistoryDto();
      assetHistoryDto.setAssetName(assetName);
      assetHistoryDto.setExchangeName(EXCHANGE_NAME);
      assetHistoryDto.setHoldingAmount(Float.valueOf(asset.getOnHandAmount()));
      if (assetName.equals("JPY")) {
        assetHistoryDto.setJpy(assetHistoryDto.getHoldingAmount());
      } else {
        Float price = getPrice(assetName);
        assetHistoryDto.setJpy(price * assetHistoryDto.getHoldingAmount());
      }
      assetHistoryDtos.add(assetHistoryDto);
    });
    return assetHistoryDtos;
  }

  private HttpHeaders makeHeader(String url, String parameters) {
    String nonce = String.valueOf(System.currentTimeMillis());
    HttpHeaders headers = new HttpHeaders();
    headers.add("ACCESS-KEY", apiKey);
    headers.add("ACCESS-NONCE", nonce);
    headers.add("ACCESS-SIGNATURE", getSignature(nonce, url, parameters));
    return headers;
  }

  /**
   * 署名
   * @param nonce
   * @param path
   * @param parameters
   * @return
   */
  private String getSignature(String nonce, String path, String parameters) {
    String message = nonce + path + parameters;
    try {
      byte[] hmac = calculateHmacSha256(message, apiSecret);
      return Hex.encodeHexString(hmac);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * HMAC_SHA256の計算
   * @param message
   * @param secretKey
   * @return
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeyException
   */
  private static byte[] calculateHmacSha256(String message, String secretKey)
      throws NoSuchAlgorithmException, InvalidKeyException {
    Mac mac = Mac.getInstance("HmacSHA256");
    SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
    mac.init(keySpec);
    return mac.doFinal(message.getBytes());
  }

}
