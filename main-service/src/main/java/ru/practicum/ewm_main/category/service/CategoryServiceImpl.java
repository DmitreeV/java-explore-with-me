package ru.practicum.ewm_main.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main.category.dto.CategoryDto;
import ru.practicum.ewm_main.category.dto.NewCategoryDto;
import ru.practicum.ewm_main.category.model.Category;
import ru.practicum.ewm_main.category.repository.CategoryRepository;
import ru.practicum.ewm_main.error.exception.NotFoundException;

import java.util.List;

import static ru.practicum.ewm_main.category.CategoryMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        Category category = toCategory(newCategoryDto);
        log.info("Категория сохранена.");
        return toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        Category categoryToUpdate = getCategory(categoryId);
        Category category = toCategory(categoryDto);
        if (category.getName() != null) {
            categoryToUpdate.setName(category.getName());
        }
        log.info("Данные категории обновлены.");
        return toCategoryDto(categoryRepository.save(categoryToUpdate));
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        return toCategoryDto(getCategory(categoryId));
    }

    @Override
    public List<CategoryDto> getAllCategories(int from, int size) {
        log.info("Получен список всех категорий.");
        return toCategoriesDto(categoryRepository.findAll(PageRequest.of(from / size, size)));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        log.info("Категория удалена.");
        categoryRepository.deleteById(categoryId);
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Неверный ID категории."));
    }
}
