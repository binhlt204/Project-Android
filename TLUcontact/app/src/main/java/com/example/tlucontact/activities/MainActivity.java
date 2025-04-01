

package com.example.tlucontact.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.Collator;
import java.util.ArrayList;
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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<>();
        originalList = new ArrayList<>();
        contactAdapter = new ContactAdapter(contactList, this::openDetail);
        recyclerView.setAdapter(contactAdapter);

        // 🔹 Chèn dữ liệu mẫu vào Firestore nếu chưa có
        SampleData.insertSampleData();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        // 🔹 Hiển thị danh sách đơn vị mặc định
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
            }
            return false;
        });

        btnSort.setOnClickListener(v -> {
            isSorted = !isSorted;
            sortContacts();
        });

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
            intent.putStringArrayListExtra("employeeIds", new ArrayList<>(unit.getEmployeeIds()));
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