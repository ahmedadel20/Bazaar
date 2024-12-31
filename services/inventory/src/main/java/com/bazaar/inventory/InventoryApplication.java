package com.bazaar.inventory;

import com.bazaar.inventory.repo.CartItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.bazaar.inventory.service.ProductService;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class}
)
public class InventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}

	@Bean
	public CommandLineRunner myMain(CartItemRepository cart) {
		return args -> {
//			System.out.println(cart.findByBazaarUserIdAndProductId(5L,8L));
		};
	}
}
