package com.maratonajava.jdbc.test;

import com.maratonajava.jdbc.conn.ConnectionFactory;
import com.maratonajava.jdbc.dominio.Producer;
import com.maratonajava.jdbc.repository.ProducerRepository;

public class ConnectionFactoryTest {

  public static void main(String[] args) {
    Producer producer = Producer.builder().name("SBT").build();
    Producer producerToUpdate = Producer.builder().id(1).name("MADHOUSE").build();
//    ProducerRepository.save(producer);
//    ProducerRepository.delete(9);
    ProducerRepository.update(producerToUpdate);

  }
  
}



