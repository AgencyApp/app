package com.theagencyapp.world.Activities;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.theagencyapp.world.R;

import java.util.Locale;

public class myTextToSpeech extends AppCompatActivity {

    TextToSpeech textToSpeech;
    Button speak;
    EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);
        speak=(Button)findViewById(R.id.Text_to_speech_speak);
        text=(EditText)findViewById(R.id.Text_to_Speech_data);
        speak.setEnabled(false);//Disable button
        textToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS)
                {
                    int result=textToSpeech.setLanguage(Locale.ENGLISH);
                    if(result==TextToSpeech.LANG_MISSING_DATA||result==TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(myTextToSpeech.this,"language not Supported",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        speak.setEnabled(true);//Enable Button
                        textToSpeech.setPitch(0.6f);
                        textToSpeech.setSpeechRate(1.0f);

                    }
                }
            }
        });
    }

    public void onClick(View view)
    {
        startSpeaking(text.getText().toString());
    }

    void startSpeaking(String data)
    {
        textToSpeech.speak(data,TextToSpeech.QUEUE_FLUSH,null,null);
    }

    @Override
    protected void onDestroy() {
        textToSpeech.stop();
        textToSpeech.shutdown();
        super.onDestroy();
    }
}
