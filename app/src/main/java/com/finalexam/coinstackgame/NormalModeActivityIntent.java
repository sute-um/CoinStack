package com.finalexam.coinstackgame;

import android.content.Intent;
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
        v = new NormalGameViewIntent(this,stageCnt);
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
                        overridePendingTransition(0, 0);
                        v.gotomain = false;
                    }

                    if(v.restart == true){
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
}