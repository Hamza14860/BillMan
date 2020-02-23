package com.hamzaazam.fyp_frontend.Model;

public class CategoryM {

    public String catId;
    public String catName;
    public String catUrl;
    public String userID;

    public CategoryM(){}


    public CategoryM(String cat_name, String cat_url, String userIDD ) {
        catName=cat_name;
        catUrl=cat_url;
        userID=userIDD;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatUrl() {
        return catUrl;
    }

    public void setCatUrl(String catUrl) {
        this.catUrl = catUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
