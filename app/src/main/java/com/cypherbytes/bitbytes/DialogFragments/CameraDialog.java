package com.cypherbytes.bitbytes.DialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.DialogPreference;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.cypherbytes.bitbytes.R;
import com.cypherbytes.bitbytes.ui.MainActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by travis on 4/8/15.
 */


public class CameraDialog extends DialogFragment
{
    // for startActivityResult
    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int PICK_PHOTO_REQUEST = 2;
    public static final int PICK_VIDEO_REQUEST = 3;

    // for urls for saving
    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;

    private final Activity activity = getActivity();
    protected Uri mMediaUri;


    protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            switch(which)
            {
                case 0: // take pic
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    if (mMediaUri == null)
                    {
                        // display error
                        displayError();
                    } else
                    {
                        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                    }
                    break;
                case 1: // take vid
                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                    if (mMediaUri == null)
                    {
                        // display error
                        displayError();
                    } else
                    {
                        // video button doesn't start video??!?!
                        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        // only allow 10 sec
                        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                        // Parse only allows 10MB to send so.. lower video quality
                        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                        startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
                    }
                    break;
                case 2: // choose pic
                    break;
                case 3: // choose vid
                    break;
            }
        }

        private void displayError()
        {
            AlertDialogFragment errorDialog = new AlertDialogFragment();
            errorDialog.setMessage(getString(R.string.error_external_storage));
            errorDialog.show(getFragmentManager(), "error_dialog");
        }

        private Uri getOutputMediaFileUri(int mediaType)
        {
            // check for external storage is mounted
            if (isExternalStorage())
            {
                // get the URI

                // 1. get external storage dir
                String appName = CameraDialog.this.getString(R.string.app_name);
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appName);

                // 2. create sub-dir
                if(!mediaStorageDir.exists())
                {
                    if(!mediaStorageDir.mkdirs())
                    {
                        displayError();
                    }
                }
                // 3. create file name

                // 4. create file
                File mediaFile;
                Date now = new Date();
                String timestemp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(now);
                String path = mediaStorageDir.getPath() + File.separator;
                if(mediaType == MEDIA_TYPE_IMAGE)
                {
                    mediaFile = new File(path + "IMG_" + timestemp + ".jpg");
                } else if(mediaType == MEDIA_TYPE_VIDEO)
                {
                    mediaFile = new File(path + "VID_" + timestemp + ".mp4");
                } else
                {
                    return null;
                }

                // 5. return file uri
                return Uri.fromFile(mediaFile);
            } else {
                return null;
            }

        }

        private boolean isExternalStorage()
        {
            String state = Environment.getExternalStorageState();

            if(state.equals(Environment.MEDIA_MOUNTED))
            {
                return true;
            } else {
                return false;
            }
        }
    };
    public CameraDialog() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(R.array.camera_choices, mDialogListener);
        AlertDialog dialog = builder.create();
        dialog.show();

        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK)
        {
            // add to the gallery
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(mMediaUri);
            activity.sendBroadcast(mediaScanIntent);
        } else if (resultCode != Activity.RESULT_CANCELED)
        {
            Toast.makeText(activity, activity.getString(R.string.general_error_toast), Toast.LENGTH_LONG).show();
        }

    }
}
