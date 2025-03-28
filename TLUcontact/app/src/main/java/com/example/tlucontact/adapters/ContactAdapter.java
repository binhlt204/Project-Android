package com.example.tlucontact.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tlucontact.R;
import com.example.tlucontact.models.Employee;
import com.example.tlucontact.models.Unit;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Object> contactList;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(Object contact);
    }

    public ContactAdapter(List<Object> contactList, OnItemClickListener clickListener) {
        this.contactList = contactList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Object contact = contactList.get(position);

        if (contact instanceof Unit) {
            Unit unit = (Unit) contact;
            holder.txtName.setText(unit.getName());
            holder.txtPhone.setText(unit.getPhone());
        } else if (contact instanceof Employee) {
            Employee employee = (Employee) contact;
            holder.txtName.setText(employee.getName());
            holder.txtPhone.setText(employee.getPhone());
        }

        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(contact));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void updateList(List<Object> newList) {
        contactList = newList;
        notifyDataSetChanged();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPhone;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tv_name);
            txtPhone = itemView.findViewById(R.id.tv_phone);
        }
    }
}
