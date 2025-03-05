package com.example.tlucontact;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContactListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private Button btnSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.searchView);
        btnSort = findViewById(R.id.btnSort); // Khởi tạo Button
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String type = getIntent().getStringExtra("type");
        if ("unit".equals(type)) {
            UnitAdapter adapter = new UnitAdapter(sample.getSampleUnits());
            recyclerView.setAdapter(adapter);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) { return false; }
                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.filter(newText); // Lọc theo tên
                    return true;
                }
            });
            btnSort.setOnClickListener(v -> {
                adapter.sortByName(); // Gọi phương thức sắp xếp
            });
        } else {
            EmpAdapter adapter = new EmpAdapter(sample.getSampleEmployees());
            recyclerView.setAdapter(adapter);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) { return false; }
                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.filter(newText); // Lọc theo tên
                    return true;
                }
            });
            btnSort.setOnClickListener(v -> {
                adapter.sortByName(); // Gọi phương thức sắp xếp
            });
        }
    }
}