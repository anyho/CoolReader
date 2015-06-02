package com.anyho.coolreader.text;

import java.util.Collections;
import java.util.List;

import com.anyho.coolreader.CoolReaderApplication;
import com.anyho.coolreader.R;
import com.anyho.coolreader.util.UIUtils;

public class TextPage
{
	/**
	 * 开始字符所在的block索引
	 */
	private int startBlockIndex;
	/**
	 * 开始字符在开始block中的偏移量
	 */
	private int startCharOffset;
	/**
	 * 结束字符所在的block索引
	 */
	private int endBlockIndex;
	/**
	 * 结束字符在结束block的偏移量
	 */
	private int endCharOffset;
	/**
	 * 含有的textline
	 */
	private List<String> textLines = Collections.emptyList();
	
	public boolean isSameBlock()
	{
		return startBlockIndex == endBlockIndex;
	}
	
	public boolean isFirstPage()
	{
		if (startBlockIndex == 0 && startCharOffset == 0)
		{
			UIUtils.showToastMessage(CoolReaderApplication.obtainInstance()
					.getApplicationContext(), R.string.toast_first_page);
			return true;
		}
		return false;
	}
	
	public boolean isLastPage()
	{
		if (endBlockIndex == BookModel.CharCached.getBlockCounts()
				&& endCharOffset == 0)
		{
			UIUtils.showToastMessage(CoolReaderApplication.obtainInstance()
					.getApplicationContext(), R.string.toast_last_page);
			return true;
		}
		return false;
	}
	
	public int getStartBlockIndex()
	{
		return startBlockIndex;
	}
	
	public void setStartBlockIndex(int startBlockIndex)
	{
		this.startBlockIndex = startBlockIndex;
	}
	
	public int getStartCharOffset()
	{
		return startCharOffset;
	}
	
	public void setStartCharOffset(int startCharOffset)
	{
		this.startCharOffset = startCharOffset;
	}
	
	public int getEndBlockIndex()
	{
		return endBlockIndex;
	}
	
	public void setEndBlockIndex(int endBlockIndex)
	{
		this.endBlockIndex = endBlockIndex;
	}
	
	public int getEndCharOffset()
	{
		return endCharOffset;
	}
	
	public void setEndCharOffset(int endCharOffset)
	{
		this.endCharOffset = endCharOffset;
	}
	
	public List<String> getTextLines()
	{
		return textLines;
	}
	
	public void setTextLines(List<String> textLines)
	{
		this.textLines = textLines;
	}
	
	@Override
	public String toString()
	{
		return "TextPage [startBlockIndex=" + startBlockIndex
				+ ", startCharOffset=" + startCharOffset + ", endBlockIndex="
				+ endBlockIndex + ", endCharOffset=" + endCharOffset + "]";
	}
	
}
