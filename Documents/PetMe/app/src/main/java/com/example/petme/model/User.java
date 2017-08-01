package com.example.petme.model;

/**
 * Created by Maria on 15/07/2017.
 */

public class User {
    private String id;

    // pets details
    private String petName;
    private String petType;
    private int petAge;

    // owners details
    private String userName;
    private String userPhone;
    private String userMail;
    private String userAddress;

    private String imagePetUrl;
    private String imageUserUrl;

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public int getPetAge() {
        return petAge;
    }

    public void setPetAge(int petAge) {
        this.petAge = petAge;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id= id;
    }

    public String getImageUserUrl() {
        return imageUserUrl;
    }

    public void setImageUserUrl(String imageUserUrl) {
        this.imageUserUrl = imageUserUrl;
    }

    public String getImagePetUrl() {
        return imagePetUrl;
    }

    public void setImagePetUrl(String imagePetUrl) {
        this.imagePetUrl = imagePetUrl;
    }
}
