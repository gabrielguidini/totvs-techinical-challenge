package totvs.technicalchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "totvs.technicalchallenge.repository")
public class TechnicalChallangeApplication {

  public static void main(String[] args) {
    SpringApplication.run(TechnicalChallangeApplication.class, args);
  }

}
