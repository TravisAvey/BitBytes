package com.cypherbytes.bitbytes.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.cypherbytes.bitbytes.R;

/**
 * @author Travis
 */
public class SignupDialogFragment extends DialogFragment
{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.sign_up_error_title))
                .setMessage(context.getString(R.string.sign_up_error))
                .setPositiveButton(context.getString(R.string.ok_button_dialog), null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
