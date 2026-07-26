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
//    ProducerService.showProducerMetadata();
//      log.info("Producers found {}", producers);
//    ProducerService.showDriveMetadata();
//      ProducerService.showTypeScrollWorking();
//    List<Producer> deen = ProducerService.findByNameAndUpdateToUpperCase("Deen");
//    List<Producer> bones = ProducerService.findByNameAndInsertWhenNotFound("A-1 Pictures");
//    ProducerService.findByNameAndDelete("A-1 Pictures");
//    log.info("Producers found {}", bones);
//    List<Producer> producerList = ProducerService.findByNamePreparedStatement("Bon");
//    log.info("Producers found {}", producerList);
//    ProducerService.updatePreparedStatemets(producerToUpdate);

    List<Producer> producerList = ProducerService.findByNameCallableStataments("sasa");
    log.info("Producers found {}", producerList);
  }
  
}



