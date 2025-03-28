package com.example.tlucontact.data;

import android.content.Context;

import com.example.tlucontact.dao.EmployeeDAO;
import com.example.tlucontact.dao.UnitDAO;
import com.example.tlucontact.models.Employee;
import com.example.tlucontact.models.Unit;
import java.util.List;

public class SampleData {
    public static void insertSampleData(Context context) {
        UnitDAO unitDAO = new UnitDAO(context);
        EmployeeDAO employeeDAO = new EmployeeDAO(context);


        // Kiểm tra xem database có dữ liệu chưa, nếu chưa thì thêm
        if (unitDAO.getAllUnits().isEmpty()) {
            unitDAO.addUnit("Khoa CNTT", "0123456789", "Tầng 5, Nhà C1");
            unitDAO.addUnit("Khoa Kinh tế", "0987654321", "Tầng 3, Nhà C2");
            unitDAO.addUnit("Khoa Ngữ Anh", "0123456789", "Tầng 5, Nhà C1");
            unitDAO.addUnit("Khoa Luật", "0123456789", "Tầng 5, Nhà C1");
            unitDAO.addUnit("Khoa Kiểm toán", "0123456789", "Tầng 5, Nhà C1");
        }

        if (employeeDAO.getAllEmployees().isEmpty()) {
            employeeDAO.addEmployee("Nguyễn Văn Thẩm", "Giảng viên", "0912345678", "nva@tlu.edu.vn", "Khoa CNTT");
            employeeDAO.addEmployee("Trương Xuân Nam", "Giảng viên", "0912345678", "lvc@tlu.edu.vn", "Khoa CNTT");
            employeeDAO.addEmployee("Kiều Tuấn Dũng", "Giảng viên", "0912345678", "tra@tlu.edu.vn", "Khoa CNTT");
            employeeDAO.addEmployee("Lê Văn Phương", "Trưởng khoa", "0987654321", "ttb@tlu.edu.vn", "Khoa Kinh tế");
            employeeDAO.addEmployee("Trần Thị Tú Anh", "Giảng viên", "0923456789", "pvd@tlu.edu.vn", "Khoa Luật");
        }


    }

    public static List<Unit> getSampleUnits(Context context) {
        UnitDAO unitDAO = new UnitDAO(context);

        List<Unit> units = unitDAO.getAllUnits();

        return units;
    }

    public static List<Employee> getSampleEmployees(Context context) {
        EmployeeDAO employeeDAO = new EmployeeDAO(context);

        List<Employee> employees = employeeDAO.getAllEmployees();

        return employees;
    }
}
