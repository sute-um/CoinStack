package com.finalexam.coinstackgame;

        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Canvas;
        import android.graphics.Movie;
        import android.graphics.Paint;
        import android.graphics.Paint.Align;
        import android.os.Looper;
        import android.os.Handler;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.SurfaceHolder;
        import android.view.SurfaceHolder.Callback;
        import android.view.SurfaceView;
        import android.view.WindowManager;

        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;

        import java.util.ArrayList;



public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    Context context;
    GameThread gt, gt2;
    GameCalculateThread gct;
    MakeThread mt;
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

    float timecnt=1;


    int monCnt;
    int i;
    int touchPoint;

    public GameView(Context context) {
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

        tf = new TextField( 0.5f, 0.5f );
        tf.x = stage.stageWidth / 2;
        tf.y = 200;
        tf.textSize = 60;
        tf.textAlign = Paint.Align.CENTER;
        tf.antiAlias = true;
        stage.addChild( tf );

        cha = new MovieClip( data.getDrawable("maincoin"), 0.5f, 1f, data );
        cha.y = stage.stageHeight - 80;
        touchPoint = cha.x = stage.stageWidth / 2;
        stage.addChild( cha );

        holder = getHolder();
        holder.addCallback( this );
        gt = new GameThread( this, stage, holder, running );
        gt2 = new GameThread( this, stage, holder, running );
        gct = new GameCalculateThread( this, stage, running, timecnt );
        mt = new MakeThread(this, running);
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
        tf.text = "Score : "+monCnt + "점";

        if(Math.random() < timecnt/1000)
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
            monSpeed.set( i, 20 );

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



class GameCalculateThread extends Thread
{
    GameView view;
    Stage stage;
    boolean running;
     float stageCnt;
    float time;
    public GameCalculateThread ( GameView view, Stage stage, boolean running, float stageCnt )
    {
        this.view = view;
        this.stage = stage;
        this.running = running;
        this.stageCnt = stageCnt;
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

                time +=10;
                if(time >= 5000)
                    view.timecnt+=0.01;
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
    }
}

class MakeThread extends Thread{
    GameView view;
    boolean running;
    public MakeThread(GameView v, boolean running) {
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

    public GameThread ( GameView view, Stage stage, SurfaceHolder holder, boolean running )
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
            finally {
                if(canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}