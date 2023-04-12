package com.example.expire_date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdepter extends RecyclerView.Adapter<MyRecyclerViewAdepter.MyViewHolder> {

    private final List<ProductInfo> info;
    private final Context ct;

    public MyRecyclerViewAdepter(List<ProductInfo> info, Context ct) {
        this.info = info;
        this.ct = ct;
    }

    @NonNull
    @Override
    public MyRecyclerViewAdepter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_adpter,null));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdepter.MyViewHolder holder, int position) {

        ProductInfo pinfo = info.get(position);



        holder.name.setText(holder.name.getText().toString()+pinfo.getName());
        holder.barcode.setText(holder.barcode.getText().toString()+pinfo.getBarcode());
        holder.date.setText(holder.date.getText().toString()+pinfo.getExpireDate());

    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView barcode,name,date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            barcode= itemView.findViewById(R.id.RVA_BARCODE);
            name =itemView.findViewById(R.id.RVA_NAME);
            date = itemView.findViewById(R.id.RVA_DATE);

        }

    }
}
