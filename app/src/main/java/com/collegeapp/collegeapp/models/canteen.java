package com.collegeapp.collegeapp.models;

public class canteen {
    public String dish;
    public String dishprice;

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

    public canteen(String dish ,String dishprice)
    {
        this.dish = dish;
        this.dishprice = dishprice;
    }
}
