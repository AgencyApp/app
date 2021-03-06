package com.theagencyapp.world.Activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.Adapters.MessagesAdapter;
import com.theagencyapp.world.ClassModel.LastMessage;
import com.theagencyapp.world.ClassModel.Message;
import com.theagencyapp.world.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SendMessage extends AppCompatActivity {
    String reciverUid;
    FirebaseDatabase firebaseDatabase;
    LastMessage lastMessage;
    String senderUid;
    String reciverName;
    String senderName;
    String status;
    EditText msg;
    ArrayList<Message> messages;
    MessagesAdapter adapter;
    boolean isMap;
    String coordinates;
    ProgressBar activityThreadProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        Intent i = getIntent();
        reciverUid = i.getStringExtra("receiverUid");
        reciverName = i.getStringExtra("receiverName");
        //status=i.getStringExtra("status");
        firebaseDatabase = FirebaseDatabase.getInstance();
        senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        messages = new ArrayList<>();
        msg = findViewById(R.id.chat_input);

        setTitle(reciverName);
        activityThreadProgress = findViewById(R.id.activity_thread_progress);
        activityThreadProgress.setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = findViewById(R.id.messages_recycler);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessagesAdapter(this, senderUid, messages);
        recyclerView.setAdapter(adapter);


        SharedPreferences sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        senderName = sharedPreferences.getString("name", "h");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_thread_send_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSend();
            }
        });
        //String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Timestamp(1524836223));
        //System.out.println(timeStamp);
        UpdateUI();

    }

    public void onMicClick(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        try {
            startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Intent problem", Toast.LENGTH_SHORT).show();
        }
    }


    void UpdateUI() {
        DatabaseReference databaseReference = firebaseDatabase.getReference("ChatRef/" + senderUid + "/" + reciverUid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null && dataSnapshot.getValue(Boolean.class)) {
                    DatabaseReference dR=firebaseDatabase.getReference("CurrentChat/"+senderUid+"/"+reciverUid);
                    dR.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshota) {
                            if (dataSnapshota.getValue() != null && dataSnapshota.getValue(LastMessage.class) != null) {
                                lastMessage = dataSnapshota.getValue(LastMessage.class);
                            } else {
                                DatabaseReference dR = firebaseDatabase.getReference("ChatContainer").push();
                                String key = dR.getKey();
                                lastMessage = new LastMessage(reciverName, key);
                            }
                            updateMessages();


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    DatabaseReference dR = firebaseDatabase.getReference("ChatContainer").push();
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


    void updateMessages() {
        DatabaseReference databaseReference = firebaseDatabase.getReference("ChatContainer/" + lastMessage.getChatContainer());
        databaseReference.orderByChild("timeStamp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                    messages.add(message);
                    activityThreadProgress.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();

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

      /* databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   Message message = snapshot.getValue(Message.class);
                   messages.add(message);
               }
               activityThreadProgress.setVisibility(View.GONE);
               adapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_icon:
                startActivityForResult(new Intent(this, MapsActivity.class), 1);
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                coordinates = Double.toString(data.getDoubleExtra("lat", 0)) + "," + Double.toString(data.getDoubleExtra("lng", 0));
                isMap = true;
                onSend();
            }
        } else if (requestCode == 200) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                msg.setText(result.get(0));
            }
        }
    }

    public void onSend() {
        activityThreadProgress.setVisibility(View.VISIBLE);
        LastMessage tempLastMsgReciver;
        LastMessage tempLastMsgSender;
        Message tempMsg;

        String message = msg.getText().toString();
        Long ts = System.currentTimeMillis() / 1000;

        if (!isMap) {
            tempLastMsgSender = new LastMessage(message, ts, reciverName, lastMessage.getChatContainer(), status, false);
            tempLastMsgReciver = new LastMessage(message, ts, senderName, lastMessage.getChatContainer(), status, false);
            tempMsg = new Message(message, ts, senderUid, reciverUid, false);
        } else {
            tempLastMsgSender = new LastMessage(coordinates, ts, reciverName, lastMessage.getChatContainer(), status, true);
            tempLastMsgReciver = new LastMessage(coordinates, ts, senderName, lastMessage.getChatContainer(), status, true);
            tempMsg = new Message(coordinates, ts, senderUid, reciverUid, true);
        }

        isMap = false;
        msg.setText("");

        DatabaseReference chatRef = firebaseDatabase.getReference("ChatRef/" + senderUid + "/" + reciverUid);
        chatRef.setValue(true);
        DatabaseReference chatRef1 = firebaseDatabase.getReference("ChatRef/" + reciverUid + "/" + senderUid);
        chatRef1.setValue(true);
        DatabaseReference senderRef=firebaseDatabase.getReference("CurrentChat/"+senderUid+"/"+reciverUid);
        senderRef.setValue(tempLastMsgSender);
        DatabaseReference reciverRef = firebaseDatabase.getReference("CurrentChat/" + reciverUid + "/" + senderUid);
        reciverRef.setValue(tempLastMsgReciver);
        DatabaseReference msgRef = firebaseDatabase.getReference("ChatContainer/" + lastMessage.getChatContainer()).push();
        msgRef.setValue(tempMsg);
        DatabaseReference notificationRef = firebaseDatabase.getReference("Notifications/Message").push();
        notificationRef.setValue(tempMsg);

        if (messages.size() == 0) {
            updateMessages();
        }


    }


}
