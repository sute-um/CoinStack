package com.finalexam.coinstackgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class Stage {
	
	View view;
	Context context;
	
	public int fps;

	Resources res;
	
	int i;
	int bgcRed = 0xFF;
	int bgcGreen = 0xFF;
	int bgcBlue = 0xFF;
	ArrayList<DisplayObject> doArr;
	DisplayObject displayObject;
	
	public float stageAlpha;
	public int stageWidth;
	public int stageHeight;

	
	public Stage ( View view, Context context )
	{
		this.view = view;
		this.context = context;
		res = context.getResources();
		fps = 30;
		stageAlpha = 1;
		WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
		stageWidth = wm.getDefaultDisplay().getWidth();
		stageHeight = wm.getDefaultDisplay().getHeight();
		doArr = new ArrayList<DisplayObject>();
	}
	
	public void render ( Canvas canvas, Bitmap scaleBitmap, Paint p ) {
		if (canvas != null) {
			canvas.drawBitmap(scaleBitmap, 0, 0, p);
		for (i = 0; i < doArr.size(); ++i) {
			displayObject = doArr.get(i);
			displayObject.render(canvas);
		}
		displayObject = null;
		}
	}
	
	public void setBackgroundColor ( int color ) {
		bgcRed = (int)( color / 0x10000 );
		bgcGreen = (int)( color / 0x100 ) - bgcRed * 0x100;
		bgcBlue = (int)( color ) - bgcRed * 0x10000 - bgcGreen * 0x100;
	}
	
	public int getBackgroundColor ( )
	{
		return bgcRed * 0x10000 + bgcGreen * 0x100 + bgcBlue;
	}
	
	public void addChild ( DisplayObject displayObject )
	{
		doArr.add( displayObject );
	}
	
	public void removeChild ( DisplayObject displayObject )
	{
		doArr.remove( displayObject );
	}
	
	public int numChildren ()
	{
		return doArr.size();
	}
}
