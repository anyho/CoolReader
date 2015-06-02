package com.anyho.coolreader;

import java.util.Date;

import android.app.Activity;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.anyho.coolreader.atys.CoolReaderAty;
import com.anyho.coolreader.entity.Book;
import com.anyho.coolreader.text.BookModel;
import com.anyho.coolreader.text.TextModel;
import com.anyho.coolreader.view.BottomWindowManager;
import com.anyho.coolreader.view.CoolReaderWidget;
import com.anyho.coolreader.view.TextPaintContext;

public class CoolReaderApp extends BaseReaderApp
{
	static final String TAG = CoolReaderApp.class.getSimpleName() + ">>>";
	private TextModel mTextModel;
	private CoolReaderAty activity;
	private CoolReaderWidget widget;
	private TextPaintContext mPaintContext;
	
	public CoolReaderApp(CoolReaderAty activity)
	{
		this.activity = activity;
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		height = dm.heightPixels;
		width = dm.widthPixels;
	}
	
	public Activity getActivity()
	{
		return activity;
	}
	
	public void setPaintContext(TextPaintContext paintContext)
	{
		mPaintContext = paintContext;
	}
	
	public TextPaintContext getPaintContext()
	{
		return mPaintContext;
	}
	
	@Override
	public CoolReaderWidget getCoolReaderWidget()
	{
		if (widget == null)
		{
			widget = (CoolReaderWidget) activity.findViewById(R.id.mainwidget);
		}
		return widget;
	}
	
	@Override
	public TextModel getTextModel()
	{
		return mTextModel;
	}
	
	public void openBook(Book book)
	{
		if (book != null)
		{
			Log.e(TAG, "openBookFromFile(): file is not null");
			mTextModel = BookModel.createTextModel(book);
		}
	}
	
	@Override
	public void repaint()
	{
		getCoolReaderWidget().postInvalidate();
	}
	
	@Override
	public void clearAndRepaint()
	{
		getCoolReaderWidget().clearAndRepaint();
	}
	
	public void resetAndRepaint()
	{
		getCoolReaderWidget().resetAndRepaint();
	}
	
	private int height;
	
	public int getHeight()
	{
		
		return height;
	}
	
	private int width;
	
	public int getWidth()
	{
		return width;
	}
	
	public int getDisplayDPI()
	{
		if (activity == null)
		{
			return 0;
		}
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return (int) (160 * metrics.density);
	}
	
	public String getCurrentTimeString()
	{
		return DateFormat.getTimeFormat(activity).format(new Date());
	}
	
	@Override
	public void showMenuWindow()
	{
		final BottomWindowManager manager = BottomWindowManager
				.obtainInstance();
		manager.toggleWindow(BottomWindowManager.KEY_MENU);
	}
	
}
