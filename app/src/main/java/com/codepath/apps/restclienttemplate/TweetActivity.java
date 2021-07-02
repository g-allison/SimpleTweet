package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.annotation.Nullable;

public class TweetActivity extends AppCompatActivity {

    private static final String TAG = "TweetActivity";
    final int CIRCLE_RADIUS = 100;
    final int ROUNDED_RADIUS = 30;

    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreen;
    TextView tvTimestamp;
    ImageView ivImageUrl;
    TextView tvName;
    TwitterClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: called.");

        ActivityTweetBinding binding = ActivityTweetBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        client = TwitterApp.getRestClient(this);

        Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        Log.d(TAG, "onCreate: tweet contents" + tweet);

        tvBody = binding.tvBody;
        tvScreen = binding.tvScreenName;
        tvTimestamp = binding.tvTimestamp;
        ivImageUrl = binding.ivImageUrl;
        tvName = binding.tvName;
        ivProfileImage = binding.ivProfileImage;

        tvBody.setText(tweet.body);
        tvScreen.setText("@" + tweet.user.screenName);
        tvTimestamp.setText(getRelativeTimeAgo(tweet.createdAt));
        tvName.setText(tweet.user.name);
        tvBody.setText(tweet.body);

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .centerCrop()
                .transform(new RoundedCorners(CIRCLE_RADIUS))
                .into(ivProfileImage);

        if (tweet.mediaUrl == null){
            ivImageUrl.setVisibility(View.GONE);
        } else {
            tvBody.setText(parsedTwitterBody(tweet.body));
            ivImageUrl.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(tweet.mediaUrl)
                    .centerCrop()
                    .transform(new RoundedCorners(ROUNDED_RADIUS))
                    .into(ivImageUrl);
        }
    }

    // removes link to image from body of text
    private String parsedTwitterBody(String body) {
        int index = body.lastIndexOf("https");
        Log.i(TAG, "index: " + index);
        Log.i(TAG, "parsedTwitterBody: " + body);
        String substring = body.substring(0, index);
        Log.i(TAG, "parsedTwitterBody: " + substring);
        return substring;
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hours ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " days ago";
            }
        } catch (ParseException e) {
            Log.i(TAG, "getRelativeTimeAgo failed");
            e.printStackTrace();
        }

        return "";
    }
}
