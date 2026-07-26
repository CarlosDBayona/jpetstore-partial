package com.example.jpetstore.service;

import com.example.jpetstore.domain.Category;
import com.example.jpetstore.domain.Product;
import com.example.jpetstore.mapper.CategoryMapper;
import com.example.jpetstore.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogService {

    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    public CatalogService(CategoryMapper categoryMapper, ProductMapper productMapper) {
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    public List<Category> getCategoryList() {
        return categoryMapper.getCategoryList();
    }

    public List<Product> getProductListByCategory(String categoryId) {
        return productMapper.getProductListByCategory(categoryId);
    }

    /**
     * Searches products by keyword, mirroring the legacy
     * CatalogService.searchProductList(keywords): the query is split on
     * whitespace and each term is matched case-insensitively against the
     * product name.
     */
    public List<Product> searchProductList(String keywords) {
        List<Product> products = new ArrayList<>();
        for (String keyword : keywords.split("\\s+")) {
            products.addAll(productMapper.searchProductList("%" + keyword.toLowerCase() + "%"));
        }
        return products;
    }
}
