package com.example.tlucontact.models;

import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;
import java.util.List;

public class Unit {
    private String unitId; // Mã đơn vị
    private String name;
    private String phone;
    private String address;
    private List<String> subUnits; // Danh sách đơn vị con
    private List<String> employeeIds; // Danh sách ID của cán bộ giảng viên

    public Unit() {
        this.subUnits = new ArrayList<>();
        this.employeeIds = new ArrayList<>();
    }

    public Unit(String unitId, String name, String phone, String address, List<String> subUnits, List<String> employeeIds) {
        this.unitId = unitId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.subUnits = subUnits != null ? subUnits : new ArrayList<>();
        this.employeeIds = employeeIds != null ? employeeIds : new ArrayList<>();
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    //    @PropertyName("unitName") // Maps "unitName" in Firestore to "name" in Java
    public String getName() {
        return name;
    }

    //    @PropertyName("unitName")
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
        this.subUnits = subUnits != null ? subUnits : new ArrayList<>();
    }

    public List<String> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<String> employeeIds) {
        this.employeeIds = employeeIds != null ? employeeIds : new ArrayList<>();
    }
}