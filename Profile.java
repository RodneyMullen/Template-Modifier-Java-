import java.util.*;
import java.io.*;
import java.lang.*;
import java.text.*;
import java.util.Vector.*;


/** this class creates, loads, deletes and edits profiles for TemplateModifier
*/


class Profile
{
	
	public static String profilename, destination, source;
	
        public static PrintWriter print_to_file;
	// constructor
	public void Profile()
	{}
	
	/** takes in a string, tokenises it, first entry is template location, sends to canFecthString 
	 *  to see if it exits. If true then asks user in the GUI if they want the profile replaced
	 *  if yes return true, otherwise false.  
	 *  second is the destination, the rest are the field names.   
	 *  Opens a new file, if already exists asks if user wishes to replace file.  
	 *  If yes, then replace.  Otherwise create file. Adds new profile to the bottom of resourcesForTemplateModifier.
	 */
	public void createProfile(Vector temp)
	{
		
                writeToFile(temp,temp.elementAt(1).toString());
		try
		{
			Vector filevector=getFile("resourcesForTemplateModifier");
			boolean alreadyexist= filevector.contains(temp.elementAt(1).toString());
			if(alreadyexist==false)
			{
				// element 0 will be a N so element 1 will be the name
				filevector.addElement(temp.elementAt(1));
				writeToFile(filevector,"resourcesForTemplateModifier");
			}
		}
		catch(NullPointerException npe)
		{
			
		}
	}
	//tries to find a profile.  returns true if found		
	public synchronized boolean canFetchProfile(String name)
	{
		
		BufferedReader readfile;
		boolean canread;
		try
		{
			readfile = new BufferedReader(new FileReader(name+".dat"));
			readfile.close();
			canread=true;
			notify();
			return canread;
			
		}
		catch(IOException ioe)
		{
			
			canread=false;
			notify();
			return canread;	
			
		}
		
	}
	// checks to find a file, returns true if found
	public synchronized boolean canFetchFile(String name)
	{
		
		BufferedReader readfile;
		boolean canread;
		try
		{
			readfile = new BufferedReader(new FileReader(name));
			readfile.close();
			canread=true;
			notify();
			return canread;
			
		}
		catch(IOException ioe)
		{
			canread=false;
			notify();
			return canread;	
			
		}
		
		
	}
	
	//  gets a Vector of all the profiles and returns it
	public Vector showProfiles()
	{
		Vector profiles= getFile("resourcesForTemplateModifier");
		
		return profiles;
	}
	// finds the selected profile and makes it the default by placing the first in the resourceForTemplateModifier.dat file
	public synchronized void setDefaultProfile(String tempstring)
	{
		
		Vector tempvector= new Vector(0,1);
		
		tempvector.addElement(tempstring);
		String checkwith=new String();
                Vector profiles_vector= getFile("resourcesForTemplateModifier");
		for(int i=0;i<profiles_vector.size();i++)
		{
			checkwith=(String)profiles_vector.elementAt(i).toString();
			if(tempstring.compareTo(checkwith)==0)
			{
				
				profiles_vector.removeElementAt(i);
				if(i==profiles_vector.size())	
				{}
				else
				{
					tempvector.addElement(profiles_vector.elementAt(i));
				}
				
				
			}
			else
			{	
				
				tempvector.addElement(profiles_vector.elementAt(i));
			
			}
		}
		
                writeToFile(tempvector, "resourcesForTemplateModifier"); 
		notify();
		
		
	}
	//  takes a profile and renames it in its own file and in resourcesForTemplateModifier.dat
	public synchronized void renameProfile(String newprofilename,String oldprofilename)
	{
		
		Vector resourcesForTemplateModifier= getFile("resourcesForTemplateModifier");
		Vector tempvector= new Vector(0,1);
		FieldValues fv;
		//sort out the resourcesForTemplateModifier file
		for(int a=0; a<resourcesForTemplateModifier.size();a++)
		{
			if(oldprofilename.compareTo(resourcesForTemplateModifier.elementAt(a).toString())==0)
			{
				tempvector.addElement(newprofilename);
				
			}
			else
			{
				
				tempvector.addElement(resourcesForTemplateModifier.elementAt(a));
			}
		}
		writeToFile(tempvector, "resourcesForTemplateModifier"); 
		//sort out the profile file
		Vector list= convertToFieldValuesVector(getFile(oldprofilename));
		fv= new FieldValues(newprofilename);
		list.setElementAt(fv,0);
		deleteProfile(oldprofilename);
		Vector newvec= convertToStringVector(list);
		writeToFile(newvec, newprofilename);
		notify();
	}
	
