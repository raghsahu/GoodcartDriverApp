package com.logical.driverapp.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.logical.driverapp.Utils.NetworkUtil;
import com.logical.driverapp.Utils.Session;
import com.logical.driverapp.Utils.VolleySingleton;
import com.logical.driverapp.R;
import com.logical.driverapp.model.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    LinearLayout share,update;
    EditText rest_name,rest_phone,rest_email,rest_address;
     LinearLayout logout;
    Session session;
    CircleImageView iv_profileimage;

    public static final String URL_Profile="https://logicalsofttech.com/goodcart/DeliveryApi/getProfile";
    public static final String URL_PROFILEUPDATE="https://logicalsofttech.com/goodcart/DeliveryApi/UpdateProfile";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
       View root= inflater.inflate(R.layout.fragment_profile, container, false);
         share=root.findViewById(R.id.share);
         rest_name=root.findViewById(R.id.rest_name);
         rest_email=root.findViewById(R.id.rest_email);
         rest_address=root.findViewById(R.id.rest_address);
         rest_phone=root.findViewById(R.id.rest_phone);
         iv_profileimage=root.findViewById(R.id.iv_profileimage);
        update=root.findViewById(R.id.update);
        logout=root.findViewById(R.id.logout);
        session=new Session(getActivity());

     final String id =  session.getID_Number();
     Log.d("mid",id.toString());


        if (NetworkUtil.isNetworkConnected(getActivity())) {
            profile();//show profile
        }
        else {
            Toast.makeText(getActivity(),"please check internet",Toast.LENGTH_LONG).show();
        }

        share();// share app
        update();//driver can not update profile

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              LogoutUser();
            }
        });


        return root;
    }

    private void LogoutUser() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity()).setTitle("Goodscart Driver App")
                .setMessage("Are you sure, you want to logout this app");

        dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                session.logout();
                dialog.dismiss();

            }


        });
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private void profile() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        final String id =  session.getID_Number();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL_Profile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("<>ab1422", response.toString());
                        try {
                            progressDialog.dismiss();
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String message=obj.getString("msg");

                            if (result.equalsIgnoreCase("true")) {
                                JSONObject obj1 = obj.getJSONObject("data");

                                String id=obj1.getString("id");
                                String id_number=obj1.getString("id_number");
                                String name=obj1.getString("name");
                                String email=obj1.getString("email");
                                String mobile=obj1.getString("mobile");
                                String address=obj1.getString("address");
                                String id_proof=obj1.getString("id_proof");
                                String bank_name=obj1.getString("bank_name");
                                String account_number=obj1.getString("account_number");
                                String branch_name=obj1.getString("branch_name");
                                String ifsc_code=obj1.getString("ifsc_code");
                                String image=obj1.getString("image");
                                String area_id=obj1.getString("area_id");
                                String user_name=obj1.getString("user_name");
                                String password=obj1.getString("password");
                                String status=obj1.getString("status");
                                String date=obj1.getString("date");
                               // Log.d("iiii",id_number.toString());


                                //creating a hero object and giving them the values from json object
                                Profile hero = new Profile(obj1.getString("id"),
                                        obj1.getString("id_number"),
                                        obj1.getString("name"),
                                        obj1.getString("email"),
                                        obj1.getString("mobile"),
                                        obj1.getString("address"),
                                        obj1.getString("id_proof"),
                                        obj1.getString("bank_name"),
                                        obj1.getString("account_number"),
                                        obj1.getString("branch_name"),
                                        obj1.getString("ifsc_code"),
                                        obj1.getString("image"),
                                        obj1.getString("area_id"),
                                        obj1.getString("user_name"),
                                        obj1.getString("password"),
                                        obj1.getString("status"),
                                        obj1.getString("date"));

                                rest_name.setText(name);
                                rest_email.setText(email);
                                rest_address.setText(address);
                                rest_phone.setText(mobile);


                                if (image!=null && !image.isEmpty()){
                                    Glide.with(getActivity())
                                            .load("https://logicalsofttech.com/goodcart/assets/uploaded/delivery_boy/"+ obj1.getString("image") )
                                            .placeholder(R.drawable.user)
                                            .override(300, 200)
                                            .into(iv_profileimage);


                                }

                                /* Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();*/

                            } else {
                                Toast.makeText(getActivity(), ""+message, Toast.LENGTH_LONG).show();

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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_number", id);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }

    private void update() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileupdate();
            }
        });
    }

    private void profileupdate() {
        final String username = rest_name.getText().toString().trim();
        final String email=rest_email.getText().toString().trim();
        final String address=rest_address.getText().toString().trim();
        final String mobile=rest_phone.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL_PROFILEUPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("<>ab2525", response.toString());
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String message=obj.getString("msg");
                            JSONObject obj1 = obj.getJSONObject("data");

                            String id=obj1.getString("id");
                            String id_number=obj1.getString("id_number");
                            String name=obj1.getString("name");
                            String email=obj1.getString("email");
                            String mobile=obj1.getString("mobile");
                            String address=obj1.getString("address");
                            String id_proof=obj1.getString("id_proof");
                            String bank_name=obj1.getString("bank_name");
                            String account_number=obj1.getString("account_number");
                            String branch_name=obj1.getString("branch_name");
                            String ifsc_code=obj1.getString("ifsc_code");
                            String image=obj1.getString("image");
                            String area_id=obj1.getString("area_id");
                            String user_name=obj1.getString("user_name");
                            String password=obj1.getString("password");
                            String status=obj1.getString("status");
                            String date=obj1.getString("date");
                            Log.d("iiii",id_number.toString());


                            //creating a hero object and giving them the values from json object
                            Profile hero = new Profile(obj1.getString("id"),
                                    obj1.getString("id_number"),
                                    obj1.getString("name"),
                                    obj1.getString("email"),
                                    obj1.getString("mobile"),
                                    obj1.getString("address"),
                                    obj1.getString("id_proof"),
                                    obj1.getString("bank_name"),
                                    obj1.getString("account_number"),
                                    obj1.getString("branch_name"),
                                    obj1.getString("ifsc_code"),
                                    obj1.getString("image"),
                                    obj1.getString("area_id"),
                                    obj1.getString("user_name"),
                                    obj1.getString("password"),
                                    obj1.getString("status"),
                                    obj1.getString("date"));

                            rest_name.setText(name);
                            rest_email.setText(email);
                            rest_address.setText(address);
                            rest_phone.setText(mobile);


                            Glide.with(getActivity())
                                    .load("https://logicalsofttech.com/goodcart/assets/uploaded/delivery_boy/"+ "img710.jpg" )
                                    .override(300, 200)
                                    .into(iv_profileimage);



                            if (result.equalsIgnoreCase("true")) {



                                Log.d("id",id.toString());


                                Toast.makeText(getActivity(), "suceesfully updated", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getActivity(), "not suceesfully updated", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_number",session.getID_Number());
                params.put("name", username);
                params.put("mobile", email);
                params.put("email", address);
                params.put("address",mobile);


                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);





    }

    private void share() {
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
                String app_url = " https://play.google.com/store/apps/details?id=my.example.javatpoint";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
    }










    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
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
