package com.example.tlucontact;

public class Employee {
    private String name;
    private String position;
    private String phone;
    private String email;
    private String unit;

    public Employee(String name, String position, String phone, String email, String unit) {
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
