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



/** this class creates a separate window connected to Template Modifier 
 * and deals with Editing profiles
 */

class EditGui extends JPanel implements ActionListener
{
	

	JFrame editframe;// frame for the edit gui
        JPanel logo_panel, edit_panel_one_top,edit_panel_one_top2,edit_panel_main, edit_panel_one_bottom,edit_panel_one_middle,edit_panel_two;
	JTextField editfield,editlabelfield,oldfields;// field for entering new fields
	JTextArea editinfotext,editarea;// editinftext shows information, editarea gives current 
	JLabel edittext,editsteps,editlabel; //edittext label for add new field, 
	// create steps and editsteps changable instructions
	JButton editfield_button, editdone_button,editnextstage_button,update_button,cancel_button;
	//editfield_button: enter new field- edit left panel
	// edit_done_button: finish editing- edit two panel
	// editnextstage_button:  finishes first part of editing and starts the second
	
	JRadioButton rbitem;//radio button to choose which field will be the main field- edit two panel
	ButtonGroup group;// button group for radio buttons- edit two panel
	String set_text= new String();// used for blanking text fields, eliminates null pointer problem
	Vector CBox= new Vector(0,1); // Vector of CheckBox Items
	Vector temp_edit_vector= new Vector(0,1);// used in editing for profiles
	Vector RBut= new Vector(0,1);// Vector of RadioButtons
	ImageIcon greenlight,nolight;// image icons for a green light and an off light
	Vector fullprofile= new Vector(0,1);// lists ALL the list in the profile
	JScrollPane edit_scroll_top2, edit_scroll_two;  // scroll panes for the edit_panel_top and edit_panel_two
	TemplateModifier TM; // TemplateModifier Object
	JTextField[][] oldtextfield; // holds olds label and field values.

	// constructor
	public void EditGui()
	{}

