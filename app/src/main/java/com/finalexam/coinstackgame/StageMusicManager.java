package com.finalexam.coinstackgame;

import android.content.Context;
import android.media.MediaPlayer;

public class StageMusicManager {
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
