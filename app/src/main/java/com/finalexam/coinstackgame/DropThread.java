package com.finalexam.coinstackgame;

//동전이 떨어지는 스레드

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class DropThread {
    ImageView coin; //동전 이미지
    int width; //화면의 너비
    int height; //화면의 높이
    int cointWidth; //동전의 너비
    int coinHeight; //동전의 높이
    Random r = new Random();


    //여러 속성들을 갖오고는 생성자
    public DropThread(ImageView v, int w, int h, int cw, int ch){
        coin = v;
        width = w;
        height = h;
        cointWidth = cw;
        coinHeight = ch;

        init();//초기화
        crashCheck();//충돌 감지
    }

    //초기화
    public void init() {
        int rNum = r.nextInt(width -(cointWidth*2) ); //랜덤 위치에 동전 생성
        coin.setX(rNum);
        coin.setY(0);   //위치는 꼭대기에

        final Handler handler = new Handler() { //ui를 바꾸기 위한 핸들러 설정
           public void handleMessage(Message msg) {
               coin.setY(coin.getY() + 10);     //동전을 10만큼 아래로 내림
           }
        };



        Timer timer = new Timer();  //루프 작업을 위한 타이머
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
        };



        timer.schedule(timerTask, 0, 10); // 10초간격으로 timertask 동작

    }

    //바닥과 충돌 감지
    public void crashCheck() {
        //ui를 변경하기 위한 핸들러
        final Handler checkLocation = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                    if (coin.getY() >= height-(coinHeight*2)) { //동전의 위치가 바닥바로위쯤?일때
                        coin.setY(height);//화면밖으로 뺌(removeView가 안됨..ㅠ)
                }
            }
        };
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = checkLocation.obtainMessage();
                checkLocation.sendMessage(msg);
            }
        };
        timer.schedule(timerTask, 0, 100);//0.1초마다 시행
    }

}
