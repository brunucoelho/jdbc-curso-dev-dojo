package com.maratonajava.jdbc.service;

import com.maratonajava.jdbc.dominio.Producer;
import com.maratonajava.jdbc.repository.ProducerRepository;

public class ProducerService {

    public static void save(Producer producer){
        ProducerRepository.save(producer);
    }

    public static void delete(Integer id){
        requireValidId(id);
        ProducerRepository.delete(id);
    }

    public static void update(Producer producer){
        requireValidId(producer.getId());
        ProducerRepository.update(producer);
    }

    private static void requireValidId(Integer id) {
        if(id == null && id <= 0) {
            throw new IllegalArgumentException("Invalid value for id: " + id);
        }
    }
}
