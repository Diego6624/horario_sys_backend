package com.sys.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sys.demo.entities.Horary;
import com.sys.demo.repositories.HoraryRepository;

@SpringBootApplication
public class HoraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoraryApplication.class, args);
	}

	@Bean
    CommandLineRunner init(HoraryRepository repo) {
        return args -> {

            if (repo.count() == 0) {

                repo.save(new Horary(null,"multiuso","","","",""));
                repo.save(new Horary(null,"201","","","",""));
                repo.save(new Horary(null,"202","","","",""));
                repo.save(new Horary(null,"301","","","",""));
                repo.save(new Horary(null,"401","","","",""));
                repo.save(new Horary(null,"402","","","",""));
            }
        };
    }
}
