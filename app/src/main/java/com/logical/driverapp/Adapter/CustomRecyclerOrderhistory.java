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
import com.logical.driverapp.model.OrderDetails;
import com.logical.driverapp.model.OrderHistory;

import java.util.List;

/**
 * Created by Ravindra Birla on 13/11/2019.
 */
public class CustomRecyclerOrderhistory extends RecyclerView.Adapter<CustomRecyclerOrderhistory.ViewHolder>{
    private Context context;
    private List<OrderHistory> orderHistories;
    public CustomRecyclerOrderhistory(Context context, List orderHistories) {
        this.context = context;
        this.orderHistories =orderHistories ;
    }
    @Override
    public CustomRecyclerOrderhistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderhistorylistitems, parent, false);
        CustomRecyclerOrderhistory.ViewHolder viewHolder = new CustomRecyclerOrderhistory.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerOrderhistory.ViewHolder holder, int position) {
        holder.itemView.setTag(orderHistories.get(position));

       OrderHistory pu =orderHistories.get(position);
        holder.date.setText(pu.getAssign_date());
        holder.name.setText(pu.getName());
        holder.total.setText(pu.getTotal());
        holder.status.setText(pu.getStatus());
        holder.orderid.setText(pu.getOrder_id());

        Log.d("sss", "" + orderHistories.size());
    }

    @Override
    public int getItemCount() {
        return orderHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,name,total,status,orderid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            name=itemView.findViewById(R.id.name);
            total=itemView.findViewById(R.id.total);
            status=itemView.findViewById(R.id.status);
            orderid=itemView.findViewById(R.id.orderid);

        }
    }
}
