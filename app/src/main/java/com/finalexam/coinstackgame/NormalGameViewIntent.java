package com.finalexam.coinstackgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
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

    Context context;
    NormalGameThreadIntent gt, gt2;
    NormalGameCalculateThreadIntent gct;
    NormalMakeThreadIntent mt;
    SurfaceHolder holder;

    Stage stage;
    Data data;
    ArrayList<MovieClip> monArr;
    ArrayList<Integer> monSpeed;
    MovieClip mon;
    MovieClip cha;
    ArrayList<MovieClip> stackArr;
    ArrayList<Integer> distance;
    MovieClip stackCoin;
    TextField tf;

    boolean endFlag = false;
    boolean gotomain = false;
    boolean restart = false;
    boolean running = true;
    boolean clear = false;

    int monCnt;
    int i;
    int touchPoint;
    float stagecnt;
    int life = 3;
    float cntforspeed = 0;

    public NormalGameViewIntent(Context context,float stagec) {
        super(context);
        this.context = context;


        stage = new Stage( this, context );
        stage.fps = 50;
        stage.stageAlpha = 0.8f;
        monArr = new ArrayList<MovieClip>();
        monSpeed = new ArrayList<Integer>();
        stackArr = new ArrayList<MovieClip>();
        distance = new ArrayList<Integer>();

        data = new Data( context );
        data.addImageResource( "maincoin", R.drawable.coins );
        data.addImageResource( "dropcoin", R.drawable.coins );
        data.addImageResource("stackCoin", R.drawable.coins);

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
        cha.y = stage.stageHeight - 80;
        touchPoint = cha.x = stage.stageWidth / 2;
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
                touchPoint = (int)event.getX();
                break;
        }
        return true;
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    public void surfaceCreated(SurfaceHolder arg0) {
        gt.start();
        gt2.start();
        gct.start();
        mt.start();
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        gt.interrupt();
        gt2.interrupt();
        gct.interrupt();
        mt.interrupt();
    }

    public void setCoinLoc() {
        cha.x +=  (touchPoint - cha.x);
    }

    public void onEnterFrame ()
    {
        int centerDistance = 0; //밑에코인과 위의코인 중심점 거리 비교변수
        int speed, idx;
        int tempx=0;
        tf.text = "Stage : "+(int)stagecnt+"\nScore : "+monCnt + "점";

        if(Math.random() < cntforspeed/300)
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
            monSpeed.set( i, (int)(10+cntforspeed) );

            if( cha.hitTestPoint( mon.x, mon.y ) )
            {

                if((cha.x + (cha.getIntrinsicWidth()/2)) - (mon.x + (mon.getIntrinsicWidth()/2)) > 0) { //왼쪽
                    centerDistance = (cha.x + (cha.getIntrinsicWidth() / 2)) - (mon.x + (mon.getIntrinsicWidth() / 2));
                    tempx = cha.x;
                }

                else if((cha.x + (cha.getIntrinsicWidth()/2)) - ((mon.x + (mon.getIntrinsicWidth()/2))) < 0) { //오른쪽
                    centerDistance = (mon.x + (mon.getIntrinsicWidth() / 2)) -(cha.x + (cha.getIntrinsicWidth() / 2)) ;
                    tempx = cha.x;
                }


                if(centerDistance > cha.getIntrinsicWidth()/2){

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
                    Log.d("dis", cha.distance + "disArr " + distance.get(stackArr.size() - 1));

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
                idx = monArr.indexOf( mon );
                stage.removeChild( mon );
                monArr.remove( idx );
                monSpeed.remove( idx );
                --i;
                //++monCnt;
            }
        }
    }
    public void stacked() {
        for(int i = 0; i < stackArr.size(); i++){
            stackArr.get(i).x = cha.x+ (distance.get(i));

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
                view.onEnterFrame();
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

        while( !isInterrupted() )
        {
            canvas = holder.lockCanvas();
            try
            {
                synchronized ( holder ) {
                    stage.render( canvas );
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
