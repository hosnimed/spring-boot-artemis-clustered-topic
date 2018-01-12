package com.sbm.artemis.demo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.sbm.artemis.demo.jms.JmsPublisher;
import com.sbm.artemis.demo.models.Company;
import com.sbm.artemis.demo.models.Product;

@EnableScheduling
@SpringBootApplication
public class SpringArtemisTopicProducerApplication {

	@Autowired
	JmsPublisher publisher;

	
	public static void main(String[] args) {
		SpringApplication.run(SpringArtemisTopicProducerApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() throws Exception {
		return args -> {
			System.out.println("============PRODUCER STARTED===========");
		};
	}

	@Scheduled(fixedDelay = 5000L)
	private void publish() {
		long i = Instant.now().getEpochSecond();
		/*
		 * Apple company & products
		 */
		// initial company and products
		Product iphone7 = new Product("Iphone 7 " + i);
		Product iPadPro = new Product("IPadPro "+i);

		List<Product> appleProducts = new ArrayList<Product>(Arrays.asList(iphone7, iPadPro));

		Company apple = new Company("Apple", appleProducts);

		iphone7.setCompany(apple);
		iPadPro.setCompany(apple);

		// send message to ActiveMQ
		publisher.send(apple);

		/*
		 * Samsung company and products
		 */
		Product galaxyS8 = new Product("Galaxy S8 "+i);
		Product gearS3 = new Product("Gear S3 "+i);

		List<Product> samsungProducts = new ArrayList<Product>(Arrays.asList(galaxyS8, gearS3));

		Company samsung = new Company("Samsung", samsungProducts);

		galaxyS8.setCompany(samsung);
		gearS3.setCompany(samsung);

		/*
		 * send message to ActiveMQ
		 */
//		publisher.send(samsung);
	}


}
