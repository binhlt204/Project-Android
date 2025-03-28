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
import com.example.tlucontact.adapters.ContactAdapter;
import com.example.tlucontact.dao.EmployeeDAO;
import com.example.tlucontact.dao.UnitDAO;
import com.example.tlucontact.data.SampleData;
import com.example.tlucontact.models.Employee;
import com.example.tlucontact.R;
import com.example.tlucontact.models.Unit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private List<Object> contactList;
    private List<Unit> unitList;
    private List<Employee> employeeList;
    private boolean isSorted = false;
//    private boolean showingUnits = true;

    private UnitDAO unitDAO;
    private EmployeeDAO employeeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unitDAO = new UnitDAO(this);
        employeeDAO = new EmployeeDAO(this);

        recyclerView = findViewById(R.id.recycler_view);
        Button btnSort = findViewById(R.id.btn_sort);
        SearchView searchView = findViewById(R.id.searchView);
        Button btnUnits = findViewById(R.id.btn_units);
        Button btnEmployees = findViewById(R.id.btn_employees);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<>();

        contactAdapter = new ContactAdapter(contactList, contact -> openDetail(contact));
        recyclerView.setAdapter(contactAdapter);

//        insertSampleData();
        SampleData.insertSampleData(this);

        unitList = unitDAO.getAllUnits();
        employeeList = employeeDAO.getAllEmployees();


        loadUnits();

        btnUnits.setOnClickListener(v -> {

                btnUnits.setSelected(true);
                btnEmployees.setSelected(false);
                loadUnits();

        });

        btnEmployees.setOnClickListener(v -> {

                btnEmployees.setSelected(true);
                btnUnits.setSelected(false);
                loadEmployees();

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

        // Chỉ giảng viên mới được vào trang chi tiết
        if (!"Giảng viên".equals(role)) {
            Toast.makeText(MainActivity.this, "Bạn không có quyền truy cập!", Toast.LENGTH_SHORT).show();
            return; // Dừng nếu không phải giảng viên
        }

        Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
        if (contact instanceof Unit) {
            Unit unit = (Unit) contact;
            intent.putExtra("type", "unit");
            intent.putExtra("name", unit.getName());
            intent.putExtra("phone", unit.getPhone());
            intent.putExtra("address", unit.getAddress());
        } else if (contact instanceof Employee) {
            Employee employee = (Employee) contact;
            intent.putExtra("type", "employee");
            intent.putExtra("name", employee.getName());
            intent.putExtra("phone", employee.getPhone());
            intent.putExtra("position", employee.getPosition());
            intent.putExtra("email", employee.getEmail());
            intent.putExtra("unit", employee.getUnit());
        }
        startActivity(intent);
    }


    private void loadUnits() {
        contactList.clear();
        contactList.addAll(unitList);
        contactAdapter.notifyDataSetChanged();
    }

    private void loadEmployees() {
        contactList.clear();
        contactList.addAll(employeeList);
        contactAdapter.notifyDataSetChanged();
    }

    private void sortContacts() {
        Collections.sort(contactList, (c1, c2) -> {
            String name1 = c1 instanceof Unit ? ((Unit) c1).getName() : ((Employee) c1).getName();
            String name2 = c2 instanceof Unit ? ((Unit) c2).getName() : ((Employee) c2).getName();
            return isSorted ? name1.compareTo(name2) : name2.compareTo(name1);
        });
        contactAdapter.notifyDataSetChanged();
    }

    // Lọc danh sách liên lạc theo tên
    private void filterContacts(String query) {
        List<Object> filteredList = new ArrayList<>();
        List<?> currentList = contactList.containsAll(unitList) ? unitList : employeeList;

        for (Object contact : currentList) {
            String name = contact instanceof Unit ? ((Unit) contact).getName() : ((Employee) contact).getName();
            if (name.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(contact);
            }
        }

        contactList.clear();
        contactList.addAll(filteredList);
        contactAdapter.notifyDataSetChanged();
    }




}
