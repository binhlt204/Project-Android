package com.example.tlucontact;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmpAdapter extends RecyclerView.Adapter<EmpAdapter.EmpViewHolder> {
    private List<Employee> employees;

    public EmpAdapter(List<Employee> employees) {
        this.employees = employees;
    }

    @NonNull
    @Override
    public EmpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_object, parent, false);
        return new EmpViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull EmpViewHolder holder, int position) {
        Employee employee = employees.get(position);
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

    @Override
    public int getItemCount() {
        return employees.size();
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
