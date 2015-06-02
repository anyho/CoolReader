package com.anyho.coolreader.tree;

/**
 * �����AbsTree�࣬�̳�Tree<T>���ṩ�˻��Tree��������ĳ��󷽷�
 */
public abstract class AbsTree extends Tree<AbsTree>
{
	/**
	 * ���򿪵�״̬ö��ֵ
	 */
	public static enum Status
	{
		CAN_NOT_OPEN, READY_TO_OPEN_DIRECTORY, READY_TO_OPEN_FILE
	}
	
	public AbsTree()
	{
	}
	
	public AbsTree(AbsTree parent, int position)
	{
		super(parent, position);
	}
	
	public AbsTree(AbsTree parent)
	{
		super(parent);
	}
	
	/**
	 * ���������ı���
	 * 
	 * @return �����ַ�
	 */
	abstract public String getTreeTitle();
	
	/**
	 * ���������ļ�飬����������
	 * 
	 * @return ����ַ�
	 */
	abstract public String getTreeSummary();
	
	/**
	 * ������򿪵�״̬
	 * 
	 * @return Statusö��ֵ
	 */
	abstract public Status getOpenStatus();
	
	/**
	 * �����ĺ�ʱ����
	 */
	public void waitForOpenTree()
	{
		
	}
}
