package com.example.tlucontact.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tlucontact.R;
import com.example.tlucontact.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText edtFullName, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private ProgressDialog progressDialog; // Thêm ProgressDialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_register);

        // Khởi tạo FirebaseAuth và Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Ánh xạ View
        edtFullName = findViewById(R.id.full_name);
        edtEmail = findViewById(R.id.Email_register);
        edtPassword = findViewById(R.id.id_password);
        edtConfirmPassword = findViewById(R.id.id_password_conform);
        btnRegister = findViewById(R.id.btn_register);

        // Khởi tạo ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng ký...");
        progressDialog.setCancelable(false);

        // Xử lý đăng ký
        btnRegister.setOnClickListener(view -> registerUser());

        // Bắt sự kiện khi nhấn "Đăng nhập"
        TextView tvLogin = findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String fullName = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị ProgressDialog
        progressDialog.show();

        // Đăng ký tài khoản với Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
//                        if (user != null) {
//                            sendEmailVerification(user, fullName, email);
//                        }
                        progressDialog.dismiss();
                        saveUserToDatabase(user, fullName, email);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    private void sendEmailVerification(FirebaseUser user, String fullName, String email) {
//        user.sendEmailVerification()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(RegisterActivity.this, "Email xác nhận đã được gửi!", Toast.LENGTH_LONG).show();
//                        progressDialog.dismiss();
//                        saveUserToDatabase(user, fullName, email);
//                    } else {
//                        progressDialog.dismiss();
//                        Toast.makeText(RegisterActivity.this, "Lỗi gửi email xác nhận: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//    }

    private void saveUserToDatabase(FirebaseUser user, String fullName, String email) {
        String userId = user.getUid();
        String role="";
        if (email.equals("ltb02102004@gmail.com")) {
            role = "Admin";
        } else if (email.endsWith("tlu.edu.vn")) {
            role = "Giảng viên";
        }

        User newUser = new User(userId, fullName, email, role);
        mDatabase.child(userId).setValue(newUser)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut(); // Đăng xuất để chặn người dùng chưa xác thực đăng nhập
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Lỗi lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
