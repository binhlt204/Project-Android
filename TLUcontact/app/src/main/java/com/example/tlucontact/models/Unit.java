package com.example.tlucontact.models;
import java.util.ArrayList;
import java.util.List;

public class Unit {
    private String unitId;
    private String name;
    private String phone;
    private String address;
    private List<String> subUnits;
    private List<String> employeeNames; // Thay employeeIds thành employeeNames

    // Constructor

    public Unit() {
    }

    public Unit(String unitId, String name, String phone, String address, List<String> subUnits, List<String> employeeNames) {
        this.unitId = unitId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.subUnits = subUnits != null ? subUnits : new ArrayList<>();
        this.employeeNames = employeeNames != null ? employeeNames : new ArrayList<>();
    }

    // Getters và Setters
    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getSubUnits() {
        return subUnits;
    }

    public void setSubUnits(List<String> subUnits) {
        this.subUnits = subUnits;
    }

    public List<String> getEmployeeNames() {
        return employeeNames;
    }

    public void setEmployeeNames(List<String> employeeNames) {
        this.employeeNames = employeeNames;
    }
}