package com.example.lab5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5.R;
import com.example.lab5.handle.Item_Distributor_handle;
import com.example.lab5.models.Distributor;

import java.util.ArrayList;

public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.UserViewHolder>{
    private Context context;
    private ArrayList<Distributor> ds;
    private Item_Distributor_handle handle;

    public DistributorAdapter(Context context, ArrayList<Distributor> ds, Item_Distributor_handle handle) {
        this.context = context;
        this.ds = ds;
        this.handle = handle;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_distributor, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Distributor distributor = ds.get(position);
        holder.tvDistributor.setText(distributor.getNameDistributor());

        holder.btnEdit.setOnClickListener(v -> handle.onEdit(position));
        holder.btnDelete.setOnClickListener(v -> handle.onDelete(position));

    }

    @Override
    public int getItemCount() {
        return ds.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvDistributor;
        Button btnEdit, btnDelete;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDistributor = itemView.findViewById(R.id.tvDistributor);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById((R.id.btnDelete));
        }
    }
}
