package com.theagencyapp.world.Services;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;

/**
 * Created by hamza on 28-Apr-18.
 */

public class CallServices {

    private static SinchClient sinchClient ;

    public static void init(Context context)
    {
        SinchClient sinchClient;
        sinchClient = Sinch.getSinchClientBuilder()
                .context(context)
                .userId(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .applicationKey("90374a65-63c2-43c6-a21a-6b6610521f31")
                .applicationSecret("tjJxR7uRDkqrpYMEeeUMOw==")
                .environmentHost("clientapi.sinch.com")
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.start();
    }

    public static SinchClient getSinchClient() {
        return sinchClient;
    }

    public static void setSinchClient(SinchClient sinchClient) {
        CallServices.sinchClient = sinchClient;
    }
}
