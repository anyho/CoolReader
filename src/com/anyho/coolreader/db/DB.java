package com.anyho.coolreader.db;

public interface DB
{
	String DB_NAME = "db_coolreader";
	int DB_VERSION = 1;
	
	interface Table_Book
	{
		String TABLE_NAME = "t_book";
		
		interface Field
		{
			String ID = "b_id";
			String NAME = "b_name";
			String PATH = "b_path";
			String START_BLOCK = "b_start_block";
			String START_CHAR = "b_start_char";
			String ISSAVED = "b_is_saved";
		}
		
		interface SQL
		{
			String CREATE_TABLE = "create table if not exists " + TABLE_NAME
					+ "(" + Field.ID + " integer autoincreament primary key, "
					+ Field.NAME + " text, " + Field.PATH + " text, "
					+ Field.START_BLOCK + " tinyint, " + Field.START_CHAR
					+ " tinyint, " + Field.ISSAVED + " tinyint " + ");";
			
			String QUERY_ALL_BOOK = "select * from " + TABLE_NAME;
			String ADD_NEW_BOOK = "insert into " + TABLE_NAME + "( "
					+ Field.NAME + ", " + Field.PATH + ", " + Field.START_BLOCK
					+ ", " + Field.START_CHAR + ", " + Field.ISSAVED
					+ " ) values('%s','%s',%d,%d,%d)";
			String UPDATE_BOOK = "update " + TABLE_NAME + " set " + Field.NAME
					+ " = '%s', " + Field.PATH + " = '%s', "
					+ Field.START_BLOCK + "=%d, " + Field.START_CHAR
					+ "=%d where " + Field.ID + " = %d";
			String REMOVE_BOOK = "delete from " + TABLE_NAME + " where "
					+ Field.NAME + " = '%s'";
		}
		
	}
	
}
