package com.codepath.apps.restclienttemplate.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    Tweet tweet;
    ImageView ivProfileImage;
    TextView tvScreenName;
    TextView userHandle;
    TextView tvBody;
    TextView createdAt;
    ImageView tweetMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvScreenName = findViewById(R.id.tvScreenName);
        userHandle = findViewById(R.id.userHandle);
        tvBody = findViewById(R.id.tvBody);
        createdAt = findViewById(R.id.createdAt);
        tweetMedia = findViewById(R.id.tweetMedia);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfileImage);
        tvScreenName.setText(tweet.user.name);
        userHandle.setText(String.format("@%s", tweet.user.screenName));
        tvBody.setText(tweet.body);
        createdAt.setText(tweet.createdAt);
        Glide.with(this).load(tweet.mediaUrl).into(tweetMedia);



    }
}