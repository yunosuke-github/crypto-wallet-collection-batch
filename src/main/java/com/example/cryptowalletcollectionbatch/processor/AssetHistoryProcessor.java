package com.example.cryptowalletcollectionbatch.processor;

import com.example.cryptowalletcollectionbatch.dto.AssetHistoryDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Component
@StepScope
public class AssetHistoryProcessor implements ItemProcessor<List<AssetHistoryDto>, List<AssetHistoryDto>> {

  @Override
  public List<AssetHistoryDto> process(List<AssetHistoryDto> assetHistoryDtos) throws Exception {
    Date date = new Date(System.currentTimeMillis());
    assetHistoryDtos.stream().forEach(assetHistoryDto -> {
      assetHistoryDto.setDate(date);
    });
    return assetHistoryDtos;
  }

}
