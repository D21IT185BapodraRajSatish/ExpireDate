package com.example.expire_date;

public class ProductInfo {

    String Barcode,Name,ExpireDate;

    public ProductInfo(String barcode, String name, String expireDate) {
        Barcode = barcode;
        Name = name;
        ExpireDate = expireDate;
    }

    public ProductInfo() {
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }
}
