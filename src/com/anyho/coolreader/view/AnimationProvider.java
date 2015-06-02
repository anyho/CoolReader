package com.anyho.coolreader.view;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.anyho.coolreader.CoolReaderApp;
import com.anyho.coolreader.text.TextModel.Direction;
import com.anyho.coolreader.text.TextModel.PageIndex;

/**
 * 提供动画的抽象类，作为模板类
 */
abstract class AnimationProvider
{
	static enum Mode
	{
		NoScrolling(false), ManualScrolling(false), AnimatedScrollingForward(
				true), AnimatedScrollingBackward(true);
		
		final boolean Auto;
		
		Mode(boolean auto)
		{
			Auto = auto;
		}
	}
	
	private Mode myMode = Mode.NoScrolling;
	
	private final BitmapManager myBitmapManager;
	protected int myStartX;
	protected int myStartY;
	protected int myEndX;
	protected int myEndY;
	protected Direction myDirection;
	protected float mySpeed;
	
	protected int myWidth;
	protected int myHeight;
	
	protected AnimationProvider(BitmapManager bitmapManager)
	{
		myBitmapManager = bitmapManager;
	}
	
	Mode getMode()
	{
		return myMode;
	}
	
	final void terminate()
	{
		Log.e("terminate()", "terminate()");
		// CoolReaderApp.obtainInstance().repaint();
		myMode = Mode.NoScrolling;
		mySpeed = 0;
		myDrawInfos.clear();
	}
	
	final void startManualScrolling(int x, int y)
	{
		if (!myMode.Auto)
		{
			myMode = Mode.ManualScrolling;
			myEndX = myStartX = x;
			myEndY = myStartY = y;
		}
	}
	
	void scrollTo(int x, int y)
	{
		if (myMode == Mode.ManualScrolling)
		{
			myEndX = x;
			myEndY = y;
		}
	}
	
	void startAnimatedScrolling(int x, int y, int speed)
	{
		if (myMode != Mode.ManualScrolling)
		{
			return;
		}
		
		if (getPageToScrollTo(x, y) == PageIndex.current)
		{
			return;
		}
		
		final int diff = myDirection.IsHorizontal ? x - myStartX : y - myStartY;
		final int dpi = CoolReaderApp.obtainInstance().getDisplayDPI();
		final int minDiff = myDirection.IsHorizontal ? (myWidth > myHeight ? myWidth / 4
				: myWidth / 3)
				: (myHeight > myWidth ? myHeight / 4 : myHeight / 3);
		boolean forward = Math.abs(diff) > Math.min(minDiff, dpi / 2);
		
		myMode = forward ? Mode.AnimatedScrollingForward
				: Mode.AnimatedScrollingBackward;
		
		float velocity = 15;
		if (myDrawInfos.size() > 1)
		{
			int duration = 0;
			for (DrawInfo info : myDrawInfos)
			{
				duration += info.Duration;
			}
			duration /= myDrawInfos.size();
			final long time = System.currentTimeMillis();
			myDrawInfos.add(new DrawInfo(x, y, time, time + duration));
			velocity = 0;
			for (int i = 1; i < myDrawInfos.size(); ++i)
			{
				final DrawInfo info0 = myDrawInfos.get(i - 1);
				final DrawInfo info1 = myDrawInfos.get(i);
				final float dX = info0.X - info1.X;
				final float dY = info0.Y - info1.Y;
				velocity += Math.sqrt(dX * dX + dY * dY)
						/ Math.max(1, info1.Start - info0.Start);
			}
			velocity /= myDrawInfos.size() - 1;
			velocity *= duration;
			velocity = Math.min(100, Math.max(15, velocity));
		}
		myDrawInfos.clear();
		
		if (getPageToScrollTo() == PageIndex.previous)
		{
			forward = !forward;
		}
		
		switch (myDirection)
		{
			case up:
			case rightToLeft:
				mySpeed = forward ? -velocity : velocity;
				break;
			case leftToRight:
			case down:
				mySpeed = forward ? velocity : -velocity;
				break;
		}
		
		startAnimatedScrollingInternal(speed);
	}
	
