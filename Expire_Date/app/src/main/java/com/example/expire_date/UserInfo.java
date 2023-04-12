package com.example.expire_date;

public class UserInfo {
    private String U_Name,U_Address,U_Phno,U_email,U_Password,U_Gender;

    public UserInfo() {
    }

    public UserInfo(String u_Name, String u_Address, String u_Phno, String u_email, String u_Password, String u_Gender) {
        U_Name = u_Name;
        U_Address = u_Address;
        U_Phno = u_Phno;
        U_email = u_email;
        U_Password = u_Password;
        U_Gender = u_Gender;
    }

    public String getU_Gender() {
        return U_Gender;
    }

    public void setU_Gender(String u_Gender) {
        U_Gender = u_Gender;
    }

    public String getU_Name() {
        return U_Name;
    }

    public void setU_Name(String u_Name) {
        U_Name = u_Name;
    }

    public String getU_Address() {
        return U_Address;
    }

    public void setU_Address(String u_Address) {
        U_Address = u_Address;
    }

    public String getU_Phno() {
        return U_Phno;
    }

    public void setU_Phno(String u_Phno) {
        U_Phno = u_Phno;
    }

    public String getU_email() {
        return U_email;
    }

    public void setU_email(String u_email) {
        U_email = u_email;
    }

    public String getU_Password() {
        return U_Password;
    }

    public void setU_Password(String u_Password) {
        U_Password = u_Password;
    }
}
