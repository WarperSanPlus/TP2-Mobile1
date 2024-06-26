package com.example.tp2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.VideoView;

import com.example.tp2.Config;
import com.example.tp2.Logger;
import com.example.tp2.R;

import java.util.Random;

public class AdActivity extends AppCompatActivity {
    private static final String DESTINATION_ACTIVITY = "destActivity";

    private static final String[] ADS_LINKS = {
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            "raw/vibing"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        playAd(ADS_LINKS[new Random().nextInt(ADS_LINKS.length)]);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() { }

    private void playAd(String adLink) {
        // TODO : Scale & Turn video depending on the ad played

        Bundle extraBundle = getIntent().getExtras();
        Class dest = (Class) extraBundle.getSerializable(DESTINATION_ACTIVITY);

        extraBundle.remove(DESTINATION_ACTIVITY); // Remove ad extra

        if (Config.ENABLE_ADS){
            if (!adLink.startsWith("http")){
                adLink = "android.resource://" + getPackageName() + "/" + adLink;
            }

            VideoView videoView = findViewById(R.id.adActivity_videoView);
            videoView.setVideoPath(adLink);
            videoView.requestFocus();
            videoView.start();

            videoView.setOnCompletionListener(mp -> videoComplete(dest, extraBundle));
        }
        else
            videoComplete(dest, extraBundle);
    }

    private void videoComplete(Class dest, Bundle extraBundle) {
        Intent c = new Intent(AdActivity.this, dest);

        c.replaceExtras(extraBundle);

        finish(); // Remove AdActivity from the callstack
        startActivity(c);
    }

    public static void startActivityWithAd(Context ctx, Class destination, Bundle extras) {
        Intent c = new Intent(ctx, AdActivity.class);

        c.replaceExtras(extras);
        c.putExtra(AdActivity.DESTINATION_ACTIVITY, destination);

        ctx.startActivity(c);
    }
}