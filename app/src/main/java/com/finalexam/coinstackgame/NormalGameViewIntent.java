package com.finalexam.coinstackgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
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


public class NormalGameViewIntent extends SurfaceView implements Callback {

    int i;
    Context context;
    NormalGameThreadIntent gt, gt2;
    NormalGameCalculateThreadIntent gct;
    NormalMakeThreadIntent mt;

    SurfaceHolder holder;

    Stage stage;
    Data data;
    ArrayList<MovieClip> monArr;
    ArrayList<MovieClip> dieArr;
    ArrayList<MovieClip> stackArr;
    ArrayList<MovieClip> lifeArr;
    ArrayList<Integer> distance;
    ArrayList<Integer> lifeSpeed;
    ArrayList<Integer> monSpeed;
    ArrayList<Integer> dieSpeed;
    MovieClip mon;
    MovieClip cha;
    MovieClip lifec;

    MovieClip die;

    MovieClip stackCoin;
    TextField tf;
    Bitmap bg;

    boolean endFlag = false;
    boolean gotomain = false;
    boolean restart = false;
    boolean running = true;
    boolean pause = false;

    int monCnt;
    int touchPointX;
    int touchPointY;
    float stagecnt;
    int life = 3;
    float cntforspeed = 0;
    float clearTime;

    public NormalGameViewIntent(Context context,float stagec) {
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
        this.stagecnt = stagec;
        cntforspeed = stagecnt;

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
        touchPointY = cha.y;
        stage.addChild( cha );

        holder = getHolder();
        holder.addCallback( this );
        gt = new NormalGameThreadIntent( this, stage, holder, running );
        gt2 = new NormalGameThreadIntent( this, stage, holder, running );
        gct = new NormalGameCalculateThreadIntent( this, stage, running );
        mt = new NormalMakeThreadIntent(this, running);
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
        if(!pause) {
            gt.start();
            gt2.start();
            gct.start();
            mt.start();
        }else {
            pause = false;
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

        if(Math.random() < cntforspeed/500){
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
            lifeSpeed.set( i, (int)(10+(cntforspeed)) );
            Log.d("lifecnt",speed+"");
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
        Log.d("diecnt",cntforspeed+"");
        if(Math.random() < cntforspeed/500){
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
            dieSpeed.set( i, (int)(10+(cntforspeed)) );
            Log.d("diecnt",speed+"");
            if( cha.anotherHitTestPoint( die.x, die.y ) ){

                SoundManager.playSound(1,1);
                if(life<1) {
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
                            String time = String.format("%.2f", clearTime);
                            resultDialog.score.setText((int) stagecnt + "스테이지 " + time + "초");
                        }
                    }, 0);
                }else {
                    idx = dieArr.indexOf(die);
                    stage.removeChild(die);
                    dieArr.remove(idx);
                    dieSpeed.remove(idx);
                    --i;
                    life--;
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
        Log.d("coincnt",cntforspeed+"");
        String time = String.format("%.2f",clearTime);
        tf.text = "Stage : "+(int)stagecnt+" "+time + "초 Life : " + life+"개";

        if(Math.random() < cntforspeed/200)
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
            monSpeed.set( i, (int)(10+(cntforspeed)) );
            Log.d("coincnt",speed+"");
            if( cha.hitTestPoint( mon.x, mon.y ) )
            {
                SoundManager.playSound(1,1);
                centerDistance = Math.abs((cha.x + (cha.getIntrinsicWidth()/2)) - (mon.x + (mon.getIntrinsicWidth()/2)));

                if(centerDistance >cha.getIntrinsicWidth()/2){

                    cha.x = tempx;
                    endFlag = true;


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
                            SoundManager.playSound(3,1);
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

                            resultDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                            resultDialog.show();
                            String time = String.format("%.2f",clearTime);
                            resultDialog.score.setText((int)stagecnt+"스테이지 "+time+"초");
                        }
                    },0);
                }
                else {
                    if(stackArr.size() > 1){

                        cha.x = tempx;
                        endFlag = true;


                        gt.interrupt();
                        gt2.interrupt();
                        gct.interrupt();
                        mt.interrupt();

                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SoundManager.playSound(2,1);
                                StageClearDialog stageClearDialog = new StageClearDialog(getContext(), new CustumDialogClickListener() {
                                    @Override
                                    public void onPositiveClick() {
                                        stagecnt++;
                                        Log.d("check", stagecnt+"");

                                        Intent intent = new Intent(getContext(), NormalModeActivityIntent.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("stage",stagecnt);
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

                    stackCoin = new MovieClip(data.getDrawable("stackCoin"), 0.5f, 1);
                    stackCoin.y = (stage.stageHeight - cha.getIntrinsicHeight()) + data.hitY;
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
            if( mon.y > stage.stageHeight + mon.height )
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
                }else {
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


class NormalGameCalculateThreadIntent extends Thread
{
    NormalGameViewIntent view;
    Stage stage;
    boolean running;
    public NormalGameCalculateThreadIntent ( NormalGameViewIntent view, Stage stage, boolean running )
    {
        this.view = view;
        this.stage = stage;
        this.running = running;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();

        while( !isInterrupted() )
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
    boolean running;
    public NormalMakeThreadIntent(NormalGameViewIntent v, boolean running) {
        this.view = v;
        this.running = running;
    }

    @Override
    public void run() {
        while(!isInterrupted()){
            try {
                view.setCoinLoc();
                sleep(1);
            } catch (InterruptedException e) {
               // e.printStackTrace();
            }
        }
    }
}


class NormalGameThreadIntent extends Thread
{
    SurfaceHolder holder;
    Stage stage;
    Canvas canvas;
    NormalGameViewIntent view;
    boolean running;

    public NormalGameThreadIntent ( NormalGameViewIntent view, Stage stage, SurfaceHolder holder, boolean running )
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
