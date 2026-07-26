package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Category;
import com.example.Ecommerce.Project.Service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")

public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Category addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @GetMapping
    public List<Category> getCategory(@RequestBody Category category){
        return categoryService.getCategory(category);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category){
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id){
        categoryService.deletecategory(id);
        return "category deleted";
    }


}
