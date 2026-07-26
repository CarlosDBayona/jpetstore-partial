package com.example.jpetstore.controller;

import com.example.jpetstore.domain.Category;
import com.example.jpetstore.domain.Product;
import com.example.jpetstore.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * GET /api/catalog/products/search?keyword={query} - Search products by keyword.
     * Implements Issue #6. Returns 400 Bad Request if the keyword parameter is
     * missing or empty.
     */
    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam(required = false) String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Product> products = catalogService.searchProductList(keyword.trim());
        return ResponseEntity.ok(products);
    }
}
