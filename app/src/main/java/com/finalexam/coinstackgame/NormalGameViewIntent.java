package com.finalexam.coinstackgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;
import java.util.ArrayList;


public class NormalGameViewIntent extends SurfaceView implements Callback {

    int i;
    Context context;
    NormalGameThreadIntent gt, gt2;
    NormalGameCalculateThreadIntent gct;
    NormalMakeThreadIntent mt;

    SurfaceHolder holder;

    Stage stage;
    Data data;
    ArrayList<Coin> dropArr, dieArr, lifeArr, stackArr;
    ArrayList<Integer> dropSpeed, dieSpeed, lifeSpeed, distance;
    Coin dropcoin, maincoin, die, lifec, stackCoin;

    TextField tf;
    Bitmap bg;

    boolean endFlag = false;
    boolean gotomain = false;
    boolean restart = false;
    boolean pause = false;

    int monCnt, touchPointX, touchPointY;
    int life = 3;

    float stagecnt, clearTime, cntforspeed;

    public NormalGameViewIntent(Context context,float stagec) {
        super(context);
        this.context = context;

        bg = BitmapFactory.decodeResource(getResources(), R.drawable.infback1);
        stage = new Stage( this, context );
        stage.fps = 50;
        stage.stageAlpha = 0.8f;
        dropArr = new ArrayList<Coin>();
        dropSpeed = new ArrayList<Integer>();
        dieSpeed = new ArrayList<Integer>();
        lifeSpeed = new ArrayList<Integer>();
        stackArr = new ArrayList<Coin>();
        distance = new ArrayList<Integer>();
        dieArr = new ArrayList<Coin>();
        lifeArr = new ArrayList<Coin>();

        data = new Data( context );
        data.addImageResource( "maincoin", R.drawable.coin );
        data.addImageResource( "dropcoin", R.drawable.coin );
        data.addImageResource("stackCoin", R.drawable.coin);
        data.addImageResource("dieCoin", R.drawable.diecoin);
        data.addImageResource("lifecoin",R.drawable.lifecoin);

        monCnt = 0;
        this.stagecnt = stagec;
        cntforspeed = stagecnt;

        tf = new TextField( 0.5f, 0.5f );
        tf.x = stage.stageWidth / 2;
        tf.y = 200;
        tf.textSize = 60;
        tf.textAlign = Align.CENTER;
        tf.antiAlias = true;
        stage.addChild( tf );

        maincoin = new Coin( data.getDrawable("maincoin"), 0.5f, 1f, data );
        maincoin.y = stage.stageHeight - 150;
        touchPointX = maincoin.x = stage.stageWidth / 2;
        touchPointY = maincoin.y;
        stage.addChild( maincoin );

        holder = getHolder();
        holder.addCallback( this );
        gt = new NormalGameThreadIntent( this, stage, holder);
        gt2 = new NormalGameThreadIntent( this, stage, holder);
        gct = new NormalGameCalculateThreadIntent( this, stage);
        mt = new NormalMakeThreadIntent(this);
    }


    public boolean onTouchEvent(MotionEvent event)
    {
        switch( event.getAction() )
        {
            case MotionEvent.ACTION_MOVE:
                touchPointX = (int)event.getX();
                touchPointY = (int)event.getY();
                break;
        }
        return true;
    }

