package com.logical.driverapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.logical.driverapp.Adapter.CustomRecyclerBillingdetails;
import com.logical.driverapp.R;
import com.logical.driverapp.Utils.NetworkUtil;
import com.logical.driverapp.Utils.Session;
import com.logical.driverapp.Utils.VolleySingleton;
import com.logical.driverapp.model.AreaData;
import com.logical.driverapp.model.CityData;
import com.logical.driverapp.model.OrderDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.logical.driverapp.Utils.Api.get_areas;
import static com.logical.driverapp.Utils.Api.get_cities;
import static com.logical.driverapp.Utils.Api.mark_as_delievered;
import static com.logical.driverapp.Utils.Api.update_shipping_address;

public class OrderDetailsActivity extends AppCompatActivity {
    ImageView iv_back,iv_edit;
    Context context;
    TextView tv_name, tv_order_id,tv_address,payment_status,tv_item_total,delivery_status,tv_delivered;
    CircleImageView imageuser;
    private static final String JSON_URL ="https://logicalsofttech.com/goodcart/DeliveryApi/assign_order_detail";


    RecyclerView recyclerView;
    CustomRecyclerBillingdetails kAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<OrderDetails>orderDetailsList;
    RequestQueue rq;
    private Dialog Hoadialog;


    ArrayList<String> spin_city=new ArrayList<>();
    ArrayList<String> spin_aera=new ArrayList<>();
    public HashMap<Integer, CityData> CityHashMap = new HashMap<>();
    public HashMap<Integer, AreaData> AreaHashMap = new HashMap<>();
    ArrayList<CityData>cityDataArrayList=new ArrayList<>();
    ArrayList<AreaData>areaDataArrayList=new ArrayList<>();
    Spinner spinner_city,spinner_area;
    ArrayAdapter<String> spinnerArrayAdapter;
    String AreaId="",CityId="";
    private String order_id;
    private Session session;
    String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        session = new Session(OrderDetailsActivity.this);
         user_id = session.getId();

        iv_back = findViewById(R.id.iv_back);
        tv_name=findViewById(R.id.tv_name);
        tv_order_id=findViewById(R.id.tv_order_id);
        imageuser=findViewById(R.id.imageuser);
        tv_address=findViewById(R.id.tv_address);
        payment_status=findViewById(R.id.payment_status);
        tv_item_total=findViewById(R.id.tv_item_total);
        iv_edit=findViewById(R.id.iv_edit);
        delivery_status=findViewById(R.id.delivery_status);
        tv_delivered=findViewById(R.id.tv_delivered);

        rq = Volley.newRequestQueue(OrderDetailsActivity.this);
       recyclerView=findViewById(R.id.recycler_view_bill);
        orderDetailsList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(OrderDetailsActivity.this);

        try {
            Intent intent=getIntent();
            order_id=intent.getStringExtra("order_id");
            String tv_addre=intent.getStringExtra("tv_address");
            tv_address.setText("Full address: "+tv_addre);
            Log.d("mm",order_id.toString());
        }catch (Exception e){

        }

