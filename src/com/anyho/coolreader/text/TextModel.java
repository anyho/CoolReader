package com.anyho.coolreader.text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.anyho.coolreader.CoolReaderApp;
import com.anyho.coolreader.CoolReaderApplication;
import com.anyho.coolreader.OptionConfig;
import com.anyho.coolreader.R;
import com.anyho.coolreader.atys.CoolReaderAty;
import com.anyho.coolreader.entity.Book;
import com.anyho.coolreader.util.UIUtils;
import com.anyho.coolreader.view.TextPaintContext;

public class TextModel
{
	static final String TAG = TextModel.class.getSimpleName() + ">>>";
	
	public static enum PageIndex
	{
		previous, current, next;
		
		public PageIndex getNext()
		{
			switch (this)
			{
				case previous:
					return current;
				case current:
					return next;
				default:
					return null;
			}
		}
		
		public PageIndex getPrevious()
		{
			switch (this)
			{
				case next:
					return current;
				case current:
					return previous;
				default:
					return null;
			}
		}
	}
	
	public static enum Direction
	{
		leftToRight(true), rightToLeft(true), up(false), down(false);
		
		public final boolean IsHorizontal;
		
		Direction(boolean isHorizontal)
		{
			IsHorizontal = isHorizontal;
		}
	};
	
	public static enum Animation
	{
		none, curl, slide, shift
	}
	
	private int linesOfPage = 0;
	private int maxWidth = 0;
	private Book currentBook;
	
	public TextModel(Book book)
	{
		currentBook = book;
		currentBlockIndex = book.getStartBlockIndex();
		currentCharOffset = book.getStartCharOffset();
		maxWidth = CoolReaderApp.obtainInstance().getWidth();
	}
	
	private TextPaintContext mPaintContext = new TextPaintContext();
	
	public void paint(TextPaintContext context, PageIndex index)
	{
		
		Log.e(TAG, "ready to paint");
		mPaintContext = context;
		mPaintContext.drawBackground();
		context.refreshPaintInfo();
		if (context == null || index == null)
		{
			return;
		}
		linesOfPage = computerLinesOfPage(context) - 1;// 预留一行画页脚信息
		switch (index)
		{
			case current:
				prepareCurrentPage();
				break;
			case previous:
				preparePreviousPage();
				break;
			case next:
				prepareNextPage();
				break;
			default:
				break;
		}
		
		drawCurrentPage(context, currentPage);
		drawFooter(context);
	}
	
	private void drawCurrentPage(TextPaintContext context, TextPage currentPage)
	{
		List<String> lines = currentPage.getTextLines();
		int top = context.getTopMargin() + context.getTextEffectHeight();
		for (String string : lines)
		{
			context.drawString(string, context.getLeftMargin(), top);
			top += context.getTextEffectHeight();
		}
	}
	
	/**
	 * 当前正在处理的block索引
	 */
	private int currentBlockIndex = 0;
	/**
	 * 记录处理的位于block的字符数，在每次跳转block时，置为0，在处理时更新数目
	 */
	private int currentCharOffset = 0;
	/**
	 * 记录正在处理的字符串，每次从中获得一个段落，需要在处理完后，置为null，在处理时更新内容
	 */
	private String currentText;
	private TextPage previousPage = new TextPage();
	private TextPage currentPage = new TextPage();
	private TextPage nextPage = new TextPage();
	private boolean hasRemain = false;
	
	public void prepareCurrentPage()
	{
		// TODO
		if (hasRemain)
		{
			currentBlockIndex = currentPage.getStartBlockIndex();
			currentCharOffset = currentPage.getStartCharOffset();
			hasRemain = false;
		}
		currentPage.setEndBlockIndex(currentBlockIndex);
		currentPage.setEndCharOffset(currentCharOffset);
		prepareNextPage();// 1
		// Log.e(TAG, "current page: " + currentPage.toString());
		// prepareNextPage();// 2
		// Log.e(TAG, "current page: " + currentPage.toString());
		// prepareNextPage();// 3
		// Log.e(TAG, "current page: " + currentPage.toString());
		// prepareNextPage();// 4
		// Log.e(TAG, "current page: " + currentPage.toString());
		// prepareNextPage();// 5
		// Log.e(TAG, "current page: " + currentPage.toString());
		// prepareNextPage();// 6
		// Log.e(TAG, "current page: " + currentPage.toString());
		// prepareNextPage();// 7
		// Log.e(TAG, "current page: " + currentPage.toString());
		// preparePreviousPage();
		// Log.e(TAG, "current page: " + currentPage.toString());
	}
	