    public void showPauseDialog() {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                PauseDialog  pauseDialog= new PauseDialog(getContext(), new CustumDialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        pause = false;
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
                        data.stagecnt = 1;
                        restart = true;
                    }

                    @Override
                    public void onNegativeClick() {

                        gotomain = true;
                    }
                });

                resultDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                resultDialog.show();
                String time = String.format("%.2f",clearTime);
                resultDialog.score.setText((int)stagecnt+"스테이지 "+time+"초");
            }
        }, 0);
    }

    public void showStageClearDialog() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SoundManager.playSound(2,1);
                final StageClearDialog stageClearDialog = new StageClearDialog(getContext(), new CustumDialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        data.stagecnt++;
                        Log.d("check", data.stagecnt+"");

                        Intent intent = new Intent(getContext(), NormalModeActivityIntent.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("stage",data.stagecnt);
                        getContext().startActivity(intent);

                    }

                    @Override
                    public void onNegativeClick() {

                        gotomain = true;
                    }
                });

                stageClearDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                stageClearDialog.show();
                String time = String.format("%.2f",clearTime);
                stageClearDialog.clearTimeText.setText(time+"초");

            }
        },0);
    }

    public void startTheads() {
        gt.start();
        gt2.start();
        gct.start();
        mt.start();
    }

    public void interruptThreads() {
        gt.interrupt();
        gt2.interrupt();
        gct.interrupt();
        mt.interrupt();
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    public void surfaceCreated(SurfaceHolder arg0) {
        if(!pause) {
            startTheads();
        }else {
            showPauseDialog();
        }
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        pause = true;
        interruptThreads();
    }

    public void setCoinLoc() {
        maincoin.x +=  (touchPointX - maincoin.x);
        maincoin.y += (touchPointY - maincoin.y);
    }

    public void lifecoin() {
        int speed,idx;

        if(Math.random() < cntforspeed/500) {
            lifec = new Coin(data.getDrawable("lifecoin"),0.5f,1);
            lifec.y = 0;
            lifec.x = (int)(stage.stageWidth * Math.random());
            stage.addChild(lifec);
            lifeArr.add(lifec);
            lifeSpeed.add(0);
            lifec = null;
        }

        for(i = 0; i< lifeArr.size(); ++i) {
            lifec = lifeArr.get(i);
            speed = lifeSpeed.get(i);
            lifec.y += speed/2;
            lifeSpeed.set( i, (int)(10+(cntforspeed)) );
            if( maincoin.anotherHitTestPoint( lifec.x, lifec.y ) ){
                SoundManager.playSound(1,1);
                idx = lifeArr.indexOf(lifec);
                stage.removeChild(lifec);
                lifeArr.remove(idx);
                lifeSpeed.remove(idx);
                life++;
            }

            if( (lifec.y > stage.stageHeight + lifec.height)) { //바닥에 닿았을때
                SoundManager.playSound(0,1);
                idx = lifeArr.indexOf(lifec);
                stage.removeChild(lifec);
                lifeArr.remove(idx);
                lifeSpeed.remove(idx);
                --i;
            }
        }
    }

    public void diecoin() {
        int speed,idx;
        if(Math.random() < cntforspeed/500){
            die = new Coin(data.getDrawable("dieCoin"),0.5f,1);
            die.y = 0;
            die.x = (int)(stage.stageWidth * Math.random());
            stage.addChild(die);
            dieArr.add(die);
            dieSpeed.add(0);
            die = null;
        }

        for(i = 0; i< dieArr.size(); ++i) {
            die = dieArr.get(i);
            speed = dieSpeed.get(i);
            die.y += speed/2;
            dieSpeed.set( i, (int)(10+(cntforspeed)) );
            if( maincoin.anotherHitTestPoint( die.x, die.y ) ){

                SoundManager.playSound(1,1);
                if(life<1) {
                    interruptThreads();
                    showResultDialog();
                }else {
                    idx = dieArr.indexOf(die);
                    stage.removeChild(die);
                    dieArr.remove(idx);
                    dieSpeed.remove(idx);
                    --i;
                    life--;
                }
            }

            if( (die.y > stage.stageHeight + die.height)) { //바닥에 닿았을때
                SoundManager.playSound(0,1);

                    idx = dieArr.indexOf(die);
                    stage.removeChild(die);
                    dieArr.remove(idx);
                    dieSpeed.remove(idx);
                    --i;
            }
        }
    }

    public void onEnterFrame ()
    {
        int centerDistance = 0; //밑에코인과 위의코인 중심점 거리 비교변수
        int speed, idx;
        int tempx=0;
        String time = String.format("%.2f",clearTime);
        tf.text = "Stage : "+(int)stagecnt+" "+time + "초 Life : " + life+"개";

        if(Math.random() < cntforspeed/200) {
            dropcoin = new Coin( data.getDrawable("dropcoin"), 0.5f, 1 );
            dropcoin.y = 0;
            dropcoin.x = (int) (stage.stageWidth * Math.random());
            stage.addChild( dropcoin );
            dropArr.add( dropcoin );
            dropSpeed.add( 0 );
            dropcoin = null;

        }

        for( i = 0; i < dropArr.size(); ++ i ) {
            dropcoin = dropArr.get( i );
            speed = dropSpeed.get( i );
            dropcoin.y += speed / 2;
            dropSpeed.set( i, (int)(10+(cntforspeed)) );
            if( maincoin.hitTestPoint( dropcoin.x, dropcoin.y ) ) {
                SoundManager.playSound(1,1);
                centerDistance = Math.abs((maincoin.x + (maincoin.getIntrinsicWidth()/2)) - (dropcoin.x + (dropcoin.getIntrinsicWidth()/2)));

                if(centerDistance >maincoin.getIntrinsicWidth()/2){
                    maincoin.x = tempx;
                    endFlag = true;

                    interruptThreads();
                    showResultDialog();
                }
                else {
                    if(stackArr.size() > 1) {

                        maincoin.x = tempx;
                        endFlag = true;
                        interruptThreads();
                        showStageClearDialog();
                    }

                    stackCoin = new Coin(data.getDrawable("stackCoin"), 0.5f, 1);
                    stackCoin.y = (stage.stageHeight - maincoin.getIntrinsicHeight()) + data.hitY;
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
            if( dropcoin.y > stage.stageHeight + dropcoin.height ) {
                SoundManager.playSound(0,1);

                if(life < 2){
                    interruptThreads();
                    showResultDialog();
                }else {
                    life--;
                    idx = dropArr.indexOf(dropcoin);
                    stage.removeChild(dropcoin);
                    dropArr.remove(idx);
                    dropSpeed.remove(idx);
                    --i;
                }
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


class NormalGameCalculateThreadIntent extends Thread
{
    NormalGameViewIntent view;
    Stage stage;
    public NormalGameCalculateThreadIntent ( NormalGameViewIntent view, Stage stage)
    {
        this.view = view;
        this.stage = stage;
    }

    @Override
    public void run() {
        super.run();
        while(!isInterrupted())
        {
            try
            {
                sleep( 10);
                if(!view.pause) {
                    view.clearTime +=0.01;
                    view.onEnterFrame();
                    view.diecoin();
                    view.lifecoin();
                }
                if(view.stackCoin != null)
                    view.stacked();
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
    }
}

class NormalMakeThreadIntent extends Thread{
    NormalGameViewIntent view;
    public NormalMakeThreadIntent(NormalGameViewIntent v) {
        this.view = v;
    }

    @Override
    public void run() {
        while(!isInterrupted()){
            try {
                view.setCoinLoc();
                sleep(1);
            } catch (InterruptedException e) { }
        }
    }
}


class NormalGameThreadIntent extends Thread
{
    SurfaceHolder holder;
    Stage stage;
    Canvas canvas;
    NormalGameViewIntent view;

    public NormalGameThreadIntent ( NormalGameViewIntent view, Stage stage, SurfaceHolder holder )
    {
        this.view = view;
        this.holder = holder;
        this.stage = stage;
    }


    @Override
    public void run() {
        super.run();

        Paint p = new android.graphics.Paint();
        p.setColor(Color.RED);

        while( !isInterrupted() )
        {
            Bitmap scaleBitmap = Bitmap.createScaledBitmap(view.bg, view.stage.stageWidth, view.stage.stageHeight, false);
            canvas = holder.lockCanvas();
            try
            {
                synchronized ( holder ) {
                    stage.render( canvas, scaleBitmap , p );
                }
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
            finally
            {
                if(canvas != null)
                holder.unlockCanvasAndPost( canvas );
            }
        }
    }
}
