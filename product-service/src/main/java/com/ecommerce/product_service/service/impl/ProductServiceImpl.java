package com.ecommerce.product_service.service.impl;

import com.ecommerce.product_service.dto.ProductRequestDTO;
import com.ecommerce.product_service.dto.ProductResponseDTO;
import com.ecommerce.product_service.exception.ResourceNotFoundException;
import com.ecommerce.product_service.mapper.ProductMapper;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import com.ecommerce.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;


    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {

        Product product = mapper.toProduct(requestDTO);

        Product savedProduct = repository.save(product);

        log.info("Saved product {}", savedProduct.getName());


        return mapper.toProductResponseDTO(savedProduct);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return repository.findAll().stream().map(mapper::toProductResponseDTO).toList();
    }

    @Override
    public ProductResponseDTO getProductById(String id) {
        Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id" , id));
        return mapper.toProductResponseDTO(product);
    }

    @Override
    public ProductResponseDTO updateProductById(String id, ProductRequestDTO productRequest) {

        Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id" , id));

        mapper.updateProductFromRequest(productRequest, product);

        Product updatedProduct = repository.save(product);

        log.info("Updated product {}", updatedProduct.getName());
        return mapper.toProductResponseDTO(updatedProduct);
    }

    @Override
    public void deleteProductById(String id) {

  if(repository.existsById(id)){
      throw new ResourceNotFoundException("Product", "id" , id);
  }
        repository.deleteById(id);

        log.info("Deleted   product  with id{}", id);



    }
}
