package com.example.cryptowalletcollectionbatch.config;

import com.example.cryptowalletcollectionbatch.dto.AssetHistoryDto;
import com.example.cryptowalletcollectionbatch.processor.AssetHistoryProcessor;
import com.example.cryptowalletcollectionbatch.reader.BitbankBatchReader;
import com.example.cryptowalletcollectionbatch.reader.BybitBatchReader;
import com.example.cryptowalletcollectionbatch.writer.AssetHistoryWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
public class BybitBatchConfig {

  @Autowired
  private BybitBatchReader bybitBatchReader;

  @Autowired
  private AssetHistoryProcessor assetHistoryProcessor;

  @Autowired
  private AssetHistoryWriter assetHistoryWriter;

  @Bean
  public Job bybitBatchJob(JobRepository jobRepository, Step bybitBatchStep) {
    return new JobBuilder("BybitBatchJob", jobRepository)
        .start(bybitBatchStep)
        .build();
  }

  @Bean
  public Step bybitBatchStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("bybitBatchStep", jobRepository)
        .<List<AssetHistoryDto>, List<AssetHistoryDto>>chunk(1, transactionManager)
        .reader(bybitBatchReader)
        .processor(assetHistoryProcessor)
        .writer(assetHistoryWriter)
        .allowStartIfComplete(true)
        .build();
  }

}
