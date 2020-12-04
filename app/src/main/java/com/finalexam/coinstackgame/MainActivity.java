package com.finalexam.coinstackgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn_Normal;
    Button btn_Infinite;
    Button btn_Exit;
    Button.OnClickListener onClickListener;
    private long Backbtncnt = 0;
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new Data(getApplicationContext());
        init();
        action();
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

        btn_Normal = findViewById(R.id.btn_mode1); // 일반 모드
        btn_Infinite = findViewById(R.id.btn_mode2);// 무한 모드
        btn_Exit = findViewById(R.id.btn_exit); // 나가기


        onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                switch(view.getId()) {
                    case R.id.btn_mode1:
                        Toast.makeText(getApplicationContext(),"일반 모드" , Toast.LENGTH_SHORT).show();
                        startIntent(R.id.btn_mode1);
                        break;
                    case R.id.btn_mode2:
                        Toast.makeText(getApplicationContext(),"무한 모드" , Toast.LENGTH_SHORT).show();
                        startIntent(R.id.btn_mode2);
                        break;
                    case R.id.btn_exit:
                        Toast.makeText(getApplicationContext(),"나가기" , Toast.LENGTH_SHORT).show();
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