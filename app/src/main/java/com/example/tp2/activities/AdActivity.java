package com.example.tp2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.VideoView;

import com.example.tp2.Config;
import com.example.tp2.R;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Random;

public class AdActivity extends AppCompatActivity {

    public static final String DESTINATION_ACTIVITY = "destActivity";

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

        startActivity(c);
    }
}