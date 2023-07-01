package com.example.cryptowalletcollectionbatch.reader;

import com.example.cryptowalletcollectionbatch.dto.AssetHistoryDto;
import com.example.cryptowalletcollectionbatch.service.BitbankApiService;
import com.example.cryptowalletcollectionbatch.service.JobCompletedService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class BitbankBatchReader implements ItemReader<List<AssetHistoryDto>> {

  @Autowired
  BitbankApiService bitbankApiService;

  @Autowired
  JobCompletedService jobCompletedService;

  @Override
  public List<AssetHistoryDto> read() {
    if (jobCompletedService.isCompleted(bitbankApiService.EXCHANGE_NAME)) {
      return null;
    }
    return bitbankApiService.get();
  }

}
