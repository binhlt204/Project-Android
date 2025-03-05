package com.example.tlucontact;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btnUnit = findViewById(R.id.btn_units);
        Button btnEmp = findViewById(R.id.btn_employees);
        btnUnit.setOnClickListener(v -> {
            Intent intent = new Intent(this, ContactListActivity.class);
            intent.putExtra("type", "unit");
            startActivity(intent);
        });

        btnEmp.setOnClickListener(v -> {
            Intent intent = new Intent(this, ContactListActivity.class);
            intent.putExtra("type", "employee");
            startActivity(intent);
        });
    }
}