package com.example.cryptowalletcollectionbatch.entity.bitbank;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponseData {

  public Integer code;

  public List<Asset> assets;

  /**
   * 終値
   */
  public Float last;

}
