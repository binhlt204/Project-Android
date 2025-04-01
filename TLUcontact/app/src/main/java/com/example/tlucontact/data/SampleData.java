package com.example.tlucontact.data;

import com.example.tlucontact.models.Employee;
import com.example.tlucontact.models.Unit;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleData {

    public static void insertSampleData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Danh sách Employee
        List<Employee> employees = Arrays.asList(
                new Employee("E001", "Nguyễn Văn Thẩm", "Khoa CNTT", "Giảng viên", "0912345678", "nvt@tlu.edu.vn"),
                new Employee("E002", "Trương Xuân Nam", "Khoa CNTT", "Giảng viên", "0912345678", "txn@tlu.edu.vn"),
                new Employee("E003", "Kiều Tuấn Dũng", "Khoa CNTT", "Giảng viên", "0912345678", "ktd@tlu.edu.vn"),
                new Employee("E004", "Lê Văn Phương", "Khoa Kinh tế", "Trưởng khoa", "0987654321", "lvp@tlu.edu.vn"),
                new Employee("E005", "Trần Thị Tú Anh", "Khoa Luật", "Giảng viên", "0923456789", "tta@tlu.edu.vn"),
                new Employee("E006", "Nguyễn Thị Hồng", "Khoa Kinh tế", "Giảng viên", "0934567890", "nth@tlu.edu.vn"),
                new Employee("E007", "Phạm Văn Dũng", "Khoa CNTT", "Phó trưởng khoa", "0976543210", "pvd@tlu.edu.vn"),
                new Employee("E008", "Hoàng Minh An", "Khoa Kiểm toán", "Giảng viên", "0911111111", "hma@tlu.edu.vn"),
                new Employee("E009", "Lê Thị Thanh", "Khoa Ngữ Anh", "Trưởng khoa", "0966666666", "ltt@tlu.edu.vn"),
                new Employee("E010", "Đỗ Văn Tuấn", "Khoa CNTT", "Trưởng khoa", "0955555555", "dvt@tlu.edu.vn"),
                new Employee("E011", "Vũ Thị Mai", "Thư viện", "Thủ thư", "0981234567", "vtm@tlu.edu.vn"),
                new Employee("E012", "Trần Hữu Phước", "Trung tâm Đào tạo Quốc tế", "Giám đốc", "0979876543", "thp@tlu.edu.vn"),
                new Employee("E013", "Ngô Bảo Châu", "Viện Thủy Công", "Nghiên cứu viên", "0965432109", "nbc@tlu.edu.vn"),
                new Employee("E014", "Lương Hoàng Anh", "Trung tâm Tư vấn", "Cố vấn", "0943210987", "lha@tlu.edu.vn")
        );


        // Danh sách Unit với đơn vị con và danh sách employeeIds
        List<Unit> units = Arrays.asList(
                new Unit("U001", "Khoa CNTT", "0123456789", "Tầng 5, Nhà C1",
                        Arrays.asList("Bộ môn Hệ thống thông tin", "Bộ môn Khoa học máy tính", "Bộ môn Mạng máy tính"),
                        Arrays.asList("E001", "E002", "E003", "E007", "E010")),
                new Unit("U002", "Khoa Kinh tế", "0987654321", "Tầng 3, Nhà C2",
                        Arrays.asList("Bộ môn Kinh tế quốc tế", "Bộ môn Quản trị kinh doanh", "Bộ môn Tài chính"),
                        Arrays.asList("E004", "E006")),
                new Unit("U003", "Khoa Ngữ Anh", "0123456789", "Tầng 5, Nhà C1",
                        Arrays.asList("Bộ môn Ngôn ngữ Anh", "Bộ môn Biên phiên dịch", "Bộ môn Giảng dạy tiếng Anh"),
                        Arrays.asList("E009")),
                new Unit("U004", "Khoa Luật", "0123456789", "Tầng 5, Nhà C1",
                        Arrays.asList("Bộ môn Luật dân sự", "Bộ môn Luật hình sự", "Bộ môn Luật quốc tế"),
                        Arrays.asList("E005")),
                new Unit("U005", "Khoa Kiểm toán", "0123456789", "Tầng 5, Nhà C1",
                        Arrays.asList("Bộ môn Kiểm toán nội bộ", "Bộ môn Kiểm toán tài chính", "Bộ môn Phân tích dữ liệu kế toán"),
                        Arrays.asList("E008")),
                new Unit("U006", "Thư viện", "0123456789", "Tầng 1, Nhà A",
                        Arrays.asList("Phòng đọc sách", "Kho tài liệu", "Dịch vụ thư viện"),
                        Arrays.asList("E011")),
                new Unit("U007", "Trung tâm Đào tạo Quốc tế", "0123456789", "Tầng 2, Nhà B",
                        Arrays.asList("Chương trình liên kết quốc tế", "Hợp tác đào tạo"),
                        Arrays.asList("E012")),
                new Unit("U008", "Viện Thủy Công", "0123456789", "Tầng 3, Nhà D",
                        Arrays.asList("Phòng nghiên cứu Thủy lợi", "Phòng nghiên cứu Môi trường nước"),
                        Arrays.asList("E013")),
                new Unit("U009", "Trung tâm Tư vấn", "0123456789", "Tầng 4, Nhà E",
                        Arrays.asList("Tư vấn hướng nghiệp", "Tư vấn pháp lý", "Tư vấn doanh nghiệp"),
                        Arrays.asList("E014"))
        );


        // Chèn Units vào Firestore
        for (Unit unit : units) {
            db.collection("units").document(unit.getUnitId())
                    .set(unit)
                    .addOnSuccessListener(aVoid -> System.out.println("Unit " + unit.getName() + " added"))
                    .addOnFailureListener(e -> System.err.println("Error adding unit: " + e.getMessage()));
        }

        // Chèn Employees vào Firestore
        for (Employee employee : employees) {
            db.collection("employees").document(employee.getId())
                    .set(employee)
                    .addOnSuccessListener(aVoid -> System.out.println("Employee " + employee.getName() + " added"))
                    .addOnFailureListener(e -> System.err.println("Error adding employee: " + e.getMessage()));
        }
    }
}