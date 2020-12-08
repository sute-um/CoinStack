package com.finalexam.coinstackgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;


public class NormalGameView extends SurfaceView implements Callback {

    Context context;
    NormalGameThread gt, gt2;
    NormalGameCalculateThread gct;
    NormalMakeThread mt;
    SurfaceHolder holder;

    Stage stage;
    Data data;
    ArrayList<MovieClip> monArr;
    ArrayList<MovieClip> dieArr;
    ArrayList<MovieClip> lifeArr;
    ArrayList<Integer> monSpeed;
    ArrayList<Integer> dieSpeed;
    ArrayList<Integer> lifeSpeed;
    MovieClip mon;
    MovieClip cha;
    MovieClip die;
    MovieClip lifec;
    ArrayList<MovieClip> stackArr;
    ArrayList<Integer> distance;
    MovieClip stackCoin;
    TextField tf;
    Bitmap bg;

    boolean gotomain = false;
    boolean restart = false;
    boolean running = true;
    boolean pause = false;

    int monCnt;
    int i;
    int touchPointX;
    int touchPointY;
    float stagecnt;
    float clearTime;
    int life = 3;



    public NormalGameView(Context context) {
        super(context);
        this.context = context;

        bg = BitmapFactory.decodeResource(getResources(), R.drawable.infback1);
        stage = new Stage( this, context );
        stage.fps = 50;
        stage.stageAlpha = 0.8f;
        monArr = new ArrayList<MovieClip>();
        monSpeed = new ArrayList<Integer>();
        dieSpeed = new ArrayList<Integer>();
        lifeSpeed = new ArrayList<Integer>();
        stackArr = new ArrayList<MovieClip>();
        distance = new ArrayList<Integer>();
        dieArr = new ArrayList<MovieClip>();
        lifeArr = new ArrayList<MovieClip>();

        data = new Data( context );
        data.addImageResource( "maincoin", R.drawable.coin );
        data.addImageResource( "dropcoin", R.drawable.coin );
        data.addImageResource("stackCoin", R.drawable.coin);
        data.addImageResource("dieCoin", R.drawable.diecoin);
        data.addImageResource("lifecoin",R.drawable.lifecoin);

        monCnt = 0;
        stagecnt = data.stagecnt;

        tf = new TextField( 0.5f, 0.5f );
        tf.x = stage.stageWidth / 2;
        tf.y = 200;
        tf.textSize = 60;
        tf.textAlign = Align.CENTER;
        tf.antiAlias = true;
        stage.addChild( tf );

        cha = new MovieClip( data.getDrawable("maincoin"), 0.5f, 1f, data );
        cha.y = stage.stageHeight - 150;
        touchPointX = cha.x = stage.stageWidth / 2;
        touchPointY = cha.y ;
        stage.addChild( cha );

        holder = getHolder();
        holder.addCallback( this );
        gt = new NormalGameThread( this, stage, holder, running );
        gt2 = new NormalGameThread( this, stage, holder, running );
        gct = new NormalGameCalculateThread( this, stage, running );
        mt = new NormalMakeThread(this, running);


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

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    public void surfaceCreated(SurfaceHolder arg0) {
        Log.d("surface","created");

        if(!pause) {
            gt.start();
            gt2.start();
            gct.start();
            mt.start();
        }else{
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Context c = getContext();
                    c.stopService(new Intent(getContext(), StageMusicService.class));

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

    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        pause = true;
        gt.interrupt();
        gt2.interrupt();
        gct.interrupt();
        mt.interrupt();

    }

    public void setCoinLoc() {
        cha.x +=  (touchPointX - cha.x);
        cha.y += (touchPointY - cha.y);
    }

    public void lifecoin() {
        int speed,idx;
        if(Math.random() < stagecnt/800){
            lifec = new MovieClip(data.getDrawable("lifecoin"),0.5f,1);
            lifec.y = 0;
            lifec.x = (int)(stage.stageWidth * Math.random());
            stage.addChild(lifec);
            lifeArr.add(lifec);
            lifeSpeed.add(0);
            lifec = null;
        }

        for(i = 0; i< lifeArr.size(); ++i){
            lifec = lifeArr.get(i);
            speed = lifeSpeed.get(i);
            lifec.y += speed/2;
            lifeSpeed.set(i, (int)(10+stagecnt));

            if( cha.anotherHitTestPoint( lifec.x, lifec.y ) ){
                SoundManager.playSound(1,1);

                idx = lifeArr.indexOf(lifec);
                stage.removeChild(lifec);
                lifeArr.remove(idx);
                lifeSpeed.remove(idx);
                life++;
            }

            if( (lifec.y > stage.stageHeight + lifec.height)) //바닥에 닿았을때
            {
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
        if(Math.random() < stagecnt/600){
            die = new MovieClip(data.getDrawable("dieCoin"),0.5f,1);
            die.y = 0;
            die.x = (int)(stage.stageWidth * Math.random());
            stage.addChild(die);
            dieArr.add(die);
            dieSpeed.add(0);
            die = null;
        }

        for(i = 0; i< dieArr.size(); ++i){
            die = dieArr.get(i);
            speed = dieSpeed.get(i);
            die.y += speed/2;
            dieSpeed.set(i, (int)(10+stagecnt));

            if( cha.anotherHitTestPoint( die.x, die.y ) ){

                if(life<1){
                    gt.interrupt();
                    gt2.interrupt();
                    gct.interrupt();
                    mt.interrupt();

                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Context c = getContext();
                            c.stopService(new Intent(getContext(), StageMusicService.class));
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
                }else {
                    SoundManager.playSound(1,1);
                    life--;
                    idx = dieArr.indexOf(die);
                    stage.removeChild(die);
                    dieArr.remove(idx);
                    dieSpeed.remove(idx);
                    --i;

                }
            }

            if( (die.y > stage.stageHeight + die.height)) //바닥에 닿았을때
            {
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

        if(Math.random() < stagecnt/300)
        {
            mon = new MovieClip( data.getDrawable("dropcoin"), 0.5f, 1 );
            mon.y = 0;
            mon.x = (int) (stage.stageWidth * Math.random());
            stage.addChild( mon );
            monArr.add( mon );
            monSpeed.add( 0 );
            mon = null;

        }

        for( i = 0; i < monArr.size(); ++ i )
        {
            mon = monArr.get( i );
            speed = monSpeed.get( i );
            mon.y += speed / 2;
            monSpeed.set( i, (int)(10+stagecnt) );

            if( cha.hitTestPoint( mon.x, mon.y ) )
            {

                centerDistance = Math.abs((cha.x + (cha.getIntrinsicWidth()/2)) - (mon.x + (mon.getIntrinsicWidth()/2)));
                Log.d("check",centerDistance+"");

                if(centerDistance > cha.getIntrinsicWidth()/2){ //중심에 어긋날 때
                    cha.x = tempx;

                    gt.interrupt();
                    gt2.interrupt();
                    gct.interrupt();
                    mt.interrupt();


                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Context c = getContext();
                            c.stopService(new Intent(getContext(), StageMusicService.class));
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
                else {
                    if(stackArr.size() > 1){

                        cha.x = tempx;
                        gt.interrupt();
                        gt2.interrupt();
                        gct.interrupt();
                        mt.interrupt();

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


                    SoundManager.playSound(1,1);
                    stackCoin = new MovieClip(data.getDrawable("stackCoin"), 0.5f, 1);
                    stackCoin.y = (cha.y - cha.getIntrinsicHeight()) + data.hitY;
                    stackCoin.x = cha.x - (cha.x - mon.x);
                    distance.add((mon.x - cha.x));
                    stackArr.add(stackCoin);

                    if (stackArr.size() > 1)
                        cha.distance += distance.get(stackArr.size() - 1) - distance.get(stackArr.size() - 2);

                    stage.addChild(stackCoin);

                    idx = monArr.indexOf(mon);
                    stage.removeChild(mon);
                    monArr.remove(idx);
                    monSpeed.remove(idx);

                    --i;
                    ++monCnt;
                }
            }
            if( mon.y > stage.stageHeight + mon.height ) //바닥에 닿았을때
            {
                SoundManager.playSound(0,1);

                if(life < 2){
                    gt.interrupt();
                    gt2.interrupt();
                    gct.interrupt();
                    mt.interrupt();

                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Context c = getContext();
                            c.stopService(new Intent(getContext(), StageMusicService.class));
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
                else {

                    life--;
                    idx = monArr.indexOf(mon);
                    stage.removeChild(mon);
                    monArr.remove(idx);
                    monSpeed.remove(idx);
                    --i;
                }
            }
        }
    }
    public void stacked() {
        for(int i = 0; i < stackArr.size(); i++){
            stackArr.get(i).x = cha.x+ (distance.get(i));
            stackArr.get(i).y = cha.y-(cha.height/8)-((i+1)*(cha.height/2));
        }
    }
}


class NormalGameCalculateThread extends Thread
{
    NormalGameView view;
    Stage stage;
    boolean running;
    public NormalGameCalculateThread ( NormalGameView view, Stage stage, boolean running )
    {
        this.view = view;
        this.stage = stage;
        this.running = running;
    }

    @Override
    public void run() {

        super.run();

        while( !isInterrupted() )
        {
            try
            {  sleep( 10);
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

class NormalMakeThread extends Thread{
    NormalGameView view;
    boolean running;
    public NormalMakeThread(NormalGameView v, boolean running) {
        this.view = v;
        this.running = running;
    }

    @Override
    public void run() {
        super.run();
        while(!isInterrupted()){
            try {
                sleep(1);
                view.setCoinLoc();

            } catch (InterruptedException e) { }
        }
    }
}



class NormalGameThread extends Thread
{
    SurfaceHolder holder;
    Stage stage;
    Canvas canvas;
    NormalGameView view;
    boolean running;

    public NormalGameThread ( NormalGameView view, Stage stage, SurfaceHolder holder, boolean running )
    {
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



        while( !isInterrupted() )
        {
            Bitmap scaleBitmap = Bitmap.createScaledBitmap(view.bg,view.stage.stageWidth, view.stage.stageHeight, false);
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
