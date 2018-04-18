package com.ksucapstone.gasandgo;

import android.app.Activity;
import android.app.ProgressDialog;

public class PopupProgressMessage
{
    private ProgressDialog _dialog;

    public PopupProgressMessage(Activity activity, boolean isCancelable, int styleId)
    {
        _dialog = new ProgressDialog(activity);
        _dialog.setCancelable(isCancelable);
        _dialog.setProgressStyle(styleId);
        _dialog.setMessage("Please wait one moment!");

    }

    public void showWithMessage(String message)
    {
        _dialog.setTitle(message);
        _dialog.show();
    }

    public void updateProgress(int progress)
    {
        _dialog.setProgress(progress);
    }

    public void dismiss()
    {
        _dialog.dismiss();
    }
}