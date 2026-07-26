package com.example.jpetstore.service;

import com.example.jpetstore.domain.Category;
import com.example.jpetstore.domain.Item;
import com.example.jpetstore.domain.Product;
import com.example.jpetstore.mapper.CategoryMapper;
import com.example.jpetstore.mapper.ItemMapper;
import com.example.jpetstore.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {

    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final ItemMapper itemMapper;

    public CatalogService(CategoryMapper categoryMapper, ProductMapper productMapper, ItemMapper itemMapper) {
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
        this.itemMapper = itemMapper;
    }

    public List<Category> getCategoryList() {
        return categoryMapper.getCategoryList();
    }

    public List<Product> getProductListByCategory(String categoryId) {
        return productMapper.getProductListByCategory(categoryId);
    }

    /**
     * Retrieves item detail and stock quantity, mirroring the legacy
     * CatalogService.getItem(itemId). Returns null when the item does not exist.
     */
    public Item getItem(String itemId) {
        return itemMapper.getItem(itemId);
    }
}
