package com.logical.driverapp.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.logical.driverapp.Activity.OrderDetailsActivity;
import com.logical.driverapp.Adapter.CustomRecyclerGetorder;
import com.logical.driverapp.R;
import com.logical.driverapp.Utils.Api;
import com.logical.driverapp.Utils.GPSTracker;
import com.logical.driverapp.Utils.NetworkUtil;
import com.logical.driverapp.Utils.Session;
import com.logical.driverapp.Utils.VolleySingleton;
import com.logical.driverapp.model.Getorderdetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import static com.logical.driverapp.Utils.Api.assign_orders;


public class MapFragment extends Fragment implements OnMapReadyCallback , LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    Session session;
    List<Getorderdetail> getorderdetailList = new ArrayList<>();
    String user_id;
    GPSTracker gpsTracker;
    double latitude, longitude;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Marker current_marker;
    private boolean isPermitted;
    private ImageView iv_down_arrow,iv_call,userimage;
    private Dialog dialogLayout;
    private TextView tv_map_root;


    public MapFragment() {
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
        View root = inflater.inflate(R.layout.fragment_listfragment, container, false);
        session = new Session(getActivity());
        user_id = session.getId();

        if (NetworkUtil.isNetworkConnected(getActivity())) {

            // GetUserOnMap();
        } else {
            Toast.makeText(getActivity(), "Please check internet", Toast.LENGTH_SHORT).show();
        }

        checkthepermisions();


        return root;
    }

    private void checkthepermisions() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ask for permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {
            lets_get_current_loc();
        }

    }

    private void lets_get_current_loc() {
        gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.canGetLocation()) {

            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();


        } else {

            gpsTracker.showSettingsAlert();

        }


    }


    private void GetUserOnMap() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.BaseUrl + assign_orders,
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
                                    getorderdetailList.add(getorderdetail);
                                    Log.d("All", "" + getorderdetailList.size());

                                    //************setmarker on map
                                    SetDataOnMap(getorderdetailList);


                                }
                            } else {
                                Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
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
                params.put("id", session.getId());

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }

    private void SetDataOnMap(List<Getorderdetail> getorderdetailList) {


        for (int i = 0; i < getorderdetailList.size(); i++) {

            double lati = Double.parseDouble(getorderdetailList.get(i).getLat());
            double longLat = Double.parseDouble(getorderdetailList.get(i).getLng());
            mMap.addMarker(new MarkerOptions().position(new LatLng(lati, longLat))
                    .title(getorderdetailList.get(i).getFirst_name())
                    .snippet(getorderdetailList.get(i)
                            .getAddress()));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        GetUserOnMap();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                buildGoogleApiClient();
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.setMyLocationEnabled(true);

            }
        } else {
            buildGoogleApiClient();
            //       mMap.setMyLocationEnabled(true);
        }


        //********************set my current location position marker******************************
        if (latitude != 0.0 && longitude != 0.0) {
            Log.e("ypur", "locatopn");
            // Toast.makeText(GET_Service_providers.this, "your location", Toast.LENGTH_SHORT).show();
            LatLng current = new LatLng(latitude, longitude);
            current_marker = mMap.addMarker(new MarkerOptions()
                    .position(current)
                    .title("My location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .snippet(" ")
            );

            final CameraPosition cameraPosition = new CameraPosition.Builder().target(current).zoom(13)
                    .bearing(90)
                    .tilt(30)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                                                        @Override
                                                        public boolean onMyLocationButtonClick() {
                                                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                                            return true;
                                                        }
                                                    }
            );


        } else {
//            LatLng sydney2 = new LatLng(-34, 151);
//            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }


        //******************************map snippet on click
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {

                //  Toast.makeText(getActivity(), ""+marker.getTitle(), Toast.LENGTH_SHORT).show();

                try {


                    for (int k = 0; k < getorderdetailList.size(); k++) {

                        if (getorderdetailList.get(k).getFirst_name().equals(marker.getTitle())) {

                            dialogLayout = new Dialog(getActivity());
                            dialogLayout.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialogLayout.setCancelable(true);
                            dialogLayout.setContentView(R.layout.map_dialog);

                            // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            // LayoutInflater inflater = getLayoutInflater();
                            //  View dialogLayout = inflater.inflate(R.layout.map_dialog, null);

                            TextView tv_person_name = dialogLayout.findViewById(R.id.tv_person_name);
                            TextView tv_order_id = dialogLayout.findViewById(R.id.tv_order_id);
                            TextView tv_asign_date = dialogLayout.findViewById(R.id.tv_asign_date);
                            TextView tv_phone_number = dialogLayout.findViewById(R.id.tv_phone_number);
                            TextView tv_total_item = dialogLayout.findViewById(R.id.tv_total_item);
                            TextView tv_total_amount = dialogLayout.findViewById(R.id.tv_total_amount);
                            TextView tv_address = dialogLayout.findViewById(R.id.tv_address);
                            iv_down_arrow = dialogLayout.findViewById(R.id.iv_down_arrow);
                             iv_call = dialogLayout.findViewById(R.id.iv_call);
                            userimage = dialogLayout.findViewById(R.id.userimage);
                            tv_map_root = dialogLayout.findViewById(R.id.tv_map_root);


                            tv_person_name.setText(getorderdetailList.get(k).getFirst_name());
                            tv_order_id.setText(getorderdetailList.get(k).getOrder_id());
                            tv_asign_date.setText(getorderdetailList.get(k).getAssign_date());
                            tv_phone_number.setText(getorderdetailList.get(k).getMobile());
                            tv_total_item.setText(getorderdetailList.get(k).getQuantity());
                            tv_total_amount.setText(getorderdetailList.get(k).getTotal_price());
                            tv_address.setText(getorderdetailList.get(k).getAddress());

                            Glide.with(getActivity())
                                    .load("http://logicalsofttech.com/goodcart/assets/uploaded/users/" + getorderdetailList.get(k).getImage())
                                    .override(300, 200)
                                    .into(userimage);

                            // builder.setView(dialogLayout);

                            try {
                                if (!getActivity().isFinishing()) {
                                    dialogLayout.show();
                                    Window window = dialogLayout.getWindow();
                                    window.setGravity(Gravity.BOTTOM);
                                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                }
                            } catch (WindowManager.BadTokenException e) {
                                //use a log message
                            }


                            break;


                        } else {
                            // Toast.makeText(getActivity(), " " + marker.getTitle(), Toast.LENGTH_SHORT).show();
                        }

                    }


                    //************onclick item
                    iv_down_arrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogLayout.dismiss();

                        }
                    });


                    tv_map_root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {



                            for (int k = 0; k < getorderdetailList.size(); k++) {

                                if (getorderdetailList.get(k).getFirst_name().equals(marker.getTitle())) {


                                    double lati= Double.parseDouble(getorderdetailList.get(k).getLat());
                                    double longi= Double.parseDouble(getorderdetailList.get(k).getLng());

                                    //   redirect to google map*****with lat lng
                                    String uri = "http://maps.google.com/maps?daddr=" + lati + "," + longi;
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                    intent.setPackage("com.google.android.apps.maps");
                                    try
                                    {
                                        startActivity(intent);
                                    }
                                    catch(ActivityNotFoundException ex)
                                    {
                                        try
                                        {
                                            Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                            startActivity(unrestrictedIntent);
                                        }
                                        catch(ActivityNotFoundException innerEx)
                                        {
                                            Toast.makeText(getActivity(), "Please install a maps application", Toast.LENGTH_LONG).show();
                                        }
                                    }


                                }
                            }


                        }
                    });


                    iv_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            for (int k = 0; k < getorderdetailList.size(); k++) {

                                if (getorderdetailList.get(k).getFirst_name().equals(marker.getTitle())) {


                                    if (!getorderdetailList.get(k).getMobile().isEmpty()) {

                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:"+getorderdetailList.get(k).getMobile()));

                                        if (ActivityCompat.checkSelfPermission(getActivity(),
                                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            checkRunTimePermission(getorderdetailList.get(k).getMobile());

                                            return;
                                        }
                                        startActivity(callIntent);

                                        
                                    } else {
                                        Toast.makeText(getActivity(), "Phone number not found", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }


                        }
                    });


                } catch (Exception e) {

                }

            }
        });
    }

    private void checkRunTimePermission(String mobile) {
        String[] permissionArrays = new String[]{Manifest.permission.CALL_PHONE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
           // Toast.makeText(getActivity(), "hheee", Toast.LENGTH_SHORT).show();
        } else {
//             if already permition granted
//             PUT YOUR ACTION (Like Open cemara etc..)

           // Toast.makeText(this, "hhh", Toast.LENGTH_SHORT).show();
            phonecall(mobile);

        }
    }

    private void phonecall(String mobile) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + mobile));

        startActivity(callIntent);
    }


    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        mMap.clear();

        MarkerOptions mp = new MarkerOptions();

        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

        mp.title("My location");

        mMap.addMarker(mp);
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                new LatLng(location.getLatitude(), location.getLongitude()), 16));


        current_marker = mMap.addMarker(new MarkerOptions()
                .position(current)
                .title("My location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .snippet(" ")
        );

        final CameraPosition cameraPosition = new CameraPosition.Builder().target(current).zoom(13)
                .bearing(90)
                .tilt(30)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                                                    @Override
                                                    public boolean onMyLocationButtonClick() {
                                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                                        return true;
                                                    }
                                                }
        );


        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, new com.google.android.gms.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }
            });
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new com.google.android.gms.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openActivityOnce = true;
        boolean openDialogOnce = true;
        if (requestCode == 11111) {

            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];

                isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        //execute when 'never Ask Again' tick and permission dialog not show
                    } else {
                        if (openDialogOnce) {
                            Toast.makeText(getActivity(), "Permission required", Toast.LENGTH_SHORT).show();
                            // alertView();
                        }
                    }
                }
            }

            try {
                //selectImage();
            }catch (Exception e){

            }

            if (isPermitted){
               // Toast.makeText(this, ""+isPermitted, Toast.LENGTH_SHORT).show();
               // phonecall("02408028500");

            }else {
                //Toast.makeText(getActivity(), "Contact list not show", Toast.LENGTH_SHORT).show();
            }

        }
    }


}

