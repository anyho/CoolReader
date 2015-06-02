package com.anyho.coolreader.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class ShiftAnimationProvider extends SimpleAnimationProvider
{
	private final Paint myPaint = new Paint();
	
	ShiftAnimationProvider(BitmapManager bitmapManager)
	{
		super(bitmapManager);
	}
	
	@Override
	protected void drawInternal(Canvas canvas)
	{
		myPaint.setColor(Color.rgb(127, 127, 127));
		if (myDirection.IsHorizontal)
		{
			final int dX = myEndX - myStartX;
			canvas.drawBitmap(getBitmapTo(), dX > 0 ? dX - myWidth : dX
					+ myWidth, 0, myPaint);
			canvas.drawBitmap(getBitmapFrom(), dX, 0, myPaint);
			if (dX > 0 && dX < myWidth)
			{
				canvas.drawLine(dX, 0, dX, myHeight + 1, myPaint);
			}
			else
				if (dX < 0 && dX > -myWidth)
				{
					canvas.drawLine(dX + myWidth, 0, dX + myWidth,
							myHeight + 1, myPaint);
				}
		}
		else
		{
			final int dY = myEndY - myStartY;
			canvas.drawBitmap(getBitmapTo(), 0, dY > 0 ? dY - myHeight : dY
					+ myHeight, myPaint);
			canvas.drawBitmap(getBitmapFrom(), 0, dY, myPaint);
			if (dY > 0 && dY < myHeight)
			{
				canvas.drawLine(0, dY, myWidth + 1, dY, myPaint);
			}
			else
				if (dY < 0 && dY > -myHeight)
				{
					canvas.drawLine(0, dY + myHeight, myWidth + 1, dY
							+ myHeight, myPaint);
				}
		}
	}
}

