package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.Category;
import com.example.Ecommerce.Project.Entity.Product;
import com.example.Ecommerce.Project.Entity.Role;
import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Repository.CategoryRepository;
import com.example.Ecommerce.Project.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Category addCategory(Category category, Authentication authentication){
        Users merchant=getLoggedInMerchant(authentication);
        category.setMerchant(merchant);
        return categoryRepository.save(category);
    }

    public List<Category> getCategory(){
        return categoryRepository.findAll();
    }

    public Category getById(Long id){
        return categoryRepository.findById(id).orElseThrow(()->new RuntimeException("Category not found"));
    }


    public Category updateCategory(Long id, Category category, Authentication authentication){
        Users merchant=getLoggedInMerchant(authentication);
        Category existingCategory=categoryRepository.findById(id).orElseThrow(()->new RuntimeException("Category not found"));
        if (existingCategory.getMerchant() == null || !existingCategory.getMerchant().getId() .equals(merchant.getId())) {
            throw new RuntimeException( "You can only manage your own products" );
        }
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

    public void deletecategory(Long id, Authentication authentication){
        Users merchant=getLoggedInMerchant(authentication);
        Category existingCategory=categoryRepository.findById(id).orElseThrow(()->new RuntimeException("Category not found"));
        if (existingCategory.getMerchant() == null || !existingCategory.getMerchant().getId() .equals(merchant.getId())) {
            throw new RuntimeException( "You can delete only your own products" );
        }
        categoryRepository.delete(existingCategory);
    }


    private Users getLoggedInMerchant(Authentication authentication) {
        Users merchant = userRepository.findByEmail( authentication.getName() ).orElseThrow(() -> new RuntimeException("Merchant not found"));
        if (merchant.getRole() != Role.MERCHANT) {
            throw new RuntimeException( "Only merchants can manage products" );
        }
        return merchant;
    }


}
