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



// this class creates the create GUI for the TemplateModifier program

class CreateGui extends JPanel implements ActionListener
{
	JFrame createframe;// frame for the create Gui
	JPanel create_main_panel,create_top_panel,create_middle_one_panel,create_middle_two_panel,create_bottom_panel,logo_panel;
	//create_main_panel: content pane for the frame
	// create_top_panel: holds text field for instructions
	//create_middle_one_panel: holds2 text fields for entering new fields and labels
	//create_middle_two_panel: button for entering fields
	// create_bottom_panel: holds a TextArea for new items
	//logo_panel: panel for the logo
	JTextField createfield,createlabelfield;// createfield: textfield to input new fields
	JTextArea createarea;// createarea: shows updates on the create screen 
	JLabel createtext,createsteps,createlabel; // createtext: label for the enter new field
	JButton finish_button, nextfield_button,cancel_button;//finish_button: update profile- createleft panel
	// nextfield_button: enter new field- create left panel

	Vector temp_create_vector=new Vector(0,1);// used in the creation of profiles
	int createtimer=0; // timer will track the steps for creation of a profile
	TemplateModifier TM;// instance of the templateModifier object
	String set_text= new String();//  used for a blank string, solves null pointer exceptions.

	//constructor
	public CreateGui()
	{}
	
