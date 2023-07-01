package com.example.cryptowalletcollectionbatch.service;

import com.example.cryptowalletcollectionbatch.dto.JobCompletedDto;
import com.example.cryptowalletcollectionbatch.repository.JobCompletedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class JobCompletedService {

  @Autowired
  JobCompletedRepository jobCompletedRepository;

  public boolean isCompleted(String exchangeName) {
    Date date = new Date(System.currentTimeMillis());
    List<JobCompletedDto> jobCompleteDtos = jobCompletedRepository.findByDateAndExchangeName(date, exchangeName);
    return jobCompleteDtos.size() > 0;
  }

}
