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

    public static void saveTransaction(List<Producer> producers){
        ProducerRepository.saveTransaction(producers);
    }

    public static void delete(Integer id){
        requireValidId(id);
        ProducerRepository.delete(id);
    }

    public static void update(Producer producer){
        requireValidId(producer.getId());
        ProducerRepository.update(producer);
    }

    public static void updatePreparedStatemets(Producer producer){
        requireValidId(producer.getId());
        ProducerRepository.updatePreparedStatemets(producer);
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

    public static void showTypeScrollWorking() {
        ProducerRepository.showTypeScrollWorking();
    }

    public static List<Producer> findByNameAndUpdateToUpperCase(String name){

        return ProducerRepository.findByNameAndUpdateToUpperCase(name);
    }

    public static List<Producer> findByNameAndInsertWhenNotFound(String name){

        return ProducerRepository.findByNameAndInsertWhenNotFound(name);
    }

    public static void findByNameAndDelete(String name){

        ProducerRepository.findByNameAndDelete(name);
    }

    public static List<Producer> findByNamePreparedStatement(String name){

        return ProducerRepository.findByNamePreparedStatement(name);
    }

    public static List<Producer> findByNameCallableStataments(String name){

        return ProducerRepository.findByNameCallableStataments(name);
    }

    private static void requireValidId(Integer id) {
        if(id == null && id <= 0) {
            throw new IllegalArgumentException("Invalid value for id: " + id);
        }
    }
}
