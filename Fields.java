import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;

/** Used with the Template Modifier program, this class holds, manipulates and allows access to the list of fields.  
 *	It holds the strings for manipulation with the values to substitute in a two tier array
 * author: Rodney Mullen
 */	
class Fields
{
        public static String[][] fieldarray;// holds the string to match with their corresponding replacements
        public String field_filename,templatename,match,replace,new_match;// name of file,name of template,match 			//to be made and its replacment
        Profile p_object;
	

	//constructor
	public void Fields() 
	{}

	
	/**  reads in values from profile and creates a two tier array sets fields to first tier 	
	 *	of array and initialises values also takes in the template file name. returns string
	 *      of the field names
	 */
	public void setUpFields(Profile p,String filename)throws NullPointerException
	{
		p_object=p;
                FieldValues fv;
		
               
		
		try
		{
			field_filename=filename;
			Vector tempvector=new Vector(0,1);
			tempvector= p_object.convertToFieldValuesVector(p_object.getFile(field_filename));
			
			

			int vectorsize= tempvector.size();
			
			//minus four for the top 3 non field entries and the additional vector imcrement
			fieldarray= new String[vectorsize-3][2];
			
                       
                        String tempstring=new String();
			int a=0;
			for(int i=3; i<vectorsize;i++)
			{
				fv= (FieldValues) tempvector.elementAt(i);
				tempstring=(String)fv.getFieldValue();
                           	fieldarray[a][0]=tempstring;
				a++;
			}
		}
		catch(NullPointerException npe)
		{
			
		}
		
		catch(Exception e)
		{
                       
		}
		
	}



	// takes in string, tokenises string and sets to second tier of array[][].  
	public static void enterFields(String inputs[])
	{
		
		try
		{
			
			
			String temp= new String();
			for(int b=0; b<fieldarray.length;b++)
			{
				

				fieldarray[b][1]=inputs[b];
				
				
					
			}
		}
		catch(NullPointerException npe)
		{
                      
		}
		catch(NoSuchElementException nsee)
		{
                    
		}
		catch(Exception e)
		{
                       
		}
		
		
	}
	
	/*Takes in a string and checks it against fields in array.  
	 * If a match is found, replaces them and returns 	 
	 * the corresponding entry in the array[][].  If not, the orginal string is returned
	 */	
	public synchronized String checkFields(String tocheck)
	{
		for(int a=0; a<fieldarray.length;a++)
		{
			match=fieldarray[a][0];
			replace=fieldarray[a][1];
			try
			{
				new_match="\\"+match;
				
				new_match= match;
				// the \\ removes any deliminters in the string that might interact with the code
				
				tocheck=tocheck.replace(new_match,replace);
				
				
				
			}
			catch(PatternSyntaxException pse)
			{
				tocheck=tocheck.replace(match,replace);
				
				
				
			}
			catch(NullPointerException npe)
			{
				tocheck=tocheck.replace(new_match,"  ");
				
				
			}
			
			
			
		}
               
		notify();
                return tocheck;
	}

	//returns the hostname from the second tier array[][]
        public String getHostname()throws NullPointerException
	{
			return fieldarray[0][1];
	}
	//returns the name of the template file
	public String getTemplatename()
	{
		return templatename;
	}
	// makes sure copies are not made of future null replacements
	public static void nullifyReplacements()
	{
		String t= new String();
		for(int i=0;i<fieldarray.length;i++)
		{
			fieldarray[i][1]=t;
		}
		
	}
	
	
	
	
}


	
		
		
