package com.bazaar.inventory;

import com.bazaar.inventory.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}

	@Bean
	public CommandLineRunner myRunner(ProductService productService) {
		return args -> {
			productService.getProducts().forEach(a -> System.out.println(a));
		};
	}
}
