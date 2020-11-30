package com.finalexam.coinstackgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class NormalModeAtivity extends AppCompatActivity {
    ImageView mainCoin;
    Vector<ImageView> dropCoins  = new Vector<ImageView>();
    ConstraintLayout layout;
    Bitmap bm;
    Matrix m;

    int width; //디스플레이 가로
    int height; //디스플레이 세로

    int coinWidth;
    int coinHeight;
    float x;
    float y;
    int i = 0;

    Thread t;
    View.OnTouchListener myTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {


            switch (event.getActionMasked()){
                case MotionEvent.ACTION_DOWN:
                    x = v.getX() - event.getRawX();
                    y = v.getY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    v.setX(event.getRawX() + x);
                    v.setY(event.getRawY() + y);
                    Log.d(event.getX()+"", event.getY()+"");
                    break;

            }
            return true;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_mode_ativity);

        layout = findViewById(R.id.activity_normal);

        init();
        getDisplaySize();
        gameStart();



    }

    public void getDisplaySize() {
        //화면의 크기 구하기
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //코인 객체 크기 구하기
        coinWidth = mainCoin.getWidth();
        coinHeight = mainCoin.getHeight();
        Log.d(coinWidth+"", " " + coinHeight);
    }

    public void init() {
        //게임 시작 전 화면 초기화

        // 이미지뷰에 등록할 비트맵 생성
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.coins);

        //매트릭스 생성
        m = new Matrix();
        m.postTranslate(0, 0);    //이미지뷰에 등록할 비트맵 초기위치

        // 이미지 뷰 생성
        int w = ViewGroup.LayoutParams.WRAP_CONTENT;
        int h = ViewGroup.LayoutParams.WRAP_CONTENT;

        mainCoin = new ImageView(this);
        mainCoin.setLayoutParams(new ViewGroup.LayoutParams(w, h));    //이미지 크기 지정
        mainCoin.setScaleType(ImageView.ScaleType.MATRIX);  //매트릭스 사용시 scaletype 지정
        mainCoin.setImageBitmap(bm);    //이미지 등록
        mainCoin.setImageMatrix(m);     //매트릭스를 이미지뷰에 적용
        mainCoin.setY(1600);
        mainCoin.setX(500);
        mainCoin.setOnTouchListener(myTouchListener);
        //레이아웃에 이미지뷰 등록
        layout.addView(mainCoin);
    }

    public void gameStart() {

        //ui를 변경하기 위한 핸들러
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {

                ImageView dropCoin = new ImageView(getBaseContext()); //이미지뷰 생성
                dropCoins.add(dropCoin); //동전 저장 벡터에 이미지뷰 추가

                //이미지뷰 설정, 위와 동일
                int w = ViewGroup.LayoutParams.WRAP_CONTENT;
                int h = ViewGroup.LayoutParams.WRAP_CONTENT;
                dropCoins.elementAt(i).setLayoutParams(new ViewGroup.LayoutParams(w, h));    //이미지 크기 지정
                dropCoins.elementAt(i).setScaleType(ImageView.ScaleType.MATRIX);  //매트릭스 사용시 scaletype 지정
                dropCoins.elementAt(i).setImageBitmap(bm);    //이미지 등록
                dropCoins.elementAt(i).setImageMatrix(m);     //매트릭스를 이미지뷰에 적용

                layout.addView(dropCoins.elementAt(i));

                //만들어진 동전객체를 떨구기 위한 동작
                new DropThread(dropCoin, width, height,coinWidth, coinHeight);

                i++;
            }
        };

        //동전 생성을 반복하기 위한 타이머루프
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);

            }
        };

        //1초마다 동전 생성
        timer.schedule(timerTask, 0, 1000);

    }







}