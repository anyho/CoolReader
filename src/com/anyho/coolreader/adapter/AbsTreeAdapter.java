package com.anyho.coolreader.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.widget.BaseAdapter;

import com.anyho.coolreader.tree.AbsTree;

/**
 * 树列表的抽象适配器，继承自BaseAdapter，并提供了更新数据源的方法
 */
public abstract class AbsTreeAdapter extends BaseAdapter
{
	/**
	 * 数据源：AbsTree对象的集合
	 */
	private List<AbsTree> trees;
	/**
	 * 绑定的list activity,可以提供更新数据时的线程同步（runOnUiThread）
	 */
	protected ListActivity activity;
	
	public AbsTreeAdapter(ListActivity activity)
	{
		this.activity = activity;
		trees = Collections.synchronizedList(new ArrayList<AbsTree>());
		activity.setListAdapter(this);
	}
	
	/**
	 * 使用指定的数据更换现有的集合数据，使用了线程同步机制，需要考虑数据源的互斥访问？
	 * 
	 * @param items
	 *            指定的数据集
	 */
	public void replaceAll(final Collection<AbsTree> items)
	{
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run()
			{
				if (!trees.equals(items))
				{
					trees.clear();
					trees.addAll(items);
					notifyDataSetChanged();
				}
			}
		});
	}
	
	@Override
	public int getCount()
	{
		return trees.size();
	}
	
	@Override
	public AbsTree getItem(int position)
	{
		return trees.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
}
