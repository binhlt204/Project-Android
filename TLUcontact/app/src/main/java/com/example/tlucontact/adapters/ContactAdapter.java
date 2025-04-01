
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

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<Object> contactList;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(Object contact);
    }

    public ContactAdapter(List<Object> contactList, OnItemClickListener clickListener) {
        this.contactList = contactList;
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return contactList.get(position) instanceof String ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
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
            if (contact instanceof Unit) {
                contactHolder.txtName.setText(((Unit) contact).getName());
                contactHolder.txtPositon.setText(((Unit) contact).getAddress());
            } else if (contact instanceof Employee) {
                contactHolder.txtName.setText(((Employee) contact).getName());
                contactHolder.txtPositon.setText(((Employee) contact).getPosition());
            }
            contactHolder.itemView.setOnClickListener(v -> clickListener.onItemClick(contact));
        }
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

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tv_name);
            txtPositon = itemView.findViewById(R.id.tv_position);
        }
    }
}
