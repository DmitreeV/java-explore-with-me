package ru.practicum.ewm_main.category.service;

import ru.practicum.ewm_main.category.dto.CategoryDto;
import ru.practicum.ewm_main.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto saveCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(Long categoryId, NewCategoryDto categoryDto);

    CategoryDto getCategoryById(Long categoryId);

    List<CategoryDto> getAllCategories(Integer from, Integer size);

    void deleteCategory(Long categoryId);
}
