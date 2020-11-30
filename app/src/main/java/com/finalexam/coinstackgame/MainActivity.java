package com.finalexam.coinstackgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn_Normal;
    Button btn_Infinite;
    Button btn_Exit;
    Button.OnClickListener onClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        action();
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
                        break;
                    case R.id.btn_exit:
                        Toast.makeText(getApplicationContext(),"나가기" , Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };


    }

    public void startIntent(int id) { // 프래그먼트 전환 함수
        switch(id){
            case R.id.btn_mode1:
               Intent intent = new Intent(getApplicationContext(), NormalModeAtivity.class);
               startActivity(intent);
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