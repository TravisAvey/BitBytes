package com.cypherbytes.bitbytes.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.cypherbytes.bitbytes.R;

/**
 * Created by travis on 3/25/15.
 */
public class SuccessDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.success_title))
                .setMessage(context.getString(R.string.success_message))
                .setPositiveButton(context.getString(R.string.ok_button_dialog), null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
