package com.example.tlucontact;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        TextView tvName = findViewById(R.id.tv_detail_name);
        TextView tvPhone = findViewById(R.id.tv_detail_phone);
        TextView tvExtra = findViewById(R.id.tv_detail_extra);
        Button btnBack = findViewById(R.id.btn_back);

        String type = getIntent().getStringExtra("type");
        tvName.setText(getIntent().getStringExtra("name"));
        tvPhone.setText(getIntent().getStringExtra("phone"));

        if ("unit".equals(type)) {
            tvExtra.setText("Địa chỉ: " + getIntent().getStringExtra("address"));
        } else {
            tvExtra.setText("Chức vụ: " + getIntent().getStringExtra("position") + "\n" +
                    "Email: " + getIntent().getStringExtra("email") + "\n" +
                    "Đơn vị: " + getIntent().getStringExtra("unit"));
        }

        btnBack.setOnClickListener(v -> finish());
    }
}