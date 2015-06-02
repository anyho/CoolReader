package com.anyho.coolreader.view;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.anyho.coolreader.CoolReaderApp;
import com.anyho.coolreader.R;
import com.anyho.coolreader.text.TextModel;

public class NavigationToolWindow extends PopupWindow implements
		SeekBar.OnSeekBarChangeListener, View.OnClickListener
{
	
	public NavigationToolWindow(Activity activity, RelativeLayout root)
	{
		super(activity, root);
	}
	
	private TextView tvBookPosition;
	private SeekBar sbBookPosition;
	private Button btnConfirm, btnCancel;
	
	@Override
	protected View createWindowView()
	{
		final LayoutInflater inflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.window_navigation, this,
				true);
		
		tvBookPosition = (TextView) view.findViewById(R.id.book_position_text);
		sbBookPosition = (SeekBar) view.findViewById(R.id.book_position_slider);
		btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
		btnCancel = (Button) view.findViewById(R.id.btnCancel);
		sbBookPosition.setOnSeekBarChangeListener(this);
		btnConfirm.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		return view;
	}
	
	@Override
	protected void show()
	{
		setupNavigation();
		super.show();
	}
	
	private int currentPageNumber;
	
	private void setupNavigation()
	{
		final TextModel model = CoolReaderApp.obtainInstance().getTextModel();
		currentPageNumber = model.getCurrentPageNumber();
		final int totalPageNumber = model.getTotalPageNumber();
		String bookpositionStr = currentPageNumber + " / " + totalPageNumber;
		tvBookPosition.setText(bookpositionStr);
		if (sbBookPosition.getMax() != totalPageNumber - 1
				|| sbBookPosition.getProgress() != currentPageNumber - 1)// 减一是为了避免进度为0的歧义
		{
			sbBookPosition.setMax(totalPageNumber - 1);
			sbBookPosition.setProgress(currentPageNumber - 1);
		}
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btnConfirm:
				
				break;
			case R.id.btnCancel:
				gotoPage(currentPageNumber);
				break;
			
			default:
				break;
		}
		hide();
	}
	
	private final String TAG = NavigationToolWindow.class.getSimpleName()
			+ ">>>";
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser)
	{
		if (fromUser)
		{
			final int page = progress + 1;
			Log.e(TAG, "page: " + page);
			String bookpositionStr = page + " / " + (seekBar.getMax() + 1);
			tvBookPosition.setText(bookpositionStr);
			gotoPage(page -1);
		}
	}
	
	private void gotoPage(int page)
	{
		final CoolReaderApp coolreader = (CoolReaderApp) CoolReaderApp
				.obtainInstance();
		coolreader.getTextModel().gotoPage(page);
		coolreader.resetAndRepaint();
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
		// TODO Auto-generated method stub
		
	}
	
}
