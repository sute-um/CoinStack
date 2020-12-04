package com.finalexam.coinstackgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import static java.lang.Thread.sleep;

public class InfinityModeActivity extends AppCompatActivity {
    GameView v;
    receiveThread receiveThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = new GameView(this);
        setContentView(v);
        receiveThread = new receiveThread();
        receiveThread.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        v = new GameView(this);
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
                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        v.gotomain = false;
                    }

                    if(v.restart == true){
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
}