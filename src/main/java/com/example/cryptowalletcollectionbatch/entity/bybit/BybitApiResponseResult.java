package com.example.cryptowalletcollectionbatch.entity.bybit;

import lombok.Data;

import java.util.List;

@Data
public class BybitApiResponseResult {

  public String totalEquity;

  public String accountIMRate;

  public List<WalletBalanceCoin> coin;

  public String symbol;

  public String price;

}
