package com.example.cofex_project_ba26_group6.Data;

public class Cafe {
    int id;
    String cafeName;
    String address;
    String LAT;
    String LNG;
    String image;
    int rate;

    public Cafe(int id, String cafeName, String address, String LAT, String LNG, String image, int rate) {
        this.id = id;
        this.cafeName = cafeName;
        this.address = address;
        this.LAT = LAT;
        this.LNG = LNG;
        this.image = image;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCafeName() {
        return cafeName;
    }

    public void setCafeName(String cafeName) {
        this.cafeName = cafeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLNG() {
        return LNG;
    }

    public void setLNG(String LNG) {
        this.LNG = LNG;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
