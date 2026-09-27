package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.Category;
import ru.darujo.repository.CategoryRepository;
import ru.darujo.specifications.Specifications;

import java.util.List;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;
    @Getter
    private static CategoryService Instance;

    @PostConstruct
    public void setInstance() {
        Instance = this;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<Category> getListCategory(String name) {

        return Specifications.findAll(
                categoryRepository,
                null,
                null,
                Specifications.eq(
                        Specification.unrestricted(),
                        "name",
                        name),
                "name");
    }

    public Category findById(Long id) {
        if (id == null) {
            return null;
        }
        return categoryRepository.findById(id).orElse(null);
    }

    public Category getCategoryByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name).orElse(null);
    }

    public Category save(Category category) {
        Category saveCategory = getCategoryByName(category.getName());
        if (category.getId() == null) {
            if (saveCategory != null) {
                throw new ResourceNotFoundRunTime("Уже есть категория с таким наименованием.");
            }
        } else {
            if (saveCategory != null && !saveCategory.getId().equals(category.getId())) {
                throw new ResourceNotFoundRunTime("Уже есть категория с таким наименованием.");
            }
        }
        return categoryRepository.save(category);
    }

    public void deleteProject(long id) {
        categoryRepository.deleteById(id);
    }

    public void checkRight(String right, List<String> rights) {
        if (right.equals("category_view") && !rights.contains("CATEGORY_AMOUNT")) {
            throw new ResourceNotFoundRunTime("У вас не права просмотра категорий");
        }
        if (right.equals("category_edit") && !rights.contains("CATEGORY_EDIT")) {
            throw new ResourceNotFoundRunTime("У вас не права редактирования категорий");
        }
    }
}
