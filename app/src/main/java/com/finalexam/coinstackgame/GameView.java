package com.finalexam.coinstackgame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Looper;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    Context context;
    GameThread gt, gt2;
    GameCalculateThread gct;
    CoinLocationSetThread ct;
    SurfaceHolder holder;

    Stage stage;
    Data data;
    ArrayList<Coin> dropArr, stackArr;
    ArrayList<Integer> dropSpeed, distance;
    Coin dropcoin, maincoin, stackCoin;

    TextField tf;
    Bitmap bg;

    boolean endFlag = false;
    boolean gotomain = false;
    boolean restart = false;
    boolean paused = false;

    float timecnt=1;
    float clearTime;

    int monCnt, i, touchPoint;
    int stageCnt = 1;

    public GameView(Context context) {
        super(context);
        this.context = context;

        changeBackground();

        stage = new Stage( this, context );
        stage.fps = 50;
        stage.stageAlpha = 0.8f;
        dropArr = new ArrayList<Coin>();
        dropSpeed = new ArrayList<Integer>();
        stackArr = new ArrayList<Coin>();
        distance = new ArrayList<Integer>();

        data = new Data( context );
        data.addImageResource( "maincoin", R.drawable.coin );
        data.addImageResource( "dropcoin", R.drawable.coin );
        data.addImageResource("stackCoin", R.drawable.coin);

        monCnt = 0;

        tf = new TextField( 0.5f, 0.5f );
        tf.x = stage.stageWidth / 2;
        tf.y = 200;
        tf.textSize = 60;
        tf.textAlign = Paint.Align.CENTER;
        tf.antiAlias = true;
        stage.addChild( tf );

        maincoin = new Coin( data.getDrawable("maincoin"), 0.5f, 1f, data );
        maincoin.y = stage.stageHeight - 150;
        touchPoint = maincoin.x = stage.stageWidth / 2;
        stage.addChild( maincoin );

        holder = getHolder();
        holder.addCallback( this );
        gt = new GameThread( this, stage, holder );
        gt2 = new GameThread( this, stage, holder);
        gct = new GameCalculateThread( this, stage, timecnt );
        ct = new CoinLocationSetThread(this);
    }

    public void changeBackground() {
        if(stageCnt > 4){
            bg = BitmapFactory.decodeResource(getResources(), R.drawable.infback5);
        }

        switch (stageCnt) {
            case 1:
                bg = BitmapFactory.decodeResource(getResources(), R.drawable.infback1);
                break;
            case 2:
                bg = BitmapFactory.decodeResource(getResources(), R.drawable.infback2);
                break;
            case 3:
                bg = BitmapFactory.decodeResource(getResources(), R.drawable.infback3);
                break;
            case 4:
                bg = BitmapFactory.decodeResource(getResources(), R.drawable.infback4);
                break;
            default:
                bg = BitmapFactory.decodeResource(getResources(), R.drawable.infback5);
                break;
        }
    }

    public void showPauseDialog() {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                PauseDialog  pauseDialog= new PauseDialog(getContext(), new CustumDialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        paused = false;
                    }

                    @Override
                    public void onNegativeClick() {

                    }
                });
                if (!(((Activity) context).isFinishing())) {
                    pauseDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    pauseDialog.show();
                }
            }
        }, 0);
    }

    public void showResultDialog() {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StageMusicManager.stop();
                SoundManager.playSound(3, 1);

                ResultDialog resultDialog = new ResultDialog(getContext(), new CustumDialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        restart = true;
                    }

                    @Override
                    public void onNegativeClick() {
                        gotomain = true;
                    }
                });
                if (!(((Activity) context).isFinishing())) {
                    resultDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    resultDialog.show();
                    String time = String.format("%.2f", clearTime);
                    resultDialog.score.setText(time + "초");
                }
            }
        }, 0);
    }

    public void startTheads() {
        gt.start();
        gt2.start();
        gct.start();
        ct.start();
    }

    public void interruptThreads() {
        gt.interrupt();
        gt2.interrupt();
        gct.interrupt();
        ct.interrupt();
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        switch( event.getAction() )
        {
            case MotionEvent.ACTION_MOVE:
                touchPoint = (int)event.getX();
                break;
        }
        return true;
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    public void surfaceCreated(SurfaceHolder arg0) {
        if(!paused) {
            startTheads();
        }else{
           showPauseDialog();
        }
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        paused = true;
        interruptThreads();
    }

    public void setCoinLocation() {
        maincoin.x +=  (touchPoint - maincoin.x);
    }

    public void onEnterFrame () {
        int centerDistance = 0; //밑에코인과 위의코인 중심점 거리 비교변수
        int speed, idx;
        int tempx=0;
        tf.text = "Score : "+monCnt + "점";


        if (Math.random() < 0.007+(timecnt / 2000)) {
            dropcoin = new Coin(data.getDrawable("dropcoin"), 0.5f, 1);
            dropcoin.y = 0;
            dropcoin.x = (int) (stage.stageWidth * Math.random());
                stage.addChild(dropcoin);
                dropArr.add(dropcoin);
            dropSpeed.add(0);
            dropcoin = null;

        }

        for (i = 0; i < dropArr.size(); ++i) {
            dropcoin = dropArr.get(i);
                speed = dropSpeed.get(i);
            dropcoin.y += speed / 2;
            dropSpeed.set(i, 20);

                if (maincoin.hitTestPoint(dropcoin.x, dropcoin.y)) {
                    if (maincoin.y < stage.stageHeight) {
                        if (dropcoin.y <= 800) {
                            maincoin.y += 600;
                            stageCnt += 1;
                            changeBackground();
                        }
                    } else {
                        if (dropcoin.y <= 800) {
                            maincoin.y += 800;
                            stageCnt += 1;
                            changeBackground();
                        }
                    }

                    centerDistance = Math.abs((maincoin.x + (maincoin.getIntrinsicWidth() / 2)) - (dropcoin.x + (dropcoin.getIntrinsicWidth() / 2)));


                    if (centerDistance > (maincoin.getIntrinsicWidth()) / 2) {
                        maincoin.x = tempx;
                        endFlag = true;

                        showResultDialog();
                        interruptThreads();
                    } else {

                        SoundManager.playSound(1, 1);
                        stackCoin = new Coin(data.getDrawable("stackCoin"), 0.5f, 1);
                        stackCoin.y = (maincoin.y - 20) + data.hitY;
                        stackCoin.x = maincoin.x - (maincoin.x - dropcoin.x);
                        distance.add((dropcoin.x - maincoin.x));
                        stackArr.add(stackCoin);


                        if (stackArr.size() > 1)
                            maincoin.distance += distance.get(stackArr.size() - 1) - distance.get(stackArr.size() - 2);

                        stage.addChild(stackCoin);

                        idx = dropArr.indexOf(dropcoin);
                        stage.removeChild(dropcoin);
                        dropArr.remove(idx);
                        dropSpeed.remove(idx);

                        --i;
                        ++monCnt;
                    }
                }
                if (dropcoin.y > stage.stageHeight + dropcoin.height) {
                    SoundManager.playSound(0, 1);
                    idx = dropArr.indexOf(dropcoin);
                    stage.removeChild(dropcoin);
                    dropArr.remove(idx);
                    dropSpeed.remove(idx);
                    --i;
                }
        }
    }
    public void stacked() {
        for(int i = 0; i < stackArr.size(); i++){
            stackArr.get(i).x = maincoin.x+ (distance.get(i));
            stackArr.get(i).y = maincoin.y-(maincoin.height/8)-((i+1)*(maincoin.height/2));
        }
    }
}



