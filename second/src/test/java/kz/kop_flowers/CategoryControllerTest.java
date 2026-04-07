package kz.kop_flowers;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kop_flowers.controller.CategoryController;
import kz.kop_flowers.model.dto.CategoryDto;
import kz.kop_flowers.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetAllCategories() throws Exception {
        // Делаем публичные CategoryDto
        List<CategoryDto> categories = List.of(
                new CategoryDto(1, "Birthday"),
                new CategoryDto(2, "Anniversary")
        );

        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/category/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Birthday"))
                .andExpect(jsonPath("$[1].name").value("Anniversary"));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void testCreateCategory() throws Exception {
        CategoryDto requestDto = new CategoryDto(null, "Holiday");
        CategoryDto responseDto = new CategoryDto(1, "Holiday");

        when(categoryService.createCategory(any(CategoryDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Holiday"));

        verify(categoryService, times(1)).createCategory(any(CategoryDto.class));
    }
}