	public void reset()
	{
		hasRemain = true;
		hasSynchcronized = false;
	}
	
	public void prepareNextPage()
	{
		// TODO is last 线程同步！
		if (currentPage.isLastPage())
		{
			return;
		}
		currentPageNumber++;
		currentBlockIndex = currentPage.getEndBlockIndex();
		currentCharOffset = currentPage.getEndCharOffset();
		currentPage.setStartBlockIndex(currentBlockIndex);
		currentPage.setStartCharOffset(currentCharOffset);
		currentBook.setStartBlockIndex(currentBlockIndex);
		currentBook.setStartCharOffset(currentCharOffset);
		ArrayList<String> lines = fillForwardPage();
		currentPage.setTextLines(lines);
		// TODO check or update the index?
		currentPage.setEndBlockIndex(currentBlockIndex);
		currentPage.setEndCharOffset(currentCharOffset);
		Log.e(TAG, "prepareNextPage: " + currentPage.toString());
	}
	
	public void preparePreviousPage()
	{
		// TODO is first 线程同步！
		if (currentPage.isFirstPage())
		{
			return;
		}
		currentPageNumber--;
		currentBlockIndex = currentPage.getStartBlockIndex();
		currentCharOffset = currentPage.getStartCharOffset();
		currentPage.setEndBlockIndex(currentBlockIndex);
		currentPage.setEndCharOffset(currentCharOffset);
		List<String> lines = fillBackwardPage();
		currentPage.setTextLines(lines);
		// TODO check or update the index?
		currentPage.setStartBlockIndex(currentBlockIndex);
		currentPage.setStartCharOffset(currentCharOffset);
		currentBook.setStartBlockIndex(currentBlockIndex);
		currentBook.setStartCharOffset(currentCharOffset);
		Log.e(TAG, "preparePreviousPage: " + currentPage.toString());
	}
	
	/**
	 * 读取段落，注意向左和向右读取的方式是一样的
	 * 虽然是往左读，但是在截断字符时仍是按照向右的方式来测量，否则，会出现翻页时数据浮动，因此获取段落的方式是相同的
	 * 
	 * @param forward
	 *            向前
	 * @return 读取的段落，当最前或最后时返回null
	 */
	private String readParagraph(boolean forward)
	{
		String text = null;
		if (currentText == null)
		{
			currentText = readBlock(forward);
		}
		text = currentText;
		if (text != null)
		{
			int index = text.indexOf("\r\n");
			if (index != -1)
			{
				text = text.substring(0, index + 2);// 注意此处，要把最后的换行符加上，以免影响后面的操作
			}
		}
		return text;
	}
	
	/**
	 * 处理向左和向右读block时，index和offset的变化，当offset达到末尾时，index自加，在开头，自减
	 * 
	 * @param forward
	 * @return 当读完时返回null
	 */
	private String readBlock(boolean forward)
	{
		int blockLength = BookModel.CharCached.getBlockSize(currentBlockIndex);
		if (forward)
		{
			if (currentCharOffset >= blockLength)
			{
				currentBlockIndex++;
				currentCharOffset = 0;
			}
		}
		else
		{
			if (currentCharOffset <= 0)
			{
				currentBlockIndex--;
				currentCharOffset = BookModel.CharCached
						.getBlockSize(currentBlockIndex);
			}
		}
		// Log.e(TAG, "currentBlockIndex: " + currentBlockIndex);
		// Log.e(TAG, "currentCharOffset: " + currentCharOffset);
		char[] block = BookModel.CharCached.getBlock(currentBlockIndex);
		if (block == null)// index is not of bound
		{
			return null;
		}
		String text = new String(block);
		if (forward)
		{
			text = text.substring(currentCharOffset);
		}
		else
		{
			text = text.substring(0, currentCharOffset);
		}
		return text;
	}
	
