package com.logical.driverapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.logical.driverapp.Adapter.CustomRecyclerNotification;
import com.logical.driverapp.R;
import com.logical.driverapp.Utils.NetworkUtil;
import com.logical.driverapp.Utils.Session;
import com.logical.driverapp.Utils.VolleySingleton;
import com.logical.driverapp.model.Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityNotification extends AppCompatActivity {
    ImageView iv_back;
    private static final String JSON_URL = "https://logicalsofttech.com/goodcart/DeliveryApi/get_notifications";

    Session session;


    RecyclerView  recyclerView;
    CustomRecyclerNotification kAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Notification> notificationList;
    RequestQueue rq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        rq = Volley.newRequestQueue(ActivityNotification.this);
        recyclerView=findViewById(R.id.recyclerview);
        notificationList = new ArrayList<>();





        session = new Session(ActivityNotification.this);
        //Toast.makeText(getApplicationContext(),"nnnn"+session.getId(),Toast.LENGTH_LONG).show();

        session.getId();
        Log.d("iid",session.getId());

        iv_back = findViewById(R.id.iv_back);


        if (NetworkUtil.isNetworkConnected(ActivityNotification.this)) {
           notification();
        }
        else {
            Toast.makeText(ActivityNotification.this,"please check internet",Toast.LENGTH_LONG).show();
        }








        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(ActivityNotification.this, NavigationActivity.class);
                startActivity(intent);*/
                onBackPressed();
            }
        });

    }




    private void notification() {
        final ProgressDialog progressDialog = new ProgressDialog(ActivityNotification.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
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
                                JSONArray heroArray = obj.getJSONArray("data");
                                for (int i = 0; i < heroArray.length(); i++) {
                                    Log.d("array", heroArray.toString());
                                    //getting the json object of the particular index inside the array
                                    JSONObject heroObject = heroArray.getJSONObject(i);

                                    String id = heroObject.getString("id");
                                    String order_id = heroObject.getString("order_id");
                                    String sender_id = heroObject.getString("sender_id");
                                    String receiver_id = heroObject.getString("receiver_id");
                                    String receiver_type = heroObject.getString("receiver_type");
                                    String message = heroObject.getString("message");
                                    String date_time = heroObject.getString("date_time");
                                    String status = heroObject.getString("status");

                                    Log.d("products", order_id.toString());


                                    Notification notification = new Notification(id, order_id,
                                            sender_id, receiver_id, receiver_type, message, date_time, status


                                    );

                                    Log.d("receiver_type", heroObject.toString());
                                    notificationList.add(notification);
                                    Log.d("receiver_type", "" + notificationList.size());
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();


                            }


                            kAdapter = new CustomRecyclerNotification(ActivityNotification.this, notificationList);

                            @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ActivityNotification.this, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(mLayoutManger);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(kAdapter);


                            kAdapter.notifyDataSetChanged();



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
                        Toast.makeText(ActivityNotification.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("delivery_boy_id", session.getId());


                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);




    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
