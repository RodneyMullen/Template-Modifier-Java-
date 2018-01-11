import java.util.*;
import javax.swing.*;
import java.lang.*;



/** this class links a field value with a saved and label value.  each can only have one linked.  
 * It is used in conjunction with the TemplateModifier application
 */



class FieldValues
{
	String fieldvalue;
	String savedvalue;
	String labelvalue;

	// Constructor,  initialises field value, creates a new savedvalue null object and sets 
	// the label value to that of the field value
	 
	public FieldValues(String fv)
	{
		fieldvalue= fv;
		savedvalue= new String();
		labelvalue=fieldvalue;
	}
	
	//takes in a String and links to saved value
	public synchronized void assignSavedValue(String input)
	{
		savedvalue= input;
	}
	// takes in string and links to label value
	public synchronized void assignLabelValue(String input)
	{
		labelvalue= input;
	}

	//returns fieldvalue	
	public synchronized String getFieldValue()
	{
		return fieldvalue;
	}

	//returns savedvalue
	public synchronized String getSavedValue()
	{
		return savedvalue;
	}
	// returns label value
	public synchronized String getLabelValue()
	{
		return labelvalue;
	}

	//takes in a String and links to fieldvalue
	public synchronized void assignFieldValue(String input)
	{
		fieldvalue=input;
	}

	//sets the saved saved value to null object
	public synchronized void removeSavedValue()
	{
		savedvalue= new String();
	}
	// resets the labelvalue to that of the fieldvalue
	public synchronized void resetLabelValue()
	{
		labelvalue= fieldvalue;
	}	
	
	//if savedvalue is not null then returns true, otherwise false
	public synchronized boolean doesSavedValueExist()
	{
		if(savedvalue.length()> 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
	
	