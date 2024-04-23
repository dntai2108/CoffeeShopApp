package com.example.coffeeshopapp.model;

public class Coupon {
    private String id;
    private String tenMa;
    private String phanTramGiam;
    private String ngayBatDau;
    private String ngayKetThuc;

    public Coupon() {
    }

    public Coupon(String id, String tenMa, String phanTramGiam, String ngayBatDau, String ngayKetThuc) {
        this.id = id;
        this.tenMa = tenMa;
        this.phanTramGiam = phanTramGiam;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenMa() {
        return tenMa;
    }

    public void setTenMa(String tenMa) {
        this.tenMa = tenMa;
    }

    public String getPhanTramGiam() {
        return phanTramGiam;
    }

    public void setPhanTramGiam(String phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }
}
