package com.example.catalog.repository;

import com.example.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // Fetch products along with their producer and attributes to prevent N+1 queries
    @Query("SELECT p FROM Product p JOIN FETCH p.producer LEFT JOIN FETCH p.attributes")
    List<Product> findAllWithDetails();
}
