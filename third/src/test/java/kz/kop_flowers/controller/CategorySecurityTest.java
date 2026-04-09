package kz.kop_flowers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kop_flowers.configuration.SecurityConfiguration;
import kz.kop_flowers.model.dto.CategoryDto;
import kz.kop_flowers.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@Import(SecurityConfiguration.class)
class CategorySecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    void getAllCategories_isPublic() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of());

        mockMvc.perform(get("/api/category/all"))
                .andExpect(status().isOk());
    }

    @Test
    void createCategory_unauthenticated_isUnauthorized() throws Exception {
        CategoryDto request = CategoryDto.builder().name("New").build();

        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createCategory_asUser_isForbidden() throws Exception {
        CategoryDto request = CategoryDto.builder().name("New").build();

        mockMvc.perform(post("/api/category")
                        .with(httpBasic("user", "parol"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createCategory_asAdmin_isOk() throws Exception {
        CategoryDto request = CategoryDto.builder().name("New").build();
        when(categoryService.createCategory(any(CategoryDto.class)))
                .thenReturn(CategoryDto.builder().id(1).name("New").build());

        mockMvc.perform(post("/api/category")
                        .with(httpBasic("admin", "admin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}

