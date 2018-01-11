import java.io.*;
import java.util.*;
import java.lang.*;
import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*; 
import java.awt.GridBagLayout.*;
import java.awt.Component; 
import java.awt.Container;
import javax.swing.filechooser.*;
import java.awt.Insets; 
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import java.awt.color.*;
import javax.swing.AbstractButton.*;
import javax.swing.JCheckBox.*;


/**   The TemplateModifier program allows the user to type in a number of entries.  
 * A Template is then read in and the entries are substituted for these values in a new document 
 * with the name of the hostname given This class starts the progrom with a GUI and holds main method.
 * author: Rodney Mullen
 */
class TemplateModifier extends JPanel implements ActionListener,ItemListener
{
        Fields Field_object = new Fields();// new Object of Field class 
        FindAndReplace Far_object=new FindAndReplace();//new object of FindAndReplace class
	Profile p_object;// new object of Profile class
	
	JFrame frame;// main gui frame
	
	JPanel main_panel,logo_panel,top_panel,middle_panel,bottom_panel;
	/** Panels for the gui:  main panel holds all other panels.  main screen holds logo panel, 
	 * top_panel, middle panel and bottom panel.   
	 */
	JTextField textfield; 
	// text fields for the GUI 
	
	
	JTextArea sourcetext=new JTextArea(1,50);// points to the Template location
	JTextArea destinationtext=new JTextArea(1,50);//points to the destination folder for the new file
	
	JLabel l,light;
	// l: used for the fields in MIddle panel, light: label icon for the light on the bottom panel
	
	
	ImageIcon greenlight,nolight;// image icons for a green light and an off light
	 
	JTextField[] textfieldarray;//holds the field objects in an array
        String profilename; //profile name used for name of profile
	JFileChooser source_fc=new JFileChooser(); // allows user to use a file locater
	JFileChooser destination_fc=new JFileChooser();// allows user to use a directory locater
        JButton source_button, destination_button,gobutton,clearbutton;  	 
	// source button: accesses file locater- top panel, destination button: accesses directory locater -top panel
	// clearbutton: clears all fields- Bottompanel, finish_button: update profile- createleft panel
	// nextfield_button: enter new field- create left panel,  

	Vector profile_fields = new Vector(0,1);//vector of the profile fields
	Vector profilelist= new Vector(0,1);// vector list of the profiles, first is set as the default profile
        
	JMenu Main_menu, Options_menu,setdefaultprofile, loadprofile,Help_menu;// list of menus and submenus	
        JMenuBar menubar;// menu bar for the menus
	JMenuItem createprofile,deleteprofile,editprofile,renameprofile, copyprofile, helptopics, about,menuItem;// items on the menu
        String set_text= new String();
	String temp= new String();// temp string used for changing of destination folder and template locations
	boolean proceed;  //proceed used for adding changes to profile of destination folder and template locations
	
	
	JOptionPane infopane= new JOptionPane(); // creates a dialog for an information message
	JOptionPane errorpane= new JOptionPane();// creates a dialog for an error message
	JOptionPane makepane= new JOptionPane(); // create a dialog to return a yes or no answer
	JOptionPane rename= new JOptionPane();// create dialog to request change in profile name
	JOptionPane newname = new JOptionPane(); // create dialog to request new name for copied profile
	JOptionPane aboutTM= new JOptionPane();
        JScrollPane mainpane;// scrollpane for the middlepanel
	JCheckBox cboxitem; // used in the middle panel for holding values
	


