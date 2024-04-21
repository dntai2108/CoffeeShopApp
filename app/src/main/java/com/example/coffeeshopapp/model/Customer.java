package com.example.coffeeshopapp.model;

public class Customer {

    private String id;
    private String name;
    private String address;
    private String phone;
    private String gender;
    private Account account;

    public Customer(String id, String name, String address, String phone, String gender, Account account) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.account = account;
    }
    public Customer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
