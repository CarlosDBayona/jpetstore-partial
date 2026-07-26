package com.example.jpetstore.controller;

import com.example.jpetstore.domain.Category;
import com.example.jpetstore.domain.Product;
import com.example.jpetstore.mapper.CategoryMapper;
import com.example.jpetstore.mapper.ProductMapper;
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
@MockBean(ProductMapper.class)
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

    @Test
    void getProductListByCategory_ShouldReturnProductsForCategory() throws Exception {
        Product angelfish = new Product("FI-SW-01", "FISH", "Angelfish", "Salt Water fish from Australia");
        Product tigerShark = new Product("FI-SW-02", "FISH", "Tiger Shark", "Salt Water fish from Australia");

        Mockito.when(catalogService.getProductListByCategory("FISH")).thenReturn(List.of(angelfish, tigerShark));

        mockMvc.perform(get("/api/catalog/categories/FISH/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productId", is("FI-SW-01")))
                .andExpect(jsonPath("$[0].categoryId", is("FISH")))
                .andExpect(jsonPath("$[0].name", is("Angelfish")))
                .andExpect(jsonPath("$[1].productId", is("FI-SW-02")))
                .andExpect(jsonPath("$[1].name", is("Tiger Shark")));
    }

    @Test
    void searchProducts_ShouldReturnMatchingProducts() throws Exception {
        Product angelfish = new Product("FI-SW-01", "FISH", "Angelfish", "Salt Water fish from Australia");
        Product goldfish = new Product("FI-FW-02", "FISH", "Goldfish", "Fresh Water fish from China");

        Mockito.when(catalogService.searchProductList("fish")).thenReturn(List.of(angelfish, goldfish));

        mockMvc.perform(get("/api/catalog/products/search")
                .param("keyword", "fish")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productId", is("FI-SW-01")))
                .andExpect(jsonPath("$[0].name", is("Angelfish")))
                .andExpect(jsonPath("$[1].productId", is("FI-FW-02")))
                .andExpect(jsonPath("$[1].name", is("Goldfish")));
    }

    @Test
    void searchProducts_ShouldReturnBadRequestWhenKeywordIsMissing() throws Exception {
        mockMvc.perform(get("/api/catalog/products/search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchProducts_ShouldReturnBadRequestWhenKeywordIsEmpty() throws Exception {
        mockMvc.perform(get("/api/catalog/products/search")
                .param("keyword", "  ")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
