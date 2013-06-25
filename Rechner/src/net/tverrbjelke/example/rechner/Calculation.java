/**
 * 
 */
package net.tverrbjelke.example.rechner;

import java.security.InvalidParameterException;

import android.util.Log;



/** Simple data class of an instance of a done calculation.
 * @author tverrbjelke
 *
 */

public class Calculation {

	/** These modes are internally known and used by the calculator.
	 * Two numbers of the input fields are used as input 
	 * and the result is displayed in results field. 
	 */
	public enum Mode {MODE_ADD, MODE_SUBSTACT, MODE_MULTIPLY, MODE_DIVIDE}

	private int zahl1;
	private int zahl2;
	private Mode mode;
	private int result;

	public void setZahl1(int z){ zahl1 = z; }
	int getZahl1(){ return zahl1; }

	public void setZahl2(int z){ zahl2 = z; }
	int getZahl2(){ return zahl2; }

	public void setResult(int z){ result = z; }
	int getResult(){ return result; }

	public void setCalculationMode(Mode m){ mode = m; }
	Mode getCalculationMode() { return mode; }

	public Calculation(){
		setZahl1(0);
		setCalculationMode ( Mode.MODE_ADD );
		setZahl2(0);
		setResult(0);
	}

	

    /** performs the calculation and updates the result data field.
     * @todo @throws "ExceptionDivideByZero" 
     */
    public void doCalculate() {
    	int result;
    	switch ( getCalculationMode()) {
    		case MODE_ADD:
    			result = zahl1 + zahl2;
    			break;
    		case MODE_SUBSTACT:
				result = zahl1 - zahl2;
    			break;
			case MODE_MULTIPLY:
				result = zahl1 * zahl2;
				break;
    		case MODE_DIVIDE:
				result = zahl1 / zahl2;
				break;
			default:
				result = zahl1 + zahl2;
    	}
    	setResult(result);
		//return retVal;
	}

    
    /** The calcualtion is converted to a meaningful string.
     * @see  mode2String
     */
    @Override
    public String toString() {
    	String retVal = String.valueOf(zahl1) + " " + 
    					mode2String(mode) + " " +
    					String.valueOf(zahl2) + " " +
    					"=" +
    					String.valueOf(result) + " ";
    	return retVal;
    }
    
    public String mode2String( Mode mode) {
    	String retVal = "";
    	switch (mode) {
			case MODE_ADD:
				retVal = "+";
				break;
			case MODE_SUBSTACT:
				retVal = "-";
				break;
			case MODE_MULTIPLY:
				retVal = "*";
				break;
			case MODE_DIVIDE:
				retVal = "/";
				break;
			default:
				retVal = "unknown";
    	}
    	return retVal;
    }

    public Mode string2mode( String modeStr) throws InvalidParameterException {
    	if (modeStr == "+") { return Mode.MODE_ADD;}
    	else if (modeStr == "-") { return Mode.MODE_SUBSTACT;}
    	else if (modeStr == "*") { return Mode.MODE_MULTIPLY;}
    	else if (modeStr == "/") { return Mode.MODE_DIVIDE;}
    	else {
    		
    		InvalidParameterException e = new InvalidParameterException("Error, string must be one of +-*/");
    		throw e;
		}
    }

}
