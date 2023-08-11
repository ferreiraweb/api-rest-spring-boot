package com.example.springboot.services;

import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

public ProductModel save(ProductModel model){
    return repository.save(model);
}

public List<ProductModel> getAll() {
    return repository.findAll();
}

public Optional<ProductModel> getById(UUID id) {
    return repository.findById(id);
}

public void delete(ProductModel model) {
    repository.delete(model);
}

}
