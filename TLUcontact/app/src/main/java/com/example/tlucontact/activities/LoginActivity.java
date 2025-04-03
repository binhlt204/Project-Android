package com.example.tlucontact.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tlucontact.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private ProgressDialog progressDialog; // Thêm ProgressDialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_login);

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.Email_register);
        edtPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);

        // Khởi tạo ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng nhập...");
        progressDialog.setCancelable(false);

        btnLogin.setOnClickListener(view -> loginUser());

        // Bắt sự kiện khi nhấn "Đăng ký"
        TextView tvRegister = findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị ProgressDialog khi đăng nhập
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {

                                checkUserRole(user);
                                progressDialog.dismiss();
                                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_LONG).show();


                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserRole(FirebaseUser user) {
        if (user == null) return;

        String email = user.getEmail();
        String role = "";

        if (email.endsWith("tlu.edu.vn")) {
            role = "Giảng viên";
        } else if(email.equals("ltb02102004@gmail.com")){
            role = "Admin";
        }

        // Lưu role vào SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user_role", role);
        editor.putString("user_email", email);
        editor.apply();

        progressDialog.dismiss();
        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

        // Chuyển sang màn hình chính
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
