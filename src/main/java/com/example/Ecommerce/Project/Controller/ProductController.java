package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Product;
import com.example.Ecommerce.Project.Service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public Product addProduct(
            @RequestPart("product") Product product,
            @RequestPart(value = "image", required = false) MultipartFile image,
            Authentication authentication) throws IOException {
                 return productService.addProduct(product, image, authentication);
    }

    @GetMapping
    public List<Product> getProduct() {
        return productService.getProduct();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public Product updateProduct(@PathVariable Long id, @RequestPart("product") Product product,
                                 @RequestPart(value = "image", required = false) MultipartFile image,
            Authentication authentication) throws IOException {
                return productService.updateProduct(id, product, image, authentication);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id, Authentication authentication) {
        productService.deleteProduct(id, authentication);
        return "product deleted";
    }
}