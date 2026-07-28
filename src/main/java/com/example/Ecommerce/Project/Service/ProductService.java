package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.Product;
import com.example.Ecommerce.Project.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Product addProduct(Product product)  {
        return productRepository.save(product);

    }

    public List<Product> getProduct(){
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new RuntimeException("product not found"));
    }

    public Product updateProduct(Long id, Product product) {
        Product existingProduct=productRepository.findById(id).orElseThrow(()-> new RuntimeException("product not found"));
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setCategory(product.getCategory());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id){
        Product existingProduct=productRepository.findById(id).orElseThrow(()->new RuntimeException("product not found"));
        productRepository.delete(existingProduct);

    }
}
