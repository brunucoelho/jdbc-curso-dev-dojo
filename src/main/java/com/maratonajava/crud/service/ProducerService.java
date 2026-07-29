package com.maratonajava.crud.service;

import com.maratonajava.crud.dominio.Producer;
import com.maratonajava.crud.repository.ProducerRespository;
import com.maratonajava.jdbc.repository.ProducerRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProducerService {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void menu(int op) {
        switch (op) {
            case 1 -> findByName();
            case 2 -> delete();
            case 3 -> save();
            case 4 -> update();
            //default -> throw new IllegalArgumentException("Not a valid option");
        }
    }

    private static void findByName()  {
        System.out.println("Type the name or empty to all");
        String name = SCANNER.nextLine();
        ProducerRespository.findByName(name)
                        .forEach(p -> System.out.printf("[%d] - %s%n",p.getId(), p.getName()));
    }

    private static void delete() {
        System.out.println("Type the id of the producer to delete");
        findByName();
        int id = SCANNER.nextInt();
        System.out.println("Are you sure? Y/N");
        String choice = SCANNER.nextLine();
        if("y".equalsIgnoreCase(choice)) {
            ProducerRespository.delete(id);
        }
    }

    private static void save() {
        System.out.println("Type the name of the producer to save");
        String name = SCANNER.nextLine();
        Producer build = Producer.builder().name(name).build();
        ProducerRespository.save(build);
        }

    private static void update() {
        System.out.println("Type the id of the producer to you want to update");
        Optional<Producer> producerOptional = ProducerRespository.findById(Integer.parseInt(SCANNER.nextLine()));
        if (producerOptional.isEmpty()) {
            System.out.println("Producer not found");
            return;
        }
        Producer producerFromDB = producerOptional.get();
        System.out.println("Producer found " + producerFromDB);
        System.out.println("Type the new name or enter to keep the same");
        String name = SCANNER.nextLine();
        name = name.isEmpty() ? producerFromDB.getName() : name;

        Producer producerToUpdate = Producer.builder()
                .name(name)
                .id(producerFromDB.getId())
                .build();

        ProducerRespository.uptade(producerToUpdate);
    }

    }

