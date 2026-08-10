package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.Category;
import com.example.Ecommerce.Project.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(Category category){
        return categoryRepository.save(category);
    }

    public List<Category> getCategory(){
        return categoryRepository.findAll();
    }

    public Category getbyId(Long id){
        return categoryRepository.findById(id).orElseThrow(()->new RuntimeException("Category not found"));
    }

    public Category updateCategory(Long id, Category category){
        Category existingCategory=categoryRepository.findById(id).orElseThrow(()->new RuntimeException("Category not found"));
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

    public void deletecategory(Long id){
        Category existingCategory=categoryRepository.findById(id).orElseThrow(()->new RuntimeException("Category not found"));
        categoryRepository.delete(existingCategory);
    }

}
