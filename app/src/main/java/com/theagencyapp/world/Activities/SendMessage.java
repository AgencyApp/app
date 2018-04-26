package com.theagencyapp.world.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.ClassModel.LastMessage;
import com.theagencyapp.world.ClassModel.Message;
import com.theagencyapp.world.R;

import java.util.ArrayList;

public class SendMessage extends AppCompatActivity {
    String reciverUid;
    FirebaseDatabase firebaseDatabase;
    LastMessage lastMessage;
    String senderUid;
    String reciverName;
    String senderName;
    String status;
    EditText msg;
    ArrayList<Message>messages;
    boolean isMap;
    String coordinates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        Intent i = getIntent();
        reciverUid=i.getStringExtra("reciverUid");
        reciverName=i.getStringExtra("reciverName");
        senderName=i.getStringExtra("senderName");
        status=i.getStringExtra("status");
        firebaseDatabase=FirebaseDatabase.getInstance();
        senderUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        messages=new ArrayList<>();
        //Start loading progress bar
        UpdateUI();

    }

    void UpdateUI()
    {
        DatabaseReference databaseReference=firebaseDatabase.getReference("ChatRef/"+senderUid+"/"+reciverUid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null&&dataSnapshot.getValue(Boolean.class))
                {
                    DatabaseReference dR=firebaseDatabase.getReference("CurrentChat/"+senderUid+"/"+reciverUid);
                    dR.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            lastMessage=dataSnapshot.getValue(LastMessage.class);
                            updateMessages();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else
                {
                    DatabaseReference dR=firebaseDatabase.getReference("ChatConatiner").push();
                    String key=dR.getKey();
                    lastMessage=new LastMessage(reciverName,key);
                    //remove loading
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void updateMessages()
    {
        DatabaseReference databaseReference=firebaseDatabase.getReference("ChatContainer/"+lastMessage.getChatContainer());
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   Message message=snapshot.getValue(Message.class);
                   messages.add(message);
                }
                //stop loading progress bar
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onSend(View view)
    {
        LastMessage tempLastMsgReciver;
        LastMessage tempLastMsgSender;
        Message tempMsg;

        String message=msg.getText().toString();
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        if(!isMap) {
             tempLastMsgSender = new LastMessage(message, ts, reciverName, lastMessage.getChatContainer(),status, false);
             tempLastMsgReciver = new LastMessage(message, ts, senderName, lastMessage.getChatContainer(),status, false);
             tempMsg = new Message(message, ts,senderUid,reciverUid, false);
        }
        else
        {
            tempLastMsgSender=new LastMessage(coordinates, ts, reciverName, lastMessage.getChatContainer(),status, true);
            tempLastMsgReciver = new LastMessage(coordinates, ts,senderName , lastMessage.getChatContainer(),status, true);
            tempMsg = new Message(coordinates, ts,senderUid,reciverUid, true);
        }

        DatabaseReference senderRef=firebaseDatabase.getReference("CurrentChat/"+senderUid+"/"+reciverUid);
        senderRef.setValue(tempLastMsgSender);
        DatabaseReference reciverRef=firebaseDatabase.getReference("CurrentChat/"+reciverUid+"/"+senderUid);
        reciverRef.setValue(tempLastMsgReciver);
        DatabaseReference msgRef=firebaseDatabase.getReference("ChatConatiner").push();
        msgRef.setValue(tempMsg);
        DatabaseReference notificationRef=firebaseDatabase.getReference("Notifications/Message").push();
        notificationRef.setValue(tempMsg);
        DatabaseReference chatref=firebaseDatabase.getReference("ChatRef/"+senderUid+"/"+reciverUid);
        chatref.setValue(true);
        DatabaseReference chatref1=firebaseDatabase.getReference("ChatRef/"+reciverUid+"/"+senderUid);
        chatref.setValue(true);





    }


}
