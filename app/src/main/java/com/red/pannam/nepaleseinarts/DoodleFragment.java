package com.red.pannam.nepaleseinarts;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.DialogFragment;


/**
 * Created by pannam on 4/15/2016.
 */


public class DoodleFragment extends Fragment {

    private DoodleView doodleView;  //handles touch events & draws
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    private boolean dialogOnScreen = false; //checks if there is a dialog on the screen


    //value used to determine if  the user shook the device to erase

    private static final int ACCELERATION_THRESHOLD = 100000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //false specifies this fragment doesn't need to be attached to a activity


        View view = inflater.inflate(R.layout.fragment_doodle, container, false);
        setHasOptionsMenu(true); //display menu items

        //get reference to doodleview
        doodleView = (DoodleView) view.findViewById(R.id.doodleView);

        //initialize acceleration values

        acceleration = 0.00f;
        currentAcceleration = SensorManager.GRAVITY_EARTH;
        lastAcceleration = SensorManager.GRAVITY_EARTH;
        return view;

    }


    @Override
    public void onStart() {
        super.onStart();

        enableAccelerometerListening(); //listen for shake

    }

    private void enableAccelerometerListening() {

        //get Sensor manager

        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        //register to listen accelerometer events

        //WE WILL create sensorEventListener later as it is used to specify which sensor are we using
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);


    }

    //Stop listening to sensor events
    @Override
    public void onPause() {
        super.onPause();
        disableAccelerometerListening(); //stop listening to shake
    }

    private void disableAccelerometerListening() {
        //get Sensor manager

        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        //unregister (STOP) to listen accelerometer events

        sensorManager.unregisterListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //left right x
            //up down y
            //forward backward z

            //ensure no dialog on screen
            if (!dialogOnScreen) {
                //get x, y, z values
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                //save previous acceleration value
                lastAcceleration = currentAcceleration;

                //CALCULATE THE CURRENT acceleration

                currentAcceleration = x * x + y * y + z * z;

                //if accln is above the threshold

                if (acceleration > ACCELERATION_THRESHOLD)
                    confirmErase();

            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void confirmErase() {

        //erase
        EraseImageDialogFragment fragment = new EraseImageDialogFragment();
        //  fragment.show(getFragmentManager(),"erase dialog");
        fragment.show(getFragmentManager(), "erase dialog");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //display menu

        inflater.inflate(R.menu.doodle_fragment_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle option in menu

        switch (item.getItemId()) {
            case R.id.color:
                ColorDialogFragment colorDialog = new ColorDialogFragment();
                colorDialog.show(getFragmentManager(), "color dialog");
                return true;

            case R.id.lineWidth:
                LineWidthDialogFragment widthDialog = new LineWidthDialogFragment();
                widthDialog.show(getFragmentManager(), "line width dialog");
                return true;

            case R.id.eraser:
                doodleView.setDrawingColor(Color.WHITE);
                //SET COLOR WHITE WHEN ERASED
                return true;
            case R.id.clear:
                confirmErase();

            case R.id.save:
                doodleView.saveImage();
                return true;

            case R.id.print:
                doodleView.printImage();
                return true;


        }
        return super.onOptionsItemSelected(item);

    }
    //returns DoodleView
    public DoodleView getDoodleView(){
        return doodleView;
    }

        //check whether a dialog is displayed

    public void setDialogOnScreen(boolean visible) {

        dialogOnScreen = visible;

    }
}




