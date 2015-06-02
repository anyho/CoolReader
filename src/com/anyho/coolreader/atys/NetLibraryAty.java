package com.anyho.coolreader.atys;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.anyho.coolreader.net.DownloadService;
import com.anyho.coolreader.net.DownloadService.DownloadCallback;
import com.anyho.coolreader.net.NetBook;
import com.anyho.coolreader.net.NetBookListAdapter;
import com.anyho.coolreader.net.NetBooksService;
import com.anyho.coolreader.util.UIUtils;

public class NetLibraryAty extends ListActivity implements DownloadCallback
{
	private List<NetBook> mBooks;
	private NetBookListAdapter mAdapter;
	private DownloadService mService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mBooks = new ArrayList<NetBook>();
		mAdapter = new NetBookListAdapter(this, mBooks);
		setListAdapter(mAdapter);
		mService = new DownloadService(this, this);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		UIUtils.runWithMessage(this, "正在查询服务器···", new Runnable() {
			@Override
			public void run()
			{
				new NetBooksService(mBooks).execute();
			}
		}, new Runnable() {
			@Override
			public void run()
			{
				mAdapter.notifyDataSetChanged();
			}
		});
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		NetBook book = mAdapter.getItem(position);
		if (book.isDownload())
		{
			UIUtils.showToastMessage(this, "书籍已经下载！可在本地目录查看");
			return;
		}
		if (mService.addDownLoadBook(book))
		{
			UIUtils.showToastMessage(this, "书籍加入下载成功！");
			mService.startDownload();
		}
		else
		{
			UIUtils.showToastMessage(this, "下载任务已存在！");
		}
	}
	
	@Override
	public void onDownloaded(NetBook book)
	{
		book.setDownload(true);
		mAdapter.notifyDataSetChanged();
	}
	
}
