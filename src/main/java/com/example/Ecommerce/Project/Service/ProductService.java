package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.Product;
import com.example.Ecommerce.Project.Entity.Role;
import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Repository.ProductRepository;
import com.example.Ecommerce.Project.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public ProductService(
            ProductRepository productRepository,
            UserRepository userRepository,
            CloudinaryService cloudinaryService) {

        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }

    // ADD PRODUCT
    public Product addProduct(Product product, MultipartFile image, Authentication authentication) throws IOException {
        Users merchant = getLoggedInMerchant(authentication);
        product.setMerchant(merchant);
        if (image != null && !image.isEmpty()) {
            //upload image to cloudinary
            String imageUrl = cloudinaryService.uploadImage(image);
            //stores image url in db
            product.setImageUrl(imageUrl);
        }
        return productRepository.save(product);
    }

    // GET ALL PRODUCTS
    public List<Product> getProduct() {
        return productRepository.findAll();
    }

    // GET PRODUCT BY ID
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // UPDATE PRODUCT
    public Product updateProduct(Long id, Product product, MultipartFile image,
            Authentication authentication) throws IOException {
        Users merchant = getLoggedInMerchant(authentication);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (existingProduct.getMerchant() == null ||
                !existingProduct.getMerchant().getId().equals(merchant.getId())) {
            throw new RuntimeException("You can only manage your own products");
        }
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setCategory(product.getCategory());

        // Upload new image if merchant selected one
        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(image);
            existingProduct.setImageUrl(imageUrl);
        }
        return productRepository.save(existingProduct);
    }

    // DELETE PRODUCT
    public void deleteProduct(Long id, Authentication authentication) {
        Users merchant = getLoggedInMerchant(authentication);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (existingProduct.getMerchant() == null ||
                !existingProduct.getMerchant().getId().equals(merchant.getId())) {
            throw new RuntimeException("You can delete only your own products");
        }
        productRepository.delete(existingProduct);
    }

    private Users getLoggedInMerchant(Authentication authentication) {
        Users merchant = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("Merchant not found"));
        if (merchant.getRole() != Role.MERCHANT) {
            throw new RuntimeException(
                    "Only merchants can manage products"
            );
        }
        return merchant;
    }
}