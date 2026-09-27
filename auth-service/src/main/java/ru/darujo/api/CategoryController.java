package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.CategoryConvertor;
import ru.darujo.dto.user.CategoryDto;
import ru.darujo.dto.user.CategoryEditDto;
import ru.darujo.service.CategoryService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("")
public class CategoryController {
    private CategoryService categoryService;

    @GetMapping("/admin/categories/{id}")
    public CategoryEditDto getProjectEditDto(@PathVariable long id,
                                             @RequestParam("system_right") List<String> rights) {
        categoryService.checkRight("category_view", rights);
        return CategoryConvertor.getCategoryEditDto(categoryService.findById(id));
    }

    @PostMapping("/admin/categories")
    public CategoryEditDto setProjectDto(@RequestBody CategoryEditDto categoryDto,
                                         @RequestParam("system_right") List<String> rights) {
        categoryService.checkRight("category_edit", rights);
        return CategoryConvertor.getCategoryEditDto(
                categoryService.save(CategoryConvertor.getCategory(categoryDto)));
    }

    @DeleteMapping("/admin/categories/{id}")
    public void delRoleEditDto(@PathVariable long id,
                               @RequestParam("system_right") List<String> rights) {
        categoryService.checkRight("category_edit", rights);
        categoryService.deleteProject(id);
    }


    @GetMapping("/categories")
    public PagedModel<?> getProjects(@RequestParam(required = false) String name,
                                     PagedResourcesAssembler<CategoryDto> pagedAssembler) {
        return pagedAssembler.toModel(categoryService.getListCategory(name).map(CategoryConvertor::getCategoryDto));

    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

}
