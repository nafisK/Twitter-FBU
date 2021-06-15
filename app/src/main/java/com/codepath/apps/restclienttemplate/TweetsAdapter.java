package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.Activities.ComposeActivity;
import com.codepath.apps.restclienttemplate.Activities.DetailActivity;
import com.codepath.apps.restclienttemplate.Activities.ProfileDetail;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;

import static com.loopj.android.http.AsyncHttpClient.log;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    List<Tweet> tweets;
    Context context;

    public TweetsAdapter(List<Tweet> tweets, Context context) {
        this.tweets = tweets;
        this.context = context;
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    // For each row, inflate the layout fo ra tweet
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get the data at this position
        Tweet tweet = tweets.get(position);
        // Bind the tweet with view holder
        holder.bind(tweet);

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }




    // Define a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfileImage;
        ImageView tweetMedia;
        TextView tvBody;
        TextView tvScreenName;
        TextView userHandle;
        TextView createdAt;
        Button replyBtn;
        Button heart;
        Button retweet;
        TwitterClient clint;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tweetMedia = itemView.findViewById(R.id.tweetMedia);
            tvBody = itemView.findViewById(R.id.tvBody);
            createdAt = itemView.findViewById(R.id.createdAt);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            userHandle = itemView.findViewById(R.id.userHandle);
            replyBtn = itemView.findViewById(R.id.replyBtn);
            heart = itemView.findViewById(R.id.heart);
            retweet = itemView.findViewById(R.id.retweet);

            clint = TwitterApp.getRestClient(context);

            itemView.setOnClickListener(this);

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent i = new Intent(context, ProfileDetail.class);
                    Tweet tweet = tweets.get(position);
                    i.putExtra("user", Parcels.wrap(tweet.user));
                    context.startActivity(i);
                }
            });

            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Favorited", Toast.LENGTH_SHORT).show();
                    view.setBackgroundResource(R.drawable.ic_vector_heart);
                    final int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        Tweet tweet = tweets.get(position);
                        clint.favoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                log.i("TweetsAdapter", "heart onSuccess");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                log.e("TweetsAdapter", "heart onFailure", throwable);

                            }
                        });

                    }


                }
            });

            retweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        final Tweet tweet = tweets.get(position);
                        clint.retweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Toast.makeText(context, "Retweeted", Toast.LENGTH_SHORT).show();
                                log.i("TweetsAdapter", "retweet onSuccess");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                log.e("TweetsAdapter", "retweet onFailure", throwable);

                            }
                        });

                    }

                }
            });

        }

        public void bind(final Tweet tweet) {
            tvScreenName.setText(tweet.user.name);
            userHandle.setText(String.format("@%s", tweet.user.screenName));
            tvBody.setText(tweet.body);
            createdAt.setText(tweet.createdAt);
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
            Glide.with(context).load(tweet.mediaUrl).into(tweetMedia);

            replyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, ComposeActivity.class);
//                    i.putExtra("user", Parcels.wrap(tweet.user));
                    i.putExtra("screenName", tweet.user.screenName);
                    i.putExtra("reply", true);
//                    i.putExtra("profileImageUrl", tweet.user.profileImageUrl);
                    context.startActivity(i);
                }
            });

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Tweet tweet = tweets.get(position);
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("tweet", Parcels.wrap(tweet));
                context.startActivity(i);
            }
        }
    }

}
