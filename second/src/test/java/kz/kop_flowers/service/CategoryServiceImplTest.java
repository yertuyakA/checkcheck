package kz.kop_flowers.service;

import kz.kop_flowers.model.dto.CategoryDto;
import kz.kop_flowers.model.entity.Category;
import kz.kop_flowers.model.exception.CategoryNotFoundException;
import kz.kop_flowers.repository.CategoryRepository;
import kz.kop_flowers.model.FlowerMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private FlowerMapper mapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void getCategoryById_success() {
        Category category = new Category();
        category.setId(1);

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(1);

        assertEquals(1, result.getId());
        verify(categoryRepository).findById(1);
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void getCategoryById_isEmpty_throwsCategoryNotFoundException() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1));
        verify(categoryRepository).findById(1);
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void getCategoryDtoById_success_mapsEntityToDto() {
        Category category = Category.builder().id(5).name("Roses").build();
        CategoryDto expected = CategoryDto.builder().id(5).name("Roses").build();

        when(categoryRepository.findById(5)).thenReturn(Optional.of(category));
        when(mapper.fromEntityToDto(category)).thenReturn(expected);

        CategoryDto result = categoryService.getCategoryDtoById(5);

        assertEquals(expected, result);
        verify(categoryRepository).findById(5);
        verify(mapper).fromEntityToDto(category);
        verifyNoMoreInteractions(categoryRepository, mapper);
    }

    @Test
    void getCategoryDtoById_notFound_throwsAndDoesNotMap() {
        when(categoryRepository.findById(5)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryDtoById(5));
        verify(categoryRepository).findById(5);
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void getAllCategories_empty_returnsEmptyList() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        List<CategoryDto> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryRepository).findAll();
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void getAllCategories_success_mapsEachEntity() {
        Category c1 = Category.builder().id(1).name("A").build();
        Category c2 = Category.builder().id(2).name("B").build();
        when(categoryRepository.findAll()).thenReturn(List.of(c1, c2));

        CategoryDto d1 = CategoryDto.builder().id(1).name("A").build();
        CategoryDto d2 = CategoryDto.builder().id(2).name("B").build();
        when(mapper.fromEntityToDto(c1)).thenReturn(d1);
        when(mapper.fromEntityToDto(c2)).thenReturn(d2);

        List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(List.of(d1, d2), result);
        verify(categoryRepository).findAll();
        verify(mapper).fromEntityToDto(c1);
        verify(mapper).fromEntityToDto(c2);
        verifyNoMoreInteractions(categoryRepository, mapper);
    }

    @Test
    void createCategory_success_savesAndMaps() {
        CategoryDto input = CategoryDto.builder().name("New category").build();
        Category saved = Category.builder().id(10).name("New category").build();
        CategoryDto expected = CategoryDto.builder().id(10).name("New category").build();

        when(categoryRepository.save(any(Category.class))).thenReturn(saved);
        when(mapper.fromEntityToDto(saved)).thenReturn(expected);

        CategoryDto result = categoryService.createCategory(input);

        assertEquals(expected, result);

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(captor.capture());
        assertNull(captor.getValue().getId());
        assertEquals("New category", captor.getValue().getName());

        verify(mapper).fromEntityToDto(saved);
        verifyNoMoreInteractions(categoryRepository, mapper);
    }
}
