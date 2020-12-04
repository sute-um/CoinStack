package com.finalexam.coinstackgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class NormalModeActivity extends AppCompatActivity {
    NormalGameView v;
    receiveThread receiveThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        v.gotomain = false;
                    }

                    if(v.restart == true){
                        finish();
                        Intent intent = new Intent(getApplicationContext(), NormalModeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        v.restart=false;
                    }

                    if(v.restart == true){
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