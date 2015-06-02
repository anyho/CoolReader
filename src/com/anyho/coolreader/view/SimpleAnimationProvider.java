package com.anyho.coolreader.view;

import com.anyho.coolreader.text.TextModel.PageIndex;

abstract class SimpleAnimationProvider extends AnimationProvider
{
	private float mySpeedFactor;
	
	SimpleAnimationProvider(BitmapManager bitmapManager)
	{
		super(bitmapManager);
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
				return myStartX < x ? PageIndex.previous : PageIndex.next;
			case leftToRight:
				return myStartX < x ? PageIndex.next : PageIndex.previous;
			case up:
				return myStartY < y ? PageIndex.previous : PageIndex.next;
			case down:
				return myStartY < y ? PageIndex.next : PageIndex.previous;
		}
		return PageIndex.current;
	}
	
	@Override
	protected void setupAnimatedScrollingStart(Integer x, Integer y)
	{
		if (x == null || y == null)
		{
			if (myDirection.IsHorizontal)
			{
				x = mySpeed < 0 ? myWidth : 0;
				y = 0;
			}
			else
			{
				x = 0;
				y = mySpeed < 0 ? myHeight : 0;
			}
		}
		myEndX = myStartX = x;
		myEndY = myStartY = y;
	}
	
	@Override
	protected void startAnimatedScrollingInternal(int speed)
	{
		mySpeedFactor = (float) Math.pow(1.5, 0.25 * speed);
		doStep();
	}
	
	@Override
	void doStep()
	{
		if (!getMode().Auto)
		{
			return;
		}
		
		switch (myDirection)
		{
			case leftToRight:
				myEndX -= (int) mySpeed;
				break;
			case rightToLeft:
				myEndX += (int) mySpeed;
				break;
			case up:
				myEndY += (int) mySpeed;
				break;
			case down:
				myEndY -= (int) mySpeed;
				break;
		}
		final int bound;
		if (getMode() == Mode.AnimatedScrollingForward)
		{
			bound = myDirection.IsHorizontal ? myWidth : myHeight;
		}
		else
		{
			bound = 0;
		}
		if (mySpeed > 0)
		{
			if (getScrollingShift() >= bound)
			{
				if (myDirection.IsHorizontal)
				{
					myEndX = myStartX + bound;
				}
				else
				{
					myEndY = myStartY + bound;
				}
				terminate();
				return;
			}
		}
		else
		{
			if (getScrollingShift() <= -bound)
			{
				if (myDirection.IsHorizontal)
				{
					myEndX = myStartX - bound;
				}
				else
				{
					myEndY = myStartY - bound;
				}
				terminate();
				return;
			}
		}
		mySpeed *= mySpeedFactor;
	}
}
