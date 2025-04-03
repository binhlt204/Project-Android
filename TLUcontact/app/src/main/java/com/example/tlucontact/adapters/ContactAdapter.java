package com.example.tlucontact.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlucontact.R;
import com.example.tlucontact.activities.EditActivity;
import com.example.tlucontact.models.Employee;
import com.example.tlucontact.models.Unit;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<Object> contactList;
    private OnItemClickListener clickListener;
    private Context context;
    private FirebaseFirestore db;

    public interface OnItemClickListener {
        void onItemClick(Object contact);
    }

    public ContactAdapter(List<Object> contactList, OnItemClickListener clickListener) {
        this.contactList = contactList;
        this.clickListener = clickListener;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        return contactList.get(position) instanceof String ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
            return new ContactViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).txtHeader.setText((String) contactList.get(position));
        } else {
            ContactViewHolder contactHolder = (ContactViewHolder) holder;
            Object contact = contactList.get(position);

            // Kiểm tra vai trò người dùng
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String userRole = prefs.getString("user_role", "Guest");

            // Hiển thị nút Sửa và Xóa nếu là Admin
            if ("Admin".equals(userRole)) {
                contactHolder.btnEdit.setVisibility(View.VISIBLE);
                contactHolder.btnDelete.setVisibility(View.VISIBLE);
            } else {
                contactHolder.btnEdit.setVisibility(View.GONE);
                contactHolder.btnDelete.setVisibility(View.GONE);
            }

            if (contact instanceof Unit) {
                Unit unit = (Unit) contact;
                contactHolder.txtName.setText(unit.getName());
                contactHolder.txtPositon.setText(unit.getAddress());
                contactHolder.itemView.setOnClickListener(v -> clickListener.onItemClick(unit));

                // Xử lý nút Sửa cho Unit
                contactHolder.btnEdit.setOnClickListener(v -> {
                    // TODO: Thêm logic sửa Unit nếu cần
                    showEditDialog(unit);
                });

                // Xử lý nút Xóa cho Unit
                contactHolder.btnDelete.setOnClickListener(v -> {
                    db.collection("units").document(unit.getUnitId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                contactList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, contactList.size());
                                Toast.makeText(context, "Xóa đơn vị thành công", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Lỗi khi xóa đơn vị: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                });

            } else if (contact instanceof Employee) {
                Employee employee = (Employee) contact;
                contactHolder.txtName.setText(employee.getName());
                contactHolder.txtPositon.setText(employee.getPosition());
                contactHolder.itemView.setOnClickListener(v -> clickListener.onItemClick(employee));

                // Xử lý nút Sửa cho Employee
                contactHolder.btnEdit.setOnClickListener(v -> {
                    Intent intent = new Intent(context, EditActivity.class);
                    intent.putExtra("type", "employee");
                    intent.putExtra("id", employee.getId());
                    intent.putExtra("name", employee.getName());
                    intent.putExtra("position", employee.getPosition());
                    intent.putExtra("phone", employee.getPhone());
                    intent.putExtra("email", employee.getEmail());
                    intent.putExtra("unit", employee.getUnit());
                    context.startActivity(intent);
                });

                // Xử lý nút Xóa cho Employee
                contactHolder.btnDelete.setOnClickListener(v -> {
                    db.collection("employees").document(employee.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                contactList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, contactList.size());
                                Toast.makeText(context, "Xóa nhân viên thành công", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Lỗi khi xóa nhân viên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                });
            }
        }
    }

    private void showEditDialog(final Unit unit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chỉnh sửa thông tin đơn vị");

        // Tạo layout cho dialog
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        final EditText editName = new EditText(context);
        editName.setHint("Tên đơn vị");
        editName.setText(unit.getName());
        layout.addView(editName);

        final EditText editPhone = new EditText(context);
        editPhone.setHint("Số điện thoại");
        editPhone.setText(unit.getPhone()); // Assuming the unit has a phone field
        layout.addView(editPhone);

        final EditText editAddress = new EditText(context);
        editAddress.setHint("Địa chỉ");
        editAddress.setText(unit.getAddress());
        layout.addView(editAddress);

        final EditText editSubUnits = new EditText(context);
        editSubUnits.setHint("Đơn vị con (cách nhau bởi dấu phẩy)");
        editSubUnits.setText(unit.getSubUnits() != null ? String.join(", ", unit.getSubUnits()) : "");
        layout.addView(editSubUnits);

        final EditText editEmployeeIds = new EditText(context);
        editEmployeeIds.setHint("Danh sách ID nhân viên (cách nhau bởi dấu phẩy)");
        editEmployeeIds.setText(unit.getEmployeeNames() != null ? String.join(", ", unit.getEmployeeNames()) : "");
        layout.addView(editEmployeeIds);

        builder.setView(layout);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            // Cập nhật giá trị mới
            String newName = editName.getText().toString().trim();
            String newPhone = editPhone.getText().toString().trim();
            String newAddress = editAddress.getText().toString().trim();
            String subUnitsInput = editSubUnits.getText().toString().trim();
            List<String> newSubUnits = subUnitsInput.isEmpty() ? new ArrayList<>() : Arrays.asList(subUnitsInput.split("\\s*,\\s*"));

            String employeeIdsInput = editEmployeeIds.getText().toString().trim();
            List<String> newEmployeeIds = employeeIdsInput.isEmpty() ? new ArrayList<>() : Arrays.asList(employeeIdsInput.split("\\s*,\\s*"));

            // Cập nhật thông tin đơn vị
            unit.setName(newName);
            unit.setPhone(newPhone);
            unit.setAddress(newAddress);
            unit.setSubUnits(newSubUnits);
            unit.setEmployeeNames(newEmployeeIds);

            // Cập nhật lên Firestore
            db.collection("units").document(unit.getUnitId())
                    .set(unit)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Cập nhật đơn vị thành công", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged(); // Cập nhật lại dữ liệu trong adapter
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Lỗi khi cập nhật đơn vị: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

            // Đóng dialog
            dialog.dismiss();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void updateList(List<Object> newList) {
        contactList = newList;
        notifyDataSetChanged();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txtHeader;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHeader = itemView.findViewById(R.id.tv_header);
        }
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPositon;
        Button btnEdit, btnDelete;


        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tv_name);
            txtPositon = itemView.findViewById(R.id.tv_position);

            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}