package com.potato.oneall.bean;

public class Person {

    private String userName;
    private String password;
    private String tel;

    public Person(String userName, String password, String tel) {
        this.userName = userName;
        this.password = password;
        this.tel = tel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "Person{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}
