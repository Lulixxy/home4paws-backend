package org.itsci.home4paws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Home4pawsApplication {

	public static void main(String[] args) {
		SpringApplication.run(Home4pawsApplication.class, args);
	}

}
