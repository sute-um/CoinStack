package com.finalexam.coinstackgame;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class TitleMusicService extends Service {
    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        mediaPlayer = MediaPlayer.create(this,R.raw.mainmu);
        mediaPlayer.start();

    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }
}