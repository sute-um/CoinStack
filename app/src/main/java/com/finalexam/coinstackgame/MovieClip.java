package com.finalexam.coinstackgame;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;

public class MovieClip implements DisplayObject {
	
	Drawable drawable;
	
	public int x;
	public int y;
	public int width;
	public int height;
	public float alpha;
	public boolean visible;
	Data data;
	int distance;
	public float centerX = 0;
	public float centerY = 0;
	
	Transform transform;

	public MovieClip ( Drawable drawable, float centerX, float centerY )
	{
		this.drawable = drawable;
		x = 0;
		y = 0;
		width = drawable.getIntrinsicWidth();
		height = drawable.getIntrinsicHeight();
		alpha = 1;
		visible = true;
		this.centerX = centerX;
		this.centerY = centerY;
		distance = 0;
		transform = new Transform( this );
	}

	public MovieClip ( Drawable drawable, float centerX, float centerY, Data d)
	{
		this.drawable = drawable;
		x = 0;
		y = 0;
		width = drawable.getIntrinsicWidth();
		height = drawable.getIntrinsicHeight();
		alpha = 1;
		visible = true;
		this.centerX = centerX;
		this.centerY = centerY;
		this.data = d;
		transform = new Transform( this );
	}
	
	public MovieClip ( Drawable drawable )
	{
		this( drawable, 0, 0 );
	}

	//
	
	public int getIntrinsicWidth ()
	{
		return drawable.getIntrinsicWidth();
	}
	
	public int getIntrinsicHeight ()
	{
		return drawable.getIntrinsicHeight();
	}
	
	public Transform getTransform ( )
	{
		transform.initTransform( this );
		return transform;
	}
	
	public boolean hitTestPoint ( int x, int y )
	{

		transform.initTransform( this );
		
		boolean checkX = false;
		boolean checkY = false;
			if( (transform.left-140) + distance <= x && transform.right + distance >= x ) checkX = true;
			if( transform.top+data.hitY <= y && transform.bottom >= y ) checkY = true;
		if( checkX && checkY )
		{
			data.setHitY(70);
			return true;
		}
		return false;
	}
	
	public void render ( Canvas canvas )
	{
		if( !visible ) return;
		drawable.setBounds( getTransform().getRect() );
		drawable.setAlpha( (int)(alpha*0xFF) );
		drawable.draw( canvas );
	}
}
