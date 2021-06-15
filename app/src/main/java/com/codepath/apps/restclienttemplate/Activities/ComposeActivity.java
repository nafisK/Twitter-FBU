package com.codepath.apps.restclienttemplate.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 140;
    EditText etCompose;
    Button tweetButton;
    TwitterClient client;
    MenuItem miActionProgressItem;
    ProgressBar progressBar;
    ImageView profileImage;
    String profileImageUrl;
    User user;
    Boolean reply;
    TextView tvHandle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        etCompose = findViewById(R.id.etCompose);
        tweetButton = findViewById(R.id.tweetButton);
//        profileImage = findViewById(R.id.ivComposeProfileImage);
//        tvHandle = findViewById(R.id.tvHandle);

        reply = getIntent().getExtras().getBoolean("reply");
//        profileImageUrl = getIntent().getStringExtra("profileImageUrl");
//        user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        setUserHandle();

        client = TwitterApp.getRestClient(this);



        // Set click listener on button
        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Empty Tweet Cannot be Posted", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Tweet Over 140 Characters", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet says: " + tweet);

                            Intent data = new Intent();
                            data.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, data); // set result code and bundle data for response
                            progressBar.setVisibility(View.INVISIBLE);
                            finish(); // closes the activity, pass data to parent
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet", throwable);
                    }
                });




                // Make an API call to Twitter to publish the tweet
            }
        });


    }

    private void setUserHandle() {
        String userHandle = getIntent().getStringExtra("screenName");
        if (reply) {

            etCompose.setText(String.format("@%s", userHandle));
//            tvHandle.setText(String.format("@%s", user.screenName));
//            Glide.with(ComposeActivity.this).load(user.profileImageUrl).into(profileImage);

        }
    }



}