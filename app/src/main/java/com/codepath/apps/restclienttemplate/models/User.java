package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity
public class User {

    public User() {
    }

    @ColumnInfo
    @PrimaryKey
    public long id;

    @ColumnInfo
    public String name;
    @ColumnInfo
    public String screenName;
    @ColumnInfo
    public String profileImageUrl;
    @ColumnInfo
    public String location;
    @ColumnInfo
    public String description;
    @ColumnInfo
    public String followers_count;
    @ColumnInfo
    public String friends_count;

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");
        user.location = jsonObject.getString("location");
        user.description = jsonObject.getString("description");
        user.followers_count = jsonObject.getString("followers_count");
        user.friends_count = jsonObject.getString("friends_count");
        user.id = jsonObject.getLong("id");
        return user;
    }

    public static List<User> fromJsonTweetArray(List<Tweet> tweetFromNetwork) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i <tweetFromNetwork.size(); i++) {
            users.add(tweetFromNetwork.get(i).user);
        }

        return users;
    }
}