	//converts the vector of objects into a vector for writing to file
	public synchronized Vector convertToStringVector(Vector input)
	{
		FieldValues fv;
		String enter;
		Vector newvec= new Vector(0,1);
		for(int i=0; i<input.size();i++)
		{
			
			fv= (FieldValues) input.elementAt(i);
			//if a saved value exist, first string will be Y followed by the field then the saved value
			if(fv.doesSavedValueExist()==true)
			{ 
				newvec.addElement("Y");
				enter= (String) fv.getFieldValue();
				newvec.addElement(enter);
				enter= (String) fv.getLabelValue();
				newvec.addElement(enter);
				enter= (String) fv.getSavedValue();
				
				
				newvec.addElement(enter);
			}
			// if no saved value exists, then first string will be a N followed by the field
			else
			{
				newvec.addElement("N");
				enter= (String) fv.getFieldValue();
				newvec.addElement(enter);
				enter= (String) fv.getLabelValue();
				newvec.addElement(enter);
			}
		}
		return newvec;
	}

	/**converts string vectors for the profiles to the proper style of FieldValue Vector
	 * also converts previous versions of profiles (3 versions in total),  The first  held
	 * the profile name, destionation, source and fields.   The second version held an N or Y
	 * in front of these values to indicate saved values.  If there was a Y the name would follow
	 * then the saved value, if not then the next N or Y would follow.   
	 * The third version duplicated each fieldvalue as the second of these would be the label value.
	 * If any of these are found, they are updated into version 3
	 */	
	public synchronized Vector convertToFieldValuesVector(Vector input)
	{
		int i=0;
		int a;
		FieldValues fv;
		String yes= "Y";
		String no= "N";
		Vector newvec= new Vector(0,1);
		Vector newinput;
		
		do
		{	
			
			a=i+2;
			//fixes 2nd version of profile to 3rd
			if(no.compareTo(input.elementAt(a).toString())==0 || yes.compareTo(input.elementAt(a).toString())==0)
			{
				
				newinput= fixThisForProfileTwo(input);
				input=newinput;
			}
			
			else if(yes.compareTo(input.elementAt(i).toString())==0)
			{
				
				i++;
				fv= new FieldValues(input.elementAt(i).toString());
				i++;
				fv.assignLabelValue(input.elementAt(i).toString());
				i++;
				fv.assignSavedValue(input.elementAt(i).toString());
				newvec.addElement(fv);
				i++;
			}
			else if(no.compareTo(input.elementAt(i).toString())==0)
			{
				i++;
				fv= new FieldValues(input.elementAt(i).toString());
				i++;
				fv.assignLabelValue(input.elementAt(i).toString());
				newvec.addElement(fv);
				i++;
				
			}
			else // fixes previous versions of profile data files to 3rd
			{
				
				newinput= fixThisForProfileOne(input);
				input=newinput;
				
				
			}
			
		
		
		}
		while(i<input.size());
		
		
		return newvec;
	}
	// converts Version 2 profiles to Version 3, by duplicating field values
	public synchronized Vector fixThisForProfileTwo(Vector input)
	{
		
		Vector fixedvector= new Vector(0,1);
		String compare;
		for(int a=0; a<input.size();a++)
		{
			compare= input.elementAt(a).toString();
			if(compare.compareTo("Y")==0)
			{
				fixedvector.addElement(input.elementAt(a).toString());
				a++;
				fixedvector.addElement(input.elementAt(a).toString());
				fixedvector.addElement(input.elementAt(a).toString());
				a++;
				fixedvector.addElement(input.elementAt(a).toString());
			}
			else
			{
				fixedvector.addElement(input.elementAt(a).toString());
				a++;
				fixedvector.addElement(input.elementAt(a).toString());
				fixedvector.addElement(input.elementAt(a).toString());
			}
		}
		writeToFile(fixedvector, fixedvector.elementAt(1).toString());
		return fixedvector;
	}			

