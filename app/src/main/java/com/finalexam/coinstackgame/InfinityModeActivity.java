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
        StageMusicManager.create(getApplicationContext());
        StageMusicManager.start();
        v = new GameView(this);
        setContentView(v);
        receiveThread = new receiveThread();
        receiveThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StageMusicManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StageMusicManager.start();
    }

    public void goToMainScreen() {
        StageMusicManager.stop();
        finishAffinity();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
        v.gotomain = false;
    }

    public void restart() {
        StageMusicManager.stop();
        finishAffinity();
        Intent intent = new Intent(getApplicationContext(), InfinityModeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
        v.restart=false;
    }



    class receiveThread extends  Thread {
        @Override
        public void run() {
            while(true){
                try {
                    sleep(100);
                    if (v.gotomain == true) {
                        goToMainScreen();
                        v.gotomain = false;
                    }

                    if(v.restart == true){
                        restart();
                        v.restart=false;
                    }
                }catch (Exception e){}
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainScreen();
    }
}