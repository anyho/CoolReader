package com.anyho.coolreader.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CachedCharBlock
{
	static final String TAG = CachedCharBlock.class.getSimpleName() + ">>>";
	private String mDirectoryName;
	private String mFileExtension;
	private ArrayList<WeakReference<char[]>> mBlocks = new ArrayList<WeakReference<char[]>>();
	private ArrayList<Integer> mBlocksSize = new ArrayList<Integer>();
	private ArrayList<Integer> mTotalSize = new ArrayList<Integer>();
	
	public CachedCharBlock(int mBlockSize, String mDirectoryName,
			String mFileExtension)
	{
		super();
		this.mDirectoryName = mDirectoryName + "/";
		this.mFileExtension = "." + mFileExtension;
		new File(mDirectoryName).mkdirs();
	}
	
	public char[] getBlock(int index)
	{
		// TODO throw exception?
		if (index < 0 || index >= getBlockCounts())
		{
			return null;
		}
		char[] block = mBlocks.get(index).get();
		if (block == null)
		{
			try
			{
				File file = new File(fileName(index));
				block = new char[getBlockSize(index)];
				final InputStreamReader isr = new InputStreamReader(
						new FileInputStream(file), CACHED_ENCODE);
				isr.read(block);
				isr.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			// 再次保存，替换掉原来的
			mBlocks.set(index, new WeakReference<char[]>(block));
		}
		// Log.e(TAG, "get block: " + new String(block));
		return block;
	}
	
	public void createNewBlock(char[] data, int length)
	{
		mBlocks.add(new WeakReference<char[]>(data));
		mBlocksSize.add(length);
		final int size = mTotalSize.size();
		final int totalSize = size > 0 ? length + mTotalSize.get(size - 1)
				: length;
		mTotalSize.add(totalSize);
		flushLastedBlock(length);
	}
	
	public static final String CACHED_ENCODE = "utf-8";
	
	private void flushLastedBlock(int length)
	{
		int index = getBlockCounts() - 1;
		char[] block = mBlocks.get(index).get();
		if (block != null)
		{
			try
			{
				// String text = new String(block, 0, length);
				// Log.e(TAG, "write text: " + text);
				OutputStreamWriter osw = new OutputStreamWriter(
						new FileOutputStream(fileName(index)), CACHED_ENCODE);
				osw.write(block, 0, length);
				osw.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private String fileName(int index)
	{
		return mDirectoryName + index + mFileExtension;
	}
	
	public int getBlockCounts()
	{
		return mBlocks.size();
	}
	
	public int getBlockSize(int index)
	{
		return (index < 0 || index >= getBlockCounts()) ? 0 : mBlocksSize
				.get(index);
	}
	
	public int getCurrentTotalTextSize(int blockIndex)
	{
		return (blockIndex < 0 || blockIndex >= getBlockCounts()) ? 0
				: mTotalSize.get(blockIndex);
	}
}
