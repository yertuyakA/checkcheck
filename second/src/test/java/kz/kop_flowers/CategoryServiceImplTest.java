package kz.kop_flowers;

import kz.kop_flowers.mapper.CategoryMapper;
import kz.kop_flowers.model.dto.CategoryDto;
import kz.kop_flowers.model.entity.Category;
import kz.kop_flowers.model.exception.CategoryNotFoundException;
import kz.kop_flowers.repository.CategoryRepository;
import kz.kop_flowers.service.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void testGetAllCategories() {
        List<Category> categories = List.of(
                new Category(1, "Birthday"),
                new Category(2, "Anniversary")
        );

        List<CategoryDto> categoryDtos = List.of(
                new CategoryDto(1, "Birthday"),
                new CategoryDto(2, "Anniversary")
        );

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDto(categories.get(0))).thenReturn(categoryDtos.get(0));
        when(categoryMapper.toDto(categories.get(1))).thenReturn(categoryDtos.get(1));

        List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Birthday", result.get(0).getName());
        assertEquals("Anniversary", result.get(1).getName());

        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(2)).toDto(any(Category.class));
    }

    @Test
    void getCategoryById_isEmpty() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1));

        verify(categoryRepository, times(1)).findById(1);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    void getCategoryDtoById_success() {
        Category category = new Category(1, "Birthday");
        CategoryDto dto = new CategoryDto(1, "Birthday");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(dto);

        CategoryDto result = categoryService.getCategoryDtoById(1);

        assertEquals(1, result.getId());
        assertEquals("Birthday", result.getName());

        verify(categoryRepository, times(1)).findById(1);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    void testCreateCategory() {
        CategoryDto requestDto = new CategoryDto(null, "Holiday");
        Category entity = new Category(null, "Holiday");
        Category savedEntity = new Category(1, "Holiday");
        CategoryDto responseDto = new CategoryDto(1, "Holiday");

        when(categoryMapper.toEntity(requestDto)).thenReturn(entity);
        when(categoryRepository.save(entity)).thenReturn(savedEntity);
        when(categoryMapper.toDto(savedEntity)).thenReturn(responseDto);

        CategoryDto result = categoryService.createCategory(requestDto);

        assertEquals(1, result.getId());
        assertEquals("Holiday", result.getName());

        verify(categoryMapper).toEntity(requestDto);
        verify(categoryRepository).save(entity);
        verify(categoryMapper).toDto(savedEntity);
    }
}
