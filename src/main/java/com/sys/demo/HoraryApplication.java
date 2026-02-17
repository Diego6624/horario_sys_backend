package com.sys.demo;



import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sys.demo.entities.Horary;
import com.sys.demo.entities.Status;
import com.sys.demo.repositories.HoraryRepository;
import com.sys.demo.repositories.StatusRepository;

@SpringBootApplication
public class HoraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoraryApplication.class, args);
	}

	@Bean
    CommandLineRunner init(HoraryRepository repo, StatusRepository statusRepo) {
        return args -> {

            if (repo.count() == 0) {

                Status disponible = statusRepo.findById(1L).orElse(null);

                repo.save(new Horary(null,"multiuso","","","","", true, disponible));
                repo.save(new Horary(null,"201","","","","", true, null));
                repo.save(new Horary(null,"202","","","","", true, null));
                repo.save(new Horary(null,"301","","","","", true, null));
                repo.save(new Horary(null,"401","","","","", true, null));
                repo.save(new Horary(null,"402","","","","", true, null));
            }
        };
    }
}
