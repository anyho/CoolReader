package com.anyho.coolreader.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public abstract class UIUtils
{
	public static void showToastMessage(Context context, String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	public static void showToastMessage(Context context, int resId)
	{
		String message = context.getString(resId);
		showToastMessage(context, message);
	}
	
	public static void runWithMessage(Activity activity, String message,
			final Runnable firstAction, final Runnable lastAction)
	{
		final ProgressDialog dialog = ProgressDialog.show(activity, null,
				message, true, false);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				dialog.dismiss();
				lastAction.run();
			}
		};
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run()
			{
				firstAction.run();
				handler.sendEmptyMessage(0);
			}
		});
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}
	
	public static void runWithMessage(Activity activity, int resId,
			final Runnable firstAction, final Runnable lastAction)
	{
		String message = activity.getString(resId);
		runWithMessage(activity, message, firstAction, lastAction);
	}
}
