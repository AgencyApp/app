package com.theagencyapp.world.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theagencyapp.world.ClassModel.Employee;
import com.theagencyapp.world.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class EmployeeProfile extends AppCompatActivity implements IPickResult {
    ImageView profilePic;
   // EditText skills;
    FirebaseStorage storage;
    String uId;
    DatabaseReference empRef;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);
        profilePic=(ImageView)findViewById(R.id.Employee_Profile_Pic);
       // skills=(EditText)findViewById(R.id.Employee_Profile_Skills);
        storage=FirebaseStorage.getInstance();
        uId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        empRef=firebaseDatabase.getReference("Employees/"+uId);
        getProfilePicture(uId);
    }

    public void onPicClick(View view)
    {
        PickSetup setup = new PickSetup().setSystemDialog(true);
        PickImageDialog.build(setup).show(this);
    }
    public void onSaveClick(View view)
    {
        UploadPicture();
    }
    void UploadPicture()
    {
        StorageReference profilePicRef = storage.getReference().child("UserDP/"+uId+".jpg");

        profilePic.setDrawingCacheEnabled(true);
        profilePic.buildDrawingCache();
        Bitmap bitmap = profilePic.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profilePicRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
               /* Uri downloadUrl = taskSnapshot.getDownloadUrl();
                ArrayList<String> temp=new ArrayList<>();
                temp.add(skills.getText().toString());
                Employee employee=new Employee(temp,downloadUrl.toString());
                empRef.setValue(employee);*/
                finish();
            }
        });
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {

            //getImageView().setImageBitmap(r.getBitmap());
            profilePic.setImageBitmap(pickResult.getBitmap());
            //Image path
            //r.getPath();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, pickResult.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void getProfilePicture(String userId)
    {
        StorageReference islandRef = storage.getReference().child("UserDP/"+userId+".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] data) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp;

                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                profilePic.setImageBitmap(bmp);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

            }
        });
    }

}
