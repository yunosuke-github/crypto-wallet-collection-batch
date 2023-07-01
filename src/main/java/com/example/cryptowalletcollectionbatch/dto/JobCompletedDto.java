package com.example.cryptowalletcollectionbatch.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;


@Data
@Entity
@Table(name = "job_completed")
public class JobCompletedDto {

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
   * 取引所
   */
  @Column(name = "exchange_name")
  public String exchangeName;

}
