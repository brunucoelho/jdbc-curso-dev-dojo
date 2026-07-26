package com.maratonajava.jdbc.test;


import com.maratonajava.jdbc.dominio.Producer;
import com.maratonajava.jdbc.service.ProducerServiceRowSet;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class ConnectionFactoryTestRowSet {
    public static void main(String[] args) {

        Producer producerToUpdate = Producer.builder().id(1).name("MadHouse").build();
        ProducerServiceRowSet.updateJdbcRowSet(producerToUpdate);
        List<Producer> producers = ProducerServiceRowSet.findByNameJdbcRowSet("");
        log.info("producers: " + producers);
    }
}
