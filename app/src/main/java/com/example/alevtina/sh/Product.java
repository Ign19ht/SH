package com.example.alevtina.sh;

public class Product {

    private String name;
    private int kall;
    private int gramm;

    public Product(int kall, String name, int gramm) {
        this.kall = kall;
        this.name = name;
        this.gramm = gramm;
    }

    public Product(int kall, String name) {
        this.kall = kall;
        this.name = name;
    }

    public Product(int kall, int gramm) {
        this.kall = kall;
        this.gramm = gramm;
    }

    public int getgramm() {
        return gramm;
    }

    public void setGramm(int gramm) {
        this.gramm = gramm;
    }

    public int getkall() {
        return kall;
    }

    public String getname() {
        return name;
    }

    public void setkall(int kall) {
        this.kall = kall;
    }

    public void setname(String name) {
        this.name = name;
    }
}
