package com.example.jpetstore.mapper;

import com.example.jpetstore.domain.Item;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {

    /**
     * Retrieves the list of item variants belonging to the given product.
     */
    List<Item> getItemListByProduct(String productId);
}
