package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Product;
import com.example.Ecommerce.Project.Service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @GetMapping
    public List<Product> getProduct(){
        return productService.getProduct();
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product){
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
        public String deleteProduct(@PathVariable Long id){
            productService.deleteProduct(id);
            return "product deleted";

        }

}
