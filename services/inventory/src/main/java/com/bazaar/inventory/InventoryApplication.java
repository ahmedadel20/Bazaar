package com.bazaar.inventory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.bazaar.inventory.entity.Category;
import com.bazaar.inventory.service.ProductService;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class}
)
public class InventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}

	@Bean
	public CommandLineRunner myMain(ProductService product) {
		return args -> {
			Category c = new Category(Long.valueOf(1), "Electronics");
			product.getProductsByCategory(c).forEach(a -> System.out.println(a));
		};
	}
}
