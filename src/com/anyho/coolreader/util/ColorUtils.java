package com.anyho.coolreader.util;

import android.graphics.Bitmap;
import android.graphics.Color;

public abstract class ColorUtils
{
	public static int rgba(CRColor color, int alpha)
	{
		return Color.argb(alpha, color.Red, color.Green, color.Blue);
	}
	
	public static int rgb(CRColor color)
	{
		return Color.rgb(color.Red, color.Green, color.Blue);
	}
	
	public static CRColor getAverageColor(Bitmap bitmap)
	{
		final int w = Math.min(bitmap.getWidth(), 7);
		final int h = Math.min(bitmap.getHeight(), 7);
		long r = 0, g = 0, b = 0;
		for (int i = 0; i < w; ++i)
		{
			for (int j = 0; j < h; ++j)
			{
				int color = bitmap.getPixel(i, j);
				r += color & 0xFF0000;
				g += color & 0xFF00;
				b += color & 0xFF;
			}
		}
		r /= w * h;
		g /= w * h;
		b /= w * h;
		r >>= 16;
		g >>= 8;
		return new CRColor((int) (r & 0xFF), (int) (g & 0xFF), (int) (b & 0xFF));
	}
}
