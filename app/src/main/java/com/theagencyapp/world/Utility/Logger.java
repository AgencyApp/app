package com.theagencyapp.world.Utility;

import android.util.Log;

/**
 * Created by hamza on 04-Apr-18.
 */

public class Logger {

    public static void logError(String Tag, String Uid, String Error)
    {
        Log.e(Tag+Uid,Error);
    }
}
