package com.maratonajava.crud.test;

import com.maratonajava.crud.service.ProducerService;

import java.util.Scanner;

public class CrudTest {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int op;
        while (true) {
            producerMenu();
            op = Integer.parseInt(scanner.nextLine());
            if (op == 0) break;
            ProducerService.buildMenu(op);
        }
    }

    private static void producerMenu() {
        System.out.println("Type the number of your option:");
        System.out.println("1. Search for producer");
        System.out.println("0. Exit");
    }
}
