package com.finalexam.coinstackgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class NormalModeActivity extends AppCompatActivity {
    NormalGameView v;
    receiveThread receiveThread;
    private long Backbtncnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StageMusicManager.create(getApplicationContext());
        StageMusicManager.start();
        v = new NormalGameView(this);
        setContentView(v);
        receiveThread = new receiveThread();
        receiveThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StageMusicManager.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StageMusicManager.pause();
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
        StageMusicManager.start();
        finishAffinity();
        Intent intent = new Intent(getApplicationContext(), NormalModeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
        v.restart=false;
    }

    @Override
    public void onBackPressed() {

        if(System.currentTimeMillis() > Backbtncnt + 2000) {
            Backbtncnt = System.currentTimeMillis();
            Toast.makeText(this,"한번 더 누르면 메인화면으로 이동됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(System.currentTimeMillis() <= Backbtncnt+2000){
            super.onBackPressed();
            goToMainScreen();
        }
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


}