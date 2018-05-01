package com.theagencyapp.world.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by abdul on 4/28/2018.
 */

public class ProfilePicture {
    public static void setProfilePicture(String userId, final ImageView profilePic) {
        FirebaseStorage storage;
        storage = FirebaseStorage.getInstance();
        StorageReference islandRef = storage.getReference().child("UserDP/" + userId + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] data) {
                if (data.length > 0) {


                    Bitmap bmp;

                    bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    profilePic.setImageBitmap(bmp);
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
