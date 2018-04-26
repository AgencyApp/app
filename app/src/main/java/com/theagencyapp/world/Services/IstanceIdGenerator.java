package com.theagencyapp.world.Services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by hamza on 26-Apr-18.
 */

public class IstanceIdGenerator extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        UploadToServer(FirebaseInstanceId.getInstance().getToken());
    }

    void UploadToServer(String key)
    {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference dR = firebaseDatabase.getReference("FCM_InstanceID").child(firebaseUser.getUid());
            dR.setValue(key);
        }
    }
}
