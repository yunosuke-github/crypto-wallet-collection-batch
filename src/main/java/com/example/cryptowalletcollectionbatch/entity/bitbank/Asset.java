package com.example.cryptowalletcollectionbatch.entity.bitbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
public class Asset {

  /**
   * アセット名
   * https://github.com/bitbankinc/bitbank-api-docs/blob/master/assets.md
   */
  @JsonProperty("asset")
  public String asset;

  /**
   * 利用可能な量
   */
  @JsonProperty("free_amount")
  public String freeAmount;

  /**
   * 精度
   */
  @JsonProperty("amount_precision")
  public Integer amountPrecision;

  /**
   * 保有量
   */
  @JsonProperty("onhand_amount")
  public String onHandAmount;

  /**
   * ロックされている量
   */
  @JsonProperty("locked_amount")
  public String lockedAmount;

  /**
   * 出金手数料
   */
//  @JsonProperty("withdrawal_fee")
//  public String withdrawalFee;

  /**
   * 入金ステータス
   */
  @JsonProperty("stop_deposit")
  public boolean stopDeposit;

  /**
   * 出金ステータス
   */
  @JsonProperty("stop_withdrawal")
  public boolean stopWithdrawal;

}
