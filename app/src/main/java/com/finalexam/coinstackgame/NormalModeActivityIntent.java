package com.finalexam.coinstackgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class NormalModeActivityIntent extends AppCompatActivity {
    NormalGameViewIntent v;
    receiveThread receiveThread;
    float stageCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stageCnt = getIntent().getFloatExtra("stage",2);
        v = new NormalGameViewIntent(this,stageCnt);
        setContentView(v);
        receiveThread = new receiveThread();
        receiveThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    class receiveThread extends  Thread {
        @Override
        public void run() {
            while(true){
                try {
                    sleep(100);
                    if (v.gotomain == true) {
                        MediaManager.stop();
                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        v.gotomain = false;
                    }

                    if(v.restart == true){
                        MediaManager.stop();
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}