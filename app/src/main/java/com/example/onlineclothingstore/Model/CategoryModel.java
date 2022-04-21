package com.example.onlineclothingstore.Model;

import java.util.List;

public class CategoryModel {

    private String cat_id, name, image;
    private List<StockModel> stocks;

    public CategoryModel() {
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<StockModel> getStocks() {
        return stocks;
    }

    public void setStocks(List<StockModel> stocks) {
        this.stocks = stocks;
    }
}
