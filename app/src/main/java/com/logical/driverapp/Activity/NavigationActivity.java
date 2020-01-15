package com.logical.driverapp.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.logical.driverapp.Fragment.HomeFragment;
import com.logical.driverapp.Fragment.OrderHistoryFragment;
import com.logical.driverapp.Fragment.ProfileFragment;
import com.logical.driverapp.R;
import com.logical.driverapp.Utils.Session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationActivity extends AppCompatActivity {
    private TextView mTextMessage,deliveryboyid;
    ImageView location,notification;
    public static ImageView iv_drawer;
    Session session;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame, fragment);
                    transaction.commit();
                    return true;


                case R.id.navigation_dashboard:
                    fragment=new ProfileFragment();
                    FragmentManager manager1 = getSupportFragmentManager();
                    FragmentTransaction transaction1 = manager1.beginTransaction();
                    transaction1.replace(R.id.frame, fragment);
                    transaction1.commit();
                    return true;

                    /*mTextMessage.setText(R.string.title_dashboard);*/

                case R.id.navigation_notifications:
                    fragment=new OrderHistoryFragment();
                    FragmentManager manager2 = getSupportFragmentManager();
                    FragmentTransaction transaction2 = manager2.beginTransaction();
                    transaction2.replace(R.id.frame, fragment);
                    transaction2.commit();
                    return true;

                    /*mTextMessage.setText(R.string.title_notifications);
                    return true;*/
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        location=findViewById(R.id.location);
        notification=findViewById(R.id.notification);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        iv_drawer=findViewById(R.id.iv_drawer);
        deliveryboyid=findViewById(R.id.deliveryboyid);
        session=new Session(NavigationActivity.this);
        session.getID_Number();
        deliveryboyid.setText(session.getID_Number());
        //Toast.makeText(getApplicationContext(),"id"+session.getId(),Toast.LENGTH_LONG).show();


        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Fragment fragment1 = null;

        Fragment fragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NavigationActivity.this,DeliverConfirmActivity.class);
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NavigationActivity.this,ActivityNotification.class);
                startActivity(intent);
            }
        });






    }

}
