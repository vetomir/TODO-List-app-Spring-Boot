package pl.gregorymartin.udemykursspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UdemyKursSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(UdemyKursSpringApplication.class, args);
    }

/*
    @Bean
    Validator validator() {
        return new LocalValidatorFactoryBean();
    }
*/

}
