package com.example.cryptowalletcollectionbatch.writer;

import com.example.cryptowalletcollectionbatch.dto.AssetHistoryDto;
import com.example.cryptowalletcollectionbatch.dto.JobCompletedDto;
import com.example.cryptowalletcollectionbatch.repository.AssetHistoryRepository;
import com.example.cryptowalletcollectionbatch.repository.JobCompletedRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class AssetHistoryWriter implements ItemWriter<List<AssetHistoryDto>> {

  @Autowired
  AssetHistoryRepository assetHistoryRepository;

  @Autowired
  JobCompletedRepository jobCompleteRepository;

  @Override
  public void write(Chunk<? extends List<AssetHistoryDto>> chunk) throws Exception {
    List<AssetHistoryDto> assetHistoryDtos = chunk.getItems().get(0);
    // 既存のがあれば削除
    assetHistoryDtos.stream().forEach(assetHistory -> {
      List<AssetHistoryDto> currentAssetHistoryDtos = assetHistoryRepository.findByDateAndAssetNameAndExchangeName(
          assetHistory.getDate(), assetHistory.getAssetName(), assetHistory.getExchangeName());
      if (currentAssetHistoryDtos.size() > 0) {
        AssetHistoryDto currentAssetHistoryDto = currentAssetHistoryDtos.get(0);
        assetHistoryRepository.delete(currentAssetHistoryDto);
      }
    });
    assetHistoryRepository.saveAll(assetHistoryDtos);

    // job_completeに登録
    if (assetHistoryDtos.size() > 0) {
      JobCompletedDto jobCompleteDto = new JobCompletedDto();
      jobCompleteDto.setDate(assetHistoryDtos.get(0).getDate());
      jobCompleteDto.setExchangeName(assetHistoryDtos.get(0).getExchangeName());
      jobCompleteRepository.save(jobCompleteDto);
    }

  }

}
