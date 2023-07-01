package com.example.cryptowalletcollectionbatch.repository;

import com.example.cryptowalletcollectionbatch.dto.JobCompletedDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JobCompletedRepository extends JpaRepository<JobCompletedDto, Long> {

  List<JobCompletedDto> findByDateAndExchangeName(Date date, String exchangeName);

}