	/**
	 * 向左填充一页
	 * 
	 * @return
	 */
	private List<String> fillBackwardPage()
	{
		Log.e(TAG, "fillBackwardPage()");
		LinkedList<String> tempLines = new LinkedList<String>();
		LinkedList<String> textLines = new LinkedList<String>();
		int lineCounts = 0;
		while (lineCounts < linesOfPage || currentText != null)
		{
			String text = readParagraph(false);
			// Log.e(TAG, "readBackwardParagraph(): " + text);
			if (text == null)// 已经到达最前了
			{
				break;
			}
			if ("\r\n".equals(text))
			{
				currentCharOffset -= 2;// 判断是否空行，每次只包括了一个换行符，见index+2
				currentText = currentText.substring(2);// 更新处理的text
				if (currentText.isEmpty())// 代表已处理完
				{
					currentText = null;
				}
				// Log.e(TAG, "add empty line: " + text);
				text = text.replaceAll("\r\n", "");
				// Log.e(TAG, "break line: " + text);
				tempLines.add(text);
				lineCounts++;
				continue;
			}
			// 这里不需要lineCounts < linesOfPage，保证将每一段都有解析完，这样范围就会包括
			// 正确的行，已便后面过滤
			while (!text.isEmpty())
			{
				int position = mPaintContext.breakText(text, true,
						mPaintContext.getMainTextWidth());
				String breakline = text.substring(0, position);
				// Log.e(TAG, "break line: " + breakline);
				text = text.substring(position);
				// Log.e(TAG, "break line: " + text);
				currentCharOffset -= breakline.length();
				currentText = currentText.substring(position);
				if (currentText.isEmpty())// 代表已处理完
				{
					currentText = null;
				}
				if (!"\r\n".equals(breakline) && !"\n".equals(breakline))// 过滤掉段落结尾的\r\n，防止再添加一个空行
				{
					tempLines.add(breakline);
					lineCounts++;
				}
			}
			
			if (tempLines.size() > 0 && currentText == null)
			{
				for (int i = tempLines.size() - 1; i >= 0; i--)
				{
					String temp = tempLines.get(i);
					textLines.add(temp);
				}
				tempLines.clear();
			}
		}
		// Log.e(TAG, "tempLines.size(): " + tempLines.size());
		// Log.e(TAG, "lineCounts: " + lineCounts);
		// Log.e(TAG, "linesOfPage: " + linesOfPage);
		// Log.e(TAG, "textLines.size(): " + textLines.size());
		if (tempLines.size() > 0)
		{
			for (int i = tempLines.size() - 1; i >= 0; i--)
			{
				String temp = tempLines.get(i);
				textLines.add(temp);
			}
			tempLines.clear();
		}
		while (lineCounts > linesOfPage)
		{
			String text = textLines.getLast();
			int length = text.length();
			if (length > 0)
			{
				currentCharOffset += length;
			}
			else
			{
				currentCharOffset += 2;
			}
			lineCounts--;
			textLines.removeLast();
		}
		
		tempLines.clear();
		for (int i = textLines.size() - 1; i >= 0; i--)
		{
			String text = textLines.get(i);
			tempLines.add(text);
		}
		currentText = null;
		return tempLines;
	}
	
	/**
	 * 一行中最多的字符数
	 */
	private int maxCharsOfLine = 0;
	private int currentPageNumber = 0;
	private boolean hasSynchcronized = false;
	
	public int getCurrentPageNumber()
	{
		return currentPageNumber;
	}
	
