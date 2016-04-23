package com.red.pannam.nepaleseinarts;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //determine the screen size

        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        //Use landscape for extra large devices

        if (screenSize== Configuration.SCREENLAYOUT_SIZE_LARGE)setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Else use portrait if small screen

        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }
}
