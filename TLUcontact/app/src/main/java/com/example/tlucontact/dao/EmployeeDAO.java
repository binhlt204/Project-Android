package com.example.tlucontact.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tlucontact.models.Employee;
import com.example.tlucontact.sqllite.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private DatabaseHelper dbHelper;

    public EmployeeDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /** Thêm một nhân viên mới vào cơ sở dữ liệu */
    public long addEmployee(String name, String unit, String position, String phone, String email) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("unit", unit);
        values.put("position", position);
        values.put("phone", phone);
        values.put("email", email);


        long result = db.insert("employees", null, values);
        db.close();
        return result;
    }

    /** Lấy danh sách tất cả nhân viên */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM employees", null);

        while (cursor.moveToNext()) {
            employees.add(new Employee(
                    cursor.getString(1), // name
                    cursor.getString(2), // unit
                    cursor.getString(3), // position
                    cursor.getString(4), // phone
                    cursor.getString(5)  //email
            ));
        }
        return employees;
    }



}
