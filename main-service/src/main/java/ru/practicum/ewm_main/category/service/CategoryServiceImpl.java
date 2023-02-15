package ru.practicum.ewm_main.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main.category.dto.CategoryDto;
import ru.practicum.ewm_main.category.dto.NewCategoryDto;
import ru.practicum.ewm_main.category.model.Category;
import ru.practicum.ewm_main.category.repository.CategoryRepository;
import ru.practicum.ewm_main.error.exception.ConflictException;
import ru.practicum.ewm_main.error.exception.NotFoundException;
import ru.practicum.ewm_main.event.repository.EventRepository;

import java.util.List;

import static ru.practicum.ewm_main.category.mapper.CategoryMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        Category category = toCategory(newCategoryDto);
        try {
            categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Категория уже существует.");
        }
        log.info("Категория сохранена.");
        return toCategoryDto(category);
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, NewCategoryDto newCategoryDto) {
        Category categoryToUpdate = getCategory(categoryId);
        Category category = toCategory(newCategoryDto);

        categoryToUpdate.setName(category.getName());

        try {
            categoryRepository.save(categoryToUpdate);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Имя категории уже существует.");
        }
        log.info("Данные категории обновлены.");
        return toCategoryDto(categoryToUpdate);
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        return toCategoryDto(getCategory(categoryId));
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        log.info("Получен список всех категорий.");
        return toCategoriesDto(categoryRepository.findAll(PageRequest.of(from / size, size)));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (!eventRepository.findAllByCategoryId(categoryId).isEmpty()) {
            throw new ConflictException("Невозможно удалить категорию, когда в ней есть события.");
        }
        categoryRepository.deleteById(categoryId);
        log.info("Категория удалена.");
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Неверный ID категории."));
    }
}
