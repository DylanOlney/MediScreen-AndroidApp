package com.project.mediScreenApp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


// A simple OK/Cancel type pop-up dialog.

public class OKCancelDialog extends AlertDialog.Builder{
    public static final int RESULT_OK = -1;
    public static final int RESULT_CANCEL = -2;

    public OKCancelDialog(Context context, String title, String message) {
        super(context);
        setTitle(title);
        setMessage(message);
        setPositiveButton("OK", (DialogInterface.OnClickListener)context);
        setNegativeButton("Cancel",(DialogInterface.OnClickListener)context);
    }
}
