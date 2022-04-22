package com.example.onlineclothingstore.Constants;

import com.example.onlineclothingstore.Model.CategoryModel;
import com.example.onlineclothingstore.Model.RatingModel;
import com.example.onlineclothingstore.Model.StockModel;
import com.example.onlineclothingstore.Model.UserModel;

public class Constants {
    public static final String USER_REFERENCES = "Users";
    public static final String ADMIN_REF = "Admin";
    public static final String CATEGORY_REFERENCE = "Category";
    public static final String RATING_REF = "Ratings";
    public static final String CART_REF = "Cart";

    public static CategoryModel categorySelected;
    public static StockModel selectedStock;

    public static final int DEFAULT_COLUMN_COUNT = 0;
    public static final int FULL_WIDTH_COLUMN = 1;
    public static UserModel currentUser;

    public enum ACTION {
        CREATE,
        UPDATE,
        DELETE
    }
}
