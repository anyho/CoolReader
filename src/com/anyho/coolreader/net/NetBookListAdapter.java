package com.anyho.coolreader.net;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyho.coolreader.R;

public class NetBookListAdapter extends ArrayAdapter<NetBook>
{
	private List<NetBook> books;
	
	public NetBookListAdapter(Context context, List<NetBook> books)
	{
		super(context, 0);
		this.books = books;
	}
	
	@Override
	public NetBook getItem(int position)
	{
		return books.get(position);
	}
	
	@Override
	public int getCount()
	{
		return books.size();
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		ViewHolder holder;
		if (view == null)
		{
			view = LayoutInflater.from(getContext()).inflate(
					R.layout.item_list_netbook, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) view.findViewById(R.id.netbook_tvName);
			holder.ivDownload = (ImageView) view
					.findViewById(R.id.netbook_ivDownload);
			view.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) view.getTag();
		}
		NetBook book = getItem(position);
		holder.tvName.setText(book.getName());
		holder.ivDownload.setVisibility(book.isDownload() ? View.GONE
				: View.VISIBLE);
		return view;
	}
	
	class ViewHolder
	{
		TextView tvName;
		ImageView ivDownload;
	}
}
