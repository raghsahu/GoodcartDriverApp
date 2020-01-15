package com.logical.driverapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.logical.driverapp.R;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LinearLayout  dialog,arrowdown,uparrow;
    ImageView imageuparrow,imagedownarrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location2);

        arrowdown=findViewById(R.id.arrowdown);
        uparrow=findViewById(R.id.uparrow);
        imagedownarrow=findViewById(R.id.imagedownarrow);
        imageuparrow=findViewById(R.id.imageuparrow);

        arrowdown();
        uparrow();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void uparrow() {
        uparrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrowdown.setVisibility(View.VISIBLE);
            }
        });
    }

    private void arrowdown() {
        imagedownarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrowdown.setVisibility(View.GONE);
            }
        });
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(22.719568, 75.857727);
        mMap.addMarker(new MarkerOptions().position(sydney).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

       mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
           @Override
           public boolean onMarkerClick(Marker marker) {
               dialog=findViewById(R.id.dialog);
               final Dialog dialog = new Dialog(LocationActivity.this);
               // Include dialog.xml file
               dialog.setContentView(R.layout.locationdialog);
               dialog.show();
             return false;
           }
       });

    }

}