	/**
	 * 开始自动的滚动
	 * 
	 * @param pageIndex
	 *            目标页索引
	 * @param x
	 *            点击的x Integer
	 * @param y
	 *            点击的y Integer
	 * @param speed
	 *            速度
	 */
	public void startAnimatedScrolling(PageIndex pageIndex, Integer x,
			Integer y, int speed)
	{
		if (myMode.Auto)
		{
			return;
		}
		
		terminate();
		myMode = Mode.AnimatedScrollingForward;
		
		switch (myDirection)
		{
			case up:
			case rightToLeft:
				mySpeed = pageIndex == PageIndex.next ? -15 : 15;
				break;
			case leftToRight:
			case down:
				mySpeed = pageIndex == PageIndex.next ? 15 : -15;
				break;
		}
		setupAnimatedScrollingStart(x, y);
		startAnimatedScrollingInternal(speed);
	}
	
	/**
	 * 开始自动滚动。mySpeedFactor = (float) Math.pow(1.5, 0.25 * speed);doStep();
	 */
	protected abstract void startAnimatedScrollingInternal(int speed);
	
	/**
	 * 初始化滚动动画的开始点，计算myEndX，myStartX， myEndY，myStartY;
	 * 
	 * @param x
	 *            点击的x Integer
	 * @param y
	 *            点击的y Integer
	 */
	protected abstract void setupAnimatedScrollingStart(Integer x, Integer y);
	
	/**
	 * @return myMode != Mode.NoScrolling;
	 */
	boolean inProgress()
	{
		return myMode != Mode.NoScrolling;
	}
	
	/**
	 * 计算滚动的偏移量
	 * 
	 * @return myDirection.IsHorizontal ? myEndX - myStartX : myEndY - myStartY;
	 */
	protected int getScrollingShift()
	{
		return myDirection.IsHorizontal ? myEndX - myStartX : myEndY - myStartY;
	}
	
	/**
	 * 指定动画的方向和宽高
	 * 
	 * @param direction
	 *            移动方向
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 */
	final void setup(Direction direction, int width, int height)
	{
		myDirection = direction;
		myWidth = width;
		myHeight = height;
	}
	
	/**
	 * 计算每次滚动的myEndX，myEndX，mySpeed *= mySpeedFactor;当滚动距离到达范围就停止动画
	 */
	abstract void doStep();
	
	int getScrolledPercent()
	{
		final int full = myDirection.IsHorizontal ? myWidth : myHeight;
		final int shift = Math.abs(getScrollingShift());
		return 100 * shift / full;
	}
	
	static class DrawInfo
	{
		final int X, Y;
		final long Start;
		final int Duration;
		
		DrawInfo(int x, int y, long start, long finish)
		{
			X = x;
			Y = y;
			Start = start;
			Duration = (int) (finish - start);
		}
	}
	
	final private List<DrawInfo> myDrawInfos = new LinkedList<DrawInfo>();
	
	/**
	 * 动画执行时的绘画，由ZLAndroidWidget.onDrawInScrolling(Canvas)调用
	 * 
	 * @param canvas
	 */
	final void draw(Canvas canvas)
	{
		myBitmapManager.setBitmapSize(myWidth, myHeight);
		final long start = System.currentTimeMillis();
		drawInternal(canvas);
		myDrawInfos.add(new DrawInfo(myEndX, myEndY, start, System
				.currentTimeMillis()));
		if (myDrawInfos.size() > 3)
		{
			myDrawInfos.remove(0);
		}
	}
	
	/**
	 * 抽象方法，由实现子类处理，由AnimationProvider.draw(Canvas canvas)调用
	 * 
	 * @param canvas
	 */
	protected abstract void drawInternal(Canvas canvas);
	
	/**
	 * 由滚动到的myEndX, myEndY，判断目标页索引
	 * 
	 * @param x
	 *            myEndX
	 * @param y
	 *            myEndY
	 * @return 目标页索引
	 */
	abstract PageIndex getPageToScrollTo(int x, int y);
	
	final PageIndex getPageToScrollTo()
	{
		return getPageToScrollTo(myEndX, myEndY);
	}
	
	protected Bitmap getBitmapFrom()
	{
		return myBitmapManager.getBitmap(PageIndex.current);
	}
	
	protected Bitmap getBitmapTo()
	{
		return myBitmapManager.getBitmap(getPageToScrollTo());
	}
}
