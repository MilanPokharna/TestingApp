package com.blupie.technotweets.models;

public class contacts {

    public String name;
    public String position;
    public String number;
    public String branch;
    public String email;
    public String image;
    public String bus;
    public String driver;
    public String contact;
    public String route;

    public contacts() { }
    public contacts(String name, String pos,String number,String emailid,String image,String branch )
    {
        this.name = name;
        this.position = pos;
        this.number = number;
        this.email = emailid;
        this.image = image;
        this.branch = branch;
    }

    public String getBranch() {
        return branch;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public contacts(String bus, String driver, String contact, String route )
    {
        this.bus = bus;
        this.driver = driver;

        this.contact = contact;
        this.route = route;
    }

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

}
