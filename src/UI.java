import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The following class runs the UI for users, depending on user login
 * 
 * @author Team 4
 * @version June 17 2014
 */
public class UI {
	private static String usertype; // record the type of user here
	/* Use these fields for our application defaults */
	static String termSel = Term.FALL.toString();
	static String fieldSel = FieldOfStudy.ARTS.toString();
	static String fundingSel = "Yes";
	/* Use to record current users username */
	static String userName = null;
	static int count = 0;
	/* Keep track of some important information when AdminOffice is validating or rejecting applications */
	static String IDSel;
	static String Items[];
	static int IDget;
	static ArrayList<ArrayList> r;
	static boolean showAgain = true;
	
	/**
	 * Method used to simulate the Carleton Central Login
	 * 
	 * @throws IOException
	 * 
	 * @since June 16 2014
	 * 
	 * Latest Change: Added lock user out after 5 fails
	 * @version June 17 2014
	 */
	private static void createLoginFrame() throws IOException
	{
		JFrame guiFrame = new JFrame(); // the frame used to display our GUI

		JPanel userPanel = new JPanel(); // the panel used to hold our username and password fields
		userPanel.setLayout(new GridLayout(2,2));

		JLabel usernameLbl = new JLabel("Username:");
		JLabel passwordLbl = new JLabel("Password:");
		JTextField username = new JTextField();
		JPasswordField passwordFld = new JPasswordField();
		
		/* Add our labels and username and password fields to our GUI */
		userPanel.add(usernameLbl);
		userPanel.add(username);
		userPanel.add(passwordLbl);
		userPanel.add(passwordFld);

		//This will center the JFrame in the middle of the screen
		guiFrame.setLocationRelativeTo(null);
		guiFrame.setVisible(true);

		//As the JOptionPane accepts an object as the message
		//it allows us to use any component we like - in this case 
		//a JPanel containing the dialog components we want
		int input = JOptionPane.showConfirmDialog(guiFrame, userPanel, "Enter your password:"
				,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (input != 0) { // if the user hits cancel
			System.out.println("User has hit cancel"); // DEBUG PRINT (DO NOT REMOVE)
			guiFrame.dispose();
			createCCFrame();
			return;
		}
		/* Retrieve what the user has entered in */
		String pass = new String(passwordFld.getPassword());
		String user = username.getText();

		boolean authenticated = authenticate(user, pass); // authenticate our username and password entered

		System.out.println(user); // DEBUG PRINT (DO NOT REMOVE)
		System.out.println(pass); // DEBUG PRINT (DO NOT REMOVE)

		if (authenticated) { 
			userName = user; // store current user logged in
			System.out.println("The user has been authenticated"); // DEBUG PRINT (DO NOT REMOVE)
			System.out.println("The user is a(n): " + usertype); // DEBUG PRINT (DO NOT REMOVE)
			guiFrame.dispose();

			createUserFrame(usertype); // spawn user frame depending on usertype
		}
		else { // we failed to authenticate the user based on credentials entered
			count++;
			if(count != 5) {
				System.out.println("You have failed to authenticate"); // DEBUG PRINT (DO NOT REMOVE)
				System.out.println("You have " + (5-count) + " trie(s) remaining, after that you will be locked out"); // DEBUG PRINT (DO NOT REMOVE)
				guiFrame.dispose();
				createLoginFrame(); // get them to try and login again
			}
			else {
				System.out.println("You have failed to authenticate 5 times, please contact the system admin for help, you will no longer be able to login from this IP"); // DEBUG PRINT (DO NOT REMOVE)
				guiFrame.dispose();
			}

		}
	}

	/**
	 * The following method will create a GUI for the AdminOffice member depending on the applications fitting their filter selection
	 * 
	 * @param applications the applications which fit their filter
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 * @since June 17 2014
	 * 
	 * Latest Change: Added Buttons to change the state of the application
	 * @version June 17 2014
	 */
	private static void createAdminOfficeView(ArrayList<ArrayList> applications) throws FileNotFoundException, IOException {
		final JFrame appFrame = new JFrame(); // frame used to display GUI
		r = applications; // store all the applications we are going to display globally
		
		String[] IDs = new String[applications.size()]; // hold the IDs of all the applications in string format
		int[] ID = new int[applications.size()]; // hold the IDs of all the applications in int format
		
		JPanel pane = new JPanel(); // panel to store all other panels and GUI components
		pane.setLayout(new GridLayout(1,4));
		
		JPanel pane2 = new JPanel(); // panel to hold the application currently selected
		pane2.setLayout(new GridLayout(6,2));
		
		JPanel pane3 = new JPanel(); // panel to hold the buttons to validate or reject an application
		pane3.setLayout(new GridLayout(2,1));
		
		final JButton validate = new JButton("Validate");
		final JButton incomplete = new JButton("Incomplete");
		
		validate.addActionListener(new ActionListener() {
			/* Define what happens when the validate button is pushed */
			public void actionPerformed(ActionEvent e) {
				showAgain = false; // don't re create GUI multiple times for AdminOffice view
				appFrame.dispose();
				ArrayList<Object> arr = new ArrayList<Object>();
				try {
					arr = ApplicationDB.getApplicationbyID(IDget);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				arr.set(1, ApplicationState.ADMINREVIEWED.toString()); // change the state of the application the user has selected
				try {
					ApplicationDB.replacebyID(IDget, arr); // replace it with new state
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					createAdminOfficeView(r); // open up the view again with the update application
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		incomplete.addActionListener(new ActionListener() {
			/* Define what happens when user hits incomplete button */
			public void actionPerformed(ActionEvent e) {
				showAgain = false; // set the GUI to display again for AdminOffice view to false
				appFrame.dispose();
				ArrayList<Object> arr = new ArrayList<Object>();
				try {
					arr = ApplicationDB.getApplicationbyID(IDget);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				arr.set(1, ApplicationState.OPEN.toString()); // set back to open
				try {
					ApplicationDB.replacebyID(IDget, arr); // replace the state
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				for(int i = 0; i < r.size(); i++) {
					if((Integer)r.get(i).get(0) == IDget) {
						r.remove(i); // remove the application from AdminOffice view as it is opened again
					}
				}
				if (r.size() == 0) {
					System.out.println("No applications available"); // don't display GUI with applications if not applications left
					try {
						createUserFrame(usertype);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else {
				try {
					createAdminOfficeView(r); // create the application view if more applications still available
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
			}
		});
		/* Disable buttons and add then to panel3 */
		validate.setEnabled(false);
		incomplete.setEnabled(false);
		pane3.add(validate);
		pane3.add(incomplete);
		
		/* Set the panel to display our application selected */
		JLabel stateLabel = new JLabel("Application State");
		JLabel dateLabel = new JLabel("Last Updated");
		JLabel nameLabel = new JLabel("Applicant Name");
		JLabel termLabel = new JLabel("Term selected");
		JLabel fosLabel = new JLabel("Selected Field Of Study");
		JLabel fundingLabel = new JLabel("Funding Required");

		final JTextArea state = new JTextArea((String)applications.get(0).get(1));
		final JTextArea date = new JTextArea((String)applications.get(0).get(2));
		final JTextArea name = new JTextArea((String)applications.get(0).get(3));
		final JTextArea term = new JTextArea((String)applications.get(0).get(4));
		final JTextArea fos = new JTextArea((String)applications.get(0).get(5));
		final JTextArea funding = new JTextArea(applications.get(0).get(6).equals("true") ? "Yes" : "No");

		state.setEditable(false);
		date.setEditable(false);
		name.setEditable(false);
		term.setEditable(false);
		fos.setEditable(false);
		funding.setEditable(false);

		pane2.add(stateLabel);
		pane2.add(state);
		pane2.add(dateLabel);
		pane2.add(date);
		pane2.add(nameLabel);
		pane2.add(name);
		pane2.add(termLabel);
		pane2.add(term);
		pane2.add(fosLabel);
		pane2.add(fos);
		pane2.add(fundingLabel);
		pane2.add(funding);
		
		//This will center the JFrame in the middle of the screen
		appFrame.setLocationRelativeTo(null);
		appFrame.setVisible(true);

		JLabel IDLabel = new JLabel("Select Application ID");

		for(int i = 0; i < applications.size(); i++) { // grab the IDs
			ID[i] = (Integer)applications.get(i).get(0);
			IDs[i] = applications.get(i).get(0).toString();
		}
		IDSel = IDs[0];
		Items = IDs;
		/* Create the list of applications by ID */
		JList<String> IDList = new JList<String>(Items);
		IDList.addListSelectionListener(new ListSelectionListener() {
			private boolean cooldown = false;
			public void valueChanged(ListSelectionEvent e) {
				if (!cooldown)
			       {
			           String sItems = "";
			           JList list = (JList) e.getSource();
			           int selected[] = list.getSelectedIndices();
			           
			           for(int i=0; i<selected.length; i++)
			               sItems += Items[selected[i]] + ", ";
			               
			          //if (selected.length > 0)
			               sItems = sItems.substring(0, sItems.length()-2);
			               
			          System.out.println(sItems + " selected in the JList."); // DEBUG PRINT (DO NOT REMOVE)
			          /* Update applications display based on new selection */
			          IDget = Integer.parseInt(sItems);
			          IDSel = sItems;
			          ArrayList<Object> application = new ArrayList<Object>();
					try {
						application = ApplicationDB.getApplicationbyID(IDget);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					if(!application.get(1).toString().equals(ApplicationState.SUBMITTED.toString())) { // if it's not in the submitted state, we can't change the state
						validate.setEnabled(false);
						incomplete.setEnabled(false);
					}
					else { // if it is, we can
						validate.setEnabled(true);
						incomplete.setEnabled(true);
					}
					/* Set the application area to the new application we selected */
			  		state.setText((String)application.get(1));
			  		date.setText((String)application.get(2));
			  		name.setText((String)application.get(3));
			  		term.setText((String)application.get(4));
			  		fos.setText((String)application.get(5));
			  		funding.setText(application.get(6).equals("true") ? "Yes" : "No");

			          cooldown = true;
			       }
			       else
			          cooldown = false;
			}
		});
		/* Add our panels and GUI components to our GUI */
		JScrollPane scroll = new JScrollPane(IDList);
		pane.add(IDLabel);
		pane.add(scroll);
		pane.add(pane2);
		pane.add(pane3);

		int input = JOptionPane.showConfirmDialog(appFrame, pane, "Application Selection"
				,JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == 0) { // if hit ok
			appFrame.dispose();
			System.out.println(IDSel); // DEBUG PRINT (DO NOT REMOVE)
			createUserFrame(usertype);
		}
		else { // if hit cancel
			appFrame.dispose();
			if(showAgain)
				createUserFrame(usertype);
		}
		appFrame.dispose();

	}
	
	/**
	 * Create the GUI for the Admin Office member to selected a term they wish to filter
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 * @since June 16 2014
	 * 
	 * Latest Change: Added Implementation
	 * @version June 17 2014
	 */
	private static void createAdminOfficeTermViewFrame() throws FileNotFoundException, IOException {
		JFrame appFrame = new JFrame(); // frame to display our GUI
		termSel = Term.FALL.toString(); // set default
		JPanel pane = new JPanel(); // create the panel which will store the label and the combobox
		pane.setLayout(new GridLayout(1,2));

		//This will center the JFrame in the middle of the screen
		appFrame.setLocationRelativeTo(null);
		appFrame.setVisible(true);

		JLabel termLabel = new JLabel("Select Term.");

		String[] terms = { Term.FALL.toString(), Term.WINTER.toString(), Term.SUMMER.toString() };
		JComboBox<String> termList = new JComboBox<String>(terms);
		termList.addActionListener(new ActionListener() {
			/* Define what happens when you hit a new item in the combobox */
			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				termSel = (String) ((JComboBox) o).getSelectedItem();				
			}
		});
		pane.add(termLabel);
		pane.add(termList);

		int input = JOptionPane.showConfirmDialog(appFrame, pane, "Fill out your application"
				,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == 0) { // if you hit okay

			System.out.println(termSel); // DEBUG PRINT (DO NOT REMOVE)
			ArrayList<ArrayList> arr = ApplicationDB.getApplicationbyTerm(Term.valueOf(termSel)); // get all by term
			//System.out.println(arr); // DEBUG PRINT (DO NOT REMOVE)
			for(int i = 0; i < arr.size(); i++) {
				if(arr.get(i).get(1).equals(ApplicationState.OPEN.toString())) { // don't care about open applications
					arr.remove(i);
					i--;
				}
			}
			//System.out.println(arr); // DEBUG PRINT (DO NOT REMOVE)
			appFrame.dispose();
			if(arr.size() != 0) {
				createAdminOfficeView(arr); // create view of all our applications
			}
			else {
				System.out.println("No applications available"); // DEBUG PRINT (DO NOT REMOVE)
				createUserFrame(usertype);
			}
				
		}
		else { // hit x
			appFrame.dispose();
			createUserFrame(usertype);
		}
		appFrame.dispose();
	}
	
	/**
	 * Create the GUI for the Admin Office member to selected a field of study they wish to filter
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 * @since June 16 2014
	 * 
	 * Latest Change: Added Implementation
	 * @version June 17 2014
	 */
	private static void createAdminOfficefosViewFrame() throws FileNotFoundException, IOException {
		JFrame appFrame = new JFrame("Select a Field Of Study"); // frame to display GUI
		termSel = Term.FALL.toString(); // set default
		JPanel pane = new JPanel(); // panel to store label and combobox
		pane.setLayout(new GridLayout(1,2));

		//This will center the JFrame in the middle of the screen
		appFrame.setLocationRelativeTo(null);
		appFrame.setVisible(true);

		JLabel fosLabel = new JLabel("Select Field Of Study.");

		String[] fields = { FieldOfStudy.ARTS.toString(), FieldOfStudy.COMMERCE.toString(), FieldOfStudy.ENGINEERING.toString(), FieldOfStudy.SCIENCE.toString() };
		JComboBox<String> fieldList = new JComboBox<String>(fields);
		fieldList.addActionListener(new ActionListener() {
			/* Define what happens when you select a new item in the combobox */
			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				fieldSel = (String) ((JComboBox) o).getSelectedItem();
			}
		});
		/* add label and combobox to panel */
		pane.add(fosLabel);
		pane.add(fieldList);

		int input = JOptionPane.showConfirmDialog(appFrame, pane, "Select a Term"
				,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == 0) { // if okay

			System.out.println(fieldSel); // DEBUG PRINT (DO NOT REMOVE)
			ArrayList<ArrayList> arr = ApplicationDB.getApplicationbyFieldOfStudy(FieldOfStudy.valueOf(fieldSel)); // get all applications with that field of study
			System.out.println(arr); // DEBUG PRINT (DO NOT REMOVE)
			
			for(int i = 0; i < arr.size(); i++) {
				if(arr.get(i).get(1).equals(ApplicationState.OPEN.toString())) { // filter out all the Open applications
					arr.remove(i);
					i--;
				}
			}
			if(arr.size() != 0) {
				createAdminOfficeView(arr); // show all applications in GUI
			}
			else {
				System.out.println("No applications available"); // DEBUG PRINT (DO NOT REMOVE)
				createUserFrame(usertype);
			}
		}
		else { // hit x
			appFrame.dispose();
			createUserFrame(usertype);
		}
		appFrame.dispose();
	}
	/**
	 * Create the new application GUI for applicant
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 * @since June 15 2014
	 * 
	 * LatestChange: Write Application to database
	 * @version June 17 2014
	 */
	private static void createApplicationFrame() throws FileNotFoundException, IOException {
		JFrame appFrame = new JFrame(); // frame to display GUI

		JPanel pane = new JPanel(); // panel to store GUI components
		pane.setLayout(new GridLayout(3,2));

		//This will center the JFrame in the middle of the screen
		appFrame.setLocationRelativeTo(null);
		appFrame.setVisible(true);

		JLabel termLabel = new JLabel("Select Term.");
		JLabel fosLabel = new JLabel("Select Field Of Study.");
		JLabel fundingLabel = new JLabel("Do you require Funding?");

		String[] terms = { Term.FALL.toString(), Term.WINTER.toString(), Term.SUMMER.toString() };
		JComboBox<String> termList = new JComboBox<String>(terms);
		termList.addActionListener(new ActionListener() {
			/* Define what happens when you select an item from the terms */
			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				termSel = (String) ((JComboBox) o).getSelectedItem();
			}
		});

		String[] fields = { FieldOfStudy.ARTS.toString(), FieldOfStudy.COMMERCE.toString(), FieldOfStudy.ENGINEERING.toString(), FieldOfStudy.SCIENCE.toString() };
		JComboBox<String> fieldList = new JComboBox<String>(fields);
		fieldList.addActionListener(new ActionListener() {
			/* Define what happens when you select an item from the field of studies */
			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				fieldSel = (String) ((JComboBox) o).getSelectedItem();
			}
		});

		String[] fundings = { "Yes", "No" };
		JComboBox<String> fundingList = new JComboBox<String>(fundings);
		fundingList.addActionListener(new ActionListener() {
			/* Define what happens when you select an item from the fundingRequired */
			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				fundingSel = (String) ((JComboBox) o).getSelectedItem();
			}
		});
		/* Add labels and comboboxes to panel */
		pane.add(termLabel);
		pane.add(termList);
		pane.add(fosLabel);
		pane.add(fieldList);
		pane.add(fundingLabel);
		pane.add(fundingList);

		int input = JOptionPane.showConfirmDialog(appFrame, pane, "Fill out your new application"
				,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (input == 0) { // if okay
			System.out.println(termSel); // DEBUG PRINT (DO NOT REMOVE)
			System.out.println(fieldSel); // DEBUG PRINT (DO NOT REMOVE)
			System.out.println(fundingSel); // DEBUG PRINT (DO NOT REMOVE)
			new Application(userName, Term.valueOf(termSel), FieldOfStudy.valueOf(fieldSel), (fundingSel.equals("Yes")) ? true : false);
			appFrame.dispose();
			createUserFrame(usertype); // show User GUI
		}
		else { // if cancel or x
			appFrame.dispose();
			createUserFrame(usertype); // show User GUI
		}
		appFrame.dispose();

	}

	/**
	 * Creates the GUI to allow applicant to edit their application
	 * 
	 * @param application the application we wish to edit
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 * @since June 17 2014
	 * 
	 * Latest Change: Added Implementation
	 * @version June 17 2014
	 */
	private static void createEditFrame(ArrayList<Object> application) throws FileNotFoundException, IOException {
		JFrame appFrame = new JFrame(); // frame to display GUI
		termSel = Term.FALL.toString(); // set default
		fieldSel = FieldOfStudy.ARTS.toString(); // set default
		fundingSel = "Yes"; // set default
		JPanel pane = new JPanel(); // panel to store labels and comboboxes
		pane.setLayout(new GridLayout(3,2));

		//This will center the JFrame in the middle of the screen
		appFrame.setLocationRelativeTo(null);
		appFrame.setVisible(true);

		JLabel termLabel = new JLabel("Select Term.");
		JLabel fosLabel = new JLabel("Select Field Of Study.");
		JLabel fundingLabel = new JLabel("Do you require Funding?");

		String[] terms = { Term.FALL.toString(), Term.WINTER.toString(), Term.SUMMER.toString() };
		JComboBox<String> termList = new JComboBox<String>(terms);
		termList.addActionListener(new ActionListener() {
			/* Define what happens when you select a term */
			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				termSel = (String) ((JComboBox) o).getSelectedItem();
			}
		});

		String[] fields = { FieldOfStudy.ARTS.toString(), FieldOfStudy.COMMERCE.toString(), FieldOfStudy.ENGINEERING.toString(), FieldOfStudy.SCIENCE.toString() };
		JComboBox<String> fieldList = new JComboBox<String>(fields);
		fieldList.addActionListener(new ActionListener() {
			/* Define what happens when you select a field of study */
			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				fieldSel = (String) ((JComboBox) o).getSelectedItem();
			}
		});

		String[] fundings = { "Yes", "No" };
		JComboBox<String> fundingList = new JComboBox<String>(fundings);
		fundingList.addActionListener(new ActionListener() {
			/* Define what happens when you select fundingRequired */
			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				fundingSel = (String) ((JComboBox) o).getSelectedItem();
			}
		});
		/* Add labels and comboboxes to panel */
		pane.add(termLabel);
		pane.add(termList);
		pane.add(fosLabel);
		pane.add(fieldList);
		pane.add(fundingLabel);
		pane.add(fundingList);

		int input = JOptionPane.showConfirmDialog(appFrame, pane, "Edit your Application"
				,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (input == 0) { // if okay
			System.out.println(termSel);  // DEBUG PRINT (DO NOT REMOVE)
			System.out.println(fieldSel); // DEBUG PRINT (DO NOT REMOVE)
			System.out.println(fundingSel); // DEBUG PRINT (DO NOT REMOVE)
			/* Changed fields of application depending on what user selected */
			application.set(4, termSel);
			application.set(5, fieldSel);
			application.set(6, (fundingSel.equals("Yes")) ? true : false);
			System.out.println((Integer)application.get(0));  // DEBUG PRINT (DO NOT REMOVE)
			ApplicationDB.replacebyID((Integer)application.get(0), application); // replace the application with new application
			appFrame.dispose();
			createUserFrame(usertype); // show user frame
		}
		else { // hit x or cancel
			appFrame.dispose();
			createUserFrame(usertype); // show user frame
		}
		appFrame.dispose();

	}

	/**
	 * Create frame to show application to user, ccan be shown udring any state
	 * @param application
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 * @since June 16 2014
	 * 
	 * Latest Change: Added Implementation
	 * @version June 16 2014
	 */
	private static void createViewFrame(ArrayList<Object> application) throws FileNotFoundException, IOException {
		JFrame gFrame = new JFrame(); // frame to display GUI

		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(6,2));

		//This will center the JFrame in the middle of the screen
		gFrame.setLocationRelativeTo(null);
		gFrame.setVisible(true);

		JLabel stateLabel = new JLabel("Application State");
		JLabel dateLabel = new JLabel("Last Updated");
		JLabel nameLabel = new JLabel("Applicant Name");
		JLabel termLabel = new JLabel("Term selected");
		JLabel fosLabel = new JLabel("Selected Field Of Study");
		JLabel fundingLabel = new JLabel("Funding Required");

		JTextArea state = new JTextArea((String)application.get(1));
		JTextArea date = new JTextArea((String)application.get(2));
		JTextArea name = new JTextArea((String)application.get(3));
		JTextArea term = new JTextArea((String)application.get(4));
		JTextArea fos = new JTextArea((String)application.get(5));
		JTextArea funding = new JTextArea(application.get(6).equals("true") ? "Yes" : "No");

		state.setEditable(false);
		date.setEditable(false);
		name.setEditable(false);
		term.setEditable(false);
		fos.setEditable(false);
		funding.setEditable(false);

		pane.add(stateLabel);
		pane.add(state);
		pane.add(dateLabel);
		pane.add(date);
		pane.add(nameLabel);
		pane.add(name);
		pane.add(termLabel);
		pane.add(term);
		pane.add(fosLabel);
		pane.add(fos);
		pane.add(fundingLabel);
		pane.add(funding);

		int input = JOptionPane.showConfirmDialog(gFrame, pane, "Application"
				,JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (input == 0) {
			gFrame.dispose();
			createUserFrame(usertype);
		}
		else {
			gFrame.dispose();
			createUserFrame(usertype);
		}
		gFrame.dispose();
	}

	private static void createUserFrame(String user) throws FileNotFoundException, IOException {
		final JFrame guiFrame = new JFrame("Select an Option");
		JButton newApp = new JButton("New Application");
		JButton viewApp = new JButton("View Current Application");
		JButton editApp = new JButton("Edit Application");
		JButton submitApp = new JButton("Submit Application");
		JButton viewByTerm = new JButton("View Applications for a Term");
		JButton viewByfos = new JButton("View Applications for a Field Of Study");

		if (user.equals(Person.Applicant.toString())) {
			final ArrayList<Object> application = ApplicationDB.getApplicationbyName(userName);
			System.out.println(application);
			if (application == null) {
				Container pane = guiFrame.getContentPane();
				pane.setLayout(new GridLayout(1,4));
				pane.add(newApp);
				pane.add(viewApp);
				pane.add(editApp);
				pane.add(submitApp);

				guiFrame.setSize(1000, 100);
				//This will center the JFrame in the middle of the screen
				guiFrame.setLocationRelativeTo(null);
				guiFrame.setVisible(true);
				submitApp.setEnabled(false);
				viewApp.setEnabled(false);
				editApp.setEnabled(false);
				newApp.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						guiFrame.dispose();
						try {
							createApplicationFrame();
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});

			}
			else {
				System.out.println("In else");
				Container pane = guiFrame.getContentPane();
				pane.setLayout(new GridLayout(1,4));
				pane.add(newApp);
				pane.add(viewApp);
				pane.add(editApp);
				pane.add(submitApp);

				guiFrame.setSize(1000, 100);
				//This will center the JFrame in the middle of the screen
				guiFrame.setLocationRelativeTo(null);
				guiFrame.setVisible(true);
				newApp.setEnabled(false);
				viewApp.setEnabled(true);
				viewApp.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						guiFrame.dispose();
						try {
							createViewFrame(application);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				if(!application.get(1).equals(ApplicationState.OPEN.toString())) {
					submitApp.setEnabled(false);
					editApp.setEnabled(false);
				}
				else {
					submitApp.setEnabled(true);
					editApp.setEnabled(true);
					submitApp.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							guiFrame.dispose();
							application.set(1, ApplicationState.SUBMITTED.toString());
							try {
								ApplicationDB.replacebyID((Integer)application.get(0), application);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							try {
								createUserFrame(usertype);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
					editApp.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							guiFrame.dispose();
							try {
								createEditFrame(application);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
				}
			}
		}
		else if (user.equals(Person.Professor.toString())) {

		}
		else if (user.equals(Person.AdminOffice.toString())) {
			Container pane = guiFrame.getContentPane();
			pane.setLayout(new GridLayout(1,2));
			pane.add(viewByTerm);
			pane.add(viewByfos);
			viewByTerm.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					guiFrame.dispose();
					try {
						createAdminOfficeTermViewFrame();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			viewByfos.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					guiFrame.dispose();
					try {
						createAdminOfficefosViewFrame();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			guiFrame.setSize(600, 150);
			//This will center the JFrame in the middle of the screen
			guiFrame.setLocationRelativeTo(null);
			guiFrame.setVisible(true);
		}
		else if (user.equals(Person.AssocChair.toString())) {

		}
	}

	private static boolean authenticate(String username, String password) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader((System.getProperty("user.dir") + "\\Database\\Users.txt")));

		String line;
		String user = null;
		String pass = null;
		int index = 0;

		while ((line = br.readLine()) != null) {
			while(true) {
				for(int i = 0; i < line.length(); i++) {
					if(line.charAt(i) == '$') {
						user = line.substring(0, i);
						index = i+1;
						break;
					}
				}
				if(!user.equals(username))
					break;
				for(int i = index; i <line.length(); i++) {
					if(line.charAt(i) == '$') {
						pass = line.substring(index, i);
						index = i+1;
						break;
					}
				}
				if(!pass.equals(password))
					break;
				usertype = line.substring(index, line.length());
				return true;
			}
		}
		return false;
	}

	private static void createCCFrame() {
		final JFrame frameCC = new JFrame("Hello, welcome to Carleton Central");
		JButton login = new JButton("Login");
		JButton exit = new JButton("Exit");

		//This will center the JFrame in the middle of the screen
		frameCC.setLocationRelativeTo(null);
		frameCC.setVisible(true);

		login.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				frameCC.dispose();
				try {
					createLoginFrame();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		Container pane = frameCC.getContentPane();
		pane.setLayout(new GridLayout(1,2));
		pane.add(login);
		pane.add(exit);

		frameCC.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameCC.setSize(400, 200);
		frameCC.setResizable(false);
		frameCC.setVisible(true);
	}

	public static void main(String[] args) {
		createCCFrame();
	}

}
