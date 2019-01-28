package com.example.alevtina.sh;

public class Product {

    int gramm, kall;
    String name;

    public Product(int kall, String name, int gramm) {
        this.gramm = gramm;
        this.kall = kall;
        this.name = name;
    }

    public Product(int kall, String name) {
        this.kall = kall;
        this.name = name;
    }

    public int getgramm() {
        return gramm;
    }

    public int getkall() {
        return kall;
    }

    public String getname() {
        return name;
    }
}