	/**constructor and initialises GUI,   Checks to see if system file "resourcesForTemplateModifier.dat" is present. 
	 * if not then creates a new blank file so program still functions even if the profiles are lost.  A check is then perfromed
	 * on the profile which is set as default.  If present continues with setup, or opens up the Create screen.
	 */
        public TemplateModifier()
	{
		p_object= new Profile();
		if(p_object.canFetchProfile("resourcesForTemplateModifier")==false)
		{
			
			sendDialog("No Profiles Found");
                        try
                        {
                                PrintWriter newfile= new PrintWriter(new FileWriter("resourcesForTemplateModifier.dat"));
                                newfile.close();
                        }
                        catch(IOException ioe)
                        {
                                errorDialog("Files cannot be accessed");
                        }
                        //here needs updating with create gui
			CreateGui CG= new CreateGui();
			
			CG.makeCreateGui(this);
			
		}
		
		else if(p_object.canFetchProfile(p_object.getDefaultProfile())==false)
		{
			sendDialog("No Profiles Found");	
			CreateGui CG= new CreateGui();
			
			CG.makeCreateGui(this);
		}
		else 
		{
			profile_fields= p_object.getFile(p_object.getDefaultProfile());
			newTemplateModifier(profile_fields);
		}	
		
		
		
	}

