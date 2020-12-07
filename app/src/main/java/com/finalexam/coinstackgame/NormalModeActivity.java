package com.finalexam.coinstackgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class NormalModeActivity extends AppCompatActivity {
    NormalGameView v;
    receiveThread receiveThread;
    //MediaPlayer m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(getApplicationContext(), StageMusicService.class));
        v = new NormalGameView(this);
        setContentView(v);
        receiveThread = new receiveThread();
        receiveThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        v = new NormalGameView(this);
        setContentView(v);
        receiveThread = new receiveThread();
        receiveThread.start();
    }


    @Override
    public void onBackPressed() {
        stopService(new Intent(getApplicationContext(), StageMusicService.class));
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
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
                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), NormalModeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        v.restart=false;
                    }

                }catch (Exception e){}
            }
        }
    }

}