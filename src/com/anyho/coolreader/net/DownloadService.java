package com.anyho.coolreader.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.anyho.coolreader.R;
import com.anyho.coolreader.util.PathUtils;
import com.anyho.coolreader.util.UIUtils;

public class DownloadService
{
	private Context context;
	private Notification.Builder builder;
	private NotificationManager nm;
	private NetBook currentBook;
	private DownloadCallback mDownloadCallback;
	
	public DownloadService(Context context, DownloadCallback downloadCallback)
	{
		this.context = context;
		builder = new Notification.Builder(context);
		nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		downloadBook = new LinkedList<NetBook>();
		mDownloadCallback = downloadCallback;
	}
	
	public boolean addDownLoadBook(NetBook book)
	{
		if (!downloadBook.contains(book))
		{
			return downloadBook.add(book);
		}
		else
		{
			return false;
		}
	}
	
	public NetBook getDownloadBook()
	{
		return downloadBook.isEmpty() ? null : downloadBook.getFirst();
	}
	
	public void removeDownloadbook()
	{
		downloadBook.removeFirst();
	}
	
	private LinkedList<NetBook> downloadBook;
	private final int NOTIFICATION_ID = 0x123;
	
	public void startDownload()
	{
		if (currentBook != null)
		{
			return;
		}
		currentBook = getDownloadBook();
		if (currentBook != null)
		{
			UIUtils.showToastMessage(context, currentBook.getName() + " 正在下载！");
			builder.setSmallIcon(R.drawable.ic_launcher)
					.setTicker(currentBook.getName())
					.setWhen(System.currentTimeMillis())
					.setContentText(currentBook.getName())
					.setProgress(100, 0, false);
			nm.notify(NOTIFICATION_ID, builder.build());
			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg)
				{
					int progress = msg.what;
					if (progress == 100)
					{
						if (mDownloadCallback != null)
						{
							mDownloadCallback.onDownloaded(currentBook);
						}
						nm.cancel(NOTIFICATION_ID);
						UIUtils.showToastMessage(context, "Download done: "
								+ currentBook.getName());
						removeDownloadbook();
						currentBook = null;
						startDownload();
					}
					else
					{
						builder.setProgress(100, progress, false);
						nm.notify(NOTIFICATION_ID, builder.build());
					}
				}
			};
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run()
				{
					try
					{
						URL url = new URL(currentBook.getUrl());
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.setConnectTimeout(8000);
						conn.setReadTimeout(5000);
						conn.connect();
						InputStream is = conn.getInputStream();
						int totalLength = is.available();
						byte[] buffer = new byte[totalLength];
						int readLength = -1;
						int currentLength = 0;
						File file = new File(PathUtils.fileSaveDirectory()
								+ "/" + currentBook.getName());
						OutputStream os = new FileOutputStream(file);
						Log.e("startDownload():", file.getAbsolutePath());
						while ((readLength = is.read(buffer)) != -1)
						{
							currentLength += readLength;
							int progress = currentLength * 100 / totalLength;
							handler.sendEmptyMessage(progress);
							os.write(buffer, 0, readLength);
							os.flush();
						}
						is.close();
						os.close();
					}
					catch (MalformedURLException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			});
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.start();
		}
	}
	
	public interface DownloadCallback
	{
		void onDownloaded(NetBook book);
	}
}
