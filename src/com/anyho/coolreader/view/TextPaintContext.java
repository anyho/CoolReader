package com.anyho.coolreader.view;

import android.R.color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.anyho.coolreader.CoolReaderApp;
import com.anyho.coolreader.CoolReaderApplication;
import com.anyho.coolreader.OptionConfig;
import com.anyho.coolreader.R;
import com.anyho.coolreader.util.CRColor;
import com.anyho.coolreader.util.ColorUtils;

public class TextPaintContext
{
	/**
	 * the canvas to draw on
	 */
	private Canvas mCanvas;
	/**
	 * the paint to draw the text
	 */
	private Paint mTextPaint = new Paint();
	/**
	 * fill the background
	 */
	private Paint mFillPaint = new Paint();
	/**
	 * width
	 */
	private int mWidth;
	/**
	 * height
	 */
	private int mHeight;
	
	public TextPaintContext()
	{
		this(null, 0, 0);
	}
	
	public TextPaintContext(Canvas mCanvas, int mWidth, int mHeight)
	{
		super();
		this.mCanvas = mCanvas;
		this.mWidth = mWidth;
		this.mHeight = mHeight;
		mTextPaint.setLinearText(false);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setSubpixelText(false);
		mTextPaint.setTextSize(24);
	}
	
	public int breakTextChar(char[] text, int index, int count, int maxWidth)
	{
		return mTextPaint.breakText(text, index, count, maxWidth, null);
	}
	
	public int getWordHeight()
	{
		return (int) (mTextPaint.getTextSize() + 0.5f)
				* OptionConfig.obtainInstance().LineSpace * 10 / 100;
	}
	
	public int getDescent()
	{
		return (int) (mTextPaint.descent() + 0.5f);
	}
	
	public void drawTextChar(char[] text, int index, int count, int x, int y)
	{
		mCanvas.drawText(text, index, count, x, y, mTextPaint);
	}
	
	private Bitmap wallpaper;
	
	public void drawBackground()
	{
		final OptionConfig config = OptionConfig.obtainInstance();
		if (config.DAY)
		{
			if (wallpaper == null)
			{
				final Bitmap temp = BitmapFactory.decodeResource(
						CoolReaderApplication.obtainInstance().getResources(),
						R.drawable.wood);
				final int width = Math.min(75, temp.getWidth());
				final int height = Math.min(100, temp.getHeight());
				final Bitmap bitmap = Bitmap.createBitmap(width * 2,
						height * 2, temp.getConfig());
				for (int i = 0; i < width; i++)
				{
					for (int j = 0; j < height; j++)
					{
						int color = temp.getPixel(i, j);
						bitmap.setPixel(i, j, color);
						bitmap.setPixel(i, 2 * height - j - 1, color);
						bitmap.setPixel(2 * width - i - 1, j, color);
						bitmap.setPixel(2 * width - i - 1, 2 * height - j - 1,
								color);
					}
				}
				wallpaper = bitmap;
			}
			final int width = wallpaper.getWidth();
			final int height = wallpaper.getHeight();
			for (int bw = 0; bw < mWidth; bw += width)
			{
				for (int bh = 0; bh < mHeight; bh += height)
				{
					mCanvas.drawBitmap(wallpaper, bw, bh, mFillPaint);
				}
			}
		}
		else
		{
			mTextPaint.setColor(ColorUtils.rgb(new CRColor(128, 128, 128)));
			mFillPaint.setColor(ColorUtils.rgb(new CRColor(0, 0, 0)));
			mCanvas.drawRect(0, 0, mWidth, mHeight, mFillPaint);
		}
	}
	
	public void drawLine(int x0, int y0, int x1, int y1, Paint paint)
	{
		mCanvas.drawLine(x0, y0, x1, y1, paint);
	}
	
	public void fillRectangle(int left, int top, int right, int bottom,
			Paint paint)
	{
		mCanvas.drawRect(left, top, right, bottom, paint);
	}
	
	public void drawString(String text, int left, int top)
	{
		mCanvas.drawText(text, left, top, mTextPaint);
	}
	
	public void drawString(String text, int left, int top, Paint paint)
	{
		mCanvas.drawText(text, left, top, paint);
	}
	
	public int breakText(String text, boolean b, int maxWidth)
	{
		return mTextPaint.breakText(text, b, maxWidth, null);
	}
	
	public byte getLeftMargin()
	{
		return OptionConfig.obtainInstance().LeftMargin;
	}
	
	public byte getTopMargin()
	{
		return OptionConfig.obtainInstance().TopMargin;
	}
	
	public byte getRightMargin()
	{
		return OptionConfig.obtainInstance().RightMargin;
	}
	
	public byte getBottomMargin()
	{
		return OptionConfig.obtainInstance().BottomMargin;
	}
	
	public int getMainTextHeight()
	{
		return mHeight - getTopMargin() - getBottomMargin();
	}
	
	public int getMainTextWidth()
	{
		return mWidth - getLeftMargin() - getRightMargin();
	}
	
	public int getRightEdge()
	{
		return mWidth - getRightMargin();
	}
	
	public int getBottomEdge()
	{
		return mHeight - getBottomMargin();
	}
	
	public void refreshPaintInfo()
	{
		final OptionConfig config = OptionConfig.obtainInstance();
		int fontSize = config.FontSize
				* CoolReaderApp.obtainInstance().getDisplayDPI() / 320 * 2;
		mTextPaint.setTextSize(fontSize);
	}
	
	public int getTextEffectHeight()
	{
		return getWordHeight() + getDescent();
	}
}