        if (NetworkUtil.isNetworkConnected(OrderDetailsActivity.this)) {
            Billing();
        }
        else {
            Toast.makeText(OrderDetailsActivity.this,"please check internet",Toast.LENGTH_LONG).show();
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            onBackPressed();

            }
        });

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            //open custom dialog for change address
                EditAddressDialog();

            }
        });

        tv_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tv_delivered.getText().toString().equalsIgnoreCase("Delivered")){
                    Toast.makeText(OrderDetailsActivity.this, "Already delivered", Toast.LENGTH_SHORT).show();
                }else {
                    MarkDelivered();
                }


            }
        });



    }

    private void MarkDelivered() {
        final ProgressDialog progressDialog = new ProgressDialog(OrderDetailsActivity.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, mark_as_delievered,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("<><>bb", response.toString());


                        try {
                            progressDialog.dismiss();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String message = obj.getString("msg");

                            Toast.makeText(OrderDetailsActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                            if (result.equalsIgnoreCase("true")){

                                Billing();

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
                        Toast.makeText(OrderDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("order_id",order_id);
                params.put("delivery_boy_id",user_id);

                return params;
            }
        };

        VolleySingleton.getInstance(OrderDetailsActivity.this).addToRequestQueue(stringRequest);



    }

    private void getAreaByCity(final String cityId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_areas,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progressDialog.dismiss();
                        Log.d("sss",response.toString());
                        try {
                            areaDataArrayList.clear();

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");


                            if (result.equalsIgnoreCase("true")) {
                                JSONArray data=obj.getJSONArray("data");

                                for (int i=0; i<data.length();i++){

                                    JSONObject jsonObject1=data.getJSONObject(i);
                                    String id=jsonObject1.getString("id");
                                    String name=jsonObject1.getString("name");


                                    spin_aera.add(i,name);
                                    areaDataArrayList.add(i, new AreaData(id,name));
                                    AreaHashMap.put(i ,new AreaData(id,name) );
                                }
                                Log.e("spin_citysize", ""+spin_city.size());
                                spinnerArrayAdapter = new ArrayAdapter<String>
                                        (OrderDetailsActivity.this, android.R.layout.simple_spinner_item, spin_aera); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_area.setAdapter(spinnerArrayAdapter);

                            }
                            else {
                                Toast.makeText(OrderDetailsActivity.this, "No area found", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            //  progressDialog.dismiss();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  progressDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("city_id", cityId);

                return params;
            }

        };



        VolleySingleton.getInstance(OrderDetailsActivity.this).addToRequestQueue(stringRequest);



    }

    private void EditAddressDialog() {

            Hoadialog = new Dialog(OrderDetailsActivity.this);
            Hoadialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Hoadialog.setCancelable(true);
            Hoadialog.setContentView(R.layout.edit_address_dialog);
            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
           Button btn_add_address=Hoadialog.findViewById(R.id.btn_add_address);
           ImageView iv_back=Hoadialog.findViewById(R.id.iv_back);
           final EditText et_address=Hoadialog.findViewById(R.id.et_address);
           final EditText et_addres_two=Hoadialog.findViewById(R.id.et_addres_two);
           final EditText et_zipcode=Hoadialog.findViewById(R.id.et_zipcode);

             spinner_city=Hoadialog.findViewById(R.id.spinner_city);
             spinner_area=Hoadialog.findViewById(R.id.spinner_area);


        if (NetworkUtil.isNetworkConnected(this)) {
            getCity();//get cities..

        }else {
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
        }


        //*******************edit address dialog on click spinner

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{

                    if(areaDataArrayList.size() !=0)
                    {
                        spin_aera.clear();
                        spinner_area.setAdapter(null);
                        spinnerArrayAdapter.notifyDataSetChanged();
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                for (int i = 0; i < CityHashMap.size(); i++)
                {
                    if (CityHashMap.get(i).getName().equals( spinner_city.getItemAtPosition(position)))
                    {
                        CityId=CityHashMap.get(i).getId();

                        getAreaByCity(CityId);

                    }else {
                        // CityId="";
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                for (int i = 0; i < AreaHashMap.size(); i++)
                {
                    if (AreaHashMap.get(i).getName().equals( spinner_area.getItemAtPosition(position)))
                    {
                        AreaId=AreaHashMap.get(i).getId();

                        //  getAreaByCity(CityId);

                    }else {
                        //  AreaId="";
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Hoadialog.dismiss();
            }
        });

        btn_add_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!et_address.getText().toString().isEmpty() &&
                            !et_addres_two.getText().toString().isEmpty() && !et_zipcode.getText().toString().isEmpty()){

                        if (CityId!=null && !CityId.isEmpty() && !AreaId.isEmpty() && AreaId!=null){

                            if (NetworkUtil.isNetworkConnected(OrderDetailsActivity.this)){
                                ChangeAddressUser(et_address.getText().toString(),et_zipcode.getText().toString(),
                                        CityId,AreaId,et_addres_two.getText().toString());

                            }else {
                                Toast.makeText(OrderDetailsActivity.this, "Please check internet", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(OrderDetailsActivity.this, "Please select city and area", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(OrderDetailsActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        //**********************************************

            try {
                if (!OrderDetailsActivity.this.isFinishing()){
                    Hoadialog.show();
                    Window window = Hoadialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                }
            }
            catch (WindowManager.BadTokenException e) {
                //use a log message
            }




    }

    private void ChangeAddressUser(final String address, final String zipcode, final String cityId, final String areaId, final String landmark) {

        final ProgressDialog progressDialog = new ProgressDialog(OrderDetailsActivity.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, update_shipping_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("<><>bb", response.toString());


                        try {
                            progressDialog.dismiss();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String message = obj.getString("msg");

                            Toast.makeText(OrderDetailsActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                            if (result.equalsIgnoreCase("true")){

                                Billing();
                                Hoadialog.dismiss();
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
                        Toast.makeText(OrderDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("order_id",order_id);
                params.put("address",address);
                params.put("city_id",cityId);
                params.put("area_id",areaId);
                params.put("zipcode",zipcode);
               params.put("address2",landmark );

                return params;
            }
        };

        VolleySingleton.getInstance(OrderDetailsActivity.this).addToRequestQueue(stringRequest);



    }



    private void getCity() {
//        final ProgressDialog progressDialog = new ProgressDialog(OrderDetailsActivity.this,R.style.MyGravity);
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, get_cities,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // progressDialog.dismiss();
                        Log.d("sss",response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");


                            if (result.equalsIgnoreCase("true")) {
                                JSONArray data=obj.getJSONArray("data");

                                for (int i=0; i<data.length();i++){

                                    JSONObject jsonObject1=data.getJSONObject(i);
                                    String id=jsonObject1.getString("id");
                                    String name=jsonObject1.getString("name");


                                    spin_city.add(i,name);
                                    cityDataArrayList.add(i, new CityData(id,name));
                                    CityHashMap.put(i ,new CityData(id,name) );
                                }
                                Log.e("spin_citysize", ""+spin_city.size());
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (OrderDetailsActivity.this, android.R.layout.simple_spinner_item, spin_city); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_city.setAdapter(spinnerArrayAdapter);



                            }
                            else {
                            }



                        } catch (JSONException e) {
                           // progressDialog.dismiss();
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // progressDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

        };

        VolleySingleton.getInstance(OrderDetailsActivity.this).addToRequestQueue(stringRequest);




    }

    private void Billing() {
        final ProgressDialog progressDialog = new ProgressDialog(OrderDetailsActivity.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        Intent intent=getIntent();
       // final String order_id=intent.getStringExtra("order_id");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("<><>bb", response.toString());


                        try {
                            orderDetailsList.clear();
                            progressDialog.dismiss();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String message = obj.getString("msg");
                            String name =obj.getString("name");
                            String image=obj.getString("image");
                            String order_id=obj.getString("order_id");
                           String payment_sta=obj.getString("payment_status");
                           String delivery_status1=obj.getString("delivery_status");
                          String grand_total=obj.getString("grand_total");

                            tv_name.setText(name);
                            tv_order_id.setText(order_id);
                            payment_status.setText(payment_sta);
                           tv_item_total.setText("Rs. "+grand_total);
                            delivery_status.setText(delivery_status1);
                           if (delivery_status1.equalsIgnoreCase("deliver")){
                               tv_delivered.setText("Delivered");
                           }else {
                               tv_delivered.setText("Mark delivered");
                           }


                            Glide.with(OrderDetailsActivity.this)
                                    .load("http://logicalsofttech.com/goodcart/assets/uploaded/users/"+ "icon.png" )
                                    .override(300, 200)
                                    .into(imageuser);

                            JSONArray heroArray = obj.getJSONArray("data");

                            for (int i = 0; i < heroArray.length(); i++) {
                                Log.d("array", heroArray.toString());
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                /*String id=heroObject.getString("id");
                                String delivery_boy_id=heroObject.getString("delivery_boy_id");
                                Log.d("b<>",delivery_boy_id.toString());*/

                                String product_id=heroObject.getString("product_id");
                                String products_name=heroObject.getString("products_name");
                                String quantity=heroObject.getString("quantity");
                                String price=heroObject.getString("price");
                                String total_price=heroObject.getString("total_price");
                                Log.d("products",product_id.toString());


                                OrderDetails orderDetails = new OrderDetails(product_id,products_name,
                                        quantity,price,total_price

                                );

                                orderDetailsList.add(orderDetails);
                                Log.d("jjj", "" + orderDetailsList.size());

                                kAdapter = new CustomRecyclerBillingdetails(OrderDetailsActivity.this, orderDetailsList);

                                @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(OrderDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                                recyclerView.setLayoutManager(mLayoutManger);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(kAdapter);

                                kAdapter.notifyDataSetChanged();
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
                        Toast.makeText(OrderDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("order_id",order_id);

                return params;
            }
        };

        VolleySingleton.getInstance(OrderDetailsActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
