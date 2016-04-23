package com.red.pannam.nepaleseinarts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * Created by pannam on 4/15/2016.
 */
public class LineWidthDialogFragment extends DialogFragment {

    //class for select color dialog

    //create alert dialog and return it
    private ImageView widthImageView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View lineWidthDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_line_width, null);
        builder.setView(lineWidthDialogView); //ADD GUI TO dialog

        //set the Alert dialog message

        builder.setTitle(R.string.title_line_width_dialog);
        builder.setCancelable(true);

        //get ImageView
        widthImageView = (ImageView) lineWidthDialogView.findViewById(R.id.widthImageView);

        //configure width seekbar

        final DoodleView doodleView = getDoodleFragment().getDoodleView();
        final SeekBar widthSeekBar = (SeekBar) lineWidthDialogView.findViewById(R.id.widthSeekBar);
        widthSeekBar.setProgress(doodleView.getLineWidth());

        //add set line width button

        builder.setPositiveButton(R.string.button_set_line_width, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doodleView.setLineWidth(widthSeekBar.getProgress());
                    }
                }

        );

        return builder.create();
    }

    //get a reference to doodle fragment

    private DoodleFragment getDoodleFragment() {
        return (DoodleFragment) getFragmentManager().findFragmentById(R.id.doodleFragment);

    }

    //tell DoodleFragment that dialog is now displayed


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        DoodleFragment fragment = getDoodleFragment();
        if (fragment != null)
            fragment.setDialogOnScreen(true);

    }

    //tell Doodle fragment that dialog i s no longer displayed


    @Override
    public void onDetach() {
        super.onDetach();

        DoodleFragment fragment = getDoodleFragment();
    }

    private SeekBar.OnSeekBarChangeListener lineWidthChanged = new SeekBar.OnSeekBarChangeListener() {

        Bitmap bitmap = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap); //associate with canvas


        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //configure a paint object for the current seekbar value

            Paint p = new Paint();
            p.setColor(getDoodleFragment().getDoodleView().getDrawingColor());
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(progress);

            //erase the bitmap and redraw the line

            bitmap.eraseColor(getResources().getColor(android.R.color.transparent));
            canvas.drawLine(30, 50, 370, 50, p);
            widthImageView.setImageBitmap(bitmap);


        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { //required

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { //required

        }
    };






}
