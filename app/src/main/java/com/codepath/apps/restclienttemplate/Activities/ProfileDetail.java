package com.codepath.apps.restclienttemplate.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class ProfileDetail extends AppCompatActivity {

    ImageView ivProfileImage;
    TextView tvScreenName;
    TextView userHandle;
    TextView followers;
    TextView following;
    TextView location;
    TextView description;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvScreenName = findViewById(R.id.tvScreenName);
        userHandle = findViewById(R.id.userHandle);
        followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);

        user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));

        Glide.with(this).load(user.profileImageUrl).into(ivProfileImage);
        tvScreenName.setText(user.name);
        userHandle.setText(String.format("@%s", user.screenName));
        followers.setText(user.followers_count);
        following.setText(user.friends_count);
        location.setText(user.location);
        description.setText(user.description);


    }
}