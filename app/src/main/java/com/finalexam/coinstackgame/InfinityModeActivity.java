package com.finalexam.coinstackgame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class InfinityModeActivity extends AppCompatActivity {
    GameView v;
    receiveThread receiveThread;
    MediaPlayer m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m = MediaPlayer.create(getApplicationContext(),R.raw.stagemusic);
        m.start();
        v = new GameView(this);
        setContentView(v);
        receiveThread = new receiveThread();
        receiveThread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        m.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        m.start();
    }



    class receiveThread extends  Thread {
        @Override
        public void run() {
            while(true){
                try {
                    sleep(100);
                    if (v.gotomain == true) {
                        stopService(new Intent(getApplicationContext(), StageMusicService.class));
                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        v.gotomain = false;
                    }

                    if(v.restart == true){
                        stopService(new Intent(getApplicationContext(), StageMusicService.class));
                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), InfinityModeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        v.restart=false;
                    }
                }catch (Exception e){}
            }
        }
    }

    @Override
    public void onBackPressed() {
        stopService(new Intent(getApplicationContext(), StageMusicService.class));
        super.onBackPressed();
        finishAffinity();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}