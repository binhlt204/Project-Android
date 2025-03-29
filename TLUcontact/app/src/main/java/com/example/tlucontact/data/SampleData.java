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
            employeeDAO.addEmployee("Nguyễn Văn Thẩm", "Khoa CNTT", "Giảng viên", "0912345678", "nvt@tlu.edu.vn");
            employeeDAO.addEmployee("Trương Xuân Nam", "Khoa CNTT", "Giảng viên", "0912345678", "txn@tlu.edu.vn");
            employeeDAO.addEmployee("Kiều Tuấn Dũng", "Khoa CNTT", "Giảng viên", "0912345678", "ktd@tlu.edu.vn");
            employeeDAO.addEmployee("Lê Văn Phương", "Khoa Kinh tế", "Trưởng khoa", "0987654321", "lvp@tlu.edu.vn");
            employeeDAO.addEmployee("Trần Thị Tú Anh", "Khoa Luật", "Giảng viên", "0923456789", "tta@tlu.edu.vn");
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
