package com.example.jpetstore.controller;

import com.example.jpetstore.domain.Category;
import com.example.jpetstore.domain.Item;
import com.example.jpetstore.domain.Product;
import com.example.jpetstore.mapper.CategoryMapper;
import com.example.jpetstore.mapper.ItemMapper;
import com.example.jpetstore.mapper.ProductMapper;
import com.example.jpetstore.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CatalogController.class)
@MockBean(CategoryMapper.class)
@MockBean(ProductMapper.class)
@MockBean(ItemMapper.class)
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
    void getItem_ShouldReturnItemDetails() throws Exception {
        Item item = new Item();
        item.setItemId("EST-1");
        item.setListPrice(new BigDecimal("16.50"));
        item.setUnitCost(new BigDecimal("10.00"));
        item.setSupplierId(1);
        item.setStatus("P");
        item.setAttribute1("Large");
        item.setQuantity(10000);
        item.setProduct(new Product("FI-SW-01", "FISH", "Angelfish", "Salt Water fish from Australia"));

        Mockito.when(catalogService.getItem("EST-1")).thenReturn(item);

        mockMvc.perform(get("/api/catalog/items/EST-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId", is("EST-1")))
                .andExpect(jsonPath("$.listPrice", is(16.50)))
                .andExpect(jsonPath("$.unitCost", is(10.00)))
                .andExpect(jsonPath("$.status", is("P")))
                .andExpect(jsonPath("$.attribute1", is("Large")))
                .andExpect(jsonPath("$.quantity", is(10000)))
                .andExpect(jsonPath("$.product.productId", is("FI-SW-01")))
                .andExpect(jsonPath("$.product.name", is("Angelfish")));
    }

    @Test
    void getItem_ShouldReturnNotFoundWhenItemDoesNotExist() throws Exception {
        Mockito.when(catalogService.getItem("EST-404")).thenReturn(null);

        mockMvc.perform(get("/api/catalog/items/EST-404")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
