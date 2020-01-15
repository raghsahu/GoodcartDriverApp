package com.logical.driverapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logical.driverapp.R;
import com.logical.driverapp.model.Getorderdetail;
import com.logical.driverapp.model.OrderDetails;

import java.util.List;

/**
 * Created by Ravindra Birla on 13/11/2019.
 */
public class CustomRecyclerBillingdetails extends RecyclerView.Adapter<CustomRecyclerBillingdetails.ViewHolder>{
    private Context context;
    private List<OrderDetails>orderDetails;
    public CustomRecyclerBillingdetails(Context context, List orderDetails) {
        this.context = context;
        this.orderDetails =orderDetails ;
    }

    @NonNull
    @Override
    public CustomRecyclerBillingdetails.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.billingitems, parent, false);
        CustomRecyclerBillingdetails.ViewHolder viewHolder = new CustomRecyclerBillingdetails.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( CustomRecyclerBillingdetails.ViewHolder holder, int position) {
        holder.itemView.setTag(orderDetails.get(position));

       OrderDetails  pu =orderDetails.get(position);
        holder.productname.setText(pu.getProducts_name());
        holder.quantitiy.setText(pu.getQuantity());
        holder.price.setText(pu.getPrice());
        holder.total_price.setText(pu.getTotal_price());
        Log.d("sss", "" + orderDetails.size());
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productname,quantitiy,price,total_price;
        public ViewHolder(View itemView) {
            super(itemView);
            productname=itemView.findViewById(R.id.productname);
            quantitiy=itemView.findViewById(R.id.quantitiy);
            price=itemView.findViewById(R.id.price);
            total_price=itemView.findViewById(R.id.total_price);
        }
    }
}
