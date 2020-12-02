package com.finalexam.coinstackgame;

//동전이 떨어지고 쌓이는 일

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;


public class DropThread {
    ImageView coin; //동전 이미지
    ImageView maincoin; //바닥 동전
    int width; //화면의 너비
    int height; //화면의 높이
    int cointWidth; //동전의 너비
    int coinHeight; //동전의 높이
    Random r = new Random();
    Data data;

    initThread initThread;




    //여러 속성들을 갖오고는 생성자
    public DropThread(ImageView v,ImageView mv, int w, int h, int cw, int ch, Data d){
        coin = v;
        maincoin = mv;
        width = w;
        height = h;
        cointWidth = cw;
        coinHeight = ch;
        data = d;

        init();//초기화

    }

    //초기화
    public void init() {
        int rNum = r.nextInt(width -(cointWidth*2) ); //랜덤 위치에 동전 생성
        coin.setX(rNum);
        coin.setY(0);   //위치는 꼭대기에

        initThread = new initThread();
        initThread.start();
    }

    class initThread extends Thread {


        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Log.d("initThread", "interrupted!");
                }
            }
        }

        final Handler handler = new Handler() { //ui를 바꾸기 위한 핸들러 설정
            public void handleMessage(Message msg) {
                float cx = coin.getX(); //코인 왼쪽
                float cy = coin.getY(); //코인 상단
                float cw = coin.getWidth() + cx; //코인 오른쪽
                float ch = cy + coin.getHeight(); //코인 바닥
                float mx = maincoin.getX(); //메인코인 왼쪽
                float my = maincoin.getY(); //메인코인 상단
                float mw = maincoin.getWidth()+mx; //메인코인 오른쪽
                float mh = my + maincoin.getHeight(); //메인코인 바닥

                if ( (ch < mh && ch> my) && ((cx > mx && cx < mw) || (cw < mw && cw > mx)) ) {

                    data.setStackcnt(data.getStackcnt() + 1);
                    Log.d("dataset", data.getStackcnt()+"");
                    coin.setX(cx);
                    coin.setY(my-(maincoin.getHeight()-35));

                    stackThread stackThread = new stackThread();
                    stackThread.start();

                    Log.d("충돌","active");
                    interrupt();

                }
                else if (coin.getY() >= height) { //동전의 위치가 바닥바로위쯤?일때
                interrupt();  //스레드 정지
            }
               else
                   coin.setY(coin.getY() + 10);     //동전을 10만큼 아래로 내림
            }
        };

    }

    class stackThread extends Thread{
        @Override
        public void run() {

            while(!Thread.currentThread().isInterrupted()) {
                Message msg = checkLocation.obtainMessage();
                checkLocation.sendMessage(msg);
                try{
                    Thread.sleep(1);
                }catch(InterruptedException e){
                    Log.d("stackThread", "Interrupted!");
                }
            }

        }

        final Handler checkLocation = new Handler(){
            float coinxx = maincoin.getX() - coin.getX();
            @Override
            public void handleMessage(@NonNull Message msg) {
                //crashThread.interrupt();
                coin.setX(maincoin.getX() - (coinxx));
                coin.setY(maincoin.getY()-(maincoin.getHeight()-35));

            }
        };
    }
}
