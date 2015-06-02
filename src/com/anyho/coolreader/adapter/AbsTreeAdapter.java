package com.anyho.coolreader.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.widget.BaseAdapter;

import com.anyho.coolreader.tree.AbsTree;

/**
 * ���б�ĳ������������̳���BaseAdapter�����ṩ�˸�������Դ�ķ���
 */
public abstract class AbsTreeAdapter extends BaseAdapter
{
	/**
	 * ����Դ��AbsTree����ļ���
	 */
	private List<AbsTree> trees;
	/**
	 * �󶨵�list activity,�����ṩ��������ʱ���߳�ͬ����runOnUiThread��
	 */
	protected ListActivity activity;
	
	public AbsTreeAdapter(ListActivity activity)
	{
		this.activity = activity;
		trees = Collections.synchronizedList(new ArrayList<AbsTree>());
		activity.setListAdapter(this);
	}
	
	/**
	 * ʹ��ָ�������ݸ������еļ������ݣ�ʹ�����߳�ͬ�����ƣ���Ҫ��������Դ�Ļ�����ʣ�
	 * 
	 * @param items
	 *            ָ�������ݼ�
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
