/**
 * 
 */
package net.tverrbjelke.example.rechner;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author tverrbjelke
 *
 */
public class HistoryDataSource {
  private SQLiteDatabase histDB;
  private HistorySqliteHelper histDBHelper;
  private String[] AllColumns = {"ID", "ZAHL1", "OPERATOR", "ZAHL2"};
  
  public HistoryDataSource(Context context ) {
	  histDBHelper = new HistorySqliteHelper(context);
}
  
  public void open () throws SQLException {
	  histDB = histDBHelper.getWritableDatabase();
  }
  
  public void close() {
	  histDBHelper.close();
  }

  public Calculation createDBEntry(Calculation calcValues) {
	  ContentValues values = new ContentValues();
	  values.put("ZAHL1", calcValues.getZahl1() );
	  values.put("OPERATOR", calcValues.mode2String(calcValues.getCalculationMode() ) );
	  values.put("ZAHL2", calcValues.getZahl2() );
	  
	  // here the row is inserted:
	  long insertId = histDB.insert("history", null, values);
	  
	  Cursor dbCursor = histDB.query("history", AllColumns, "ID = " +  insertId, null, null, null, null);
	  dbCursor.moveToFirst();
	  return cursorToCalculation(dbCursor);
  }
  

  protected List<Calculation> getAllDBEntries() {
	  List<Calculation> calcList = new ArrayList<Calculation>();
	  calcList = new ArrayList<Calculation>(); // wtf why double do this?
	  
	  Cursor dbCursor = histDB.query("history", AllColumns, null, null, null, null, null);
	  dbCursor.moveToFirst();
	  
	  if (dbCursor.getCount() == 0) {return calcList; } // nothing in the table.
	  while (dbCursor.isAfterLast() == false) {
		  Calculation calc = cursorToCalculation(dbCursor);
		  calcList.add(calc);
		  dbCursor.moveToNext(); // returns false if move did notsucceed (e.g. past last element.)
	  }
	  dbCursor.close();
	  return calcList;
  }
  protected Calculation cursorToCalculation(Cursor dbCursor){
	  Calculation calc = new Calculation();
	  // the ID is accessed via cursor.getLong(0));
	  calc.setZahl1(dbCursor.getInt(1));
	  calc.setCalculationMode( calc.string2mode( dbCursor.getString(2) ));	  
	  calc.setZahl2(dbCursor.getInt(3));
	  return calc;
  }
}
