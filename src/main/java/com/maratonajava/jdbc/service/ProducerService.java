package com.maratonajava.jdbc.service;

import com.maratonajava.jdbc.dominio.Producer;
import com.maratonajava.jdbc.repository.ProducerRepository;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
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

    public static List<Producer> findAll(){

        return ProducerRepository.findAll();
    }

    public static List<Producer> findByName(String name){

        return ProducerRepository.findByName(name);
    }

    public static void showProducerMetadata(){

        ProducerRepository.showProducerMetaData();
    }

    public static void showDriveMetadata(){

        ProducerRepository.showDriveMetaData();
    }

    private static void requireValidId(Integer id) {
        if(id == null && id <= 0) {
            throw new IllegalArgumentException("Invalid value for id: " + id);
        }
    }
}
