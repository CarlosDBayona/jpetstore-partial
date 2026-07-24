package com.example.jpetstore.mapper;

import com.example.jpetstore.domain.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * Retrieves all pet categories from the database.
     */
    List<Category> getCategoryList();
}
