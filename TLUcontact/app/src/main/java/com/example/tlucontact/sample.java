package com.example.tlucontact;

import java.util.ArrayList;
import java.util.List;

public class sample {

        public static List<Unit> getSampleUnits() {
            List<Unit> Units = new ArrayList<>();
            Units.add(new Unit("Khoa CNTT", "0123456789", "Tầng 5, Nhà C1"));
            Units.add(new Unit("Khoa Kinh tế", "0987654321", "Tầng 3, Nhà C2"));
            return Units;
        }

        public static List<Employee> getSampleEmployees() {
            List<Employee> employees = new ArrayList<>();
            employees.add(new Employee("Nguyễn Văn A", "Giảng viên", "0912345678", "nva@tlu.edu.vn", "Khoa CNTT"));
            employees.add(new Employee("Trần Thị B", "Trưởng khoa", "0987654321", "ttb@tlu.edu.vn", "Khoa Kinh tế"));
            return employees;
        }

}