class GameCalculateThread extends Thread
{
    GameView view;
    Stage stage;
     float stageCnt;
    float time;
    public GameCalculateThread ( GameView view, Stage stage, float stageCnt )
    {
        this.view = view;
        this.stage = stage;
        this.stageCnt = stageCnt;
    }

    @Override
    public void run() {
        super.run();
        while(!isInterrupted()) {
            try {
                view.clearTime += 0.01;
                sleep( 10);
                if(!view.paused)
                    view.onEnterFrame();
                if(view.stackCoin != null)
                    view.stacked();

                time +=10;
                if(time >= 5000)
                    view.timecnt+=0.01;
            }
            catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }
}

class CoinLocationSetThread extends Thread{
    GameView view;
    public CoinLocationSetThread(GameView v) {
        this.view = v;
    }

    @Override
    public void run() {
        while(!isInterrupted()){
            try {
                sleep(1);
                view.setCoinLocation();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}



class GameThread extends Thread
{
    SurfaceHolder holder;
    Stage stage;
    Canvas canvas;
    GameView view;
    boolean running;

    public GameThread ( GameView view, Stage stage, SurfaceHolder holder ) {
        this.view = view;
        this.holder = holder;
        this.stage = stage;
        this.running = running;
    }

    @Override
    public void run() {
        super.run();

        Paint p = new android.graphics.Paint();
        p.setColor(Color.RED);

        while( !isInterrupted() ) {
            Bitmap scaleBitmap = Bitmap.createScaledBitmap(view.bg, view.stage.stageWidth, view.stage.stageHeight, false);
            canvas = holder.lockCanvas();
            try {
                synchronized ( holder ) {
                    stage.render( canvas, scaleBitmap , p);
                }
            }
            catch( Exception e ) {
                e.printStackTrace();
            }
            finally {
                if(canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
