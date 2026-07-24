package com.maratonajava.jdbc.service;

import com.maratonajava.jdbc.dominio.Producer;
import com.maratonajava.jdbc.repository.ProducerRepository;

public class ProducerService {

    public static void save(Producer producer){
        ProducerRepository.save(producer);
    }

    public static void delete(int id){
        if(id <= 0) {
            throw new IllegalArgumentException("Invalid value for id: " + id);
        }
        ProducerRepository.delete(id);
    }
}
