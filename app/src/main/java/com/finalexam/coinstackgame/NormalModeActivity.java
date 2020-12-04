package com.finalexam.coinstackgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class NormalModeActivity extends AppCompatActivity {
    NormalGameView v;
    receiveThread receiveThread;
    MediaPlayer m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m = MediaPlayer.create(getApplicationContext(),R.raw.stagemusic);
        m.setLooping(true);
        m.start();


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

    class receiveThread extends  Thread {
        @Override
        public void run() {
            while(true){
                try {
                    sleep(100);
                    if (v.gotomain == true) {
                        m.stop();
                        m.release();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        v.gotomain = false;
                    }

                    if(v.restart == true){
                        m.stop();
                        m.release();
                        finish();
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