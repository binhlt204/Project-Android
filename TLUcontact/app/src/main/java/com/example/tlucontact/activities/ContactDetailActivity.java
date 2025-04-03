package com.example.tlucontact.activities;

import static org.jetbrains.annotations.Nls.Capitalization.Title;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tlucontact.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDetailName, tvDetailPhone, tvDetailPosition, tvDetailEmail, tvDetailAddress, tvDetailUnit, tvUnitLabel, tvStaff;
    private ImageView imgAvatar;
    private ImageButton btnBack, btnEdit;
    private Button btnMessage, btnCall, btnEmail;
    private LinearLayout layoutPhone, layoutPosition, layoutEmail, layoutAddress, layoutUnit, layoutStaff;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private boolean isAdmin = false;

    private String type, id, unitId, name, phone, position, email, address, unit;
    private List<String> subUnits;
    private List<String> employeeNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        // Khởi tạo Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Khởi tạo các view
        tvTitle = findViewById(R.id.tv_title);
        tvDetailName = findViewById(R.id.tv_detail_name);
        tvDetailPhone = findViewById(R.id.tv_detail_phone);
        tvDetailPosition = findViewById(R.id.tv_detail_position);
        tvDetailEmail = findViewById(R.id.tv_detail_email);
        tvDetailAddress = findViewById(R.id.tv_detail_address);
        tvDetailUnit = findViewById(R.id.tv_detail_unit);
        tvUnitLabel = findViewById(R.id.tv_unit_label);
        tvStaff = findViewById(R.id.tv_staff);
        imgAvatar = findViewById(R.id.img_avatar);
        btnBack = findViewById(R.id.btn_back);
        btnMessage = findViewById(R.id.btn_message);
        btnCall = findViewById(R.id.btn_call);
        btnEmail = findViewById(R.id.btn_email);
        layoutPhone = findViewById(R.id.layout_phone);
        layoutPosition = findViewById(R.id.layout_position);
        layoutEmail = findViewById(R.id.layout_email);
        layoutAddress = findViewById(R.id.layout_address);
        layoutUnit = findViewById(R.id.layout_unit);
        layoutStaff = findViewById(R.id.layout_staff);

        btnEdit = findViewById(R.id.btn_edit);
        // Kiểm tra quyền admin
        checkAdminPermission();

        // Lấy dữ liệu từ Intent
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        unitId = getIntent().getStringExtra("unitId");
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        position = getIntent().getStringExtra("position");
        email = getIntent().getStringExtra("email");
        address = getIntent().getStringExtra("address");
        unit = getIntent().getStringExtra("unit");
        subUnits = getIntent().getStringArrayListExtra("subUnits");
        employeeNames = getIntent().getStringArrayListExtra("employeeNames");

        // Hiển thị thông tin
        displayContactDetails();

        // Nếu là admin, cho phép chỉnh sửa khi nhấn vào tên
        if (isAdmin) {
            btnEdit.setVisibility(View.GONE);
            btnEdit.setOnClickListener(v -> showEditDialog());
        } else {
            btnEdit.setVisibility(View.GONE);
        }

        // Xử lý nút Call
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
                Toast.makeText(this, "Không tìm thấy số điện thoại", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý nút Email
        btnEmail.setOnClickListener(v -> {
            if (email != null && !email.isEmpty()) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + email));
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

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> finish());
    }

    private void checkAdminPermission() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && "ltb02102004@gmail.com".equals(currentUser.getEmail())) {
            isAdmin = true;
        }
    }

    private void displayContactDetails() {
        tvDetailName.setText(name);

        if ("unit".equals(type)) {
            tvTitle.setText("Đơn Vị");
            imgAvatar.setImageResource(R.drawable.ic_contact);

            layoutPhone.setVisibility(View.VISIBLE);
            tvDetailPhone.setText(phone != null ? phone : "Không có");

            layoutAddress.setVisibility(View.VISIBLE);
            tvDetailAddress.setText(address != null ? address : "Không có");

            layoutUnit.setVisibility(View.VISIBLE);
            tvUnitLabel.setText("Đơn vị con");
            tvDetailUnit.setText(subUnits != null && !subUnits.isEmpty() ? String.join("\n", subUnits) : "Không có");

            layoutStaff.setVisibility(View.VISIBLE);
            tvStaff.setText(employeeNames != null && !employeeNames.isEmpty() ? String.join("\n", employeeNames) : "Không có");

            layoutPosition.setVisibility(View.GONE);
            layoutEmail.setVisibility(View.GONE);
        } else {


            tvTitle.setText("Giảng Viên");
            imgAvatar.setImageResource(R.drawable.anhavt);

            layoutPhone.setVisibility(View.VISIBLE);
            tvDetailPhone.setText(phone != null ? phone : "Không có");

            layoutPosition.setVisibility(View.VISIBLE);
            tvDetailPosition.setText(position != null ? position : "Không có");

            layoutEmail.setVisibility(View.VISIBLE);
            tvDetailEmail.setText(email != null ? email : "Không có");

            layoutUnit.setVisibility(View.VISIBLE);
            tvUnitLabel.setText("Đơn vị");
            tvDetailUnit.setText(unit != null ? unit : "Không có");

            layoutAddress.setVisibility(View.GONE);
            layoutStaff.setVisibility(View.GONE);
        }
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chỉnh sửa thông tin");

        // Tạo layout cho dialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        final EditText editName = new EditText(this);
        editName.setHint("Tên");
        editName.setText(name);
        layout.addView(editName);

        final EditText editPhone = new EditText(this);
        editPhone.setHint("Điện thoại");
        editPhone.setText(phone);
        layout.addView(editPhone);

        if ("employee".equals(type)) {
            final EditText editPosition = new EditText(this);
            editPosition.setHint("Chức vụ");
            editPosition.setText(position);
            layout.addView(editPosition);

            final EditText editEmail = new EditText(this);
            editEmail.setHint("Email");
            editEmail.setText(email);
            layout.addView(editEmail);

            final EditText editUnit = new EditText(this);
            editUnit.setHint("Đơn vị");
            editUnit.setText(unit);
            layout.addView(editUnit);
        } else {
            final EditText editAddress = new EditText(this);
            editAddress.setHint("Địa chỉ");
            editAddress.setText(address);
            layout.addView(editAddress);

            final EditText editSubUnits = new EditText(this);
            editSubUnits.setHint("Đơn vị con (cách nhau bởi dấu phẩy)");
            editSubUnits.setText(subUnits != null ? String.join(", ", subUnits) : "");
            layout.addView(editSubUnits);

            final EditText editEmployeeNames = new EditText(this);
            editEmployeeNames.setHint("Danh sách tên nhân viên (cách nhau bởi dấu phẩy)");
            editEmployeeNames.setText(employeeNames != null ? String.join(", ", employeeNames) : "");
            layout.addView(editEmployeeNames);
        }

        builder.setView(layout);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            // Cập nhật giá trị mới nhưng không thay đổi id
            name = editName.getText().toString().trim();
            phone = editPhone.getText().toString().trim();

            if ("employee".equals(type)) {
                position = ((EditText) layout.getChildAt(2)).getText().toString().trim();
                email = ((EditText) layout.getChildAt(3)).getText().toString().trim();
                unit = ((EditText) layout.getChildAt(4)).getText().toString().trim();
            } else {
                address = ((EditText) layout.getChildAt(2)).getText().toString().trim();
                String subUnitsInput = ((EditText) layout.getChildAt(3)).getText().toString().trim();
                subUnits = subUnitsInput.isEmpty() ? new ArrayList<>() : Arrays.asList(subUnitsInput.split("\\s*,\\s*"));

                String employeeNamesInput = ((EditText) layout.getChildAt(4)).getText().toString().trim();
                employeeNames = employeeNamesInput.isEmpty() ? new ArrayList<>() : Arrays.asList(employeeNamesInput.split("\\s*,\\s*"));
            }

            // Cập nhật lên Firestore
            updateContactToFirestore();

            // Cập nhật giao diện
            displayContactDetails();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void updateContactToFirestore() {
        if ("unit".equals(type)) {
            // Cập nhật thông tin Unit
            db.collection("units").document(unitId)
                    .update(
                            "name", name,
                            "phone", phone,
                            "address", address,
                            "subUnits", subUnits,
                            "employeeNames", employeeNames
                    )
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else if ("employee".equals(type)) {
            // Cập nhật thông tin Employee
            db.collection("employees").document(id)
                    .update(
                            "name", name,
                            "phone", phone,
                            "position", position,
                            "email", email,
                            "unit", unit
                    )
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}