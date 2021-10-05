package it.majorbit.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan ("it.majorbit")
@EntityScan("it.majorbit.model")
@EnableJpaRepositories("it.majorbit.repositories")
@SpringBootApplication
public class MsQuotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsQuotesApplication.class, args);
	}

}