	// converts version 1 profiles to version 3, duplicates field values with an N every thrid place					
	public synchronized Vector fixThisForProfileOne(Vector input)
	{
		
		Vector fixedvector= new Vector(0,1);
		for(int a=0; a<input.size();a++)
		{
			fixedvector.addElement("N");
			fixedvector.addElement(input.elementAt(a).toString());
			fixedvector.addElement(input.elementAt(a).toString());
		}
		writeToFile(fixedvector, fixedvector.elementAt(1).toString());
		return fixedvector;
	}			
	// deletes profile from the resourcesForTemplateModifier.dat file and deletes the profile file.
	public synchronized void deleteProfile(String profile)
	{
		try
		{
			File file= new File(profile+".dat");
			file.delete();
			Vector profiles= getFile("resourcesForTemplateModifier");
			String checkwith=new String();
			for(int i=0;i<profiles.size();i++)
			{
				checkwith=(String) profiles.elementAt(i).toString();
				if(profile.compareTo(checkwith)==0)
				{
					profiles.removeElementAt(i);
				}
			}
			writeToFile(profiles,"resourcesForTemplateModifier");
		}
		catch(NullPointerException npe)
		{
			
		}
		notify();
		
		
	}
	
		
	// a common method to read the contents of a file into a vector then returns the vector
	public synchronized Vector getFile(String name)
	{
		FieldValues fv;
		
		Vector resourcesForTemplateModifierfile= new Vector(0,1);
		try
		{
			BufferedReader read_resourcesForTemplateModifier_file = new BufferedReader(new FileReader(name+".dat"));
			
			String temp=read_resourcesForTemplateModifier_file.readLine();
			do
			{
				resourcesForTemplateModifierfile.addElement(temp);
				temp=read_resourcesForTemplateModifier_file.readLine();
			}
			while(temp != null);
			
			read_resourcesForTemplateModifier_file.close();
		}
		catch(IOException ioe)
		{
			
		}
		notify();
		return resourcesForTemplateModifierfile;
	}
	
	
	// common method to write the contents of a vector to a file, takes in a vector and string filename
        public synchronized void writeToFile(Vector towrite, String filename)
        {
	 	
                try
                {
                        PrintWriter newfile= new PrintWriter(new FileWriter(filename+".dat"));

                        for(int a=0; a<towrite.size(); a++)
                        {
                              newfile.println(towrite.elementAt(a));
                        }
                        newfile.close();
                }
                catch(IOException ioe)
                {
                    System.out.println(ioe);    
                }
		notify();
        }
	
	
	// common method for writing the contents of a vector to a file, takes in a string array, first element is the name of the file
	public synchronized void writeToFile(String[] towrite)
	{
		try
		{
			
			PrintWriter newfile= new PrintWriter(new FileWriter(towrite[0]+".dat"));
			String tempstring=new String();
			
			for(int i=1;i<towrite.length;i++)
			{
				newfile.println(towrite[i]);
                        } 
			newfile.close();
		}
		catch(IOException ioe)
		{
			
		}
		notify();
	}

	/** finds the default profile from the first profile in resourcesForTemplateModifier.dat.  
  	 * if it does not exist then deletes it from the file and reads in the next.  when one if found
	 *  that profile is set as the default.   If no profiles are found the returns a noProfilesExist String.  
	 */
	public String getDefaultProfile()
	{
		String temp=new String();
		Vector profiles= getFile("resourcesForTemplateModifier");
		boolean canfetch=false;
		int a=0;
		try
		{
			do
			{
				temp= (String) profiles.elementAt(a);
				canfetch= canFetchProfile(temp);
				if(canfetch==false)
				{
					deleteProfile(temp);
				}
				a++;
			}
			while(canfetch==false);
		}
		catch(NullPointerException npe)
		{
			temp="noProfilesExist";
		}
		catch(ArrayIndexOutOfBoundsException aioobe)
		{
			temp="noProfilesExist";
		}
		
			
		return temp;
	}

	public synchronized boolean checWriteToFile(String checkfilename)
	{
		try
                {
                        PrintWriter checkfile= new PrintWriter(new FileWriter(checkfilename+".dat"));

                        
                        checkfile.close();
			notify();
			return true;
			
                }
                catch(IOException ioe)
                {
			notify();
                    return false;
			
                }
		
	}	
	
	
}
		
		

	
			
			
	
			
			
				
			
			
		
		
