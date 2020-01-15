package com.logical.driverapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.logical.driverapp.Activity.NavigationActivity;
import com.logical.driverapp.Utils.VolleySingleton;
import com.logical.driverapp.R;
import com.logical.driverapp.model.Notification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ravindra Birla on 14/11/2019.
 */
public class CustomRecyclerNotification extends RecyclerView.Adapter<CustomRecyclerNotification.ViewHolder>{
    private Context context;
    private List<Notification>notifications;
    private static final String JSON_STATUS ="https://logicalsofttech.com/goodcart/DeliveryApi/change_notification_status";
    public CustomRecyclerNotification(Context context, List notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public CustomRecyclerNotification.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationitems, parent, false);
       CustomRecyclerNotification.ViewHolder viewHolder = new CustomRecyclerNotification.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomRecyclerNotification.ViewHolder holder, final int position) {
        holder.itemView.setTag(notifications.get(position));

       Notification  pu =notifications.get(position);
        holder.order_id.setText(pu.getOrder_id());
        holder.message.setText(pu.getMessage());
        holder.date_time.setText(pu.getDate_time());


        holder.notifystatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_STATUS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("<><>no", response.toString());


                                try {
                                    progressDialog.dismiss();

                                    //getting the whole json object from the response
                                    JSONObject obj = new JSONObject(response);
                                    String result = obj.getString("result");
                                    String msg = obj.getString("msg");
                                    if (result.equalsIgnoreCase("true")) {
                                        Toast.makeText(context, ""+msg, Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(context, NavigationActivity.class);
                                        context.startActivity(intent);
                                    } else {
                                        Toast.makeText(context, ""+msg, Toast.LENGTH_LONG).show();

                                    }

                                } catch (JSONException e) {
                                    progressDialog.dismiss();

                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", notifications.get(position).getId());


                        return params;
                    }
                };

                VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);


            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_id,message,date_time;
        CardView notifystatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id=itemView.findViewById(R.id.order_id);
            message=itemView.findViewById(R.id.message);
            date_time=itemView.findViewById(R.id.date_time);
            notifystatus=itemView.findViewById(R.id.notifystatus);
        }
    }
}
