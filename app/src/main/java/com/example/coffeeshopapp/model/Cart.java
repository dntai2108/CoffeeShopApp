package com.example.coffeeshopapp.model;

public class Cart {
    int ID, IDCustomer;
    String Expỉytime;
    int Status;

    public Cart(int ID, int status) {
        this.ID = ID;
        Status = status;
    }
    public Cart() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIDCustomer() {
        return IDCustomer;
    }

    public void setIDCustomer(int IDCustomer) {
        this.IDCustomer = IDCustomer;
    }

    public String getExpỉytime() {
        return Expỉytime;
    }

    public void setExpỉytime(String expỉytime) {
        Expỉytime = expỉytime;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
