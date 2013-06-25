package net.tverrbjelke.example.rechner;

import java.util.ArrayList;
import java.util.List;

//import net.tverrbjelke.example.rechner.Calculation.Mode;

import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	/** hier kann abgefragt werden, ob wir uns in der Main activity befinden (true),
	 * oder ob wir uns in anderen layouts / activities beinden (false).
	 * Dies wird genutzt um zu entscheiden ob wir bei "back" key in die main activity gehen 
	 * oder die app beenden.
	 */
	protected boolean isMainActivityLayoutActive = true;
	
	/**  Holds the current calculation instance, 
	 * containing all information about the needed values zahl1 zahl2 etc.
	 * Ecpecting to have all values a good (displayable) default (e.g. 0)
	 */
	Calculation currentCalc = new Calculation();

	/** Stores the past calculations as string. 
	  */
	//List<Calculation> calcHistoryList = new ArrayList<Calculation>();
	List<String> calcHistoryList = new ArrayList<String>();
	
	private HistoryDataSource historyDB;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goMainActivityLayout();
        currentCalc.setCalculationMode(Calculation.Mode.MODE_SUBSTACT);// Have a default starting mode.
        historyDB = new HistoryDataSource(this); //this ist hier ein Context! 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	try
    	{
    		// R.menu.activity_main references the xml file in :  res->menu-> there xml file activity_main.xml
	        getMenuInflater().inflate(R.menu.activity_main, menu);
	        return true; //true = menu is visible
    	}
    	catch (InflateException e)
    	{
    		writeStringOnStatusLine(R.string.textSLErrorInternalMenuInflation);
    		return false; //false = menu is not visible
    	}
    }
    
    protected void printStringOnCalculationMode(int stringId){
    	// hier soll der Text rein
    	TextView tvCalcMode = (TextView) findViewById(R.id.tvCalcModeContent );
    	tvCalcMode.setText(stringId);
    	//writeStringOnStatusLine(stringId);
    }
    
    /** Checks which menu item was selected and performs its activity.
     * That is to change the calculation modus and writes that string into the textview field.
     * param item is here set/written to the used menu of the app.   
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	int itemId = item.getItemId();

    	boolean isKnownItem = false; //maybe we dont recognize the selected menu-item.
    	switch (itemId){
    	
	    	case R.id.menu_add:
	    	{
	    		currentCalc.setCalculationMode(Calculation.Mode.MODE_ADD);
    			printStringOnCalculationMode( R.string.textCalcModeAdd );
	        	isKnownItem = true;
	    		break;
	    	}
	    	case R.id.menu_minus:
	    	{
	    		currentCalc.setCalculationMode(Calculation.Mode.MODE_SUBSTACT);
    			printStringOnCalculationMode( R.string.textCalcModeSubstract );
	        	isKnownItem = true;
	    		break;
	    	}
	    	case R.id.menu_multiply:
	    	{
	    		currentCalc.setCalculationMode(Calculation.Mode.MODE_MULTIPLY);
    			printStringOnCalculationMode( R.string.textCalcModeMultiply );
	        	isKnownItem = true;
	    		break;
	    	}
	    	case R.id.menu_divide:
	    	{
	    		currentCalc.setCalculationMode(Calculation.Mode.MODE_DIVIDE);
    			printStringOnCalculationMode( R.string.textCalcModeDivide );
	        	isKnownItem = true;
	    		break;
	    	}
	    	case R.id.menu_exit:
	    	{
	        	isKnownItem = true;
	        	showDialog(10);
	    		break;
	    	}
	    	
	    	default:
	    	{
	    		//report that an unhandeled/unknown menu item was selected by user
	    		writeStringOnStatusLine( R.string.textSLErrorMenuItem);
	    	}
    	}
		if (isKnownItem) {
			return true; // true = we have processed the menu selection here.
		}
		else 
		{ 
    		writeStringOnStatusLine( R.string.textSLErrorCalcMode);
			return false;
		}
    }

    
    @Override 
    /**
     * @param id (input) id of dialog to create. 
     * see if I can use the newer function onCreateDialog( int, android.os.Bundle)
     */
    protected Dialog onCreateDialog (int id) {
    	switch (id) { 
    		case 10: 
    			Builder builder = new AlertDialog.Builder(this);
    			builder.setMessage("Applikation wird geschlossen!"); 
    			builder.setCancelable(true); 
    			builder.setPositiveButton("OK", 
    									  new DialogInterface.OnClickListener() 
    										{ // Exits the app (i.e. this class)
    											public void onClick(DialogInterface dialog, int which) { MainActivity.this.finish(); } 
    										}
				);
    			builder.setNegativeButton("Nein, doch nicht!", new DialogInterface.OnClickListener() 
    				{
    					public void onClick(DialogInterface dialog, int which) 
    					{
    						Toast.makeText(getApplicationContext(), "Applikation wird fortgesetzt", Toast.LENGTH_LONG).show(); 
						} 
					}
    			);
    			
    			AlertDialog dialog = builder.create(); 
    			dialog.show();
    			break;
		} 
    	return super.onCreateDialog(id); 
	}

    /** Shows the other view/layout with the list of the history (verlauf)
     * 
     * @param view is passed on to become the new content view. 
     */
    
    public void buttonHistoryOnClick(View view){
    	goHistoryActivityLayout();
    	
//    	//completely re-read from database.
//    	calcHistoryList.clear();
//    	try {
//    		historyDB.open();
//    		calcHistoryList  = historyDB.getAllDBEntries();
//    		historyDB.close();
//    	}
//    	catch (Exception e){
//    		Toast.makeText(this, R.string.textSLErrorDatabaseAccess, Toast.LENGTH_LONG);
//    		Log.e(historyDB.getClass().getName(), "Error, during Database access buttonHistoryOnClick()." );
//    	}
//    	
//    	 // update the history list view
//    	 ArrayAdapter<Calculation> historyAdapter = 
//    			 new ArrayAdapter<Calculation>( 	MainActivity.this,
//    					 					android.R.layout.simple_list_item_1,
//    					 					calcHistoryList);
    	// use simple strings in memory
	   	 // update the history list view
	   	 ArrayAdapter<String> historyAdapter = 
	   			 new ArrayAdapter<String>( 	MainActivity.this,
	   					 					android.R.layout.simple_list_item_1,
	   					 					calcHistoryList);
    	
    	
    	 ListView lvHistory = (ListView) findViewById(R.id.listViewHistory	);
    	 lvHistory.setAdapter(historyAdapter);
    }

    protected void goMainActivityLayout(){
    	setContentView(R.layout.activity_main);
    	isMainActivityLayoutActive = true;
    }
    

    protected void goHistoryActivityLayout(){
    	setContentView(R.layout.activity_history);
    	// we changed the activity layout to "non main".
   	 	isMainActivityLayoutActive = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK && !isMainActivityLayoutActive){
    		goMainActivityLayout();
    		return true; // we handled the key event ourselves
    	}
    	
    	return super.onKeyDown(keyCode, event); // we pass it on
    }
    
    /** Performs the given calculation.
     * @param view of the button, provided by the framework
     */
    public void buttonRechneOnClick( View view){
    	// Get the value of the edit View (field should delivber only numbers)
    	// to be sure, convert to int, but cancel if no string entered.

    	EditText editZahl1 = (EditText) findViewById( R.id.editText1);
    	String strZahl1 = editZahl1.getText().toString();

    	try
    	{
    		currentCalc.setZahl1( Integer.parseInt(strZahl1));
    	}
    	catch (NumberFormatException  e) //Parsen nicht geklappt
    	{
    		writeStringOnStatusLine( R.string.textSLErrorParseZahl1);
    		return;
    	}

    	EditText editZahl2 = (EditText) findViewById( R.id.editText2 );
    	String strZahl2 = editZahl2.getText().toString();

    	try
    	{
        	currentCalc.setZahl2( Integer.parseInt(strZahl2));    		
    	}
    	catch ( NumberFormatException e)
    	{
    		writeStringOnStatusLine( R.string.textSLErrorParseZahl2);
    		return;    		
    	}
    	
    	try {
    	// aktualisiert das Ergebnis. @ggf cath divide zer0?
    	currentCalc.doCalculate();
    	}
    	catch (ArithmeticException e) { // catch divide-by-zero
    		String errStr ="Error, Division by zero.";
    	    writeStringOnStatusLine(R.string.textSLErrorDivisionByZero);
    		Log.e(Calculation.class.getName(), errStr);
    		return;
     	} 

    	TextView tvErgebnisContent = (TextView) findViewById( R.id.tvErgebnisContent);
    	tvErgebnisContent.setText(String.valueOf(currentCalc.getResult()));
    	
    	writeStringOnStatusLine(R.string.textSLEmpty);
    	
//    	// add the string of just done calculation to the calcHistoryList.
    	calcHistoryList.add(currentCalc.toString());

//    	// Instead add it to the database
//    	try {
//    		historyDB.open();
//    		historyDB.createDBEntry(currentCalc);
//    		historyDB.close();
//		}
//    	catch (Exception e) // not know what happens with Database
//    	{	
//    		String errStr ="Error while Database access.";
//    	    writeStringOnStatusLine(R.string.textSLErrorDatabaseAccess);
//    		Log.e(historyDB.getClass().getName(), errStr);
//    		Toast.makeText(this, R.string.textSLErrorDatabaseAccess, Toast.LENGTH_LONG).show(); 
//    		return;
//    	}
    }

    
    /** Called from GUI, sets calculation mode to the given mode, 
     * and the text view tvOperationMode is set to display the new mode.
     * Question is, if we display strings from the backend Calculation.Mode.toString()
     * or if we use constant strings coming from res(values/strings.xml (which we now do)
     * 
     * @param mode (input) holds the new mode to use.
     */
    protected void setCalcMode(Calculation.Mode mode){
    	switch (mode) {
    		case MODE_ADD:
    			currentCalc.setCalculationMode( mode);
    			break;
    		case MODE_SUBSTACT:
    			currentCalc.setCalculationMode( mode);
    			break;
    		case MODE_MULTIPLY:
    			currentCalc.setCalculationMode( mode);
    			break;
    		case MODE_DIVIDE:
    			currentCalc.setCalculationMode( mode);
    			break;
			default:
				//ups did we extend the mode?
				writeStringOnStatusLine( R.string.textSLErrorCalcMode);
		    	String errStr = "setCalcMode was called with unhandled mode.";
		    	Log.e(Calculation.class.getName(), errStr);
				return;
    	}
    	//String textOfMode = getString(stringId);
    	
    }

    /** A predefinded String (res->values->strings.xml) on Statusline. 
     *  
     * @param stringId (input)
     */
    protected void writeStringOnStatusLine( int stringId ){
    	TextView tvSL = (TextView) findViewById(R.id.tvStatusLine);
    	tvSL.setText(stringId);
    }
}

