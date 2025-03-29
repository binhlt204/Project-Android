package com.example.tlucontact.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tlucontact.R;

public class ContactDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        // Khởi tạo các view
        TextView tvName = findViewById(R.id.tv_detail_name);
        TextView tvPhone = findViewById(R.id.tv_detail_phone);
        TextView tvExtra = findViewById(R.id.tv_detail_extra);
        Button btnCall = findViewById(R.id.btn_call);
        Button btnMessage = findViewById(R.id.btn_message);
        Button btnMail = findViewById(R.id.btn_email);
        ImageButton btnBack = findViewById(R.id.btn_back);

        // Lấy dữ liệu từ Intent
        String type = getIntent().getStringExtra("type");
        String phone = getIntent().getStringExtra("phone");
        String email = getIntent().getStringExtra("email");

        tvName.setText(getIntent().getStringExtra("name"));
        tvPhone.setText(phone);

        // Hiển thị thông tin bổ sung dựa trên type
        if ("unit".equals(type)) {
            tvExtra.setText("Địa chỉ: " + getIntent().getStringExtra("address"));
        } else {
            tvExtra.setText("Chức vụ: " + getIntent().getStringExtra("position") + "\n" +
                    "Email: " + getIntent().getStringExtra("email") + "\n" +
                    "Đơn vị: " + getIntent().getStringExtra("unit"));
        }

        btnCall.setOnClickListener(v -> {
            if (phone != null && !phone.isEmpty()) {
                Intent callIntent = new Intent(ContactDetailActivity.this, CallActivity.class);
                callIntent.putExtra("phone", phone);
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "Không tìm thấy số điện thoại", Toast.LENGTH_SHORT).show();
            }
        });



        // Xử lý nút Message
        btnMessage.setOnClickListener(v -> {
            if (phone != null && !phone.isEmpty()) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("sms:" + phone));
                startActivity(smsIntent);
            } else {
                Toast.makeText(this, "Không tìm thấy so dien thoai", Toast.LENGTH_SHORT).show();
            }
        });

        btnMail.setOnClickListener(v -> {
            if (email != null && !email.isEmpty()) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + email)); // Chỉ email apps mới xử lý Intent này
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Tiêu đề Email");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Nội dung email...");

                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(emailIntent);
                } else {
                    Toast.makeText(this, "Không tìm thấy ứng dụng email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Không có địa chỉ email", Toast.LENGTH_SHORT).show();
            }
        });

        TextView tvTitle = findViewById(R.id.tv_title);

// Cập nhật tiêu đề theo loại
        if ("unit".equals(type)) {
            tvTitle.setText("Chi tiết Đơn Vị");
        } else if ("employee".equals(type)) {
            tvTitle.setText("Chi tiết Giảng Viên");
        }


        // Xử lý nút Back
        btnBack.setOnClickListener(v -> finish());
    }
}