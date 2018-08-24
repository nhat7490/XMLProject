package com.xml.repository;

import com.xml.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    Category findByName(String name);

    Category findByNameLike(String name);

    Category findById(int id);
}
