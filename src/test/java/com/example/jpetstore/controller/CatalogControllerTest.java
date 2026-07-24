package com.example.jpetstore.controller;

import com.example.jpetstore.domain.Category;
import com.example.jpetstore.mapper.CategoryMapper;
import com.example.jpetstore.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CatalogController.class)
@MockBean(CategoryMapper.class)
class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatalogService catalogService;

    @Test
    void getAllCategories_ShouldReturnListOfCategories() throws Exception {
        Category fish = new Category("FISH", "Fish", "Fresh and Saltwater Fish");
        Category dogs = new Category("DOGS", "Dogs", "Various Dog Breeds");

        Mockito.when(catalogService.getCategoryList()).thenReturn(List.of(fish, dogs));

        mockMvc.perform(get("/api/catalog/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].categoryId", is("FISH")))
                .andExpect(jsonPath("$[0].name", is("Fish")))
                .andExpect(jsonPath("$[1].categoryId", is("DOGS")))
                .andExpect(jsonPath("$[1].name", is("Dogs")));
    }
}