	// setups the frame and panels
	public void makeCreateGui(TemplateModifier T)
	{
		TM=T;	

		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true); 

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			
		}
		// creating the frame
		createframe= new JFrame("Creating new Profile");
		createframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
                createframe.setSize(new Dimension(420,400));  
		
		//creating the Panels
		logo_panel=new JPanel();
		create_main_panel=new JPanel();
		create_main_panel.setLayout(new BoxLayout(create_main_panel, BoxLayout.Y_AXIS));
		create_top_panel=new JPanel();
		
		create_middle_one_panel= new JPanel();
		create_middle_two_panel= new JPanel();
		create_bottom_panel= new JPanel();
		create_bottom_panel.setLayout(new BoxLayout(create_bottom_panel, BoxLayout.Y_AXIS));
		
		//assigning panels
		create_main_panel.add(logo_panel);
		create_main_panel.add(create_top_panel);
		create_main_panel.add(create_middle_one_panel);
		create_main_panel.add(create_middle_two_panel);
		create_main_panel.add(create_bottom_panel);
		createframe.getContentPane().add(create_main_panel, BorderLayout.CENTER);

		//setups up components
		addLogoComponents();
		addCreateComponents();
		create_main_panel.setOpaque(true);
		create_main_panel.setPreferredSize(new Dimension(600,500));
		
		createframe.getRootPane().setDefaultButton(nextfield_button);
		createframe.pack(); 
		createframe.setVisible(true); 

		
	}
	
	//installs the logo components
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
			TM.errorDialog("Logo not found");
		}
	}

	// creates the components for the panels,  fields, textareas, cancel , next field and finsihed buttons  
	private void addCreateComponents()
	{
		createsteps= new JLabel("STEP 1: Please enter name of profile ");
		createsteps.setFont(new Font("Serif", Font.PLAIN, 20));

		create_top_panel.add(createsteps);
		createfield= new JTextField(10);
		createtext=new JLabel("Enter new Fields Here:",JLabel.TRAILING);
		createtext.setLabelFor(createfield);
		create_middle_one_panel.add(createtext);
		create_middle_one_panel.add(createfield);
		createlabel= new JLabel("                                            ");
		createlabelfield= new JTextField(10);
		createlabel.setLabelFor(createlabelfield);
		create_middle_one_panel.add(createlabel);
		create_middle_one_panel.add(createlabelfield);
		createlabelfield.setEditable(false);	
		
		nextfield_button = new JButton("Enter New Field"); 
		nextfield_button.addActionListener(this); 
		create_middle_two_panel.add(nextfield_button);
		JLabel newlabel= new JLabel(" ");
		create_bottom_panel.add(newlabel);
		
		
		

		createarea= new JTextArea(20,30);
		
		create_bottom_panel.add(createarea);  
		
		JScrollPane scrollPane = new JScrollPane(createarea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
		createarea.setEditable(false);
		
		
		
		
		create_bottom_panel.add(scrollPane);
	
		finish_button = new JButton("Finished"); 
		finish_button.addActionListener(this); 
		create_bottom_panel.add(finish_button);
		create_bottom_panel.add(new JLabel(" "));  
		cancel_button= new JButton("Cancel");
		cancel_button.setVerticalTextPosition(AbstractButton.CENTER); 
		cancel_button.addActionListener(this); 
		create_bottom_panel.add(cancel_button); 
	}

	// handels action events
	public void actionPerformed(ActionEvent e)
	{
		String temp;
		String resourcesfilename= "resourcesForTemplateModifier";
		//if the next field button is pressed,  for the first time it is pressed;  assigns name of profile.  then adds the new fields
		if(e.getSource() == nextfield_button)
		{
			
			if(createfield.getText().length()== 0)
			{
				TM.sendDialog("Field cannot have a null value");
			}
			
							
			else if(createtimer==0)
			{
				Profile checkp= new Profile();
				boolean checkwf=checkp.checWriteToFile(createfield.getText());
				
				if(resourcesfilename.compareTo(createfield.getText())==0)
				{
					TM.sendDialog("The name resourcesForTemplateModifier is a reserved file name in TempateModifier, please rename your profile");
				}
				else if(checkwf==false)
				{
					TM.sendDialog("The name of the Profle is invalid, please rename using valid filename characters");
				}
				else
				{
				
					createsteps.setText("Enter The first Field, whose substitution will be the name of the new text file");
					createlabel.setText("If you wish to use labels please enter here:");
					createlabelfield.setEditable(true);	
					temp_create_vector.addElement(createfield.getText());
					temp_create_vector.addElement(createfield.getText());
					temp_create_vector.addElement("noFieldEntered");
					temp_create_vector.addElement("noFieldEntered");
					temp_create_vector.addElement("noFieldEntered");
					temp_create_vector.addElement("noFieldEntered");
					createtimer++;
				}
			}
			else
			{
				createsteps.setText("Enter Next Field, when done press finish");
				temp_create_vector.addElement(createfield.getText());
				temp= createlabelfield.getText();
				if(temp.length()==0)
				{
					temp_create_vector.addElement(createfield.getText());
				}
				else
				{
					temp_create_vector.addElement(temp);
				}
			}
			createfield.setText(set_text);
			createlabelfield.setText(set_text);
			try
			{
				createarea.setText("Profile Name: "+temp_create_vector.elementAt(0).toString()+"\n");
			}
			catch(ArrayIndexOutOfBoundsException aioobe)
			{}
			for(int a=6; a<temp_create_vector.size(); a++)
			{
				
				createarea.append(temp_create_vector.elementAt(a).toString()+" With label set as: "+temp_create_vector.elementAt(a+1).toString()+"\n");
				a++;
				
			}
		}

		//if the finish button is pressed creates new profile and opens the main frame with new profile
		else if(e.getSource() == finish_button)
		{
			Profile newp=  TM.getProfileObject();
			//  prompts user if no profiles exist
			if(temp_create_vector.isEmpty())
			{
				if(TM.profile_fields.isEmpty()==true)
				{
					TM.sendDialog("Main screen cannot be accessed until a new Profile is created");
				}
				else
				{
					TM.sendDialog("No Entries made, new profile was not created");
					createframe.dispose();
					TM.newTemplateModifier(newp.convertToStringVector(TM.profile_fields));
				}
			}
			else
			{
				Vector editedvector= new Vector(0,1);
				
				for(int a=0; a<temp_create_vector.size(); a++)
				{
					if(a%2==0)// every third item must be a N
					{
						editedvector.addElement("N");
					}
					editedvector.addElement(temp_create_vector.elementAt(a));
				}
			       	boolean exists= newp.canFetchProfile(editedvector.elementAt(1).toString());
				boolean proceed= true; // if profile already exists, prompts user to over write it
				if(exists==true)
				{
					proceed= TM.requestDialog("Profile already exists, do you wish to overwrite existing Profile?");
						
				}
				
				
				if(proceed== true)
				{
					newp.createProfile(editedvector);
					createframe.dispose();
					TM.newTemplateModifier(editedvector);
				}
				else	
				{
					createframe.dispose();
					TM.newTemplateModifier(newp.convertToStringVector(TM.profile_fields));	
		
				}				
				
			}
			
			
		}
		// cancel_button is pressed,   returns to main screen with no new created profiles.
		else if(e.getSource() == cancel_button)
		{
					TM.sendDialog("Operation cancelled, No Profile created");
					createframe.dispose();
					Profile newp= TM.p_object;
					TM.newTemplateModifier(newp.convertToStringVector(TM.profile_fields));	
		}
	}
}