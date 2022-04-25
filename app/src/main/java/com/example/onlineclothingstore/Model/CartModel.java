package com.example.onlineclothingstore.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class CartModel implements Parcelable {
    private String key, name, image;
    private int quantity, stockCount;
    private double price, totalPrice;



    public CartModel() {
    }

    protected CartModel(Parcel in) {
        key = in.readString();
        name = in.readString();
        image = in.readString();
        quantity = in.readInt();
        stockCount = in.readInt();
        price = in.readDouble();
        totalPrice = in.readDouble();
    }

    public static final Creator<CartModel> CREATOR = new Creator<CartModel>() {
        @Override
        public CartModel createFromParcel(Parcel in) {
            return new CartModel(in);
        }

        @Override
        public CartModel[] newArray(int size) {
            return new CartModel[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeInt(quantity);
        dest.writeInt(stockCount);
        dest.writeDouble(price);
        dest.writeDouble(totalPrice);
    }
}
