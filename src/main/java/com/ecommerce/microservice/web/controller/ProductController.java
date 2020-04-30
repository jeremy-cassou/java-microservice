package com.ecommerce.microservice.web.controller;

import com.ecommerce.microservice.dao.ProductDao;
import com.ecommerce.microservice.model.Product;
import com.ecommerce.microservice.web.exceptions.ProductNotFoundException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.omg.CORBA.WStringSeqHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(description = "Rest API for manage products")
@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @ApiOperation(value = "Get a list contains all products")
    @GetMapping(value = "/products")
    public MappingJacksonValue getProducts() {
        List<Product> products = productDao.findAll();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("buyingPrice");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("productFilter", filter);
        MappingJacksonValue filterProduct = new MappingJacksonValue(products);
        filterProduct.setFilters(filterProvider);
        return filterProduct;
    }

    @ApiOperation(value = "Get a product by id")
    @GetMapping(value = "/products/{id}")
    public Product getProduct (@PathVariable int id) {
        Product product = productDao.findById(id);

        if (product == null) {
            throw new ProductNotFoundException("Product with id " + id + " not found.");
        }

        return product;
    }

    @ApiOperation(value = "Create a new product")
    @PostMapping(value = "/products")
    public ResponseEntity<Void> postProduct (@Valid @RequestBody Product product) {
        Product addedProduct = productDao.save(product);

        if (addedProduct == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(addedProduct.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @ApiOperation(value = "Remove a product by id")
    @DeleteMapping(value = "/products/{id}")
    public void deleteProduct(@PathVariable int id) {
        productDao.deleteById(id);
    }

    @ApiOperation(value = "Update an existing product")
    @PutMapping(value = "/products")
    public void updateProduct(@RequestBody Product product) {
        productDao.save(product);
    }

    @GetMapping(value = "/AdminProducts")
    public Map<String, Integer> getAdminProducts() {
        List<Product> products = productDao.findAll();
        return products.stream().collect(Collectors.toMap(
                p -> p.toString(),
                p -> p.getPrice() - p.getBuyingPrice()
        ));
    }
}
