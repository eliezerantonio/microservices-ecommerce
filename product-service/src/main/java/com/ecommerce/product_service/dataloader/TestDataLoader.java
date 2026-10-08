package com.ecommerce.product_service.dataloader;

import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;


@Component
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;


    @Override
    public void run(String... args) throws Exception {

//        Product product = Product.builder()
//                .name("iPhone 18 Pro Max")
//                .description("Smartphone with Apple Intellingence")
//                .price(BigDecimal.valueOf(1200)).build();
//
//        productRepository.save(product);
//
//        System.out.println("Saved product with id " + product.getName());
    }
}