	// creates the frame and panels. reads in the profile to be edited
	public void makeGui(Vector profile,TemplateModifier T)
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
			System.err.println(e);
		}
		//set up the frame
		fullprofile= profile;
		
		for(int a=3; a<fullprofile.size(); a++)
		{
			temp_edit_vector.addElement(fullprofile.elementAt(a));
		}
		FieldValues fv= (FieldValues) fullprofile.elementAt(0);	
                editframe= new JFrame("Editing "+fv.getFieldValue());
		editframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
                editframe.setSize(new Dimension(100,200));  

		//set up the panels
		edit_panel_main=new JPanel();
		edit_panel_main.setLayout(new BoxLayout(edit_panel_main, BoxLayout.Y_AXIS));
		logo_panel=new JPanel();
		edit_panel_one_top=new JPanel();
		edit_panel_one_top.setLayout(new BoxLayout(edit_panel_one_top, BoxLayout.Y_AXIS));
		edit_panel_one_top2= new JPanel(new SpringLayout());
		edit_panel_one_middle = new JPanel();
		edit_panel_one_bottom=new JPanel(new GridBagLayout());
		edit_panel_two=new JPanel(new BoxLayout(edit_panel_two, BoxLayout.Y_AXIS));
		edit_panel_two.setLayout(new BoxLayout(edit_panel_two, BoxLayout.Y_AXIS));
		// adding panels to the main panel
		edit_panel_main.add(logo_panel);
		edit_panel_main.add(edit_panel_one_top);
		edit_panel_main.add(edit_panel_one_top2);
		
		edit_scroll_top2= new JScrollPane(edit_panel_one_top2,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
		edit_panel_main.add(edit_scroll_top2);
		edit_panel_main.add(edit_panel_one_middle);
		
		//edit_scroll_top.setPreferredSize(new Dimension(250,250));
		
		edit_panel_main.add(edit_panel_one_bottom);
		edit_panel_main.add(edit_panel_two);
		edit_scroll_two= new JScrollPane(edit_panel_two,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
		//edit_scroll_two.setPreferredSize(new Dimension(250,150));
		edit_panel_main.add(edit_scroll_two);

		editframe.getContentPane().add(edit_panel_main, BorderLayout.CENTER);
		// assign components
		addLogoComponents();
		addEditComponents();
                
		
		edit_panel_main.setOpaque(true);
               
                editframe.getRootPane().setDefaultButton(editfield_button);
		editframe.pack(); 
		editframe.setVisible(true); 
		edit_panel_two.setVisible(false);
		edit_scroll_two.setVisible(false);
	}
	// sets up the logo
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
	
	/** sets up the components for the first edit screen such as a selection of the fields and labels, 
	 *	enter field text and button, a text area to show changes with a cancel, update, enter field and next stage buttons
	 *  makes use of SpringUtilities tool and resizes panels depending on the number of fields.  
	 *    The panel can hold 12 before the scroll bar is needed
	 */
	private void addEditComponents()
	{
		
		editsteps= new JLabel("Untick the fields you wish to remove and enter new fields.  When done press Next Stage", SwingConstants.CENTER);
		
		editsteps.setFont(new Font("Serif", Font.PLAIN, 20));
		edit_panel_one_top.add(editsteps);
		FieldValues fv;
		JLabel l;
		int numPairs= temp_edit_vector.size();
		oldtextfield= new JTextField[numPairs][numPairs];
		int count=1;
                for(int a=0; a<numPairs; a++)
		{	
			fv= (FieldValues) temp_edit_vector.elementAt(a);
			l=new JLabel("Field: "+count);
			oldfields= new JTextField(10);
			l.setLabelFor(oldfields);
			oldfields.setText(fv.getFieldValue());
			oldfields.setEditable(true);
			edit_panel_one_top2.add(l);
			edit_panel_one_top2.add(oldfields);
			oldtextfield[a][0]=oldfields; 
			
			oldfields= new JTextField(10);
			l=new JLabel("Label: "+count);
			l.setLabelFor(oldfields);
			oldfields.setText(fv.getLabelValue());
			oldfields.setEditable(true);
			edit_panel_one_top2.add(l);
			edit_panel_one_top2.add(oldfields);
			oldtextfield[a][1]=oldfields;
			count++; 
			
			
		}
		
		SpringUtilities.makeCompactGrid(edit_panel_one_top2, numPairs, 4, 6, 6, 6, 6); 
		int value=1;
		if(numPairs==1)
		{
			edit_scroll_top2.setPreferredSize(new Dimension(40,40));
		}
		else if(numPairs > 1 && numPairs <= 12)
		{
			value= numPairs-value;
			value=  50+value*25;
			edit_scroll_top2.setPreferredSize(new Dimension(40,value));
		}
		else 
		{
			edit_scroll_top2.setPreferredSize(new Dimension(40,325));
		}
		
		editfield= new JTextField(10);
		edittext=new JLabel("Enter new Fields Here:");
		edittext.setLabelFor(editfield);
		edit_panel_one_middle.add(edittext);
		edit_panel_one_middle.add(editfield);
		editlabelfield = new JTextField(10);
		editlabel = new JLabel("If you wish to use labels please enter here:");
		edit_panel_one_middle.add(editlabel);
		edit_panel_one_middle.add(editlabelfield);
		

		

		editfield_button = new JButton("Enter New Field");
		editfield_button.setVerticalTextPosition(AbstractButton.CENTER); 
		editfield_button.addActionListener(this); 
		edit_panel_one_middle.add(editfield_button);
		
		
		
		edit_panel_one_middle.add( new JLabel(" "));
		
		
		editinfotext= new JTextArea(10,10);
		

		
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;  
		c.fill = GridBagConstraints.HORIZONTAL; 
		edit_panel_one_bottom.add(editinfotext, c);  
		
		JScrollPane scrollPane = new JScrollPane(editinfotext, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
		editinfotext.setEditable(false);
		fv= (FieldValues) fullprofile.elementAt(0);
                editinfotext.setText("Profile: "+fv.getFieldValue()+"\n");
                for(int b= 0; b<temp_edit_vector.size(); b++)
		{
			fv= (FieldValues) temp_edit_vector.elementAt(b);
			editinfotext.append(fv.getFieldValue()+", with label:"+fv.getLabelValue()+"\n");
		}
		
		c.fill = GridBagConstraints.BOTH; 
		c.weightx = 1.0; 
		c.weighty = 1.0; 
		edit_panel_one_bottom.add(scrollPane,c); 
		editnextstage_button= new JButton("Next Stage");
		editnextstage_button.addActionListener(this); 
		edit_panel_one_bottom.add(editnextstage_button);
		update_button= new JButton("Update");
		update_button.addActionListener(this); 
		edit_panel_one_bottom.add(update_button);
		cancel_button= new JButton("Cancel");
		cancel_button.setVerticalTextPosition(AbstractButton.CENTER); 
		cancel_button.addActionListener(this); 
		edit_panel_one_bottom.add(cancel_button);

		
		
	}
	// sets up the components for the second edit screen;  Holds  radio buttons for each field with a Done button.
	private void addEditExtraComponents()
	{	
		group= new ButtonGroup();
                
		FieldValues fv;
		JLabel newlabel= new JLabel("Select Main Field \n whose substition will be the name \n of the new text file",JLabel.TRAILING);
		newlabel.setFont(new Font("Serif", Font.PLAIN, 20));
		edit_panel_two.add(newlabel);
		for(int a=0; a<temp_edit_vector.size(); a++)
		{
			fv= (FieldValues) temp_edit_vector.elementAt(a);
			rbitem= new JRadioButton(fv.getFieldValue()+", With Label: "+fv.getLabelValue());
			RBut.addElement(rbitem);
			rbitem.setMnemonic(KeyEvent.VK_C);
                        rbitem.setActionCommand(fv.getFieldValue()+", With Label: "+fv.getLabelValue());
			if(a==0)
			{
				rbitem.setSelected(true);
			}
			else
			{
				rbitem.setSelected(false);
			}
			rbitem.addActionListener(this);
			group.add(rbitem);
			edit_panel_two.add(rbitem);
		}
		editdone_button = new JButton("Done"); 
		editdone_button.addActionListener(this); 
		edit_panel_two.add(editdone_button);
		edit_panel_two.add(new JLabel(" "));
		cancel_button= new JButton("Cancel");
		cancel_button.setVerticalTextPosition(AbstractButton.CENTER); 
		cancel_button.addActionListener(this); 
		edit_panel_two.add(cancel_button);
		
	}


	// handels action events
	public void actionPerformed(ActionEvent e)
	{
		FieldValues fv;
		String tempstring,temp;
		// if editfield_button is pressed; creates a new entry and adds to the profile, updates the text area
		if(e.getSource() == editfield_button)
		{
			computeOldFields();
                       
			
			if(editfield.getText().length() != 0)
			{
				fv= new FieldValues(editfield.getText());
				fv.assignLabelValue(editlabelfield.getText());
				temp_edit_vector.addElement(fv);
							
				editfield.setText(set_text);
				editlabelfield.setText(set_text);	
				
			}
			else
			{
				TM.sendDialog("Field cannot have a null value");
			}
			for(int b= 0; b<temp_edit_vector.size(); b++)
			{
				fv= (FieldValues) temp_edit_vector.elementAt(b);
				tempstring= fv.getFieldValue();
				
				if(tempstring.length()!=0)
				{
					if(fv.getLabelValue().length() !=0)
					{
						editinfotext.append(fv.getFieldValue()+", With Label: "+fv.getLabelValue()+"\n");
					}
					else
					{
						editinfotext.append(fv.getFieldValue()+", With Label: "+fv.getFieldValue()+"\n");
					}
				}
			}
		}
		// nextstage button is pressed;  takes out any null values, takes away the first edit screen and sets up the second.
		else if(e.getSource() == editnextstage_button)
		{
			computeOldFields();
			int h= 0;
			do
			{
			
				fv= (FieldValues) temp_edit_vector.elementAt(h);
				tempstring= (String) fv.getFieldValue();
				
				if(tempstring.length()==0)
				{
					
					temp_edit_vector.removeElementAt(h);
				}
				else if(fv.getLabelValue().length() ==0)
				{
					
					fv.assignLabelValue(fv.getFieldValue());
					temp_edit_vector.setElementAt(fv, h);
					h++;
				}
				else
				{
					h++;		
				}
			}
			while(temp_edit_vector.size()>h);
			
			addEditExtraComponents();
			edit_panel_two.setVisible(true);
			edit_scroll_two.setVisible(true);
			edit_panel_one_top.setVisible(false);
			edit_panel_one_top2.setVisible(false);
			edit_panel_one_middle.setVisible(false);
			edit_panel_one_bottom.setVisible(false);
			edit_scroll_top2.setVisible(false);
		}
		// update button is pressed; updates the profile and the text area
		else if(e.getSource() == update_button)
		{
			computeOldFields();
						
				
			for(int b= 0; b<temp_edit_vector.size(); b++)
			{
				fv= (FieldValues) temp_edit_vector.elementAt(b);
				tempstring= fv.getFieldValue();
				if(tempstring.length()!=0)
				{
					if(fv.getLabelValue().length() !=0)
					{
						editinfotext.append(fv.getFieldValue()+", With Label: "+fv.getLabelValue()+"\n");
					}
					else
					{
						editinfotext.append(fv.getFieldValue()+", With Label: "+fv.getFieldValue()+"\n");
					}
				}
			}
			


		}
			
		// done button is pressed:   Finds the selection of the radio buttons for the main field.
		//  places fieldValue on the top of the fields and edits the profile file.,  returns to main screen with updated version
		else if (e.getSource() == editdone_button)
		{
			Vector tempvector= new Vector(0,1);
			
			
			JRadioButton temprb= new JRadioButton();
			
			for(int b=0; b<RBut.size(); b++)
			{
                                temprb= (JRadioButton) RBut.elementAt(b);
				if(temprb.isSelected())
				{
                                        tempvector.addElement(fullprofile.elementAt(0));
                        		tempvector.addElement(fullprofile.elementAt(1));
                        		tempvector.addElement(fullprofile.elementAt(2));
					for(int h=0; h<temp_edit_vector.size();h++)
					{
						fv= (FieldValues) temp_edit_vector.elementAt(h);
						if(temprb.getText().compareTo(fv.getFieldValue()+", With Label: "+fv.getLabelValue())==0)
						{
							tempvector.addElement(temp_edit_vector.elementAt(h));
						}
					}
                                        RBut.removeElementAt(b);
				}		
			}
			
                        for(int c=0; c<RBut.size(); c++)
			{
				for(int j=0; j<temp_edit_vector.size();j++)
				{
					temprb= (JRadioButton) RBut.elementAt(c);
					fv= (FieldValues) temp_edit_vector.elementAt(j);
					if(temprb.getText().compareTo(fv.getFieldValue()+", With Label: "+fv.getLabelValue())==0)
					{
						tempvector.addElement(temp_edit_vector.elementAt(j));
					}
				}
			}
			Profile newp=  TM.getProfileObject();
			Vector tempv= newp.convertToStringVector(tempvector);
			newp.writeToFile(tempv, tempv.elementAt(1).toString());
			editframe.dispose();
			TM.newTemplateModifier(tempv);
				
			
			
		}
		//  cancel button is pressed;  returns to main screen with no changes being made
		else if(e.getSource() == cancel_button)
		{
					TM.sendDialog("Operation cancelled, No Changes made");
					editframe.dispose();
					Profile newp= TM.p_object;
					TM.newTemplateModifier(newp.convertToStringVector(TM.profile_fields));	
		}
		
		else
		{}
		editinfotext.setCaretPosition(editinfotext.getDocument().getLength());

	}
	/** common object tool to check that the old values exist or has been changed.  
	 * removes null values being left in the profile and removes labels that are attacted 
	 * 	to null field values
	 */
	private void computeOldFields()
	{
		String temp, tempstring;
		FieldValues fv;
		fv= (FieldValues) fullprofile.elementAt(0);
                editinfotext.setText("Profile: "+fv.getFieldValue()+"\n");
			
		boolean fieldstillthere;
		boolean labelstillthere;
		for( int c=0; c<oldtextfield.length; c++)
		{
			for(int d=0;d<2;d++)
			{
				fieldstillthere=false;
				labelstillthere=false;
				oldfields = (JTextField) oldtextfield[c][d];
				tempstring= oldfields.getText();
		
				if(d==0)
				{
					fv= (FieldValues) temp_edit_vector.elementAt(c);
					
					if(tempstring.compareTo(fv.getFieldValue())==0)
					{
						fieldstillthere=true;
					}
					
				}
				else
				{
					fv= (FieldValues) temp_edit_vector.elementAt(c);
					if(tempstring.compareTo(fv.getLabelValue())==0)
					{
						labelstillthere=true;
					}
				}
					
						
				if(fieldstillthere==false && d==0)
				{
					fv= new FieldValues(tempstring);
					oldfields= (JTextField) oldtextfield[c][1];
					fv.assignLabelValue(oldfields.getText());
					temp_edit_vector.setElementAt(fv,c);
				}
					
				if(labelstillthere==false && d==1)
				{
					oldfields= (JTextField) oldtextfield[c][0];
					temp= oldfields.getText();
					for(int k=0;k<temp_edit_vector.size();k++)
					{
						fv= (FieldValues) temp_edit_vector.elementAt(k);
						if(temp.compareTo(fv.getFieldValue())==0)
						{
							if(tempstring.length() ==0)
							{
								fv.assignLabelValue(fv.getFieldValue());
							}
							else
							{
								fv.assignLabelValue(tempstring);
							}
							temp_edit_vector.setElementAt(fv,k);
						}
					}
				}
				fieldstillthere=false;
				labelstillthere=false;
			}
		}
	}
}
			
		
	
