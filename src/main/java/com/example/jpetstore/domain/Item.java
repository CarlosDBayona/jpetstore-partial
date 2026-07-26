package com.example.jpetstore.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private String itemId;
    private String productId;
    private BigDecimal listPrice;
    private BigDecimal unitCost;
    private int supplierId;
    private String status;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;

    public Item() {
    }

    public Item(String itemId, String productId, BigDecimal listPrice, BigDecimal unitCost, int supplierId,
            String status, String attribute1, String attribute2, String attribute3, String attribute4,
            String attribute5) {
        this.itemId = itemId;
        this.productId = productId;
        this.listPrice = listPrice;
        this.unitCost = unitCost;
        this.supplierId = supplierId;
        this.status = status;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }
}
