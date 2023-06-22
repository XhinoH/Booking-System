package webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableJpaRepositories(basePackages = {"backend.repository"})
@EntityScan(basePackages = {"backend.model"})
public class HungerNetApplication {
    public static void main(String[] args) {
        SpringApplication.run(HungerNetApplication.class, args);
    }
}
