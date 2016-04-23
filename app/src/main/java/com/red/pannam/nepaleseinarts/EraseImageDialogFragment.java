package com.red.pannam.nepaleseinarts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by pannam on 4/15/2016.
 */
public class EraseImageDialogFragment extends DialogFragment {
    //create AlertDialog


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //set Dialog Mesage
        builder.setMessage(R.string.message_erase);
        builder.setCancelable(false);

        //add erase button

        builder.setPositiveButton(R.string.button_erase, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getDoodleFragment().getDoodleView().clear(); //clear Image
            }
        });

        //add a cancel button
        builder.setNegativeButton(R.string.button_cancel,null);
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


}
