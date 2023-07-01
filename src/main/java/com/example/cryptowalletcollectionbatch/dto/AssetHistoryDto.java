package com.example.cryptowalletcollectionbatch.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;


@Data
@Entity
@Table(name = "asset_history")
public class AssetHistoryDto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  public Long id;

  /**
   * 日付
   */
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Column(name = "date")
  public Date date;

  /**
   * アセット名
   */
  @Column(name = "asset_name")
  public String assetName;

  /**
   * 取引所
   */
  @Column(name = "exchange_name")
  public String exchangeName;

  /**
   * 保有量
   */
  @Column(name = "holding_amount")
  public Float holdingAmount;

  /**
   * 日本円換算
   */
  @Column(name = "jpy")
  public Float jpy;

}
