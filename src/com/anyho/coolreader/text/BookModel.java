package com.anyho.coolreader.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.res.AssetManager;
import android.util.Log;

import com.anyho.coolreader.CoolReaderApplication;
import com.anyho.coolreader.entity.Book;
import com.anyho.coolreader.util.PathUtils;

public class BookModel
{
	static final String TAG = BookModel.class.getSimpleName() + ">>>";
	private static long totalTextSize;
	public static CachedCharBlock CharCached;
	public static final int READ_CHAR_SIZE = 1024;
	
	public static TextModel createTextModel(Book book)
	{
		Log.e(TAG, "createTextModel()");
		totalTextSize = 0;// 消除未关闭程序，再次打开另一个文件时，大小会叠加的影响
		CharCached = new CachedCharBlock(READ_CHAR_SIZE,
				PathUtils.cardCachedDirectory(), "cache");
		startReadBook(book);
		TextModel model = new TextModel(book);
		return model;
	}
	
	private static final int BUFFER_SIZE = 1024;
	
	public static void startRead(InputStream is)
	{
		InputStreamReader isr = null;
		try
		{
			isr = new InputStreamReader(is, CachedCharBlock.CACHED_ENCODE);
			char[] textBlock = new char[1024];// 存放text
			int desPosition = 0;// textBlock数组存放text的末尾位置，空白位置的开始索引
			int paragraphCounts = 0;// 段落数
			char[] buffer = new char[BUFFER_SIZE];
			int readLength = 0;
			while ((readLength = isr.read(buffer)) != -1)
			{
				totalTextSize += readLength;
				int currentOffset = 0;// 记录buffer中解析段落的偏移量，下次解析的开始索引
				for (int i = 0; i < readLength;)
				{
					if (i < readLength - 1 && buffer[i] == '\r'
							&& buffer[i + 1] == '\n')
					{
						int paragraphLength = i - currentOffset + 2;// 段落的长度
						int freeBlockLength = textBlock.length - desPosition;// 剩下空间的大小
						if (paragraphLength > freeBlockLength)// 空间不够，扩展数组
						{
							textBlock = extendTextBlock(textBlock,
									paragraphLength - freeBlockLength,
									desPosition);
						}
						// 将段落保存在text数组中
						System.arraycopy(buffer, currentOffset, textBlock,
								desPosition, paragraphLength);
						// ////TODO delete the debug
						currentOffset += paragraphLength;
						// Log.e(TAG, "currentOffset: " + currentOffset);
						desPosition += paragraphLength;
						// Log.e(TAG, "desPosition: " + desPosition);
						paragraphCounts++;
						// Log.e(TAG, "paragraphCounts: " + paragraphCounts);
						if (paragraphCounts % 16 == 0)
						{
							CharCached.createNewBlock(textBlock, desPosition);
							textBlock = new char[BUFFER_SIZE];
							desPosition = 0;
						}
						i += 2;
					}
					else
					{
						i++;
					}
				}
				if (currentOffset != readLength)
				{
					int restLength = readLength - currentOffset;
					int freeLength = textBlock.length - desPosition;
					if (restLength > freeLength)
					{
						textBlock = extendTextBlock(textBlock, restLength
								- freeLength, desPosition);
					}
					System.arraycopy(buffer, currentOffset, textBlock,
							desPosition, restLength);
					desPosition += restLength;
				}
			}
			if (desPosition != 0)
			{
				CharCached.createNewBlock(textBlock, desPosition);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void startReadBook(Book book)
	{
		
		try
		{
			if (book != null)
			{
				String path = book.getPath();
				if (path.startsWith("file:"))
				{
					File file = new File(path.substring(path.indexOf(":") + 1));
					startRead(new FileInputStream(file));
				}
				else
				{
					if (path.startsWith("asset:"))
					{
						AssetManager am = CoolReaderApplication
								.obtainInstance().getAssets();
						String fileName = path.substring(path.indexOf(":") + 1);
						Log.e(TAG, fileName);
						InputStream input = am.open(fileName);
						startRead(input);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 扩展数组
	 * 
	 * @param textBlock
	 *            包含的text的数组
	 * @param extendLength
	 *            需要扩展的大小
	 * @param textLength
	 *            包含text数组的text长度
	 * @return 包含text的扩展的数组
	 */
	private static char[] extendTextBlock(char[] textBlock, int extendLength,
			int textLength)
	{
		// Log.e(TAG, "extendTextBlock(): extendLength: " + extendLength);
		char[] blockTemp = new char[textBlock.length + extendLength];
		System.arraycopy(textBlock, 0, blockTemp, 0, textLength);
		return blockTemp;
	}
	
	public static long getTotalTextSize()
	{
		return totalTextSize;
	}
}
