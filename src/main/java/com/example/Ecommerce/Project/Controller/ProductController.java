package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Product;
import com.example.Ecommerce.Project.Service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping public Product addProduct( @RequestBody Product product, Authentication authentication){
        return productService.addProduct( product, authentication );
    }

    @GetMapping
    public List<Product> getProduct() {
        return productService.getProduct();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id,@RequestBody Product product, Authentication authentication){
        return productService.updateProduct(id, product, authentication);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id, Authentication authentication){
        productService.deleteProduct(id, authentication);
        return "product deleted";
    }
}