	/**
	 * 向右填充一页
	 * 
	 * @return
	 */
	private ArrayList<String> fillForwardPage()
	{
		Log.e(TAG, "fillForwardPage()");
		ArrayList<String> textLines = new ArrayList<String>();
		int lineCounts = 0;
		while (lineCounts < linesOfPage)
		{
			String text = readParagraph(true);
			if (text == null)// 读完了
			{
				break;
			}
			// Log.e(TAG, "readForwardParagraph(): " + text);
			if ("\r\n".equals(text))
			{
				currentCharOffset += 2;// 判断是否空行，每次只包括了一个换行符，见index+2
				currentText = currentText.substring(2);// 更新处理的text
				if (currentText.isEmpty())// 代表已处理完
				{
					currentText = null;
				}
				// Log.e(TAG, "add empty line: " + text);
				text = text.replaceAll("\r", "1");
				text = text.replaceAll("\n", "2");
				textLines.add(text);
				lineCounts++;
				continue;
			}
			while (!text.isEmpty() && lineCounts < linesOfPage)
			{
				int position = mPaintContext.breakText(text, true,
						mPaintContext.getMainTextWidth());
				if (!hasSynchcronized)
				{
					maxCharsOfLine = position > maxCharsOfLine ? position
							: maxCharsOfLine;
				}
				String breakline = text.substring(0, position);
				text = text.substring(position);
				currentCharOffset += breakline.length();
				currentText = currentText.substring(position);
				if (currentText.isEmpty())// 代表已处理完
				{
					currentText = null;
				}
				
				if (!"\r\n".equals(breakline) && !"\n".equals(breakline))// 过滤掉段落结尾的\r\n，防止再添加一个空行
				{
					breakline = breakline.replaceAll("\r", "1");
					breakline = breakline.replaceAll("\n", "2");
					textLines.add(breakline);
					lineCounts++;
				}
			}
		}
		Log.e(TAG, "maxCharsOfLine: " + maxCharsOfLine);
		currentText = null;
		return textLines;
	}
	
	private int totalPageNumber;
	
	public int getTotalPageNumber()
	{
		return totalPageNumber;
	}
	
	/**
	 * 绘画页脚
	 * 
	 * @param context
	 */
	private void drawFooter(TextPaintContext context)
	{
		final OptionConfig config = OptionConfig.obtainInstance();
		currentPageNumber = computerCurrentPageNumber();
		totalPageNumber = computerTotalPageNumbers();
		Log.e(TAG, "computerCurrentPageNumber: " + currentPageNumber);
		Log.e(TAG, "computerTotalPageNumbers: " + totalPageNumber);
		
		StringBuilder footerInfo = new StringBuilder();
		if (config.ShowPageNumberAtFooter)
		{
			footerInfo.append(currentPageNumber).append("/")
					.append(totalPageNumber).append("  ");
			Log.e(TAG, "footerInfo: " + footerInfo.toString());
		}
		if (config.ShowBatteryPercentAtFooter)
		{
			final int level = ((CoolReaderAty) CoolReaderApp.obtainInstance()
					.getActivity()).getBatteryLevel();
			footerInfo.append(level).append("%").append("  ");
			Log.e(TAG, "footerInfo: " + footerInfo.toString());
		}
		if (config.ShowTimeAtFooter)
		{
			final String currentTimeString = CoolReaderApp.obtainInstance()
					.getCurrentTimeString();
			footerInfo.append(currentTimeString).append("  ");
			Log.e(TAG, "footerInfo: " + footerInfo.toString());
		}
		final String info = footerInfo.toString();
		Paint paint = new Paint();
		int infoLength = 0;
		if (info.length() > 0)
		{
			if (config.DAY)
			{
				paint.setColor(Color.rgb(0, 0, 0));
			}
			else
			{
				paint.setColor(Color.rgb(128, 128, 128));
			}
			paint.setTextSize(26);
			infoLength = (int) paint.measureText(info);
			context.drawString(footerInfo.toString(), context.getRightEdge()
					- infoLength, (int) (context.getBottomEdge()), paint);
		}
		final int rectLength = context.getMainTextWidth() - infoLength - 5;
		final int rectWidth = 10;
		final int topLeftX = context.getLeftMargin();
		final int topLeftY = context.getBottomEdge() - rectWidth;
		final int bottomRightX = topLeftX + rectLength;
		final int bottomRightY = topLeftY + rectWidth;
		// 绘画矩形
		context.drawLine(topLeftX, topLeftY, bottomRightX, topLeftY, paint);
		context.drawLine(topLeftX, topLeftY, topLeftX, bottomRightY, paint);
		context.drawLine(bottomRightX, topLeftY, bottomRightX, bottomRightY,
				paint);
		context.drawLine(topLeftX, bottomRightY, bottomRightX, bottomRightY,
				paint);
		// 绘画进度
		final int progress = (int) (currentPageNumber * 1.0 / totalPageNumber * rectLength);
		if (config.DAY)
		{
			paint.setColor(Color.rgb(170, 170, 170));
		}
		else
		{
			paint.setColor(Color.rgb(85, 85, 85));
		}
		context.fillRectangle(topLeftX + 1, topLeftY + 1, topLeftX + progress,
				bottomRightY - 1, paint);
	}
	
