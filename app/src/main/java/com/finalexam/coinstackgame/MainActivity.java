package com.finalexam.coinstackgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn_Normal;
    Button btn_Infinite;
    Button btn_Exit;
    Button.OnClickListener onClickListener;
    private long Backbtncnt = 0;
    Data data;

    TextView title;

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.d("화면방향", "세로");
            setContentView(R.layout.activity_main);
        }else if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            Log.d("화면방향", "가로");
            setContentView(R.layout.activity_main);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        stopService(new Intent(this, TitleMusicService.class));
        data = new Data(getApplicationContext());
        Log.d("oncreate","do");
    }

    @Override
    public void onBackPressed() {


        if(System.currentTimeMillis() > Backbtncnt + 2000) {
            Backbtncnt = System.currentTimeMillis();
            Toast.makeText(this,"한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(System.currentTimeMillis() <= Backbtncnt+2000){
            stopService(new Intent(this, TitleMusicService.class));
            finishAffinity();
           super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {

        super.onResume();
        setContentView(R.layout.activity_main);
        init();
        action();
        Log.d("onresume","do");
    }

    public void init() {
        /*
        * 초기화 하는 함수
        * 버튼객체생성 등
        */
        startService(new Intent(this, TitleMusicService.class));

        SoundManager.getInstance();
        SoundManager.initSounds(getApplicationContext());
        SoundManager.loadSounds();
        title = findViewById(R.id.maintitle);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_title);
        title.startAnimation(animation);

        btn_Normal = findViewById(R.id.btn_mode1); // 일반 모드
        btn_Infinite = findViewById(R.id.btn_mode2);// 무한 모드
        btn_Exit = findViewById(R.id.btn_exit); // 나가기


        onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                stopService(new Intent(getApplicationContext(), TitleMusicService.class));
                switch(view.getId()) {
                    case R.id.btn_mode1:

                        finishAffinity();
                        startIntent(R.id.btn_mode1);
                        break;
                    case R.id.btn_mode2:

                        finishAffinity();
                        startIntent(R.id.btn_mode2);
                        break;
                    case R.id.btn_exit:

                        finishAffinity();
                        System.runFinalization();
                        break;
                }
            }
        };


    }

    public void startIntent(int id) { // 인텐트

        Intent intent;
        switch(id){
            case R.id.btn_mode1:

                intent = new Intent(getApplicationContext(), NormalModeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.btn_mode2:

                intent = new Intent(getApplicationContext(), InfinityModeActivity.class);
               startActivity(intent);
                overridePendingTransition(0, 0);
                break;
        }
    }

    public void action() {
        /*
        여러 액션 추가 및 정의
         */
        btn_Normal.setOnClickListener(onClickListener);
        btn_Infinite.setOnClickListener(onClickListener);
        btn_Exit.setOnClickListener(onClickListener);
    }


}