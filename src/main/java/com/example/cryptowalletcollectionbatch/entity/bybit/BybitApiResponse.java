package com.example.cryptowalletcollectionbatch.entity.bybit;

import lombok.Data;

@Data
public class BybitApiResponse {

  public Integer redCode;

  public String retMsg;

  public BybitApiResponseResult result;

}
