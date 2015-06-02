package com.anyho.coolreader.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.GridView;

import com.anyho.coolreader.R;

public class BookShelfGridView extends GridView
{
	private Bitmap background;
	
	public BookShelfGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.bookshelf_layer);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		int cellWidth = background.getWidth();
		int cellHeight = background.getHeight();
		int totalWidth = getWidth();
		int totalHeight = getHeight();
		for (int y = 0; y < totalHeight; y += cellHeight)
		{
			for (int x = 0; x < totalWidth; x += cellWidth)
			{
				canvas.drawBitmap(background, x, y, null);
			}
		}
		super.dispatchDraw(canvas);
	}
	
}