	/**
	 * 计算当前页码
	 * 
	 * @return
	 */
	private int computerCurrentPageNumber()
	{
		int pageNumber = 1;
		
		final float charsOfPerPage = linesOfPage * maxCharsOfLine * factor;
		int currentTotalChars = (int) (BookModel.CharCached
				.getCurrentTotalTextSize(currentPage.getEndBlockIndex() - 1)
				+ currentPage.getEndCharOffset() + charsOfPerPage / 2);
		Log.e(TAG, "currentTotalChars: " + currentTotalChars);
		Log.e(TAG, "charsOfPerPage: " + charsOfPerPage);
		pageNumber = (int) (currentTotalChars / charsOfPerPage);
		hasSynchcronized = true;
		
		return pageNumber;
	}
	
	private final float factor = 0.8f;
	
	/**
	 * 计算总页码
	 * 
	 * @return
	 */
	private int computerTotalPageNumbers()
	{
		final long totalTextSize = BookModel.getTotalTextSize();
		Log.e(TAG, "totalTextSize: " + totalTextSize);
		final float charsOfPerPage = linesOfPage * maxCharsOfLine * factor;
		final int pageNumber = (int) (totalTextSize / charsOfPerPage);
		return Math.max(1, pageNumber);
	}
	
	private int computerLinesOfPage(TextPaintContext context)
	{
		int height = context.getMainTextHeight();
		int wordHeight = context.getTextEffectHeight();
		return height / wordHeight;
	}
	
	public boolean canScroll(PageIndex pageIndex)
	{
		switch (pageIndex)
		{
			case previous:
				return !currentPage.isFirstPage();
			case next:
				return !currentPage.isLastPage();
			default:
				break;
		}
		return false;
	}
	
	public void onLongPress(int eventX, int eventY)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void moveAfterLongPress(int x, int y)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void manulMove()
	{
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 点击页面的区域，执行相应的事件action
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void onClickZone(int x, int y, int width, int height)
	{
		final int xIndex = (int) (x * 1.0 / width * SIZE);
		final int yIndex = (int) (y * 1.0 / height * SIZE);
		// Log.e(TAG, "onClickZone(): x-index: " + xIndex);
		// Log.e(TAG, "onClickZone(): y-index: " + yIndex);
		ActionCode action = mActions[xIndex][yIndex];
		Log.e(TAG, "action: " + action);
		if (action == null)
			return;
		switch (action)
		{
			case turn_page_previous:
				turnPage(x, y, false);
				break;
			case turn_page_next:
				turnPage(x, y, true);
				break;
			case show_menu:
				showMenuWindow();
				break;
			case show_navigation:
				
				break;
			
			default:
				break;
		}
	}
	
	private void showMenuWindow()
	{
		CoolReaderApp.obtainInstance().showMenuWindow();
	}
	
	private void turnPage(int x, int y, boolean forward)
	{
		final int speed = OptionConfig.obtainInstance().getAnimationSpeed();
		CoolReaderApp
				.obtainInstance()
				.getCoolReaderWidget()
				.startAnimatedScrolling(
						forward ? PageIndex.next : PageIndex.previous, x, y,
						Direction.rightToLeft, speed);
	}
	
	private enum ActionCode
	{
		turn_page_previous, turn_page_next, show_menu, show_navigation
	}
	
	static final int SIZE = 3;
	private ActionCode[][] mActions = new ActionCode[][] {
			{ ActionCode.turn_page_previous, ActionCode.turn_page_previous,
					ActionCode.turn_page_previous },
			{ null, ActionCode.show_navigation, ActionCode.show_menu },
			{ ActionCode.turn_page_next, ActionCode.turn_page_next,
					ActionCode.turn_page_next } };
	
	public void gotoPage(int page)
	{
		Log.e(TAG, "page: " + page);
		Log.e(TAG, "offset: " + (page - currentPageNumber));
		if (page > currentPageNumber)
		{
			int pageOffset = page - currentPageNumber;
			for (int i = 0; i < pageOffset; i++)
			{
				prepareNextPage();
			}
		}
		else
		{
			if (page < currentPageNumber)
			{
				int pageOffset = currentPageNumber - page;
				for (int i = 0; i < pageOffset; i++)
				{
					preparePreviousPage();
				}
			}
		}
		
	}
	
}
