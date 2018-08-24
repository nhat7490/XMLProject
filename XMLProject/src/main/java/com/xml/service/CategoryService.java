package com.xml.service;

import com.xml.model.Category;
import com.xml.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    public final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void save(Category category) {
        this.categoryRepository.save(category);
    }

    public Category findCateByNameLike(String name) {
        return this.categoryRepository.findByNameLike(name);
    }

    public void saveAll(List<Category> categories) {
        this.categoryRepository.saveAll(categories);
    }

    public Category findById(int id) {
        return this.categoryRepository.findById(id);
    }
}
