package com.example.tlucontact;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContactListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String type = getIntent().getStringExtra("type");
        if ("unit".equals(type)) {
            UnitAdapter adapter = new UnitAdapter(sample.getSampleUnits());
            recyclerView.setAdapter(adapter);
        } else {
            EmpAdapter adapter = new EmpAdapter(sample.getSampleEmployees());
            recyclerView.setAdapter(adapter);
        }
    }
}
