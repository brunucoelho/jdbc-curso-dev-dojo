package com.maratonajava.jdbc.test;

import com.maratonajava.jdbc.dominio.Producer;
import com.maratonajava.jdbc.repository.ProducerRepository;
import com.maratonajava.jdbc.service.ProducerService;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class ConnectionFactoryTest {

  public static void main(String[] args) {
    Producer producer = Producer.builder().name("SBT").build();
    Producer producerToUpdate = Producer.builder().id(1).name("MadHouse").build();
//    ProducerRepository.save(producer);
//    ProducerRepository.delete(9);
//    ProducerRepository.update(producerToUpdate);
//      List<Producer> producers = ProducerService.findAll();
//      log.info("Producers found {}", producers);
//    ProducerService.showProducerMetadata();
    ProducerService.showDriveMetadata();
  }
  
}



