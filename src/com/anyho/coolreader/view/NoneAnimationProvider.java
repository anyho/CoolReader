package com.anyho.coolreader.view;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.anyho.coolreader.text.TextModel.PageIndex;

class NoneAnimationProvider extends AnimationProvider
{
	private final Paint myPaint = new Paint();
	
	NoneAnimationProvider(BitmapManager bitmapManager)
	{
		super(bitmapManager);
	}
	
	@Override
	protected void drawInternal(Canvas canvas)
	{
		canvas.drawBitmap(getBitmapFrom(), 0, 0, myPaint);
	}
	
	@Override
	void doStep()
	{
		if (getMode().Auto)
		{
			terminate();
		}
	}
	
	@Override
	protected void setupAnimatedScrollingStart(Integer x, Integer y)
	{
		if (myDirection.IsHorizontal)
		{
			myStartX = mySpeed < 0 ? myWidth : 0;
			myEndX = myWidth - myStartX;
			myEndY = myStartY = 0;
		}
		else
		{
			myEndX = myStartX = 0;
			myStartY = mySpeed < 0 ? myHeight : 0;
			myEndY = myHeight - myStartY;
		}
	}
	
	@Override
	protected void startAnimatedScrollingInternal(int speed)
	{
	}
	
	@Override
	PageIndex getPageToScrollTo(int x, int y)
	{
		if (myDirection == null)
		{
			return PageIndex.current;
		}
		
		switch (myDirection)
		{
			case rightToLeft:
				return myStartX < x ? PageIndex.previous
						: PageIndex.next;
			case leftToRight:
				return myStartX < x ? PageIndex.next
						: PageIndex.previous;
			case up:
				return myStartY < y ? PageIndex.previous
						: PageIndex.next;
			case down:
				return myStartY < y ? PageIndex.next
						: PageIndex.previous;
		}
		return PageIndex.current;
	}
}

