package com.finalexam.coinstackgame;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class Coin implements DisplayObject {
	
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
	
	Shape shape;

	public Coin(Drawable drawable, float centerX, float centerY )
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
		shape = new Shape( this );

	}

	public Coin(Drawable drawable, float centerX, float centerY, Data d)
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
		shape = new Shape( this );
	}
	
	public Coin(Drawable drawable )
	{
		this( drawable, 0, 0 );
	}

	public int getIntrinsicWidth ()
	{
		return drawable.getIntrinsicWidth();
	}
	
	public int getIntrinsicHeight ()
	{
		return drawable.getIntrinsicHeight();
	}
	
	public Shape getShape( )
	{
		shape.initShape( this );
		return shape;
	}
	
	public boolean hitTestPoint ( int x, int y )
	{

		shape.initShape( this );
		
		boolean checkX = false;
		boolean checkY = false;
			if( (shape.left-(width/2)) + distance <= x && (shape.right+(width/2)) + distance >= x ) checkX = true;
			if( shape.top+data.hitY <= y && shape.bottom >= y ) checkY = true;
		if( checkX && checkY )
		{
			data.setHitY(shape.getHeight()/2);
			return true;
		}
		return false;
	}

	public boolean anotherHitTestPoint ( int x, int y )
	{

		shape.initShape( this );

		boolean checkX = false;
		boolean checkY = false;
		if( (shape.left-(width/2)) + distance <= x && (shape.right+(width/2)) + distance >= x ) checkX = true;
		if( shape.top+data.hitY <= y && shape.bottom >= y ) checkY = true;
		if( checkX && checkY )
		{
			return true;
		}
		return false;
	}

	public void render ( Canvas canvas )
	{
		if( !visible ) return;
		drawable.setBounds( getShape().getRect() );
		drawable.setAlpha( (int)(alpha*0xFF) );
		drawable.draw( canvas );
	}
}
