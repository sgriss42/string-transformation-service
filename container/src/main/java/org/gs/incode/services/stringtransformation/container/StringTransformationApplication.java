package org.gs.incode.services.stringtransformation.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(
    basePackages = "org.gs.incode.services.stringtransformation.persistance.jpa.repository")
@EntityScan(basePackages = "org.gs.incode.services.stringtransformation.persistance.jpa.entity")
@SpringBootApplication(scanBasePackages = "org.gs.incode.services.stringtransformation")
public class StringTransformationApplication {

  public static void main(String[] args) {
    SpringApplication.run(StringTransformationApplication.class, args);
  }
}
