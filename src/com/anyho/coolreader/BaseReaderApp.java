package com.anyho.coolreader;

import android.app.Activity;

import com.anyho.coolreader.text.TextModel;
import com.anyho.coolreader.view.CoolReaderWidget;
import com.anyho.coolreader.view.TextPaintContext;

public abstract class BaseReaderApp
{
	private static BaseReaderApp instance;
	
	protected BaseReaderApp()
	{
		instance = this;
	}
	
	public static BaseReaderApp obtainInstance()
	{
		return instance;
	}
	
	abstract public CoolReaderWidget getCoolReaderWidget();
	
	abstract public TextModel getTextModel();
	
	abstract public Activity getActivity();
	
	abstract public void repaint();
	
	abstract public void clearAndRepaint();
	
	abstract public int getHeight();
	
	abstract public int getWidth();
	
	abstract public int getDisplayDPI();
	
	abstract public String getCurrentTimeString();
	
	abstract public void showMenuWindow();
	
	abstract public TextPaintContext getPaintContext();
	
	abstract public void setPaintContext(TextPaintContext context);
}
