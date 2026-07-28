package com.maratonajava.crud.service;

import com.maratonajava.crud.dominio.Producer;
import com.maratonajava.crud.repository.ProducerRespository;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProducerService {
    private static Scanner scanner = new Scanner(System.in);

    public static void buildMenu(int op) {
        switch (op) {
            case 1:
                findByName();
                break;
            default:
                throw new IllegalArgumentException("Not a valid option");
        }
    }

    private static void findByName()  {
        System.out.println("Type the name or empty to all");
        String name = scanner.nextLine();
        List<Producer> producers = ProducerRespository.findByName(name);
        for (int i = 0; i < producers.size(); i++) {
            System.out.printf("[%d] - %s\n", i + 1, producers.get(i).getName());
        }
    }
}
