package com.example.tlucontact;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.UnitViewHolder> {
    private List<Unit> unitList;

    public UnitAdapter(List<Unit> unitList) {
        this.unitList = unitList;
    }

    @NonNull
    @Override
    public UnitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_object, parent, false);
        return new UnitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnitViewHolder holder, int position) {
        Unit unit = unitList.get(position);
        holder.tvName.setText(unit.getName());
        holder.tvPhone.setText(unit.getPhone());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ContactDetailActivity.class);
            intent.putExtra("type", "unit");
            intent.putExtra("name", unit.getName());
            intent.putExtra("phone", unit.getPhone());
            intent.putExtra("address", unit.getAddress());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return unitList.size();
    }

    static class UnitViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone;

        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_ob_name);
            tvPhone = itemView.findViewById(R.id.tv_ob_phone);
        }
    }
}