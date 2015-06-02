package com.anyho.coolreader.view;

import android.graphics.Bitmap;

import com.anyho.coolreader.text.TextModel.PageIndex;

/**
 * 
 */
public class BitmapManager
{
	static final String TAG = BitmapManager.class.getSimpleName() + ">>>";
	/**
	 * bitmap[] size
	 */
	private final int SIZE = 2;// the front or the behind
	/**
	 * save the bitmap
	 */
	private Bitmap[] bitmaps = new Bitmap[SIZE];
	/**
	 * save the page index of the bitmap
	 */
	private PageIndex[] pageIndexs = new PageIndex[SIZE];
	/**
	 * width of bitmap
	 */
	private int mWidth;
	/**
	 * height of bitmap
	 */
	private int mHeight;
	private CoolReaderWidget crWidget;
	
	public BitmapManager(CoolReaderWidget widget)
	{
		crWidget = widget;
	}
	
	public Bitmap getBitmap(PageIndex index)
	{
		// Log.e(TAG, "getBitmap()");
		for (int i = 0; i < SIZE && index != null; i++)
		{
			if (index == pageIndexs[i])
			{
				return bitmaps[i];
			}
		}
		int aIndex = getAvaliableIndex();
		// Log.e(TAG, "getBitmap(): aIndex:" + aIndex);
		pageIndexs[aIndex] = index;// save the index in the pageindexs
		Bitmap bitmap = bitmaps[aIndex];
		if (bitmap == null)
		{
			bitmap = Bitmap
					.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
			bitmaps[aIndex] = bitmap;// save the bitmap in the bitmaps
		}
		// draw the bitmap by the coolreaderwidget
		crWidget.drawOnBitmap(bitmap, index);
		// Log.e(TAG, "end getBitmap()");
		return bitmap;
	}
	
	private int getAvaliableIndex()
	{
		// Log.e(TAG, "getAvaliableIndex()");
		for (int i = 0; i < SIZE; i++)
		{
			if (pageIndexs[i] == null)
			{
				return i;
			}
		}
		for (int i = 0; i < SIZE; i++)
		{
			if (pageIndexs[i] != PageIndex.current)
			{
				return i;
			}
		}
		throw new RuntimeException("it is impossiable!");
	}
	
	public void setBitmapSize(int width, int height)
	{
		mWidth = width;
		mHeight = height;
	}
	
	/**
	 * 滚动后也要将页索引做相应的移动
	 * 
	 * @param forward
	 */
	void shift(boolean forward)
	{
		for (int i = 0; i < SIZE; ++i)
		{
			if (pageIndexs[i] == null)
			{
				continue;
			}
			pageIndexs[i] = forward ? pageIndexs[i].getPrevious()
					: pageIndexs[i].getNext();
		}
	}
	
	public void clear()
	{
		for (int i = 0; i < SIZE; i++)
		{
			pageIndexs[i] = null;
		}
	}
}
