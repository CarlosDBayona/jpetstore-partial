package com.example.jpetstore.mapper;

import com.example.jpetstore.domain.Item;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemMapper {

    /**
     * Retrieves an item with its product details and stock quantity, or null
     * if no item exists with the given id.
     */
    Item getItem(String itemId);
}
