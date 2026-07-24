package com.example.jpetstore.service;

import com.example.jpetstore.domain.Category;
import com.example.jpetstore.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {

    private final CategoryMapper categoryMapper;

    public CatalogService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public List<Category> getCategoryList() {
        return categoryMapper.getCategoryList();
    }
}
