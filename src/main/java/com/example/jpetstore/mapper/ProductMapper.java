package com.example.jpetstore.mapper;

import com.example.jpetstore.domain.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    /**
     * Retrieves a single product by its id, or null if it does not exist.
     */
    Product getProduct(String productId);

    /**
     * Retrieves the list of products belonging to the given category.
     */
    List<Product> getProductListByCategory(String categoryId);
}
