package com.example.onlineclothingstore.Constants;

import com.example.onlineclothingstore.Model.CategoryModel;
import com.example.onlineclothingstore.Model.StockModel;

public class Constants {
    public static final String USER_REFERENCES = "Users";
    public static final String ADMIN_REF = "Admin";
    public static final String CATEGORY_REFERENCE = "Category";

    public static CategoryModel categorySelected;
    public static StockModel selectedStock;

    public enum ACTION {
        CREATE,
        UPDATE,
        DELETE
    }
}
