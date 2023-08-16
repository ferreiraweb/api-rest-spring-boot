package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping(value = "/api/products")
public class ProductController {

    @Autowired
    private ProductService service;


   @PostMapping
    public ResponseEntity<ProductModel> save(@RequestBody @Valid ProductRecordDto productRecordDto){

       ProductModel productModel = new ProductModel();
       BeanUtils.copyProperties(productRecordDto, productModel);

       return ResponseEntity.status(HttpStatus.CREATED).body(service.save(productModel));
    }

    @GetMapping
    public ResponseEntity<List<ProductModel>> getAll() {

       List<ProductModel> productList = service.getAll();
       productList.forEach(p -> {
           p.add(linkTo(methodOn(ProductController.class).getById(p.getId())).withSelfRel());
       });

       return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") UUID id) {
       Optional<ProductModel> productModel = service.getById(id);
       if (productModel.isEmpty()) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
       }

       return ResponseEntity
               .status(HttpStatus.OK)
               .body(productModel);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody @Valid ProductRecordDto productRecordDto) {

       Optional<ProductModel> optionalProductModel = service.getById(productRecordDto.id());

       if (optionalProductModel.isEmpty()) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
       }

       ProductModel productModel = optionalProductModel.get();
       BeanUtils.copyProperties(productRecordDto, productModel);

       return ResponseEntity.status(HttpStatus.OK).body(service.save(productModel));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id) {

        Optional<ProductModel> optionalProductModel = service.getById(id);

        if (optionalProductModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        ProductModel productModel = optionalProductModel.get();
        service.delete(productModel);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product deleted successfully");

    }


}
