package ru.darujo.convertor;

import ru.darujo.dto.user.CategoryDto;
import ru.darujo.dto.user.CategoryEditDto;
import ru.darujo.model.Category;

public class CategoryConvertor {

    public static CategoryEditDto getCategoryEditDto(Category category) {
        return new CategoryEditDto(
                category.getId(),
                category.getName(),
                category.getAmount(),
                category.getAmount());
    }

    public static CategoryDto getCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName());
    }

    public static Category getCategory(CategoryEditDto categoryDto) {
        return new Category(
                categoryDto.getId(),
                categoryDto.getName(),
                categoryDto.getAmount());
    }
}
