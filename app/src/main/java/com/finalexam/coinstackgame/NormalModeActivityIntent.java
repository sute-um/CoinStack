package com.finalexam.coinstackgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class NormalModeActivityIntent extends AppCompatActivity {
    NormalGameViewIntent v;
    receiveThread receiveThread;
    float stageCnt;
    MediaPlayer m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m = MediaPlayer.create(getApplicationContext(),R.raw.stagemusic);
        stageCnt = getIntent().getFloatExtra("stage",2);
        v = new NormalGameViewIntent(this,stageCnt);
        setContentView(v);
        receiveThread = new receiveThread();
        receiveThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
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
                        overridePendingTransition(0, 0);
                        v.gotomain = false;
                    }

                    if(v.restart == true){
                        stopService(new Intent(getApplicationContext(), StageMusicService.class));
                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), NormalModeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
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
        System.exit(0);
    }
}