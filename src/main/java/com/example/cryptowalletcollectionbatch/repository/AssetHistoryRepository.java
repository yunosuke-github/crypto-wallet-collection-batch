package com.example.cryptowalletcollectionbatch.repository;

import com.example.cryptowalletcollectionbatch.dto.AssetHistoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AssetHistoryRepository extends JpaRepository<AssetHistoryDto, Long> {

  List<AssetHistoryDto> findByDateAndAssetNameAndExchangeName(Date date, String assetName, String exchangeName);

}
