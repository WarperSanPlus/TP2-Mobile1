package com.example.tp2.managers;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

public class AudioManager {
    public static void playAudio(Context ctx, int sound_file_id, MediaPlayer.OnCompletionListener onCompletionListener) {
        MediaPlayer mediaPlayer = MediaPlayer.create(ctx, sound_file_id);

        mediaPlayer.setOnCompletionListener(mp1 -> {
            if (onCompletionListener != null)
                onCompletionListener.onCompletion(mp1);

            clearMediaPlayer(mp1);
        });

        mediaPlayer.start();
    }

    public static MediaPlayer playUrlAudio(String url, boolean useAsync, MediaPlayer.OnCompletionListener onCompletionListener) {
        MediaPlayer mp = new MediaPlayer();

        mp.setOnCompletionListener(mp1 -> {
            if (onCompletionListener != null)
                onCompletionListener.onCompletion(mp1);

            clearMediaPlayer(mp1);
        });


        try {
            mp.setDataSource(url);

            mp.setOnPreparedListener(mediaPlayer -> {
                mediaPlayer.start();
            });

            if (useAsync)
                mp.prepareAsync();
            else
                mp.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mp;
    }

    private static void clearMediaPlayer(MediaPlayer mp){
        if (mp == null)
            return;

        mp.stop();
        mp.release();// this will clear memory
        mp = null;
    }
}
