package com.example.jpetstore.service;

import com.example.jpetstore.domain.Category;
import com.example.jpetstore.domain.Product;
import com.example.jpetstore.mapper.CategoryMapper;
import com.example.jpetstore.mapper.ProductMapper;
import org.springframework.stereotype.Service;

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
}
