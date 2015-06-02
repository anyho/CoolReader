package com.anyho.coolreader.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ListView的Tree对象，作为文件树的基类
 * 
 * @author Anyho
 * 
 * @param <T>
 *            泛型，必须是继承Tree的子类
 */
public abstract class Tree<T extends Tree<T>>
{
	/**
	 * 父节点
	 */
	private T parent;
	/**
	 * 子节点的集合对象
	 */
	private volatile List<T> subTrees;
	
	/**
	 * 默认的构造函数，指定父节点为空，即默认为根节点
	 */
	protected Tree()
	{
		this(null);
	}
	
	/**
	 * 指定了父节点的构造函数
	 * 
	 * @param parent
	 *            如果父节点为null，则作为根节点，指定索引为0
	 */
	protected Tree(T parent)
	{
		this(parent, parent == null ? 0 : parent.getSubTrees().size());
	}
	
	/**
	 * 指定父节点，插入子节点位置的索引值
	 * 
	 * @param parent
	 * @param position
	 */
	protected Tree(T parent, int position)
	{
		if (parent != null
				&& (position < 0 || position > parent.getSubTrees().size()))
		{
			throw new IndexOutOfBoundsException("`position` value equals "
					+ position + " but must be in range[0;"
					+ parent.getSubTrees().size() + "]");
		}
		this.parent = parent;
		if (parent != null)
		{
			parent.addSubTree((T) this, position);
		}
	}
	
	/**
	 * 得到父节点
	 * 
	 * @return
	 */
	public T getParent()
	{
		return parent;
	}
	
	/**
	 * 获得子节点集合
	 * 
	 * @return 为null时，返回空集合
	 */
	public List<T> getSubTrees()
	{
		if (subTrees == null)
		{
			return Collections.emptyList();
		}
		return new ArrayList<T>(subTrees);
	}
	
	/**
	 * 添加子节点及插入子节点的索引
	 * 
	 * @param subtree
	 * @param position
	 */
	protected void addSubTree(T subtree, int position)
	{
		if (subTrees == null)
		{
			subTrees = new ArrayList<T>();
		}
		int size = subTrees.size();
		while (position < size)
		{
			subtree = subTrees.set(position++, subtree);
		}
		subTrees.add(subtree);
	}
	/**
	 * 清除子节点
	 */
	public void clear()
	{
		subTrees = null;
	}
}
