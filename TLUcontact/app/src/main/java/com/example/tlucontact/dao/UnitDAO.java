package com.example.tlucontact.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tlucontact.models.Unit;
import com.example.tlucontact.sqllite.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class UnitDAO {
    private DatabaseHelper dbHelper;

    public UnitDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /** 📌 Thêm đơn vị */
    public long addUnit(String name, String phone, String address) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("address", address);

        long result = db.insert("units", null, values);
        db.close();
        return result;
    }

    /** 📌 Lấy danh sách tất cả đơn vị */
    public List<Unit> getAllUnits() {
        List<Unit> units = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM units", null);
        while (cursor.moveToNext()) {
            units.add(new Unit(
                    cursor.getString(1), // name
                    cursor.getString(2), // phone
                    cursor.getString(3)  // address
            ));
        }
        cursor.close();
        db.close();
        return units;
    }

    /** 📌 Sửa đơn vị theo tên */
    public int updateUnitByName(String oldName, String newName, String phone, String address) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("phone", phone);
        values.put("address", address);

        int rowsAffected = db.update("units", values, "name = ?", new String[]{oldName});
        db.close();
        return rowsAffected;
    }

    /** 📌 Xóa đơn vị theo tên */
    public int deleteUnitByName(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("units", "name = ?", new String[]{name});
        db.close();
        return rowsDeleted;
    }
}
