package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Product;
import com.example.Ecommerce.Project.Service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) throws IOException {
        Product savedProduct= productService.addProduct(product, imageFile);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {

        Product product = productService.getById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(product.getImageType()))
                .body(product.getImageData());
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
