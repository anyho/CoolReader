package com.anyho.coolreader.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.anyho.coolreader.CoolReaderApp;
import com.anyho.coolreader.OptionConfig;
import com.anyho.coolreader.text.TextModel;
import com.anyho.coolreader.text.TextModel.Animation;
import com.anyho.coolreader.text.TextModel.Direction;
import com.anyho.coolreader.text.TextModel.PageIndex;

public class CoolReaderWidget extends View implements View.OnClickListener,
		View.OnLongClickListener
{
	static final String TAG = CoolReaderWidget.class.getSimpleName() + ">>>";
	private BitmapManager bitmapManager = new BitmapManager(this);
	
	public CoolReaderWidget(Context context, AttributeSet attrs,
			int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init();
	}
	
	public CoolReaderWidget(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}
	
	public CoolReaderWidget(Context context)
	{
		super(context);
		init();
	}
	
	private void init()
	{
		setFocusableInTouchMode(true);// request the focus when touched
		setDrawingCacheEnabled(false);// disable the draw cache
		setOnLongClickListener(this);
		setOnClickListener(this);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// Log.e(TAG, "onDraw()");
		super.onDraw(canvas);
		TextModel model = CoolReaderApp.obtainInstance().getTextModel();// 过滤掉没有数据的无用绘画
		if (model == null)
		{
			return;
		}
		if (getAnimationProvider().inProgress())
		{
			drawScrollingInternal(canvas);
		}
		else
		{
			drawOnStatic(canvas);
		}
		// Log.e(TAG, "end onDraw()");
	}
	
	/**
	 * 当滚动已经开始，绘画中间的动画，由AnimationProvider.draw(Canvas canvas)处理
	 * 
	 * @param canvas
	 *            ondraw()传过来的canvas
	 */
	private void drawScrollingInternal(Canvas canvas)
	{
		TextModel model = CoolReaderApp.obtainInstance().getTextModel();
		final AnimationProvider animator = getAnimationProvider();
		final AnimationProvider.Mode oldMode = animator.getMode();
		animator.doStep();
		if (animator.inProgress())
		{
			animator.draw(canvas);
			if (animator.getMode().Auto)
			{
				postInvalidate();
			}
		}
		else
		{
			switch (oldMode)
			{
				case AnimatedScrollingForward:
				{
					final PageIndex index = animator.getPageToScrollTo();
					boolean forward = index == PageIndex.next;
					bitmapManager.shift(forward);
					if (hasTrun == null)
					{
						hasTrun = forward;
					}
					else
					{
						if (hasTrun != forward)
						{
							if (forward)
							{
								model.prepareNextPage();
							}
							else
							{
								model.preparePreviousPage();
							}
							hasTrun = forward;
						}
					}
					drawOnStatic(canvas);
					break;
				}
				case AnimatedScrollingBackward:
					break;
				default:
					break;
			}
			
		}
	}
	
	private Boolean hasTrun = null;
	private Paint mPaint = new Paint();
	
	private void drawOnStatic(Canvas canvas)
	{
		// Log.e(TAG, "drawOnStatic()");
		bitmapManager.setBitmapSize(getMainWidth(), getMainHeight());
		Bitmap bitmap = bitmapManager.getBitmap(PageIndex.current);
		canvas.drawBitmap(bitmap, 0, 0, mPaint);
		// Log.e(TAG, "end drawOnStatic()");
	}
	
	private int eventX, eventY;
	
	private class LongPressAction implements Runnable
	{
		@Override
		public void run()
		{
			Log.e(TAG, "long press is run");
			hasPendingPressed = false;
			if (performLongClick())
			{
				hasLongPressPerformed = true;
			}
		}
	}
	
	private boolean hasLongPressPerformed = false;
	private LongPressAction longPressAction;
	private boolean hasPendingPressed = false;
	
	/**
	 * 要处理以下动作： 1.单击 2.长按+移动 3.移动
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int x = (int) event.getX();
		int y = (int) event.getY();
		final TextModel model = CoolReaderApp.obtainInstance().getTextModel();
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				hasPendingPressed = true;
				if (longPressAction == null)
				{
					longPressAction = new LongPressAction();
				}
				postDelayed(longPressAction,
						ViewConfiguration.getLongPressTimeout());
				// performTouchDown(event);
				eventX = x;
				eventY = y;
				break;
			case MotionEvent.ACTION_UP:
				if (hasPendingPressed)// 长按事件未处理，移除长按处理，执行单击事件
				{
					if (longPressAction != null)
					{
						removeCallbacks(longPressAction);
						longPressAction = null;
					}
					performClick();
				}
				else
				{
					if (hasLongPressPerformed)
					{
						Log.e(TAG, "release after long press ");
					}
				}
				hasLongPressPerformed = false;
				hasPendingPressed = false;
				break;
			case MotionEvent.ACTION_MOVE:
				final int slop = ViewConfiguration.get(getContext())
						.getScaledTouchSlop();
				final boolean isMove = Math.abs(x - eventX) > slop
						|| Math.abs(y - eventY) > slop;
				if (hasLongPressPerformed)
				{
					Log.e(TAG, "move after long press");
					model.moveAfterLongPress(x, y);
				}
				else
				{
					if (longPressAction != null)
					{
						removeCallbacks(longPressAction);
						longPressAction = null;
					}
					if (isMove)
					{
						if (hasPendingPressed)
						{
							Log.e(TAG, "manul move");
							model.manulMove();
						}
						hasPendingPressed = false;
					}
				}
				break;
			
			default:
				break;
		}
		return true;
	}
	
	/**
	 * draw the text on bitmap
	 * 
	 * @param bitmap
	 * @param index
	 */
	public void drawOnBitmap(Bitmap bitmap, PageIndex index)
	{
		// Log.e(TAG, "drawOnBitmap()");
		
		if (bitmap == null || index == null)
		{
			return;
		}
		TextModel model = CoolReaderApp.obtainInstance().getTextModel();
		if (model != null)
		{
			// Log.e(TAG, "drawOnBitmap():model is not null");
			TextPaintContext context = new TextPaintContext(new Canvas(bitmap),
					getMainWidth(), getMainHeight());
			CoolReaderApp.obtainInstance().setPaintContext(context);
			model.paint(context, index);
		}
		// Log.e(TAG, "end drawOnBitmap()");
	}
	
	public int getTextLeftMargin()
	{
		return 5;
	}
	
	public int getTextTopMargin()
	{
		return 5;
	}
	
	public int getMainWidth()
	{
		return getWidth();
	}
	
	public int getMainHeight()
	{
		return getHeight();
	}
	
	/**
	 * @param pageIndex
	 *            目标页索引
	 */
	public void startAnimatedScrolling(PageIndex pageIndex, int x, int y,
			Direction direction, int speed)
	{
		final TextModel model = CoolReaderApp.obtainInstance().getTextModel();
		if (pageIndex == PageIndex.current || !model.canScroll(pageIndex))
		{
			return;
		}
		final AnimationProvider animator = getAnimationProvider();
		animator.setup(direction, getWidth(), getMainHeight());
		animator.startAnimatedScrolling(pageIndex, x, y, speed);
		if (animator.getMode().Auto)
		{
			postInvalidate();
		}
	}
	
	private AnimationProvider mAnimationProvider;
	private Animation mAnimationType;
	
	private AnimationProvider getAnimationProvider()
	{
		Animation type = OptionConfig.obtainInstance().getAnimationType();
		if (mAnimationProvider == null || mAnimationType != type)
		{
			mAnimationType = type;
			switch (type)
			{
				case slide:
					mAnimationProvider = new SlideAnimationProvider(
							bitmapManager);
					break;
				case shift:
					mAnimationProvider = new ShiftAnimationProvider(
							bitmapManager);
					break;
				case curl:
					mAnimationProvider = new CurlAnimationProvider(
							bitmapManager);
					break;
				default:
					mAnimationProvider = new NoneAnimationProvider(
							bitmapManager);
					break;
			}
		}
		
		return mAnimationProvider;
	}
	
	@Override
	public boolean onLongClick(View v)
	{
		Log.e(TAG, "onLongClick");
		final TextModel model = CoolReaderApp.obtainInstance().getTextModel();
		model.onLongPress(eventX, eventY);
		return true;
	}
	
	@Override
	public void onClick(View v)
	{
		Log.e(TAG, "onClick");
		final TextModel model = CoolReaderApp.obtainInstance().getTextModel();
		model.onClickZone(eventX, eventY, getWidth(), getHeight());
	}
	
	public void resetAndRepaint()
	{
		bitmapManager.clear();
		postInvalidate();
	}
	
	public void clearAndRepaint()
	{
		final TextModel model = CoolReaderApp.obtainInstance().getTextModel();
		model.reset();
		bitmapManager.clear();
		postInvalidate();
	}
	
}
