package com.theagencyapp.world.Activities;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.theagencyapp.world.R;
import com.theagencyapp.world.Services.CallServices;

public class MakeCall extends AppCompatActivity {
    Button makecall;
    private Call call;
    SinchClient sinchClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_call);
        makecall = (Button) findViewById(R.id.MakeCall);
        CallServices.init(this);
        sinchClient = CallServices.getSinchClient();
    }

    public void onClick(View view) {
        if (call == null) {
            call = sinchClient.getCallClient().callUser("Dad5213");
            makecall.setText("Hang Up");
        } else {
            call.hangup();
            call = null;
            makecall.setText("Call");
        }
    }
}
