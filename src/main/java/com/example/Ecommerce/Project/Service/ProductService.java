package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.Product;
import com.example.Ecommerce.Project.Entity.Role;
import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Repository.ProductRepository;
import com.example.Ecommerce.Project.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Product addProduct(Product product, Authentication authentication){
        Users merchant=getLoggedInMerchant(authentication);
        product.setMerchant(merchant);
        return  productRepository.save(product);

    }

    public List<Product> getProduct() {
        return productRepository.findAll();
    }
    public Product getById(Long id) {
        return productRepository.findById(id) .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product updateProduct(Long id, Product product, Authentication authentication) {
        Users merchant = getLoggedInMerchant(authentication);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (existingProduct.getMerchant() == null || !existingProduct.getMerchant().getId() .equals(merchant.getId())) {
            throw new RuntimeException( "You can only manage your own products" );
        }
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setCategory(product.getCategory());

        return productRepository.save(existingProduct);

    }

    public void deleteProduct(Long id, Authentication authentication){
        Users merchant=getLoggedInMerchant(authentication);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (existingProduct.getMerchant() == null || !existingProduct.getMerchant().getId() .equals(merchant.getId())) {
            throw new RuntimeException( "You can delete only your own products" );
        }
        productRepository.delete(existingProduct);

    }

    private Users getLoggedInMerchant(Authentication authentication) {
        Users merchant = userRepository.findByEmail( authentication.getName() ).orElseThrow(() -> new RuntimeException("Merchant not found"));
        if (merchant.getRole() != Role.MERCHANT) {
            throw new RuntimeException( "Only merchants can manage products" );
        }
        return merchant;
    }


}
