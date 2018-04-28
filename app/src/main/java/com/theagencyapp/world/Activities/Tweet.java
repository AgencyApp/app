package com.theagencyapp.world.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.theagencyapp.world.R;

import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;


public class Tweet extends AppCompatActivity {
    Uri uri;
    String mCallbackUrl=null;
    RequestToken mRequestToken=null;
    AccessToken mAcesstoken;
    String pin;
    Twitter mtwitter;
    // Twitter mTwitterWithAuth;
    SharedPreferences sharedPreferences;
    String mTwitterVerifier = null;
    String oAuth_token;
    String oAuth_tokenSecret;
    EditText tweetText;
    List<Status> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("81hnFl5b4VM3hUqaApWlZt9eU")
                .setOAuthConsumerSecret("MqWwpdEbryt3gyf4AbQGVlGJ4U8SGNXJJ5X7nYYBTaCNRbPQnB")
                .setOAuthAccessToken("988011971223216128-RlnnJWjgqVzYXo0NA9T2kCR29AEo7oQ")
                .setOAuthAccessTokenSecret("Ob28nHSnq3lA2zSD7tIWmrmiGAGSAEzHMK3VBVh6GhuQ8");
        TwitterFactory tf = new TwitterFactory(cb.build());
        mtwitter = tf.getInstance();
        tweetText=(EditText)findViewById(R.id.twitter_tweetText);
        /*sharedPreferences=getSharedPreferences("twitter", Context.MODE_PRIVATE);
        if(!checkAuthentication())
        {
            StartAuthentication sA=new StartAuthentication();
            sA.execute();
        }
        else
        {
           loadTwitter();
        }*/
        GetTweets getTweets = new GetTweets();
        getTweets.execute();


    }

   /* void loadTwitter()
    {
        oAuth_token=sharedPreferences.getString("oAuth_token",null);
        oAuth_tokenSecret=sharedPreferences.getString("oAuth_tokenSecret",null);
        ConfigurationBuilder cb1 = new ConfigurationBuilder();
        cb1.setDebugEnabled(true)
                .setOAuthConsumerKey("81hnFl5b4VM3hUqaApWlZt9eU")
                .setOAuthConsumerSecret("MqWwpdEbryt3gyf4AbQGVlGJ4U8SGNXJJ5X7nYYBTaCNRbPQnB")
                .setOAuthAccessToken(oAuth_token)
                .setOAuthAccessTokenSecret(oAuth_tokenSecret);
        TwitterFactory tf1 = new TwitterFactory(cb1.build());
        mTwitterWithAuth=tf1.getInstance();
        PublishTweet publishTweet=new PublishTweet();
        publishTweet.execute("Hi");

    }*/


    /*boolean checkAuthentication()
    {
        return sharedPreferences.getBoolean("Authenticated",false);
    }*/


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
            pin=data.getStringExtra("pin");
        Acessotkenkk atg=new Acessotkenkk();
            atg.execute();
    }*/


    public class PublishTweet extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            try {
                mtwitter.updateStatus(strings[0]);
                //  Toast.makeText(Tweet.this,"Tweet Published",Toast.LENGTH_LONG).show();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public class GetTweets extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                tweets = mtwitter.getHomeTimeline();

                //notify data set change.
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


   /* public class StartAuthentication extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            /*ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("81hnFl5b4VM3hUqaApWlZt9eU")
                    .setOAuthConsumerSecret("MqWwpdEbryt3gyf4AbQGVlGJ4U8SGNXJJ5X7nYYBTaCNRbPQnB");
                   // .setOAuthAccessToken("988011971223216128-RlnnJWjgqVzYXo0NA9T2kCR29AEo7oQ")
                    //.setOAuthAccessTokenSecret("Ob28nHSnq3lA2zSD7tIWmrmiGAGSAEzHMK3VBVh6GhuQ8");
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
            try {
                mRequestToken = mtwitter.getOAuthRequestToken(mCallbackUrl);

            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(Tweet.this,
                    TwitterAuthenticationActivity.class);
            intent.putExtra(TwitterAuthenticationActivity.EXTRA_URL,
                    mRequestToken.getAuthenticationURL());
            startActivityForResult(intent, 123);


        }
    }*/

   /* public class Acessotkenkk extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                mAcesstoken = mtwitter.getOAuthAccessToken(mRequestToken,
                        pin);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
             return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("oAuth_token",mAcesstoken.getToken());
            editor.putString("oAuth_tokenSecret",mAcesstoken.getTokenSecret());
            editor.putBoolean("Authenticated",true);
            editor.commit();
            loadTwitter();
        }
    }*/

    public void onTweetClick(View view)
    {
        PublishTweet publishTweet=new PublishTweet();
        publishTweet.execute(tweetText.getText().toString());
    }


}
