package com.collegeapp.collegeapp.models;

public class User {
    String profileimage;
    String email;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    String userid;
    public User(){

    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    String posttime;

    public User(String profileimage, String email, String name, String postimage, String postdata,String posttime,String userid) {
        this.profileimage = profileimage;
        this.email = email;
        this.name = name;
        this.postimage = postimage;
        this.postdata = postdata;
        this.posttime=posttime;
        this.userid = userid;
    }

    String name;

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getPostdata() {
        return postdata;
    }

    public void setPostdata(String postdata) {
        this.postdata = postdata;
    }

    String postimage;
    String postdata;
}

