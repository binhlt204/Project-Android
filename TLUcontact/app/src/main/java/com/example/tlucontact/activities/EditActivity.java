package com.example.tlucontact.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tlucontact.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    private EditText editName, editPhone, editPosition, editEmail, editUnit;
    private Button btnSave;
    private ImageButton btnBack;
    private FirebaseFirestore db;
    private String employeeId; // ID của nhân viên từ Firestore
    private static final String TAG = "EditActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Khởi tạo Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Ánh xạ View
        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        editPosition = findViewById(R.id.edit_position);
        editEmail = findViewById(R.id.edit_email);
        editUnit = findViewById(R.id.edit_unit);
        btnSave = findViewById(R.id.btn_save);

        btnBack = findViewById(R.id.btn_back);
        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        employeeId = intent.getStringExtra("id"); // Lấy ID của nhân viên từ Intent
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String position = intent.getStringExtra("position");
        String email = intent.getStringExtra("email");
        String unit = intent.getStringExtra("unit");

        // Kiểm tra employeeId
        if (employeeId == null || employeeId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy ID nhân viên", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị dữ liệu lên các EditText
        editName.setText(name);
        editPhone.setText(phone);
        editPosition.setText(position);
        editEmail.setText(email);
        editUnit.setText(unit);

        // Xử lý sự kiện nhấn nút Save
        btnSave.setOnClickListener(v -> saveUserData());
        // Xử lý nút Back
        btnBack.setOnClickListener(v -> finish());
    }

    private void saveUserData() {
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String position = editPosition.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Tên và Email không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo map chứa dữ liệu cập nhật
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("phone", phone);
        updates.put("position", position);
        updates.put("email", email);
        updates.put("unit", unit);

        // Cập nhật dữ liệu vào Firestore
        db.collection("employees").document(employeeId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi khi cập nhật dữ liệu: ", e);
                    Toast.makeText(EditActivity.this, "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}