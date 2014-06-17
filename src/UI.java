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
 * @author Kais
 *
 */
public class UI {
	private static String usertype;
	static String termSel = Term.FALL.toString();
	static String fieldSel = FieldOfStudy.ARTS.toString();
	static String fundingSel = "Yes";
	static String userName = null;
	static int count = 0;
	static String IDSel;
	static String Items[];
	/**
	 * @param args
	 * @throws IOException 
	 */
	//handles the creation of the JFrame and
	//all it's components
	private static void createLoginFrame() throws IOException
	{
		JFrame guiFrame = new JFrame();



		JPanel userPanel = new JPanel();
		userPanel.setLayout(new GridLayout(2,2));

		JLabel usernameLbl = new JLabel("Username:");
		JLabel passwordLbl = new JLabel("Password:");
		JTextField username = new JTextField();
		JPasswordField passwordFld = new JPasswordField();

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

		if (input != 0) {
			System.out.println("User has hit cancel");
			guiFrame.dispose();
			createCCFrame();
			return;
		}

		String pass = new String(passwordFld.getPassword());
		String user = username.getText();

		boolean authenticated = authenticate(user, pass);

		System.out.println(user);
		System.out.println(pass);

		userName = user;

		if (authenticated) {
			System.out.println("The user has been authenticated");
			System.out.println("The user is a(n): " + usertype);
			guiFrame.dispose();

			createUserFrame(usertype);
		}
		else {
			count++;
			if(count != 5) {
				System.out.println("You have failed to authenticate");
				System.out.println("You have " + (5-count) + " trie(s) remaining, after that you will be locked out");
				guiFrame.dispose();
				createLoginFrame();
			}
			else {
				System.out.println("You have failed to authenticate 5 times, please contact the system admin for help, you will no longer be able to login from this IP");
				guiFrame.dispose();
			}

		}
	}

	private static void createTermAdminOfficeView(ArrayList<ArrayList> applications) throws FileNotFoundException, IOException {
		JFrame appFrame = new JFrame();

		String[] IDs = new String[applications.size()];
		int[] ID = new int[applications.size()];
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(1,3));
		
		JPanel pane2 = new JPanel();
		pane2.setLayout(new GridLayout(6,2));


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

		for(int i = 0; i < applications.size(); i++) {
			ID[i] = (int)applications.get(i).get(0);
			IDs[i] = applications.get(i).get(0).toString();
		}
		IDSel = IDs[0];
		Items = IDs;
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
			               
