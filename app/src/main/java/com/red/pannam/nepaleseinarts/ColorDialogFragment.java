package com.red.pannam.nepaleseinarts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.support.v4.app.DialogFragment;

/**
 * Created by pannam on 4/15/2016.
 */
public class ColorDialogFragment extends DialogFragment {

    private SeekBar alphaSeekBar;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;
    private View colorView;
    private int color;

    //create an Alert Dialog and return it


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View colorDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_color, null);
        builder.setView(colorDialogView); //add gui to dialog

        //set the Alert Dialogs message
        builder.setTitle(R.string.title_color_dialog);
        builder.setCancelable(true); //when back is hit the dialog will destroy

        //get the color SeekBar and set their onChan listeners

        alphaSeekBar = (SeekBar) colorDialogView.findViewById(R.id.alphaSeekBar);
        redSeekBar = (SeekBar) colorDialogView.findViewById(R.id.redSeekBar);
        greenSeekBar = (SeekBar) colorDialogView.findViewById(R.id.greenSeekBar);
        blueSeekBar = (SeekBar) colorDialogView.findViewById(R.id.blueSeekBar);
        colorView = colorDialogView.findViewById(R.id.colorView);

        //register SeekBar EVENT listener

        alphaSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        redSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        greenSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        blueSeekBar.setOnSeekBarChangeListener(colorChangedListener);

        //use current drawing color to set SeekBar Values

        final DoodleView doodleView = getDoodleFragment().getDoodleView();
        color = doodleView.getDrawingColor();
        alphaSeekBar.setProgress(Color.alpha(color));
        redSeekBar.setProgress(Color.alpha(color));
        greenSeekBar.setProgress(Color.alpha(color));
        blueSeekBar.setProgress(Color.alpha(color));





        //add set color button

        builder.setPositiveButton(R.string.button_set_color,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doodleView.setDrawingColor(color);
                    }
                });


        return builder.create(); //return dialog


    }

    //gets a reference to the doodle fragment

    private DoodleFragment getDoodleFragment() {
        return (DoodleFragment) getFragmentManager().findFragmentById(R.id.doodleFragment);

    }

    // tell doodle fragment that dialog is now displayed
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        DoodleFragment fragment = getDoodleFragment();

        if (fragment != null)
            fragment.setDialogOnScreen(true);
    }

    //tell doodlefragment the dialog is no longer displayed


    @Override
    public void onDetach() {
        super.onDetach();

        DoodleFragment fragment = getDoodleFragment();

        if (fragment != null)
            fragment.setDialogOnScreen(false);
    }

    //OnSeekBarChangeListener  for the seekbar in the color dialog
    private SeekBar.OnSeekBarChangeListener colorChangedListener = new SeekBar.OnSeekBarChangeListener() {
        //display the updated color
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if (fromUser) // user & not program changed the seekbar progress
                color = Color.argb(alphaSeekBar.getProgress(), redSeekBar.getProgress(), greenSeekBar.getProgress(),
                        blueSeekBar.getProgress());
            colorView.setBackgroundColor(color);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { //requied but not filled in

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { //required but not filled in

        }
    };

}
