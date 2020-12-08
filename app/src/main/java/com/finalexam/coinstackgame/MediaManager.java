package com.finalexam.coinstackgame;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class MediaManager{
    Context context;

    static MediaPlayer m;
    public static void create(Context context) {
        m = MediaPlayer.create(context, R.raw.stagemusic);
    }

    public static void start() {
        m.start();
    }

    public static void stop(){
        m.stop();
    }

    public static void pause() {
        m.pause();
    }
}
