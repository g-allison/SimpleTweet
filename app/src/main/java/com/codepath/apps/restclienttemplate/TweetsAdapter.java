package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;
    public static final String TAG = "TweetsAdapter";
    private OnTweetListener mOnTweetListener;

    // Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets, OnTweetListener onTweetListener) {
        this.context = context;
        this.tweets = tweets;
        this.mOnTweetListener = onTweetListener;
    }

    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);

        return new ViewHolder(view, mOnTweetListener);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get data at position
        Tweet tweet = tweets.get(position);

        // Bind the tweet with view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    // Defines a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreen;
        TextView tvTimestamp;
        ImageView ivImageUrl;
        TextView tvName;

        final int CIRCLE_RADIUS = 100;
        final int ROUNDED_RADIUS = 30;

        OnTweetListener onTweetListener;

        public ViewHolder(@NonNull View itemView, OnTweetListener onTweetListener) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreen = itemView.findViewById(R.id.tvScreenName);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ivImageUrl = itemView.findViewById(R.id.ivImageUrl);
            tvName = itemView.findViewById(R.id.tvName);

            itemView.setOnClickListener(this);
            this.onTweetListener = onTweetListener;
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreen.setText("@" + tweet.user.screenName);
            tvTimestamp.setText(" • " + (getRelativeTimeAgo(tweet.createdAt))); // getRelativeTimeAgo
            tvName.setText(tweet.user.name);

            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .centerCrop()
                    .transform(new RoundedCorners(CIRCLE_RADIUS))
                    .into(ivProfileImage);
            Log.i(TAG, "tweet images: " + tweet.mediaUrl);

            if (tweet.mediaUrl == null){
                ivImageUrl.setVisibility(View.GONE);
            } else {
                tvBody.setText(parsedTwitterBody(tweet.body));
                ivImageUrl.setVisibility(View.VISIBLE);
                Glide.with(context)
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

        @Override
        public void onClick(View v) {
            onTweetListener.onTweetClick(getAdapterPosition());
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
                    return diff / MINUTE_MILLIS + "m";
                } else if (diff < 90 * MINUTE_MILLIS) {
                    return "an hour ago";
                } else if (diff < 24 * HOUR_MILLIS) {
                    return diff / HOUR_MILLIS + "h";
                } else if (diff < 48 * HOUR_MILLIS) {
                    return "yesterday";
                } else {
                    return diff / DAY_MILLIS + "d";
                }
            } catch (ParseException e) {
                Log.i(TAG, "getRelativeTimeAgo failed");
                e.printStackTrace();
            }

            return "";
        }
    }

    public interface OnTweetListener{
        void onTweetClick(int position);
    }

}