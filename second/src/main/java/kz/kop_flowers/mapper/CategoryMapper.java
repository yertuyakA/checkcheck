package kz.kop_flowers.mapper;

import kz.kop_flowers.model.dto.CategoryDto;
import kz.kop_flowers.model.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        if (category == null) return null;

        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public Category toEntity(CategoryDto dto) {
        if (dto == null) return null;

        return new Category(
                dto.getId(),
                dto.getName()
        );
    }
}