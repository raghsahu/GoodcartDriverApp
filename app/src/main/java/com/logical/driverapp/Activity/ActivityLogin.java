package com.logical.driverapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.logical.driverapp.R;
import com.logical.driverapp.Utils.NetworkUtil;
import com.logical.driverapp.Utils.Session;
import com.logical.driverapp.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityLogin extends AppCompatActivity {
    private String  URL_LOGIN="https://logicalsofttech.com/goodcart/DeliveryApi/login";
    EditText mobile, password;
    TextView tv_forgot;
    ImageView iv_login;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mobile=findViewById(R.id.mobile);
        password=findViewById(R.id.password);
        session=new Session(ActivityLogin.this);

        iv_login=findViewById(R.id.iv_login);
        iv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.isNetworkConnected(ActivityLogin.this)) {
                    userLogin();
                }
               else {
                   Toast.makeText(getApplicationContext(),"please check internet",Toast.LENGTH_LONG).show();
                }

               /* Intent intent=new Intent(ActivityLogin.this,NavigationActivity.class);
                startActivity(intent);*/
            }
        });
    }


  private void userLogin() {
    final String username = mobile.getText().toString();
    final String epassword = password.getText().toString();

    if (TextUtils.isEmpty(username)) {
        mobile.setError("Please enter your username");
        mobile.requestFocus();
        return;
    }else if (TextUtils.isEmpty(epassword)) {
        password.setError("Please enter your password");
        password.requestFocus();
        return;
    }else if (username.length() != 10) {
        Toast.makeText(getApplicationContext(),"Please enter valid number",Toast.LENGTH_LONG).show();

      } else {

        if (NetworkUtil.isNetworkConnected(ActivityLogin.this)){
            CallLoginApi(username,epassword);
        }else {
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
        }


      }

    }

    private void CallLoginApi(final String username, final String epassword) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("<>", response.toString());
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                JSONObject obj1 = obj.getJSONObject("data");
                                String id = obj1.getString("id");
                                String id_number = obj1.getString("id_number");
                                String name = obj1.getString("name");
                                String email = obj1.getString("email");
                                String mobile = obj1.getString("mobile");
                                String address = obj1.getString("address");
                                String id_proof = obj1.getString("id_proof");
                                String bank_name = obj1.getString("bank_name");
                                String account_number = obj1.getString("account_number");
                                String branch_name = obj1.getString("branch_name");
                                String ifsc_code = obj1.getString("ifsc_code");
                                String image = obj1.getString("image");
                                String area_id = obj1.getString("area_id");
                                String user_name = obj1.getString("user_name");
                                String password = obj1.getString("password");
                                String status = obj1.getString("status");
                                String date = obj1.getString("date");


                                Session session=new Session(ActivityLogin.this);
                                session.saveIdNumber(id_number);
                                session.setId(id);
                                session.setLogin(true);


                                Toast.makeText(getApplicationContext(), "suceesfully login", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ActivityLogin.this, NavigationActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), " Invalid login", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", username);
                params.put("password", epassword);
                return params;
            }
        };

        VolleySingleton.getInstance(ActivityLogin.this).addToRequestQueue(stringRequest);


    }

}