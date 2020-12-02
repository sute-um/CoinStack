package com.finalexam.coinstackgame;

        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Movie;
        import android.graphics.Paint;
        import android.graphics.Paint.Align;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.SurfaceHolder;
        import android.view.SurfaceHolder.Callback;
        import android.view.SurfaceView;

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

    int makeInt = 0;

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
        gt = new GameThread( this, stage, holder );
        gt2 = new GameThread( this, stage, holder );
        gct = new GameCalculateThread( this, stage );
        mt = new MakeThread(this);
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
        gt.stop();
        gt2.stop();
        gct.stop();
        mt.stop();
    }

    public void setCoinLoc() {
        cha.x +=  (touchPoint - cha.x);
    }

    public void onEnterFrame ()
    {
        int speed, idx;

        tf.text = monCnt + "";

        if(Math.random() < 0.01)
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
            monSpeed.set( i, speed +1 );

            if( cha.hitTestPoint( mon.x, mon.y ) )
            {
                stackCoin = new MovieClip(data.getDrawable("stackCoin"),0.5f,1);
                stackCoin.y = (stage.stageHeight -cha.getIntrinsicHeight())+data.hitY;
                stackCoin.x = cha.x - (cha.x - mon.x);
                distance.add(cha.x - mon.x);
                stackArr.add(stackCoin);
                cha.distance += distance.get(stackArr.size()-1)*-1;
                Log.d("dis",cha.distance+"");

                stage.addChild(stackCoin);

                idx = monArr.indexOf( mon );
                stage.removeChild( mon );
                monArr.remove( idx );
                monSpeed.remove( idx );

                --i;
                ++monCnt;
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
            stackArr.get(i).x = cha.x+ (distance.get(i)*-1);

        }
    }
}



class GameCalculateThread extends Thread
{
    GameView view;
    Stage stage;

    public GameCalculateThread ( GameView view, Stage stage )
    {
        this.view = view;
        this.stage = stage;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();

        while( true )
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

class MakeThread extends Thread{
    GameView view;

    public MakeThread(GameView v) {
        this.view = v;
    }

    @Override
    public void run() {
        while(true){
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

    public GameThread ( GameView view, Stage stage, SurfaceHolder holder )
    {
        this.view = view;
        this.holder = holder;
        this.stage = stage;
    }

    @Override
    public void run() {
        super.run();

        while( true )
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
                holder.unlockCanvasAndPost( canvas );
            }
        }
    }
}
