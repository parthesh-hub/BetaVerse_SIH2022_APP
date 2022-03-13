package com.example.kaaryakhoj;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

public class LoadingDialog {

    Activity activity;
    AlertDialog dialog;

    LoadingDialog(Activity myActivity){
        activity = myActivity;
    }


    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.custom_dialog, null));

        builder.setCancelable(false); // set false : as user cannot close it

        dialog = builder.create();
        dialog.show();

    }

    void dismissDialog(){
        dialog.dismiss();
    }

}


// final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
// loadingDialog.startLoadingDialog(); // where to load
// loadingDialog.dismissDialog(); // stop load