import java.io.*;
import java.util.*;
import java.lang.*;
import java.awt.Font.*;
import java.awt.GraphicsEnvironment.*;

import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*; 
import java.awt.GridBagLayout.*;
import java.awt.Component; 
import java.awt.Container;
import java.awt.Insets; 
import javax.swing.BoxLayout;
import java.awt.color.*;




/** This class creates a separate frame displaying the help topics for the Template Modifier Application
*/

class HelpTopics extends JPanel
{

	JFrame helpframe;// frame for the HelpTopics GUI
	JPanel help_main_panel, help_top_panel, help_bottom_panel; 
	// help_main_panel: main panel for the frame
	// help_top_panel holds a label about the text area
	// help_bottom_panel holds the help text area
	JTextArea helptext;// helptext displays the readme.txt file
	JLabel help; // help label for the text area
	
	// constructor, setsup the frame and panels
	public HelpTopics()
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
 		// create frame
		helpframe= new JFrame("Template Modifier");
		
		helpframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); 
                helpframe.setSize(new Dimension(250,150));
		// create panels 
		help_main_panel= new JPanel();
		help_main_panel.setLayout(new BoxLayout(help_main_panel, BoxLayout.Y_AXIS));
		help_top_panel= new JPanel();
		help_bottom_panel= new JPanel(new GridBagLayout()); 

		help_main_panel.add(help_top_panel);
		help_main_panel.add(help_bottom_panel);
		helpframe.getContentPane().add(help_main_panel, BorderLayout.CENTER);
		// assign components.
		addComponents();
		help_main_panel.setOpaque(true);
		
		helpframe.pack(); 
		helpframe.setVisible(true); 
	}

	//  sets up the components,  a label and text area of the information form readme.txt
	private void addComponents()
	{
		help= new JLabel("Template Modifier Overview",JLabel.CENTER);
		
		
		help.setFont(new Font("Serif", Font.BOLD,30));
		help_top_panel.add(help);
		
		try
		{
			
			helptext= new JTextArea(50,100);
			GridBagConstraints c = new GridBagConstraints();
			c.gridwidth = GridBagConstraints.REMAINDER;  
			c.fill = GridBagConstraints.HORIZONTAL; 
			help_bottom_panel.add(helptext, c);  
			helptext.setLineWrap(true);
			helptext.setWrapStyleWord(true);
			JScrollPane scrollPane = new JScrollPane(helptext, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
			helptext.setEditable(false);
			scrollPane.setWheelScrollingEnabled(true);
			
			c.fill = GridBagConstraints.BOTH; 
			c.weightx = 1.0; 
			c.weighty = 1.0; 
			help_bottom_panel.add(scrollPane, c); 

			BufferedReader infile= new BufferedReader(new FileReader("readme.txt"));
			do
			{
				helptext.append(infile.readLine()+"\n");
			}
			while(infile.ready()==true);
			infile.close();
			helptext.setCaretPosition(0);
		
		}
		catch(IOException ioe)
		{
			helptext.setText("readme.txt not found");
		}
	}
}
		
		
		
