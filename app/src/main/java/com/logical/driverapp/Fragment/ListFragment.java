package com.logical.driverapp.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.logical.driverapp.Utils.Api;
import com.logical.driverapp.Utils.NetworkUtil;
import com.logical.driverapp.Utils.Session;
import com.logical.driverapp.Utils.VolleySingleton;
import com.logical.driverapp.Adapter.CustomRecyclerGetorder;
import com.logical.driverapp.R;
import com.logical.driverapp.model.Getorderdetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.logical.driverapp.Utils.Api.assign_orders;


public class ListFragment extends Fragment {

    LinearLayout homelinearlayout,location;
    Session session;

    RecyclerView recyclerView;
   CustomRecyclerGetorder kAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Getorderdetail> getorderdetailList;


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_maplistfragment, container, false);

        homelinearlayout = root.findViewById(R.id.homelinearlayout);
        location = root.findViewById(R.id.location);
        session = new Session(getActivity());

        recyclerView = (RecyclerView) root.findViewById(R.id.recycleViewContainer);

        getorderdetailList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());


        final String id = session.getId();
        Log.d("iddd", id.toString());

        if (NetworkUtil.isNetworkConnected(getActivity())) {
            userList();
        }
        else {
            Toast.makeText(getActivity(),"please check internet",Toast.LENGTH_LONG).show();
        }


        return root;

    }

    private void userList() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.BaseUrl+assign_orders,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("<><>", response.toString());

                        try {
                            getorderdetailList.clear();
                            progressDialog.dismiss();

                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String message = obj.getString("msg");

                            if (result.equalsIgnoreCase("true")) {

                            JSONArray heroArray = obj.getJSONArray("data");
                            for (int i = 0; i < heroArray.length(); i++) {
                                Log.d("array", heroArray.toString());
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                /*String id=heroObject.getString("id");
                                String delivery_boy_id=heroObject.getString("delivery_boy_id");
                                Log.d("b<>",delivery_boy_id.toString());*/

                                Getorderdetail getorderdetail = new Getorderdetail(
                                        heroObject.getString("order_id"),
                                        heroObject.getString("assign_date"),
                                        heroObject.getString("user_id"),
                                        heroObject.getString("image"),
                                        heroObject.getString("quantity"),
                                        heroObject.getString("total_price"),
                                        heroObject.getString("first_name"),
                                        heroObject.getString("last_name"),
                                        heroObject.getString("email"),
                                        heroObject.getString("mobile"),
                                        heroObject.getString("address"),
                                        heroObject.getString("lat"),
                                        heroObject.getString("lng")

                                );


                                Log.d("price", heroObject.toString());
                                getorderdetailList.add(getorderdetail);
                                Log.d("All", "" + getorderdetailList.size());
                            }
                        }else {
                                Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
                            }

                                kAdapter = new CustomRecyclerGetorder(getActivity(), getorderdetailList);

                                @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", session.getId());

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }


    @Override
    public void onResume() {
        super.onResume();
        if (NetworkUtil.isNetworkConnected(getActivity())) {
            userList();
        }
        else {
            Toast.makeText(getActivity(),"please check internet",Toast.LENGTH_LONG).show();
        }
    }
}
