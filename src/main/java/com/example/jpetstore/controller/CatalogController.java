package com.example.jpetstore.controller;

import com.example.jpetstore.domain.Category;
import com.example.jpetstore.domain.Item;
import com.example.jpetstore.domain.Product;
import com.example.jpetstore.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@CrossOrigin(origins = "*")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /**
     * GET /api/catalog/categories - List all pet categories.
     * Implements Issue #1.
     */
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = catalogService.getCategoryList();
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /api/catalog/categories/{categoryId}/products - List products by category.
     * Implements Issue #2.
     */
    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<List<Product>> getProductListByCategory(@PathVariable String categoryId) {
        List<Product> products = catalogService.getProductListByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/catalog/items/{itemId} - Item detail and stock quantity.
     * Implements Issue #5. Returns 404 Not Found when the item does not exist.
     */
    @GetMapping("/items/{itemId}")
    public ResponseEntity<Item> getItem(@PathVariable String itemId) {
        Item item = catalogService.getItem(itemId);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }
}
