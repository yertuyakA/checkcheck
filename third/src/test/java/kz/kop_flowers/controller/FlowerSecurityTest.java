package kz.kop_flowers.controller;

import kz.kop_flowers.configuration.SecurityConfiguration;
import kz.kop_flowers.model.dto.FlowerDto;
import kz.kop_flowers.service.FlowerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlowerController.class)
@Import(SecurityConfiguration.class)
class FlowerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlowerService flowerService;

    @Test
    void getFlowers_isPublic() throws Exception {
        when(flowerService.getAllFlowers()).thenReturn(List.of());

        mockMvc.perform(get("/api/flowers"))
                .andExpect(status().isOk());
    }

    @Test
    void createFlower_unauthenticated_isUnauthorized() throws Exception {
        mockMvc.perform(post("/api/flowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createFlower_asAdmin_isAllowedBySecurityLayer() throws Exception {
        mockMvc.perform(post("/api/flowers")
                        .with(httpBasic("admin", "admin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }
}
