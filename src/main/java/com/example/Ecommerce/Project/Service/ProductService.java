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
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Locale;

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

    public List<Product> searchStorefrontProducts(String search, String category, BigDecimal minPrice,
                                                   BigDecimal maxPrice, Boolean inStock, String sort) {
        String cleanSearch = search == null || search.isBlank() ? null : search.trim();
        String cleanCategory = category == null || category.isBlank() ? null : category.trim();
        List<Product> products = productRepository.findAll().stream()
                .filter(product -> cleanSearch == null || matchesSearch(product, cleanSearch))
                .filter(product -> cleanCategory == null || (product.getCategory() != null
                        && cleanCategory.equalsIgnoreCase(product.getCategory().getName())))
                .filter(product -> minPrice == null || product.getPrice().compareTo(minPrice) >= 0)
                .filter(product -> maxPrice == null || product.getPrice().compareTo(maxPrice) <= 0)
                .filter(product -> inStock == null || (inStock ? product.getStock() > 0 : product.getStock() == 0))
                .collect(java.util.stream.Collectors.toList());

        if ("price-low".equals(sort)) {
            products.sort(Comparator.comparing(Product::getPrice));
        } else if ("price-high".equals(sort)) {
            products.sort(Comparator.comparing(Product::getPrice).reversed());
        } else if ("newest".equals(sort)) {
            products.sort(Comparator.comparing(Product::getId).reversed());
        }
        return products;
    }

    private boolean matchesSearch(Product product, String search) {
        String query = search.toLowerCase(Locale.ROOT);
        return containsIgnoreCase(product.getName(), query)
                || containsIgnoreCase(product.getDescription(), query)
                || (product.getCategory() != null && containsIgnoreCase(product.getCategory().getName(), query));
    }

    private boolean containsIgnoreCase(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
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
