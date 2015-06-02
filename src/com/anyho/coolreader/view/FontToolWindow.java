package com.anyho.coolreader.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.anyho.coolreader.CoolReaderApp;
import com.anyho.coolreader.OptionConfig;
import com.anyho.coolreader.R;

public class FontToolWindow extends PopupWindow implements View.OnClickListener
{
	public FontToolWindow(Activity activity, RelativeLayout root)
	{
		super(activity, root);
	}
	
	private Button btnZoomIn, btnZoomOut;
	
	@Override
	protected View createWindowView()
	{
		final LayoutInflater inflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.window_font_tool, this,
				true);
		btnZoomIn = (Button) view.findViewById(R.id.btnZoomIn);
		btnZoomOut = (Button) view.findViewById(R.id.btnZoomOut);
		btnZoomIn.setOnClickListener(this);
		btnZoomOut.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btnZoomIn:
				changeFontSize(true);
				break;
			case R.id.btnZoomOut:
				changeFontSize(false);
				break;
			
			default:
				break;
		}
	}
	
	private final byte delta = 2;
	
	private void changeFontSize(boolean increase)
	{
		final OptionConfig config = OptionConfig.obtainInstance();
		byte fontSize = config.FontSize;
		fontSize = config.setFontSizeOption((byte) (increase ? fontSize + delta
				: fontSize - delta));
		config.FontSize = fontSize;
		CoolReaderApp.obtainInstance().clearAndRepaint();
	}
}
