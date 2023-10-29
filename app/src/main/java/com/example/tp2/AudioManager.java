package com.example.tp2;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class AudioManager {
    public static void playAudio(Context ctx, String uriString, MediaPlayer.OnCompletionListener onCompletionListener) {
        MediaPlayer mp = MediaPlayer.create(ctx, Uri.parse(uriString));

        mp.setOnCompletionListener(onCompletionListener);

        mp.start();
    }

    public static void playUrlAudio(String url, boolean useAsync, MediaPlayer.OnCompletionListener onCompletionListener) {
        MediaPlayer mp = new MediaPlayer();

        mp.setOnCompletionListener(mp1 -> {

            if (onCompletionListener != null)
                onCompletionListener.onCompletion(mp1);

            //mp1 = null;

            mp1 = null;
        });


        try {
            mp.setDataSource(url);

            if (useAsync)
            {
                mp.setOnPreparedListener(mediaPlayer -> {
                    mediaPlayer.start();
                });

                mp.prepareAsync();
            }
            else
            {
                mp.prepare();
                mp.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
