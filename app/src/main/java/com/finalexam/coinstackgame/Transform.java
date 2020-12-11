package com.finalexam.coinstackgame;

import android.graphics.Rect;

public class Transform {
	
	int left;
	int right;
	int top;
	int bottom;
	
	public Transform ( MovieClip mc ) {
		initTransform( mc );
	}
	
	public void initTransform ( MovieClip mc )
	{
		left = (int) (mc.x - mc.width * mc.centerX);
		right = (int) (mc.x + mc.width * (1-mc.centerX));
		bottom = (int) (mc.y);
		top = (int) (mc.y - mc.height);

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
