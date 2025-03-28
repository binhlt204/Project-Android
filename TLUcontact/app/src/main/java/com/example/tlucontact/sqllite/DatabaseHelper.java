package com.example.tlucontact.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tlu_contact.db";
    private static final int DATABASE_VERSION = 2;

    // Bảng Unit
    public static final String TABLE_UNITS = "units";
    public static final String COLUMN_UNIT_ID = "id";
    public static final String COLUMN_UNIT_NAME = "name";
    public static final String COLUMN_UNIT_PHONE = "phone";
    public static final String COLUMN_UNIT_ADDRESS = "address";

    private static final String CREATE_TABLE_UNITS =
            "CREATE TABLE " + TABLE_UNITS + " ("
                    + COLUMN_UNIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_UNIT_NAME + " TEXT, "
                    + COLUMN_UNIT_PHONE + " TEXT, "
                    + COLUMN_UNIT_ADDRESS + " TEXT);";

    // Bảng Employee
    public static final String TABLE_EMPLOYEES = "employees";
    public static final String COLUMN_EMPLOYEE_ID = "id";
    public static final String COLUMN_EMPLOYEE_NAME = "name";
    public static final String COLUMN_EMPLOYEE_ROLE = "position";
    public static final String COLUMN_EMPLOYEE_PHONE = "phone";
    public static final String COLUMN_EMPLOYEE_EMAIL = "email";
    public static final String COLUMN_EMPLOYEE_UNIT = "unit";

    private static final String CREATE_TABLE_EMPLOYEES =
            "CREATE TABLE " + TABLE_EMPLOYEES + " ("
                    + COLUMN_EMPLOYEE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_EMPLOYEE_NAME + " TEXT, "
                    + COLUMN_EMPLOYEE_ROLE + " TEXT, "
                    + COLUMN_EMPLOYEE_PHONE + " TEXT, "
                    + COLUMN_EMPLOYEE_EMAIL + " TEXT, "
                    + COLUMN_EMPLOYEE_UNIT + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_UNITS);
        db.execSQL(CREATE_TABLE_EMPLOYEES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNITS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        onCreate(db);
        // Chỉ nâng cấp nếu phiên bản mới cao hơn phiên bản cũ
        if (oldVersion < newVersion) {
            // Kiểm tra và thêm cột "position" vào bảng "employees" nếu chưa có
            try {
                // Thêm cột "position" vào bảng "employees" nếu chưa tồn tại
                db.execSQL("ALTER TABLE " + TABLE_EMPLOYEES + " ADD COLUMN " + COLUMN_EMPLOYEE_ROLE + " TEXT;");
            } catch (Exception e) {
                // Xử lý ngoại lệ nếu cột đã tồn tại hoặc có vấn đề khác
                e.printStackTrace();
            }
        }
    }
}
