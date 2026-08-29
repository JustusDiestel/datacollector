package tech.justus.diestel.datacollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DatacollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatacollectorApplication.class, args);
    }

}
