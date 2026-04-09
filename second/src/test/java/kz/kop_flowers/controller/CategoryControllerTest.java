package kz.kop_flowers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kop_flowers.model.dto.CategoryDto;
import kz.kop_flowers.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    void getAllCategories_returnsJsonArray_andVerifiesServiceCall() throws Exception {
        List<CategoryDto> categories = List.of(
                CategoryDto.builder().id(1).name("A").build(),
                CategoryDto.builder().id(2).name("B").build()
        );
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/category/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("A"));

        verify(categoryService).getAllCategories();
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    void createCategory_postsJson_andVerifiesServiceCall() throws Exception {
        CategoryDto requestDto = CategoryDto.builder().name("New").build();
        CategoryDto responseDto = CategoryDto.builder().id(10).name("New").build();

        when(categoryService.createCategory(any(CategoryDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("New"));

        ArgumentCaptor<CategoryDto> captor = ArgumentCaptor.forClass(CategoryDto.class);
        verify(categoryService).createCategory(captor.capture());
        assertEquals("New", captor.getValue().getName());
        verifyNoMoreInteractions(categoryService);
    }
}

