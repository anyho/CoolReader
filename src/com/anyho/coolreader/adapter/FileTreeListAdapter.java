package com.anyho.coolreader.adapter;

import java.io.File;

import android.app.ListActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyho.coolreader.R;
import com.anyho.coolreader.tree.FileTree;

public class FileTreeListAdapter extends AbsTreeAdapter
{
	
	public FileTreeListAdapter(ListActivity activity)
	{
		super(activity);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View content;
		Holder holder;
		if (convertView == null)
		{
			content = View
					.inflate(activity, R.layout.file_tree_list_item, null);
			holder = new Holder();
			holder.imgCover = (ImageView) content.findViewById(R.id.imgCover);
			holder.tvTitle = (TextView) content.findViewById(R.id.tvTitle);
			holder.tvSummary = (TextView) content.findViewById(R.id.tvSummary);
			content.setTag(holder);
		}
		else
		{
			content = convertView;
			holder = (Holder) content.getTag();
		}
		FileTree item = (FileTree) getItem(position);
		File file = item.getFile();
		if (file.isDirectory())
		{
			if (file.canRead())
			{
				holder.imgCover.setImageResource(R.drawable.ic_list_folder);
			}
			else
			{
				holder.imgCover
						.setImageResource(R.drawable.ic_list_folder_permission_denied);
			}
		}
		holder.tvTitle.setText(item.getTreeTitle());
		holder.tvSummary.setText(item.getTreeSummary());
		return content;
	}
	
	class Holder
	{
		ImageView imgCover;
		TextView tvTitle;
		TextView tvSummary;
	}
}
