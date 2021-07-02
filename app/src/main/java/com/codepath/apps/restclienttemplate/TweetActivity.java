package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.util.Log;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import javax.annotation.Nullable;

public class TweetActivity extends AppCompatActivity {

    private static final String TAG = "TweetActivity";
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

        setContentView(R.layout.activity_tweet);
        client = TwitterApp.getRestClient(this);

        Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        String body = tweet.body;
        Log.d(TAG, "onCreate: tweet contents" + body);

//        tvBody.setText(findViewById(R.id.tvBody));
        tvBody.setText(body);



        // binding.tvTitle.setText(movie.getTitle());





//        tvBody.setText(tweet.body);
//        tvScreen.setText("@" + tweet.user.screenName);
////        tvTimestamp.setText(" • " + getRelativeTimeAgo(tweet.createdAt));
//        tvName.setText(tweet.user.name);

    }

//    protected void
}
