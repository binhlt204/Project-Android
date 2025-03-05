package com.example.tlucontact;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmpAdapter extends RecyclerView.Adapter<EmpAdapter.EmpViewHolder> {
    private List<Employee> empList;
    private List<Employee> originalList;

    public EmpAdapter(List<Employee> empList) {
        this.empList = new ArrayList<>(empList);
        this.originalList = new ArrayList<>(empList);
    }

    @NonNull
    @Override
    public EmpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_object, parent, false);
        return new EmpViewHolder(view);
    }



    @Override
    public int getItemCount() {
        return empList.size();
    }

    public void filter(String query) {
        empList.clear();
        if (query.isEmpty()) {
            empList.addAll(originalList);
        } else {
            for (Employee emp : originalList) {
                if (emp.getName().toLowerCase().contains(query.toLowerCase())) {
                    empList.add(emp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull EmpViewHolder holder, int position) {
        Employee employee = empList.get(position);
        holder.tvName.setText(employee.getName());
        holder.tvPhone.setText(employee.getPhone());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ContactDetailActivity.class);
            intent.putExtra("type", "employee");
            intent.putExtra("name", employee.getName());
            intent.putExtra("position", employee.getPosition());
            intent.putExtra("phone", employee.getPhone());
            intent.putExtra("email", employee.getEmail());
            intent.putExtra("unit", employee.getUnit());
            holder.itemView.getContext().startActivity(intent);

        });
    }

    // Phương thức sắp xếp theo tên
    public void sortByName() {
        Collections.sort(empList, (e1, e2) -> e1.getName().compareTo(e2.getName()));
        notifyDataSetChanged();
    }

    static class EmpViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvPhone;

        public EmpViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_ob_name);
            tvPhone = itemView.findViewById(R.id.tv_ob_phone);
        }
    }
}
