package net.tverrbjelke.example.rechner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/** Contains the Database to hold the history of our rechner app
 *   
 * @author tverrbjelke
 *
 */
public class HistorySqliteHelper extends SQLiteOpenHelper {
	
	
	private static final String DATABASE_NAME="historyRechner.db";
	private static final int DATABASE_VERSION = 1; 
	
	/** SQL string to create the table holding the history of the calculations.
	 * see http://developer.android.com/guide/topics/data/data-storage.html#db
	 */
	private static final String DATABASE_TABLE_CREATE_HISTORY = 
			"create table history (" + 
			"  ID integer primary key autoincrement, " + 
			" zahl1 int," + 
			" operator text," +
			" zahl2 int";

	/** SQL string to drop all tables in  the database.
	 */
	private static final String DATABASE_DROP_TABLE =
			"DROP TABLE IF EXISTS SCANITEM";
		
	
	/** creates a new instance of the database, after any existing db are dropped
	 * see doc for superclass. 
	 * 
	 * @param context for the superclass of the android framework. 
	 */
	public HistorySqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/** @param sqliteDatabase is existing in the android framework.
	 * @todo I wonder where it actually resides and where it is set up.
	 * 
	 */
	@Override
	public void onCreate( SQLiteDatabase sqliteDatabase){
		sqliteDatabase.execSQL(DATABASE_TABLE_CREATE_HISTORY);
	}
	
	/** we arre called when the database gets upgraded, erase all existing tables.
	 *  writes this to log. uses reflection to get the TAG of this class. 
	 * @param sqliteDatabase which database is getting upgraded.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, int oldVersion, int newVersion){
		Log.w( HistorySqliteHelper.class.getName(), 
				"upgrading databased from Version " + String.valueOf(oldVersion) +
				" to " + String.valueOf(newVersion) + 
				", which will delete all existing tables (and content).");
		
		sqliteDatabase.execSQL(DATABASE_DROP_TABLE);
		onCreate(sqliteDatabase);
	}
		
}
