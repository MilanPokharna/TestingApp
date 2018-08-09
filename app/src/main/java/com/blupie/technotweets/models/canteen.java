package com.blupie.technotweets.models;

public class canteen {
    public String dish;
    public String dishprice;
    public String image;

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public String getDishprice() {
        return dishprice;
    }

    public void setDishprice(String dishprice) {
        this.dishprice = dishprice;
    }

    public canteen(){}

    public String getImg() {
        return image;
    }

    public void setImg(String image) {
        this.image = image;
    }

    public canteen(String dish , String dishprice, String image)
    {

        this.dish = dish;
        this.dishprice = dishprice;
        this.image = image;
    }
}