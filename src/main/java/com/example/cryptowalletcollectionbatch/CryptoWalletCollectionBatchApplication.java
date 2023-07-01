package com.example.cryptowalletcollectionbatch;

import com.example.cryptowalletcollectionbatch.config.BitbankBatchConfig;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class CryptoWalletCollectionBatchApplication {

  public static void main(String[] args) {
    SpringApplication.run(CryptoWalletCollectionBatchApplication.class, args);
  }

}
