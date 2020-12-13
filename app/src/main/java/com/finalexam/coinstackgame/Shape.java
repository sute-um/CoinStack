package com.finalexam.coinstackgame;

import android.graphics.Rect;

public class Shape {
	
	int left;
	int right;
	int top;
	int bottom;
	
	public Shape(Coin c ) {
		initShape( c );
	}
	
	public void initShape ( Coin c )
	{
		left = (int) (c.x - c.width * c.centerX);
		right = (int) (c.x + c.width * (1-c.centerX));
		bottom = (int) (c.y);
		top = (int) (c.y - c.height);

	}
	
	public Rect getRect ()
	{
		return (new Rect( left, top, right, bottom ));
	}
	
	public int getWidth ()
	{
		return Math.abs( right - left );
	}
	
	public int getHeight ()
	{
		return Math.abs( bottom - top );
	}

}
