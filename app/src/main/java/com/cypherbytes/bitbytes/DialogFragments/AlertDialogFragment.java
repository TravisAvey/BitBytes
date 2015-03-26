package com.cypherbytes.bitbytes.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.cypherbytes.bitbytes.R;

/**
 * @author travis
 */
public class AlertDialogFragment extends DialogFragment
{
    private String mMessage;

    public void setMessage(String message)
    {
        mMessage = message;
    }
    public AlertDialogFragment () {}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.error_general_title))
               .setMessage(mMessage)
               .setPositiveButton(context.getString(R.string.ok_button_dialog), null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
