package com.example.coffeeshopapp.model;

public class Account {

    // Account: TenTK, MatKhau, LoaiTK (TKKH,Nv)
    private String username;

    private String password;

    private String role;

    private boolean State;

    public Account(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.State=true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isState() {
        return State;
    }

    public void setState(boolean state) {
        State = state;
    }
}
