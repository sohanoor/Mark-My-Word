package com.srsohan.markmyword.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import com.srsohan.markmyword.App.InternetConnectionDetector;
import com.srsohan.markmyword.R;


public class showNetworkConnectionPopUp {

    private Context mContext;

    public showNetworkConnectionPopUp(Context context) {
        this.mContext = context;
    }

    InternetConnectionDetector internetDetector;

    public void showNetworkDialog() {
        internetDetector = new InternetConnectionDetector(mContext);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
        alertDialog.setTitle(mContext.getString(R.string.no_internet));
        alertDialog.setMessage(mContext.getString(R.string.network_error_msg));
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(mContext.getString(R.string.wifi), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                try {
                    internetDetector.connectToWifi(mContext, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(mContext, mContext.getString(R.string.wifi_on), Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNeutralButton(mContext.getString(R.string.mobile_data),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Settings.ACTION_DATA_ROAMING_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton(mContext.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, mContext.getString(R.string.process_terminated), Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog.show();
    }
}