	/** All new profiles to be setup are sent to this method.  A check is performed on the system file "resourcesForTemplateModifer.dat"
	 *  if not found, a new file is created and the create screen is opened.  If the profile does not exist the user is sent to the create screen.
	 *  The profile is converted to the right format, if this in turn does not exist, the user is brought to the create screen.
	 *  the profile and the the GUI are setup.
	 */
	public void newTemplateModifier(Vector newfields)
	{
		
		if(p_object.canFetchProfile("resourcesForTemplateModifier")==false)
		{
			
			sendDialog("No Profiles Found");
                        try
                        {
                                PrintWriter newfile= new PrintWriter(new FileWriter("resourcesForTemplateModifier.dat"));
                                newfile.close();
                        }
                        catch(IOException ioe)
                        {
                                errorDialog("Files cannot be accessed");
                        }
                        //here needs updating with create gui
			CreateGui CG= new CreateGui();
									
			CG.makeCreateGui(this);
			
		}
		if(newfields.isEmpty())
		{
			sendDialog("No Profiles Found");
			CreateGui CG= new CreateGui();
			CG.makeCreateGui(this);
			frame.dispose();
		}
			
		profile_fields=p_object.convertToFieldValuesVector(newfields);
		p_object= new Profile();
		
		if(profile_fields.isEmpty())
		{
			
			sendDialog("No Profiles Found");
			CreateGui CG= new CreateGui();
			CG.makeCreateGui(this);
			frame.dispose();
		}	
		else
		{
			setUpProfile();
		}
		//frame setup
		frame= new JFrame(profilename);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
                frame.setSize(new Dimension(320,240));  
		setMenuItems();
                frame.setJMenuBar(menubar);
		
		
		
		// panels setup
		main_panel=new JPanel();
		main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
		
		logo_panel=new JPanel();
		top_panel=new JPanel();
		top_panel.setLayout(new GridBagLayout());
		
		
		middle_panel=new JPanel(new SpringLayout());
		bottom_panel= new JPanel();
		bottom_panel.setLayout(new GridBagLayout());
		
		


		main_panel.add(logo_panel);
		main_panel.add(top_panel);
		
		
		main_panel.add(middle_panel);
		
		mainpane= new JScrollPane(middle_panel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
		
		main_panel.add(mainpane);
		main_panel.add(bottom_panel);

		
		
		frame.getContentPane().add(main_panel, BorderLayout.CENTER);
		//assigning items to each panel.
		addLogoComponents();
		addTopComponents();
		addMiddleComponents();
		addBottomComponents();
		
		main_panel.setOpaque(true);
		top_panel.setOpaque(true);
		
		middle_panel.setOpaque(true);
		bottom_panel.setOpaque(true);
		
		frame.getRootPane().setDefaultButton(gobutton);
		
		
		frame.pack(); 
		frame.setVisible(true); 
		
                
		
		
		
	}

	
	/**sets up the Template Modifier with the Profile:   assigns values to the profile name, source file, destination file
	 * and the rest to a Field object to prepare for operation.
	 */
	public void setUpProfile()
	{
		FieldValues fv;
		String temp=new String();
		fv= (FieldValues) profile_fields.elementAt(0);
		profilename=(String) fv.getFieldValue();
		fv= (FieldValues) profile_fields.elementAt(1);
		temp=(String) fv.getFieldValue();
		if(temp.compareTo("noFieldEntered")!=0)
		{
			sourcetext.setText(temp);
			Far_object.setTemplate(temp);
			
		}
		fv= (FieldValues) profile_fields.elementAt(2);
		temp=(String) fv.getFieldValue();
		if(temp.compareTo("noFieldEntered")!=0)
		{
			destinationtext.setText(temp);
			Far_object.setDestinationFolder(temp);
		}
		try
		{
			Field_object.setUpFields(p_object,profilename);	
		}
		catch(NullPointerException npe)
		{
			sendDialog("Profile: "+profilename+" does not exist");
		}
	}
	
	// creates a small frame to ask the user a question giving a yes or no option. Inputs the string question and outputs the boolean responce.
        public boolean requestDialog(String message)
	{
		
		int n = JOptionPane.showConfirmDialog(frame, message,"Make a Selection",JOptionPane.YES_NO_OPTION);


		if(n== JOptionPane.YES_OPTION)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// takes in a string message to create a frame and displayed to the user.
        public void sendDialog(String message) 
	{
		infopane.showMessageDialog(frame,message);
	}
	// takes in a string message and creates a frame to display an error message to the user
        public void errorDialog(String message) 
	{
		errorpane.showMessageDialog(frame, message,"Error", errorpane.ERROR_MESSAGE);
	}
	
	// used for renaming a profile, asks user to type in a new name.  This name is returned.
	private String renameDialog()	
	{
		String s= (String)newname.showInputDialog(frame,"Type in new Profile name","Copy Profile",newname.PLAIN_MESSAGE);

		return s;
	}

	private String newNameDialog()	
	{
		String s= (String)rename.showInputDialog(frame,"Type in new Profile name","Rename Profile",rename.PLAIN_MESSAGE);

		return s;
	}
	// used for About TemplateModifier, creates a frame with information about the program.
	private void aboutDialog()
	{
		
		aboutTM.showMessageDialog(frame, " Template Modifier \n Version 1.03 \n Author: Rodney Mullen \n 2004","About Template Modifier",JOptionPane.INFORMATION_MESSAGE);
	}
		
		
	
 	/** menu items for the main frame are setup up here.  Three menus, Main Menu, Options Menu and Help Menu.  Main Menu 
	 * allows users to load profiles, set default profiles, and create profile.  Options menu allows users to edit profiles, delete or rename profiles
	 * Options menu allows users to load a help section and an About Template Modifier section
	 */
	private void setMenuItems()
	{
		
                menubar=new JMenuBar();
		Main_menu=new JMenu("Main Menu");
		Main_menu.setMnemonic(KeyEvent.VK_A); 
		Main_menu.getAccessibleContext().setAccessibleDescription( "Main menu, allows user acces to set default profiles,load and create new profiles"); 
                menubar.add(Main_menu);
		
		Options_menu=new JMenu("Options");
		Options_menu.setMnemonic(KeyEvent.VK_A); 
		Options_menu.getAccessibleContext().setAccessibleDescription( "Options menu, allows user to delete or edit current profile"); 
                menubar.add(Options_menu);

		Help_menu= new JMenu("Help");
		Help_menu.setMnemonic(KeyEvent.VK_A);
		Help_menu.getAccessibleContext().setAccessibleDescription( "Help menu, shows two items, Help Topics and About TemplateModifier"); 
		menubar.add(Help_menu);
		 
		loadprofile=new JMenu("Load Profile");
		loadprofile.setMnemonic(KeyEvent.VK_S);
		profilelist =p_object.showProfiles();
		if(profilelist.isEmpty()==true)
		{
			sendDialog("No profiles found");
		}
		else
		{
			for(int a=0; a<profilelist.size();a++)
			{
				menuItem = new JMenuItem(profilelist.elementAt(a).toString()); 
				menuItem.setMnemonic(KeyEvent.VK_B);
				menuItem.addActionListener(this); 
				loadprofile.add(menuItem);
			
			}
			Main_menu.add(loadprofile);
			Main_menu.addSeparator(); 
		
			setdefaultprofile=new JMenu("Set Default Profile");
			setdefaultprofile.setMnemonic(KeyEvent.VK_S);
			
			menuItem = new JMenuItem(profilelist.elementAt(0).toString()); 
			menuItem.setMnemonic(KeyEvent.VK_B);
			setdefaultprofile.add(menuItem);
			
			
			for(int b=1; b<profilelist.size();b++)
			{	
				menuItem = new JMenuItem(profilelist.elementAt(b).toString()); 
				menuItem.setMnemonic(KeyEvent.VK_B);
				menuItem.addActionListener(this); 
				setdefaultprofile.add(menuItem);
				
			}	
			Main_menu.add(setdefaultprofile);
		
			Main_menu.addSeparator();
			createprofile = new JMenuItem("Create Profile"); 
			createprofile.setMnemonic(KeyEvent.VK_B);
			createprofile.addActionListener(this); 
			Main_menu.add(createprofile);
	
			editprofile = new JMenuItem("Edit Profile"); 
			editprofile.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_1, ActionEvent.ALT_MASK)); 
			editprofile.addActionListener(this); 
			Options_menu.add(editprofile); 
			deleteprofile = new JMenuItem("Delete Profile"); 
			deleteprofile.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_2, ActionEvent.ALT_MASK)); 
			deleteprofile.addActionListener(this); 
			Options_menu.add(deleteprofile); 