			          System.out.println(sItems + " selected in the JList.");
			          int ID = Integer.parseInt(sItems);
			          IDSel = sItems;
			          ArrayList<Object> application = new ArrayList<Object>();
					try {
						application = ApplicationDB.getApplicationbyID(ID);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

			  		state.setText((String)application.get(1));
			  		date.setText((String)application.get(2));
			  		name.setText((String)application.get(3));
			  		term.setText((String)application.get(4));
			  		fos.setText((String)application.get(5));
			  		funding.setText(application.get(6).equals("true") ? "Yes" : "No");
			/*  		state.setEditable(false);
			  		date.setEditable(false);
			  		name.setEditable(false);
			  		term.setEditable(false);
			  		fos.setEditable(false);
			  		funding.setEditable(false);*/

			          cooldown = true;
			       }
			       else
			          cooldown = false;
			}
		});
		JScrollPane scroll = new JScrollPane(IDList);
		pane.add(IDLabel);
		pane.add(scroll);
		pane.add(pane2);

		int input = JOptionPane.showConfirmDialog(appFrame, pane, "Application Selection"
				,JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == 0) {
			appFrame.dispose();
			System.out.println(IDSel);
			createUserFrame(usertype);
		}
		else {
			appFrame.dispose();
			createUserFrame(usertype);
		}
		appFrame.dispose();

	}

	private static void createAdminOfficeTermViewFrame() throws FileNotFoundException, IOException {
		JFrame appFrame = new JFrame();

		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(1,2));

		//This will center the JFrame in the middle of the screen
		appFrame.setLocationRelativeTo(null);
		appFrame.setVisible(true);

		JLabel termLabel = new JLabel("Select Term.");

		String[] terms = { Term.FALL.toString(), Term.WINTER.toString(), Term.SUMMER.toString() };
		JComboBox<String> termList = new JComboBox<String>(terms);
		termList.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				termSel = (String) ((JComboBox) o).getSelectedItem();				
			}
		});
		pane.add(termLabel);
		pane.add(termList);

		int input = JOptionPane.showConfirmDialog(appFrame, pane, "Fill out your application"
				,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == 0) {

			System.out.println(termSel);
			ArrayList<ArrayList> arr = ApplicationDB.getApplicationbyTerm(Term.valueOf(termSel));
			//System.out.println(arr);
			for(int i = 0; i < arr.size(); i++) {
				if(arr.get(i).get(1).equals(ApplicationState.OPEN.toString())) {
					arr.remove(i);
					i--;
				}
			}
			//System.out.println(arr);
			appFrame.dispose();
			if(arr.size() != 0) {
				createTermAdminOfficeView(arr);
			}
			else {
				// display something here
			}
				
		}
		else {
			appFrame.dispose();
			createUserFrame(usertype);
		}
		appFrame.dispose();
	}

	private static void createProfessorfosViewFrame() throws FileNotFoundException, IOException {
		JFrame appFrame = new JFrame("Select a Field Of Study");

		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(1,2));

		//This will center the JFrame in the middle of the screen
		appFrame.setLocationRelativeTo(null);
		appFrame.setVisible(true);

		JLabel fosLabel = new JLabel("Select Field Of Study.");

		String[] fields = { FieldOfStudy.ARTS.toString(), FieldOfStudy.COMMERCE.toString(), FieldOfStudy.ENGINEERING.toString(), FieldOfStudy.SCIENCE.toString() };
		JComboBox<String> fieldList = new JComboBox<String>(fields);
		fieldList.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				fieldSel = (String) ((JComboBox) o).getSelectedItem();
			}
		});
		pane.add(fosLabel);
		pane.add(fieldList);

		int input = JOptionPane.showConfirmDialog(appFrame, pane, "Select a Term"
				,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == 0) {

			System.out.println(fieldSel);
			ArrayList<ArrayList> arr = ApplicationDB.getApplicationbyFieldOfStudy(FieldOfStudy.valueOf(fieldSel));
			System.out.println(arr);
		}
		else {
			appFrame.dispose();
			createUserFrame(usertype);
		}
		appFrame.dispose();
	}

	private static void createApplicationFrame() throws FileNotFoundException, IOException {
		JFrame appFrame = new JFrame();

		JPanel pane = new JPanel();
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

			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				termSel = (String) ((JComboBox) o).getSelectedItem();
			}
		});

		String[] fields = { FieldOfStudy.ARTS.toString(), FieldOfStudy.COMMERCE.toString(), FieldOfStudy.ENGINEERING.toString(), FieldOfStudy.SCIENCE.toString() };
		JComboBox<String> fieldList = new JComboBox<String>(fields);
		fieldList.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				fieldSel = (String) ((JComboBox) o).getSelectedItem();
			}
		});

		String[] fundings = { "Yes", "No" };
		JComboBox<String> fundingList = new JComboBox<String>(fundings);
		fundingList.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				fundingSel = (String) ((JComboBox) o).getSelectedItem();
			}
		});

		pane.add(termLabel);
		pane.add(termList);
		pane.add(fosLabel);
		pane.add(fieldList);
		pane.add(fundingLabel);
		pane.add(fundingList);

		int input = JOptionPane.showConfirmDialog(appFrame, pane, "Fill out your new application"
				,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == 0) {

			System.out.println(termSel);
			System.out.println(fieldSel);
			System.out.println(fundingSel);
			new Application(userName, Term.valueOf(termSel), FieldOfStudy.valueOf(fieldSel), (fundingSel.equals("Yes")) ? true : false);
			appFrame.dispose();
			createUserFrame(usertype);
		}
		else {
			appFrame.dispose();
			createUserFrame(usertype);
		}
		appFrame.dispose();

	}

	private static void createEditFrame(ArrayList<Object> application) throws FileNotFoundException, IOException {
		JFrame appFrame = new JFrame();
		termSel = Term.FALL.toString();
		fieldSel = FieldOfStudy.ARTS.toString();
		fundingSel = "Yes";
		JPanel pane = new JPanel();
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

			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				termSel = (String) ((JComboBox) o).getSelectedItem();
			}
		});

		String[] fields = { FieldOfStudy.ARTS.toString(), FieldOfStudy.COMMERCE.toString(), FieldOfStudy.ENGINEERING.toString(), FieldOfStudy.SCIENCE.toString() };
		JComboBox<String> fieldList = new JComboBox<String>(fields);
		fieldList.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				fieldSel = (String) ((JComboBox) o).getSelectedItem();
			}
		});

		String[] fundings = { "Yes", "No" };
		JComboBox<String> fundingList = new JComboBox<String>(fundings);
		fundingList.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				fundingSel = (String) ((JComboBox) o).getSelectedItem();
			}
		});

		pane.add(termLabel);
		pane.add(termList);
		pane.add(fosLabel);
		pane.add(fieldList);
		pane.add(fundingLabel);
		pane.add(fundingList);

		int input = JOptionPane.showConfirmDialog(appFrame, pane, "Edit your Application"
				,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == 0) {
			System.out.println(termSel);
			System.out.println(fieldSel);
			System.out.println(fundingSel);
			application.set(4, termSel);
			application.set(5, fieldSel);
			application.set(6, (fundingSel.equals("Yes")) ? true : false);
			System.out.println((int)application.get(0));
			ApplicationDB.replacebyID((int)application.get(0), application);
			appFrame.dispose();
			createUserFrame(usertype);
		}
		else {
			appFrame.dispose();
			createUserFrame(usertype);
		}
		appFrame.dispose();

	}

	private static void createViewFrame(ArrayList<Object> application) throws FileNotFoundException, IOException {
		JFrame gFrame = new JFrame();

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
								ApplicationDB.replacebyID((int)application.get(0), application);
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
						createProfessorfosViewFrame();
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
		//NotesGui gui = new NotesGui();
	}

}
