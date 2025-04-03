

package com.example.tlucontact.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlucontact.R;
import com.example.tlucontact.adapters.ContactAdapter;
import com.example.tlucontact.data.SampleData;
import com.example.tlucontact.models.Employee;
import com.example.tlucontact.models.Unit;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private List<Object> contactList; // Danh sách hiển thị (gồm header và item)
    private List<Object> originalList; // Danh sách gốc từ Firestore
    private boolean isSorted = false;
    private boolean isShowingUnits = true;


    private ImageButton btnAdd, btn_logout;
    private FirebaseFirestore db;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recycler_view);
        Button btnSort = findViewById(R.id.btn_sort);
        SearchView searchView = findViewById(R.id.searchView);

        // Add button and bottom navigation setup
        btnAdd = findViewById(R.id.btn_add);

        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<>();
        originalList = new ArrayList<>();
        contactAdapter = new ContactAdapter(contactList, this::openDetail);
        recyclerView.setAdapter(contactAdapter);

        // Insert sample data into Firestore if not present
        SampleData.insertSampleData();

        // Get user role from SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String userRole = prefs.getString("user_role", "Guest");

        // Hide profile menu for Admin
        if ("Admin".equals(userRole)) {
            btnAdd.setVisibility(View.VISIBLE);
            bottomNav.getMenu().findItem(R.id.nav_profile_edit).setVisible(false);
        }

        // Show default unit list
        isShowingUnits = true;
        loadUnits();
        bottomNav.setSelectedItemId(R.id.nav_departments_contact);

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_departments_contact) {
                isShowingUnits = true;
                loadUnits();
                return true;
            } else if (item.getItemId() == R.id.nav_staffs_contact) {
                isShowingUnits = false;
                loadEmployees();
                return true;
            } else if (item.getItemId() == R.id.nav_profile_edit) {
                String userEmail = prefs.getString("user_email", ""); // Reuse prefs from onCreate

                if ("Admin".equals(userRole)) { // Admin can't access this menu
                    return false;
                }

                if (userEmail.isEmpty()) {
                    Toast.makeText(this, "Không tìm thấy thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                    return false;
                }

                // Query Firestore to get employee info based on email
                db.collection("employees")
                        .whereEqualTo("email", userEmail)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                // Get first document (assumes email is unique)
                                DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                Employee employee = doc.toObject(Employee.class);
                                employee.setId(doc.getId());

                                // Create Intent and pass data to EditActivity
                                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                                intent.putExtra("type", "employee");
                                intent.putExtra("id", employee.getId());
                                intent.putExtra("name", employee.getName());
                                intent.putExtra("position", employee.getPosition());
                                intent.putExtra("phone", employee.getPhone());
                                intent.putExtra("email", employee.getEmail());
                                intent.putExtra("unit", employee.getUnit());
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Không tìm thấy thông tin nhân viên", Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "Không tìm thấy nhân viên với email: " + userEmail);
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Lỗi khi tải thông tin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Lỗi truy vấn Firestore", e);
                        });

                return true;
            }

            return false;
        });

        // Sort button click handler
        btnSort.setOnClickListener(v -> {
            isSorted = !isSorted;
            sortContacts();
        });

        // Search view text listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContacts(newText);
                return true;
            }
        });

        // Handle Add button click to show Add dialog (for Employee or Unit)
        btnAdd.setOnClickListener(v -> {
            // Show a dialog to add either Employee or Unit
            String abc = "unit";
            if(!isShowingUnits) abc="employee";
            showAddDialog(abc); // or "unit" depending on what you want to add
        });
    }

    private void showAddDialog(String type) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(type.equals("employee") ? "Thêm nhân viên" : "Thêm đơn vị");

        // Create layout for dialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        final EditText editName = new EditText(this);
        editName.setHint("Tên");
        layout.addView(editName);

        final EditText editPhone = new EditText(this);
        editPhone.setHint("Điện thoại");
        layout.addView(editPhone);

        final EditText editPosition = new EditText(this);
        final EditText editEmail = new EditText(this);
        final EditText editUnit = new EditText(this);
        final EditText editAddress = new EditText(this);
        final EditText editSubUnits = new EditText(this);
        final EditText editEmployeeNames = new EditText(this);

        if ("employee".equals(type)) {
            editPosition.setHint("Chức vụ");
            layout.addView(editPosition);

            editEmail.setHint("Email");
            layout.addView(editEmail);

            editUnit.setHint("Đơn vị");
            layout.addView(editUnit);
        } else {
            editAddress.setHint("Địa chỉ");
            layout.addView(editAddress);

            editSubUnits.setHint("Đơn vị con (cách nhau bởi dấu phẩy)");
            layout.addView(editSubUnits);

            editEmployeeNames.setHint("Danh sách tên nhân viên (cách nhau bởi dấu phẩy)");
            layout.addView(editEmployeeNames);
        }

        builder.setView(layout);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            // Retrieve input data
            String name = editName.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();

            // Kiểm tra dữ liệu đầu vào
            if (name.isEmpty()) {
                Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            // Xác định loại cần thêm: "employee" hoặc "unit"
            String collection = type.equals("employee") ? "employees" : "units";

            // Tạo một ID tự động từ Firestore
            DocumentReference newDocRef = db.collection(collection).document();

            // Tạo ID mặc định (nếu cần thiết)
            String newId = newDocRef.getId();

            // Thêm dữ liệu vào Firestore
            if ("employee".equals(type)) {
                String position = editPosition.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String unit = editUnit.getText().toString().trim();

                // Kiểm tra email bắt buộc
                if (email.isEmpty()) {
                    Toast.makeText(this, "Email không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo đối tượng Employee
                Employee newEmployee = new Employee(newId, name, unit, position, phone, email);

                // Thêm nhân viên vào Firestore
                newDocRef.set(newEmployee)
                        .addOnSuccessListener(unused -> {
                            Log.d("showAddDialog", "Employee added successfully: " + newId);
                            Toast.makeText(this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                            loadEmployees(); // Reload employee list
                        })
                        .addOnFailureListener(e -> {
                            Log.e("showAddDialog", "Error adding employee: " + e.getMessage());
                            Toast.makeText(this, "Lỗi khi thêm nhân viên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                String address = editAddress.getText().toString().trim();
                String subUnitsInput = editSubUnits.getText().toString().trim();
                List<String> subUnits = subUnitsInput.isEmpty() ? new ArrayList<>() : Arrays.asList(subUnitsInput.split("\\s*,\\s*"));

                String employeeNamesInput = editEmployeeNames.getText().toString().trim();
                List<String> employeeNames = employeeNamesInput.isEmpty() ? new ArrayList<>() : Arrays.asList(employeeNamesInput.split("\\s*,\\s*"));

                // Tạo đối tượng Unit
                Unit newUnit = new Unit(newId, name, phone, address, subUnits, employeeNames);

                // Thêm đơn vị vào Firestore
                newDocRef.set(newUnit)
                        .addOnSuccessListener(unused -> {
                            Log.d("showAddDialog", "Unit added successfully: " + newId);
                            Toast.makeText(this, "Thêm đơn vị thành công", Toast.LENGTH_SHORT).show();
                            loadUnits(); // Reload unit list
                        })
                        .addOnFailureListener(e -> {
                            Log.e("showAddDialog", "Error adding unit: " + e.getMessage());
                            Toast.makeText(this, "Lỗi khi thêm đơn vị: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }



    private void openDetail(Object contact) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String role = prefs.getString("user_role", "Guest");

        if (!"Giảng viên".equals(role)  && !"Admin".equals(role)) {
            Toast.makeText(MainActivity.this, "Bạn không có quyền truy cập!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
        if (contact instanceof Unit) {
            Unit unit = (Unit) contact;
            intent.putExtra("type", "unit");
            intent.putExtra("unitId", unit.getUnitId());
            intent.putExtra("name", unit.getName());
            intent.putExtra("phone", unit.getPhone());
            intent.putExtra("address", unit.getAddress());
            intent.putStringArrayListExtra("subUnits", new ArrayList<>(unit.getSubUnits()));
            intent.putStringArrayListExtra("employeeNames", new ArrayList<>(unit.getEmployeeNames()));
            startActivity(intent);
        } else if (contact instanceof Employee) {
            Employee employee = (Employee) contact;
            intent.putExtra("type", "employee");
            intent.putExtra("id", employee.getId());
            intent.putExtra("name", employee.getName());
            intent.putExtra("position", employee.getPosition());
            intent.putExtra("phone", employee.getPhone());
            intent.putExtra("email", employee.getEmail());
            intent.putExtra("unit", employee.getUnit());
            startActivity(intent);
        }
    }

    private void loadUnits() {
        // Tải danh sách units
        db.collection("units").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                originalList.clear();
                List<Unit> units = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Unit unit = doc.toObject(Unit.class);
                    unit.setUnitId(doc.getId());
                    units.add(unit);
                }
                originalList.addAll(units);
                // Sắp xếp và nhóm danh sách
                contactList.clear();
                contactList.addAll(sortAndGroupList(originalList, isSorted));
                contactAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadEmployees() {
        db.collection("employees").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                originalList.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Employee employee = doc.toObject(Employee.class);
                    employee.setId(doc.getId());
                    originalList.add(employee);
                }
                // Sắp xếp và nhóm danh sách
                contactList.clear();
                contactList.addAll(sortAndGroupList(originalList, isSorted));
                contactAdapter.notifyDataSetChanged();
            }
        });
    }

    private void sortContacts() {
        contactList.clear();
        contactList.addAll(sortAndGroupList(originalList, isSorted));
        contactAdapter.notifyDataSetChanged();
    }

    private void filterContacts(String query) {
        List<Object> filteredList = new ArrayList<>();

        db.collection(isShowingUnits ? "units" : "employees")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    filteredList.clear();
                    if (isShowingUnits) {
                        List<Unit> units = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Unit unit = doc.toObject(Unit.class);
                            unit.setUnitId(doc.getId());
                            if (unit.getName().toLowerCase().contains(query.toLowerCase())) {
                                units.add(unit);
                            }
                        }
                        filteredList.addAll(units);
                        contactList.clear();
                        contactList.addAll(sortAndGroupList(filteredList, isSorted));
                        contactAdapter.notifyDataSetChanged();
                    } else {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Employee employee = doc.toObject(Employee.class);
                            employee.setId(doc.getId());
                            if (employee.getName().toLowerCase().contains(query.toLowerCase())) {
                                filteredList.add(employee);
                            }
                        }
                        contactList.clear();
                        contactList.addAll(sortAndGroupList(filteredList, isSorted));
                        contactAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Lỗi khi tìm kiếm", e));
    }

    private List<Object> sortAndGroupList(List<Object> list, boolean ascending) {
        // Sắp xếp danh sách
        Collator collator = Collator.getInstance(new Locale("vi", "VN"));
        collator.setStrength(Collator.PRIMARY);
        list.sort((o1, o2) -> {
            String name1 = o1 instanceof Unit ? ((Unit) o1).getName() : ((Employee) o1).getName();
            String name2 = o2 instanceof Unit ? ((Unit) o2).getName() : ((Employee) o2).getName();
            return ascending ? collator.compare(name1, name2) : collator.compare(name2, name1);
        });

        // Nhóm theo chữ cái đầu tiên
        LinkedHashMap<String, List<Object>> groupedContacts = new LinkedHashMap<>();
        for (Object contact : list) {
            String name = contact instanceof Unit ? ((Unit) contact).getName() : ((Employee) contact).getName();
            String firstLetter = name.substring(0, 1).toUpperCase();
            groupedContacts.putIfAbsent(firstLetter, new ArrayList<>());
            groupedContacts.get(firstLetter).add(contact);
        }

        // Tạo danh sách mới với header và item
        List<Object> resultList = new ArrayList<>();
        for (String letter : groupedContacts.keySet()) {
            resultList.add(letter);
            resultList.addAll(groupedContacts.get(letter));
        }

        return resultList;
    }
}