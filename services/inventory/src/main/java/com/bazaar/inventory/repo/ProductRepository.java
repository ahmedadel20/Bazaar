package com.bazaar.inventory.repo;

import com.bazaar.inventory.entity.Category;
import com.bazaar.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductCategory(Category productCategory);
}
