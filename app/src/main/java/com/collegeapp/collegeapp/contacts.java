package com.collegeapp.collegeapp;

public class contacts {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPos() {
        return position;
    }

    public void setPos(String position) {
        this.position = position;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String name,position,number,email,image;
    public contacts() { }
    public contacts(String name, String pos,String number,String emailid,String image )
    {
        this.name = name;
        this.position = pos;
        this.number = number;
        this.email = emailid;
        this.image = image;
    }
}
