package br.com.upa.notifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PortariaNotifierServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortariaNotifierServerApplication.class, args);
    }
}
