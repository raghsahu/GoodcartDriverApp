package com.logical.driverapp.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.logical.driverapp.Adapter.CustomRecyclerOrderhistory;
import com.logical.driverapp.R;
import com.logical.driverapp.Utils.NetworkUtil;
import com.logical.driverapp.Utils.Session;
import com.logical.driverapp.Utils.VolleySingleton;
import com.logical.driverapp.model.OrderHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    LinearLayout orderhistory,calenderlayout;
    TextView tv_start_date,tv_end_date;
    private Boolean isDateApply = false;
    Context context;
    Session session;
    Button  btn_serach;
    List<OrderHistory> orderHistoryList;

    RecyclerView recyclerView;
   CustomRecyclerOrderhistory kAdapter;
    RecyclerView.LayoutManager layoutManager;

    RequestQueue rq;


    private static final String JSON_URL ="https://logicalsofttech.com/goodcart/DeliveryApi/order_history";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderHistoryFragment newInstance(String param1, String param2) {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_order_history, container, false);
        orderhistory=root.findViewById(R.id.orderhistory);
        calenderlayout=root.findViewById(R.id.calenderlayout);
        tv_start_date=root.findViewById(R.id.tv_start_date);
        btn_serach=root.findViewById(R.id.btn_serach);
        session=new Session(getActivity());
        orderHistoryList= new ArrayList<>();

        rq = Volley.newRequestQueue(getActivity());
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);


        layoutManager = new LinearLayoutManager(getActivity());


        final Drawable start_date=root.getResources().getDrawable(R.drawable.ic_date_range_black_24dp);
        tv_start_date.setCompoundDrawablesRelativeWithIntrinsicBounds(start_date,null,null,null);
        tv_end_date=root.findViewById(R.id.tv_end_date);
        Drawable end_date=root.getResources().getDrawable(R.drawable.ic_date_range_black_24dp);
        tv_end_date.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,end_date,null);


        if (NetworkUtil.isNetworkConnected(getActivity())) {
            historyorder();
        }
        else {
            Toast.makeText(getActivity(),"please check internet",Toast.LENGTH_LONG).show();
        }





       /* orderhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),OrderDetailsActivity.class);
                startActivity(intent);
            }
        });
*/

        tv_start_date.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             //getDate();
             onDateSet();
             isDateApply=true;

         }
     });
        tv_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getDate();
                onDateSet();
                isDateApply=false;
            }
        });
        btn_serach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (NetworkUtil.isNetworkConnected(getActivity())) {
                    btn_serach();
                }
                else {
                    Toast.makeText(getActivity(),"please check internet",Toast.LENGTH_LONG).show();
                }
            }
        });





        return root;
    }

    private void historyorder() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Processing...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("<><>", response.toString());

                        try {
                            progressDialog.dismiss();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String message = obj.getString("msg");

                            if (result.equalsIgnoreCase("true")){

                            JSONArray heroArray = obj.getJSONArray("data");
                            for (int i = 0; i < heroArray.length(); i++) {
                                Log.d("array", heroArray.toString());
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);

                                String order_id=heroObject.getString("order_id");
                                String assign_date=heroObject.getString("assign_date");
                                String status=heroObject.getString("status");
                                String user_id=heroObject.getString("user_id");
                                String name=heroObject.getString("name");
                                String total=heroObject.getString("total");

                                Log.d("products",order_id.toString());

                                OrderHistory orderHistory = new OrderHistory(order_id,assign_date,
                                        status,user_id,name,total);

                                Log.d("price", heroObject.toString());
                                orderHistoryList.add(orderHistory);

                            }

                        }else {
                                Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
                            }

                           // Log.d("All", "" + orderHistoryList.size());

                            kAdapter = new CustomRecyclerOrderhistory(getActivity(),orderHistoryList);

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
                params.put("delivery_boy_id", session.getId());

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void onDateSet() {
        Calendar mcurrentDate = Calendar.getInstance();
        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(),R.style.calender_dialog_theme, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "yyyy/MM/dd"; //Change as you need
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

                if (isDateApply) {
                    //tv_start_date.setText(strDate);
                    tv_start_date.setText(sdf.format(myCalendar.getTime()));
                } else {
                    //tv_end_date.setText(strDate);
                    tv_end_date.setText(sdf.format(myCalendar.getTime()));
                }

                mDay[0] = selectedday;
                mMonth[0] = selectedmonth;
                mYear[0] = selectedyear;
            }
        }, mYear[0], mMonth[0], mDay[0]);

        mDatePicker.show();

       // return true;
    }

    private void btn_serach() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        final String startdate = tv_start_date.getText().toString();
       final String enddate=tv_end_date.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("<><>", response.toString());

                        try {
                            progressDialog.dismiss();
                            orderHistoryList.clear();
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

                                String order_id=heroObject.getString("order_id");
                                String assign_date=heroObject.getString("assign_date");
                                String status=heroObject.getString("status");
                                String user_id=heroObject.getString("user_id");
                                String name=heroObject.getString("name");
                                String total=heroObject.getString("total");
                                Log.d("products",order_id.toString());


                                OrderHistory orderHistory = new OrderHistory(order_id,assign_date,
                                        status,user_id,name,total

                                );

                                Log.d("price", heroObject.toString());
                                orderHistoryList.add(orderHistory);
                                Log.d("All", "" + orderHistoryList.size());

                            } }else {
                                Toast.makeText(getActivity(),"false",Toast.LENGTH_LONG).show();

                            }
                            kAdapter = new CustomRecyclerOrderhistory(getActivity(),orderHistoryList);

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
                params.put("delivery_boy_id", session.getId());
                params.put("to_date", startdate);
                params.put("from_date", enddate);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);










    }


    //mDatePicker.setTitle("Select date");
               // mDatePicker.show();



















    private void getDate() {

            int day, month, year;

            if (isDateApply) {
                if (tv_start_date.getText().toString().trim().equals("")) {
                    // Get Current Date
                    Calendar c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);

                } else {
                    //when edit then open calender select date
                    String[] nameArray = tv_start_date.getText().toString().split("/");
                    year = Integer.parseInt(nameArray[2]);
                    month = Integer.parseInt(nameArray[1]) - 1;
                    day = Integer.parseInt(nameArray[0]);
                }
            } else {
                if (tv_end_date.getText().toString().trim().equals("")) {
                    // Get Current Date
                    Calendar c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);

                } else {
                    //when edit then open calender select date
                    String[] nameArray = tv_end_date.getText().toString().split("/");
                    year = Integer.parseInt(nameArray[2]);
                    month = Integer.parseInt(nameArray[1]) - 1;
                    day = Integer.parseInt(nameArray[0]);
                }
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.calender_dialog_theme, datePicker, year, month, day);
            datePickerDialog.show();

    }

    private DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the Date in the
        // DatePickerDialog
        public void onDateSet(DatePicker view, int selectedDay, int selectedMonth, int selectedYear) {

            selectedMonth += 1;
            String date =  selectedDay + "/" + selectedMonth + "/" + selectedYear;

            //change date farmate 1/1/2018 to 01/01/2018
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date d_date = null;
            try {
                d_date = dateFormat.parse(date);    /*selectedDay + "/" + selectedMonth + "/" + selectedYear*/
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String strDate = dateFormat.format(d_date);

            if (isDateApply) {
                tv_start_date.setText(strDate);
            } else {
                tv_end_date.setText(strDate);
            }

        }
    };


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
