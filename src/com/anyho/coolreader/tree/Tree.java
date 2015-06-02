package com.anyho.coolreader.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ListView��Tree������Ϊ�ļ����Ļ���
 * 
 * @author Anyho
 * 
 * @param <T>
 *            ���ͣ������Ǽ̳�Tree������
 */
public abstract class Tree<T extends Tree<T>>
{
	/**
	 * ���ڵ�
	 */
	private T parent;
	/**
	 * �ӽڵ�ļ��϶���
	 */
	private volatile List<T> subTrees;
	
	/**
	 * Ĭ�ϵĹ��캯����ָ�����ڵ�Ϊ�գ���Ĭ��Ϊ���ڵ�
	 */
	protected Tree()
	{
		this(null);
	}
	
	/**
	 * ָ���˸��ڵ�Ĺ��캯��
	 * 
	 * @param parent
	 *            ������ڵ�Ϊnull������Ϊ���ڵ㣬ָ������Ϊ0
	 */
	protected Tree(T parent)
	{
		this(parent, parent == null ? 0 : parent.getSubTrees().size());
	}
	
	/**
	 * ָ�����ڵ㣬�����ӽڵ�λ�õ�����ֵ
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
	 * �õ����ڵ�
	 * 
	 * @return
	 */
	public T getParent()
	{
		return parent;
	}
	
	/**
	 * ����ӽڵ㼯��
	 * 
	 * @return Ϊnullʱ�����ؿռ���
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
	 * ����ӽڵ㼰�����ӽڵ������
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
	 * ����ӽڵ�
	 */
	public void clear()
	{
		subTrees = null;
	}
}
