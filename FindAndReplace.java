import java.io.*;
import java.util.*;
import java.lang.*;


/** Used in the TemplateModifier program, This class looks through the template finds 
 *	and replaces the new values to a  new file.
 * author: Rodney Mullen
 */

class FindAndReplace
{
	
        public Fields fieldsobject;
	public String templatename, destinationfolder;
	public String hostname,edited_string;
	public PrintWriter print_to_file;
	

	//constructor
	void FindAndReplace()
	{}


		
	// takes in a string and sends it to checkfelds to check and replace against fields.  Passes returned string to writeToFile
	private void find(String fromtemplate)
	{
		
		edited_string=fieldsobject.checkFields(fromtemplate);
			
		writeToFile(edited_string);
		
	}


	/**assign Field object from TemplateModifier to object get hostname with Field.getHostname() 
	 *	and open stream  to hostname.txt. Reads in line from template and sends string to find(),
	 *	continous until done.  When done, close stream return true, otherwise false
	 */
        public synchronized boolean modify(Fields passingFields)
	{
                
                try
                {	
			
                        fieldsobject=passingFields;
                        hostname= fieldsobject.getHostname();
			
                        String file= destinationfolder+"\\"+hostname+".txt";
			
			
                        print_to_file= new PrintWriter(new FileWriter(file));
			
			
                        
                        BufferedReader filein = new BufferedReader(new FileReader(templatename));
			
                        try
                        { 
				
                                do
                                {
					try
					{
						find(filein.readLine());
					}
					catch(NullPointerException npe)
					{}
					catch(NoSuchElementException nsee)
					{}
			        }
                               	while(filein.ready()==true);
				filein.close();
                                print_to_file.close();
                                return true;
                        }
                        
			catch(EOFException eofe)
			{
				filein.close();
                                print_to_file.close();
                                return true;
			}
			
                }
                catch(IOException ioe)
                {
                        
                }
		catch(Exception e)
		{
			 
		}
		notify();
		
                return false;
	}
	// takes in String and sets input to destinationfolder.
	public void setDestinationFolder(String temp)
	{
		destinationfolder=temp;
	}

	//Takes in a string, writes newline to file to opened stream
        public synchronized void writeToFile(String writing)
	{
                print_to_file.println(writing);
		notify();
		
	}

	
	//this method gets and returns the name of the template		
	public void setTemplate(String temp)
	{
               
		templatename=temp;
                
	}
	// returns templatename
	public String getTemplateName()
	{
		return templatename;
	}

}
		
	
		
		
	
		
		
 	
