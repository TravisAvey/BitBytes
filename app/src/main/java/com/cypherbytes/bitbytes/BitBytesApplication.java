package com.cypherbytes.bitbytes;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * @author Travis
 */
public class BitBytesApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "t0CU05M50hyUsbZLPBPx8fMc2WlYQETDiy3qRL5m", "iS6vOHTrYNQxTRvJ9mtoxFFDHaGygE4JC93oADfS");

    }
}