			renameprofile = new JMenuItem("Rename Profile"); 
			renameprofile.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_3, ActionEvent.ALT_MASK)); 
			renameprofile.addActionListener(this); 
			Options_menu.add(renameprofile); 

			copyprofile = new JMenuItem("Copy Profile"); 
			copyprofile.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_4, ActionEvent.ALT_MASK)); 
			copyprofile.addActionListener(this); 
			Options_menu.add(copyprofile); 


			helptopics = new JMenuItem("Help Topics");
			helptopics.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_1, ActionEvent.ALT_MASK)); 
			helptopics.addActionListener(this); 
			Help_menu.add(helptopics); 

			Help_menu.addSeparator();
			about = new JMenuItem("About TemplateModifier");
			about.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_2, ActionEvent.ALT_MASK)); 
			about.addActionListener(this); 
			Help_menu.add(about); 

			
			
		}	
	}

	// logo components are setup here
	private void addLogoComponents()
	{
		try
		{
			logo_panel.setBackground(Color.WHITE);
                	ImageIcon logo = new ImageIcon("Images/logo.gif","Interfusion logo");
			JLabel icon= new JLabel(logo,SwingConstants.LEFT);
			logo_panel.add(icon);
		}
		catch(NullPointerException npe)
		{
			errorDialog("Logo not found");
		}
	}

	
	// Adds the top components such as the fields, buttons and extra browsing frames for Template source location
	// and Distination directory.
	private void addTopComponents()
	{
		
		
		
		
		
		source_fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		
		source_fc.setDialogTitle("Template Selection");
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;  
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.weightx = 1.0; 
		c.weighty = 1.0; 
		source_button = new JButton("Locate Template File...");
		source_button.setMnemonic(KeyEvent.VK_I); 
		source_button.addActionListener(this); 
		sourcetext.setEditable(false);
		
		top_panel.add(source_button);
		top_panel.add(sourcetext,c);
		
		JScrollPane scrollPane_source = new JScrollPane(sourcetext, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
		top_panel.add(scrollPane_source,c);
		
		
		
		
		destination_button = new JButton("Select Destination....."); 
		destination_button.setMnemonic(KeyEvent.VK_I); 
		destination_button.addActionListener(this);
		GridBagConstraints d = new GridBagConstraints();
		d.fill = GridBagConstraints.HORIZONTAL; 
		d.weightx = 1.0; 
		d.weighty = 1.0; 
		destinationtext.setEditable(false);
		
		top_panel.add(destination_button);
		top_panel.add(destinationtext,d);
			
		destination_fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		destination_fc.setApproveButtonMnemonic(KeyEvent.VK_I);	
		destination_fc.setApproveButtonText("Select Directory");
		
		destination_fc.setDialogTitle("Directory Selection");
		JScrollPane scrollPane_destination = new JScrollPane(destinationtext, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
		
		
		
		
		
			
		
			
		
		top_panel.add(scrollPane_destination,d);
		
		
	}

	/** sets up the middle components.  This involves the fields and labels for the program.  Since these may change, access to the
	 * tool SringUtilites is used.   The panels are also resized to fit the number of fields.  12 fields can fit on the one screen until the scrollbar is needed.
	 */
	public void addMiddleComponents()
	{
			
		
		int other=0;
		int numPairs= profile_fields.size();
		
		//numPairs= numPairs-3;
		FieldValues fv;
		 
		textfieldarray= new JTextField[numPairs-3];
		String temp= new String();
		for (int i = 3; i < numPairs; i++) 
		{ 
			
			fv= (FieldValues) profile_fields.elementAt(i);
			l = new JLabel(fv.getLabelValue(),JLabel.TRAILING); 
			//middle_panel.add(l);  
                        cboxitem = new JCheckBox(fv.getLabelValue()); 
                        textfield = new JTextField(10); 
			
			l.setLabelFor(textfield); 
			if(fv.doesSavedValueExist()==true)
			{
				
				textfield.setText(fv.getSavedValue());
				textfield.setEditable(false);
				cboxitem.setSelected(true);
			}
			else
			{
				cboxitem.setSelected(false);
			}
			cboxitem.addItemListener(this);
			middle_panel.add(cboxitem);
			
			middle_panel.add(textfield); 
			textfieldarray[other]=textfield;
			other++;
		}
		
		//Lay out the panel. 
		int value=1;
		numPairs= numPairs-3;
		SpringUtilities.makeCompactGrid(middle_panel, numPairs, 2, 6, 6, 6, 6); 
		//the following lays out the size for the scrollpane in the middle panel.  
		// 1 is set to 50 long, 2 is 75, 3 is 100 and so on until the max of 12 being 450
		if(numPairs==1)
		{
			mainpane.setPreferredSize(new Dimension(70,40));
		}
		else if(numPairs > 1 && numPairs <= 12)
		{
			value= numPairs-value;
			value=  70+value*25;
			mainpane.setPreferredSize(new Dimension(70,value));
		}
		else 
		{
			mainpane.setPreferredSize(new Dimension(70,325));
		}
		
		
	}
	
	// adds the bottome components.  go button, clear button and a light used to indicate successful operations.
	private void addBottomComponents()
	{
		try
		{
			nolight = new ImageIcon("Images/nolight.gif"," an unlit icon");
			greenlight = new ImageIcon("Images/greenlight.gif"," a green light");
			light= new JLabel();
		
			light.setIcon(nolight);
			bottom_panel.add(light);
		
			gobutton=new JButton("GO");
			gobutton.setMnemonic(KeyEvent.VK_I); 
			gobutton.addActionListener(this); 
			
			bottom_panel.add(gobutton);
		
			clearbutton=new JButton("Clear All");
			clearbutton.setMnemonic(KeyEvent.VK_I); 
			clearbutton.addActionListener(this); 
		
			bottom_panel.add(clearbutton);
			bottom_panel.setPreferredSize(new Dimension(100,200));
		}
		catch(NullPointerException npe)
		{
			errorDialog("Image icon missing");
		}
		
		
		
		
		
	}
	
	// event handeling for item state changes.  Mainly used for the tick boxes on the middle panel.  If a field is ticked then the 
	// string in the text box is saved to the profile and always comes up when loaded.  Unticking resets this value. 
	public void itemStateChanged(ItemEvent e) 
	{
		
		JCheckBox source = (JCheckBox)(e.getSource()); 
		JTextField tf;
		FieldValues fv;
		for(int i=3; i<profile_fields.size();i++)
		{
			fv= (FieldValues) profile_fields.elementAt(i);
			if(source.getText().compareTo(fv.getLabelValue())==0)
			{
				fv= (FieldValues) profile_fields.elementAt(i);
				tf= (JTextField) textfieldarray[i-3];
				if(source.isSelected()==true)
				{
					if(fv.getSavedValue().compareTo(tf.getText())!=0 || fv.doesSavedValueExist()==false)
					{
						fv.assignSavedValue(tf.getText());
						tf.setEditable(false);
						textfieldarray[i-3]=tf;
						profile_fields.setElementAt(fv,i);
					}
				}
				else
				{
					if(fv.doesSavedValueExist()==true)
					{
						fv.removeSavedValue();
						tf.setEditable(true);
						textfieldarray[i-3]=tf;
						profile_fields.setElementAt(fv,i);
					}
					else
					{
						tf.setEditable(true);
						textfieldarray[i-3]=tf;
					}
						
				}
			}
			
				
		}
		Vector newvec=p_object.convertToStringVector(profile_fields);
		p_object.writeToFile(newvec,newvec.elementAt(1).toString());
	}
				
						
						
	//handles events for action perfromed.			
	public void actionPerformed(ActionEvent e)
	{
		FieldValues fv;
		//if the destination button is hit;   opens a new frame to browse for directorys.  Then places in text box.
		// user is prompted if value should be saved to the profile. 
		if(e.getSource() == destination_button)
		{
			int returnVal = destination_fc.showDialog(top_panel,"Select Directory"); 
			if (returnVal == JFileChooser.APPROVE_OPTION) 
			{ 
				File file = destination_fc.getSelectedFile(); 
				set_text= file.getAbsolutePath();
                                destinationtext.setText(set_text);
				Far_object.setDestinationFolder(set_text); 
			}
			destinationtext.setCaretPosition(destinationtext.getDocument().getLength());
			light.setIcon(nolight);
			proceed=false;
			fv= (FieldValues) profile_fields.elementAt(2);
			temp= (String) fv.getFieldValue();
			if(temp.compareTo(destinationtext.getText())!=0)
			{
                        	proceed=requestDialog("Destination folder has changed, do you wish to update profile?");
				if(proceed==true)
				{
					fv= new FieldValues(destinationtext.getText());
					profile_fields.set(2,fv);
					p_object.writeToFile(p_object.convertToStringVector(profile_fields),profilename);
				}
			}

			
			
			
		}
	
		//if the get template source button is hit;   opens a new frame to browse for files.  Then places in text box.
		// user is prompted if value should be saved to the profile. 
		else if(e.getSource() == source_button)
		{
			int returnVal = source_fc.showDialog(top_panel,"Select Template"); 
			if (returnVal == JFileChooser.APPROVE_OPTION) 
			{ 
				File file = source_fc.getSelectedFile(); 
				set_text= file.getAbsolutePath();
                                sourcetext.setText(set_text);
                                Far_object.setTemplate(set_text); 
			}
			sourcetext.setCaretPosition(sourcetext.getDocument().getLength());
			light.setIcon(nolight);
			proceed=false;
			fv= (FieldValues) profile_fields.elementAt(1);
			temp=(String) fv.getFieldValue();
			if(temp.compareTo(sourcetext.getText())!=0)
			{	
                        	proceed=requestDialog("Template location has changed, do you wish to update profile?");
				if(proceed==true)
				{
					fv= new FieldValues(sourcetext.getText());
					profile_fields.set(1,fv);
					p_object.writeToFile(p_object.convertToStringVector(profile_fields),profilename);
				}
			}
			
		}

		// if the go button is pressed, fields are sent to Fields and Find and Replace objects to be compared to the template
		// file and swamped in a new file.
		else if(e.getSource() == gobutton)
 		{
			set_text=new String();
			String textarray[]= new String[textfieldarray.length];
			boolean iscomplete=false;
			boolean canget= p_object.canFetchFile(Far_object.getTemplateName());
			if(canget==false)
			{
				errorDialog("Template cannot be be found at Chosen location");
			}
			else
			{
				for(int a=0; a<textfieldarray.length;a++)
				{
					textfield=(JTextField)textfieldarray[a];
					
					set_text= textfield.getText();
					textarray[a]=set_text.trim();
				
			
				}
				
				
			
				Fields.enterFields(textarray);
				
				iscomplete= Far_object.modify(Field_object);
				Fields.nullifyReplacements();
			}
                                
			
			if(iscomplete==true)
                        {
                                light.setIcon(greenlight);
                        }

		}

		//if the clear button is pressed.  All fields (except those that are saved) are cleared
		else if(e.getSource() == clearbutton)
		{
			for(int a=0; a<textfieldarray.length;a++)
			{
				
				textfield=(JTextField)textfieldarray[a];
				if(textfield.isEditable()==true)
				{
					textfield.setText(new String());
				}
				
			
			}
			light.setIcon(nolight);
		}
		// the following allows the different operations in the menus.
		else 
		{
			
			JMenuItem source = (JMenuItem)(e.getSource()); 
			String actionperformed= source.getText();
			// create profile item, opens the create screen
			if(actionperformed.compareTo(createprofile.getText())==0)
			{
				
				CreateGui CG= new CreateGui();
				frame.dispose();
				CG.makeCreateGui(this);
				
				
			}
			// editprofile item, opens the edit screen
			else if(actionperformed.compareTo(editprofile.getText())==0)
			{
				
                                EditGui EG= new EditGui();
				EG.makeGui(profile_fields,this);
				frame.dispose();
				
				
				
			}
			//delete profile item, prompts user for a "are you sure". Checks if profile still exists then deletes
			else if(actionperformed.compareTo(deleteprofile.getText())==0)
			{
				
                                boolean proceed=requestDialog("Are you sure you wish to delete Profile: "+profilename+"?");
				if(proceed==true)
				{
                                        p_object.deleteProfile(profilename);
                                        if(profilelist.isEmpty())
					{
			
						sendDialog("No Profiles Found");
						CreateGui CG= new CreateGui();
						frame.dispose();
						CG.makeCreateGui(this);
					}
					else
					{
						p_object.deleteProfile(profilename);
						
						
						if(p_object.canFetchProfile(p_object.getDefaultProfile())==false)
						{
							sendDialog("No Profiles Found");	
							CreateGui CG= new CreateGui();
							frame.dispose();	
							CG.makeCreateGui(this);
						}
						else 
						{
							frame.dispose();
							profile_fields= p_object.getFile(p_object.getDefaultProfile());
							newTemplateModifier(profile_fields);
						}	
						
					}
					
				}
			}
			// rename profile item,  opens rename dialog box and passes new name to be changed.
			else if(actionperformed.compareTo(renameprofile.getText())==0)
			{
				String results= renameDialog();
				if(results!=null)
				{
					StringTokenizer ST= new StringTokenizer(results," ");
					if(ST.hasMoreTokens()==true)
					{
						p_object.renameProfile(results,profilename);
						frame.dispose();
						profile_fields= p_object.getFile(results);
						newTemplateModifier(profile_fields);
					}
					else
					{
						sendDialog("No value entered, profile was not renamed");
					}
					
				}
				
			}

			// copy profile item, opens dialog box for name of new profile and creates new profile
			else if(actionperformed.compareTo(copyprofile.getText())==0)
			{
				String newname = newNameDialog();
				if(newname!=null)
				{
					boolean isthere= false;
					for(int k=0;k<profilelist.size();k++)
					{
						temp= profilelist.elementAt(k).toString();
						if(temp.compareTo(newname)==0)
						{
							isthere=true;
						}
					}
					if(isthere==false)
					{
						StringTokenizer ST= new StringTokenizer(newname," ");
						if(ST.hasMoreTokens()==true)
						{

							FieldValues newfv= new FieldValues(newname);
							profile_fields.setElementAt(newfv,0);
							Vector newvector= p_object.convertToStringVector(profile_fields);

							p_object.createProfile(newvector);
							frame.dispose();
							profile_fields= p_object.getFile(newname);
							newTemplateModifier(profile_fields);
						}
						else
						{
							sendDialog("No value entered, profile was not copied");
						}
					}
					else
					{
						sendDialog("New profile name already exists");
					}	
					
				}
			}
							

			// opens Help topics screen
			else if(actionperformed.compareTo(helptopics.getText())==0)
			{
				HelpTopics HT= new HelpTopics();
			}
			// opens about TemplateModifier dialog
			else if(actionperformed.compareTo(about.getText())==0)
			{
				aboutDialog();
			}

			// the following indicates event handeling for submenus, each will have a list of profiles available	
			else
			{
				JMenuItem checkitem;
				
				Component[] loadarray= loadprofile.getMenuComponents();
				// checks if items in the load profile menu are selected, if so loads profile
                                for(int a=0; a<loadarray.length;a++)
				{
                                        checkitem= (JMenuItem) loadarray[a];
                                        if(source.equals(checkitem))
					{
						
                                                boolean proceed= p_object.canFetchProfile(source.getText());
						if(proceed==true)
						{
							profile_fields= p_object.getFile(source.getText());
							frame.dispose();
							newTemplateModifier(profile_fields);
							
						}
						else
						{
							errorDialog("This Profile has been removed");
							p_object.deleteProfile(source.getText());
							frame.dispose();
							
							newTemplateModifier(p_object.convertToStringVector(profile_fields));
						}
					}
				}
				// checks if items from the set default profile menu is selected.  If so then sets it as the new default profile
				Component[] setuparray= setdefaultprofile.getMenuComponents();
                                for(int b=0; b<setuparray.length;b++)
				{
                                        checkitem= (JMenuItem) setuparray[b];
                                        if(source.equals(checkitem))
					{
                                                if(profilelist.isEmpty())
						{
							sendDialog("No Profiles Found");
							
						}
						else
						{
							boolean isthere=p_object.canFetchProfile(source.getText());
							if(isthere==false)
							{
								errorDialog("This Profile has been removed");
								p_object.deleteProfile(source.getText());
								frame.dispose();
								newTemplateModifier(p_object.convertToStringVector(profile_fields));
							}
							else
							{
								p_object.setDefaultProfile(source.getText());
								frame.dispose();
								newTemplateModifier(p_object.convertToStringVector(profile_fields));
							}
						}
					}
				}
				
			}
			
		}
		
			
			
	}

	// returns the profile object.
	public Profile getProfileObject()
	{
		return p_object;
	}
	
	// creates the GUI and frame
	public static void createAndShowGUI()
	{
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true); 

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			
		}
		
		JComponent newContentPane = new TemplateModifier();
		newContentPane.setOpaque(true);
		
	}

	// starts the program and thread for the GUI
	public static void main(String[] args) 
	{ 
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() 
		{ 
			public void run() 
			{ 
				createAndShowGUI(); 
			} 
		}); 
	} 	
		
}
	
		
