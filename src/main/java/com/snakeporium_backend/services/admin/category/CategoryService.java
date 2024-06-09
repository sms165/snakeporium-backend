package com.snakeporium_backend.services.admin.category;

import com.snakeporium_backend.dto.CategoryDto;
import com.snakeporium_backend.entity.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(CategoryDto categoryDto);

    List<Category> getAllCategories();
}
