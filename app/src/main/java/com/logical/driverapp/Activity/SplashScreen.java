package com.logical.driverapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.logical.driverapp.R;
import com.logical.driverapp.Utils.Session;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=5000;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        session=new Session(SplashScreen.this);
   /*     getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if (session.isLoggedIn()) {
                    intent = new Intent(SplashScreen.this,NavigationActivity.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else {
                    intent = new Intent(SplashScreen.this, ActivityLogin.class);
                    startActivity(intent);
                    finish();
                }
               /* Intent i=new Intent(SplashScreen.this,
                      ActivityLogin.class);


                startActivity(i);


                finish();*/

            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}




