package com.finalexam.coinstackgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

public class TextField implements DisplayObject {
	
	public int x;
	public int y;
	public boolean antiAlias;
	public String text;
	public int textSize;
	public boolean visible;
	public int textColor;
	public float alpha;
	public Align textAlign;
	
	public float centerX;
	public float centerY;
	
	int tcRed;
	int tcGreen;
	int tcBlue;
	
	Paint paint;
	
	public TextField ( float centerX, float centerY )
	{
		x = 0;
		y = 0;
		antiAlias = true;
		text = "";
		textSize = 20;
		visible = true;
		paint = new Paint();
		textColor = 0;
		alpha = 1;
		tcRed = tcGreen = tcBlue = 0;
		this.centerX = centerX;
		this.centerY = centerY;
		textAlign = Align.LEFT;
	}
	
	public TextField ()
	{
		this( 0, 0 );
	}

	public void setPaint ( Paint paint )
	{
		this.paint = paint;
	}
	
	public Paint getPaint ()
	{
		return paint;
	}
	
	public void setTextColor ( int color )
	{
		tcRed = (int)( color / 0x10000 );
		tcGreen = (int)( color / 0x100 ) - tcRed * 0x100;
		tcBlue = (int)( color ) - tcRed * 0x10000 - tcGreen * 0x100;
	}

	public int getTextColor ( )
	{
		return tcRed * 0x10000 + tcGreen * 0x100 + tcBlue;
	}
	
	public void render(Canvas canvas) {
		// TODO Auto-generated method stub
		
		if( !visible ) return;
		
		Paint paint = new Paint();
		paint.setColor( Color.rgb( tcRed, tcGreen, tcBlue ) );
		paint.setStyle( Style.FILL );
		paint.setTextSize( textSize );
		paint.setAntiAlias( antiAlias );
		paint.setTextAlign( textAlign );
		paint.setAlpha( (int)(alpha*0xFF) );
		
		
		canvas.drawText( text, 0, text.length(), x, y, paint );
	}
}
