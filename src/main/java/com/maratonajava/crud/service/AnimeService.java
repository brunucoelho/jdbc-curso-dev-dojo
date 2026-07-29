package com.maratonajava.crud.service;

import com.maratonajava.crud.dominio.Anime;
import com.maratonajava.crud.dominio.Producer;
import com.maratonajava.crud.repository.AnimeRespository;

import java.util.Optional;
import java.util.Scanner;

public class AnimeService {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void menu(int op) {
        switch (op) {
            case 1 -> findByName();
            case 2 -> delete();
            case 3 -> save();
            case 4 -> update();
           // default -> throw new IllegalArgumentException("Not a valid option");
        }
    }

    private static void findByName()  {
        System.out.println("Type the name or empty to all");
        String name = SCANNER.nextLine();
        AnimeRespository.findByName(name)
                        .forEach(p -> System.out.printf("[%d] - %s %d %s%n",p.getId(), p.getName(), p.getEpisodes(), p.getProducer().getName()));
    }

    private static void delete() {
        System.out.println("Type the id of the anime to delete");
        findByName();
        int id = SCANNER.nextInt();
        System.out.println("Are you sure? Y/N");
        String choice = SCANNER.nextLine();
        if("y".equalsIgnoreCase(choice)) {
            AnimeRespository.delete(id);
        }
    }

    private static void save() {
        System.out.println("Type the name of the anime to save");
        String name = SCANNER.nextLine();
        System.out.println("Type the number of episodes to save");
        int episodes = Integer.parseInt(SCANNER.nextLine());
        System.out.println("Type the id of the producer to save");
        Integer producerId = Integer.parseInt(SCANNER.nextLine());
//        Anime build = Anime.builder().name(name).build();
        Anime anime = Anime.builder()
                .episodes(episodes)
                .name(name)
                .producer(Producer.builder().id(producerId).build())
                .build();
        AnimeRespository.save(anime);
        }

    private static void update() {
        System.out.println("Type the id of the anime to you want to update");
        Optional<Anime> animeOptional = AnimeRespository.findById(Integer.parseInt(SCANNER.nextLine()));
        if (animeOptional.isEmpty()) {
            System.out.println("Anime not found");
            return;
        }
        Anime animeFromDB = animeOptional.get();
        System.out.println("Anime found " + animeFromDB);
        System.out.println("Type the new name or enter to keep the same");
        String name = SCANNER.nextLine();
        name = name.isEmpty() ? animeFromDB.getName() : name;

        System.out.println("Type the new number of episodes");
        int episodes = Integer.parseInt(SCANNER.nextLine());

        Anime animeToUpdate = Anime.builder()
                .id(animeFromDB.getId())
                .episodes(episodes)
                .producer(animeFromDB.getProducer())
                .name(name)
                .build();

        AnimeRespository.uptade(animeToUpdate);
    }

    }

