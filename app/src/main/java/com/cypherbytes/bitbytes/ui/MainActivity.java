package com.cypherbytes.bitbytes.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cypherbytes.bitbytes.DialogFragments.AlertDialogFragment;
import com.cypherbytes.bitbytes.DialogFragments.CameraDialog;
import com.cypherbytes.bitbytes.Fragments.SectionPagerAdapter;
import com.cypherbytes.bitbytes.R;
import com.cypherbytes.bitbytes.Social.EditFriendsActivity;
import com.cypherbytes.bitbytes.Social.RecipientsActivity;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener
{
    public static final String TAG = MainActivity.class.getSimpleName();

    // for startActivityResult
    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int PICK_PHOTO_REQUEST = 2;
    public static final int PICK_VIDEO_REQUEST = 3;

    // for urls for saving
    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;

    protected Uri mMediaUri;

    public static final int FILE_SIZE_LIMIT = 1024*1024*10; // 10 MB

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null)
        {
            navigateToLogin();
        } else {
            Log.i(TAG, currentUser.getUsername());
        }
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    private void navigateToLogin()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id)
        {
            case R.id.action_logout:
                ParseUser.logOut();
                navigateToLogin();
                break;
            case R.id.action_edit_friends:
                Intent intent = new Intent(this, EditFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_camera:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.camera_choices, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
                    Intent takeVideoIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
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
                        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
                    }
                    break;
                case 2: // choose pic
                    Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    choosePhotoIntent.setType("image/*");
                    startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
                    break;
                case 3: // choose vid
                    Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseVideoIntent.setType("video/*");
                    startActivityForResult(chooseVideoIntent, PICK_VIDEO_REQUEST);
                    break;
            }
        }



        private Uri getOutputMediaFileUri(int mediaType)
        {
            // check for external storage is mounted
            if (isExternalStorage())
            {
                // get the URI

                // 1. get external storage dir
                String appName = MainActivity.this.getString(R.string.app_name);
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appName);

                // 2. create sub-dir
                if(!mediaStorageDir.exists())
                {
                    if(!mediaStorageDir.mkdirs())
                    {
                        displayError();
                        return null;
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
                    Log.v("MEDIA FILE: ", mediaFile.toString());
                } else if(mediaType == MEDIA_TYPE_VIDEO)
                {
                    mediaFile = new File(path + "VID_" + timestemp + ".mp4");
                    Log.v("MEDIA FILE: ", mediaFile.toString());
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



    private void displayError()
    {
        AlertDialogFragment errorDialog = new AlertDialogFragment();
        errorDialog.setMessage(getString(R.string.error_external_storage));
        errorDialog.show(getFragmentManager(), "error_dialog");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            if(requestCode == PICK_PHOTO_REQUEST || requestCode == PICK_VIDEO_REQUEST)
            {
                if(data == null)
                {
                    displayError();
                }
                else
                {
                    mMediaUri = data.getData();
                }
                Log.i(TAG, "Media URI: " + mMediaUri);
                if(requestCode == PICK_VIDEO_REQUEST)
                {
                    // make sure file is less than 10 MB
                    int fileSize = 0;
                    InputStream inputStream = null;
                    try
                    {
                        inputStream = getContentResolver().openInputStream(mMediaUri);
                        fileSize = inputStream.available();

                    } catch (FileNotFoundException e)
                    {
                        displayError();
                        return;
                    }
                    catch (IOException e)
                    {
                        displayError();
                        return;
                    }
                    finally
                    {
                        try
                        {
                            inputStream.close();
                        } catch (IOException e)
                        {
                            // inputStream.close() needed a try/catch block
                        }
                        if(fileSize >= FILE_SIZE_LIMIT)
                        {
                            Toast.makeText(this, "The selected file is too large, select another file", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                } else {
                    // add to the gallery
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(mMediaUri);
                    sendBroadcast(mediaScanIntent);
                }

                Intent recipeintIntent = new Intent(this, RecipientsActivity.class);
                recipeintIntent.setData(mMediaUri);
                startActivity(recipeintIntent);

            }
        } else if (resultCode != Activity.RESULT_CANCELED) {
            Toast.makeText(this, R.string.general_error_toast, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
    }


}
