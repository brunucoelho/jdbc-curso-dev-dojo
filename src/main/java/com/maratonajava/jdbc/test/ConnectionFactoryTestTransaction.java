package com.maratonajava.jdbc.test;

import com.maratonajava.jdbc.dominio.Producer;
import com.maratonajava.jdbc.service.ProducerService;

import java.util.List;

public class ConnectionFactoryTestTransaction {
    public static void main(String[] args) {
        Producer producer1 = Producer.builder().name("Toei Animation").build();
        Producer producer2 = Producer.builder().name("White Fox").build();
        Producer producer3 = Producer.builder().name("Studio Ghibli").build();

        ProducerService.saveTransaction(List.of(producer1,producer2, producer3));
    }
}
