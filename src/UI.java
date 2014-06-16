import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

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
	/**
	 * @param args
	 * @throws IOException 
	 */
	//handles the creation of the JFrame and
	//all it's components
	private static void createLoginFrame() throws IOException
	{
		int count = 0;
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
	
	private static void createProfessorTermViewFrame() throws FileNotFoundException, IOException {
		JFrame appFrame = new JFrame("Select a Term");
		
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(3,2));

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
			System.out.println(arr);
		}
		else {
			appFrame.dispose();
			createUserFrame(usertype);
		}
		appFrame.dispose();
	}

	private static void createApplicationFrame() throws FileNotFoundException, IOException {
		JFrame appFrame = new JFrame("Fill out your new application");

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

		int input = JOptionPane.showConfirmDialog(appFrame, pane, "Fill out your application"
				,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (input == 0) {

			System.out.println(termSel);
			System.out.println(fieldSel);
			System.out.println(fundingSel);
		}
		else {
			appFrame.dispose();
			createUserFrame(usertype);
		}
		appFrame.dispose();

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
			ArrayList<Object> application = ApplicationDB.getApplicationbyName(userName);
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
				if(!application.get(1).equals(ApplicationState.OPEN.toString())) {
					submitApp.setEnabled(false);
					editApp.setEnabled(false);
				}
			}
		}
		else if (user.equals(Person.Professor.toString())) {
			Container pane = guiFrame.getContentPane();
			pane.setLayout(new GridLayout(1,2));
			pane.add(viewByTerm);
			pane.add(viewByfos);
			viewByTerm.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					guiFrame.dispose();
					try {
						createProfessorTermViewFrame();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			/*viewByfos.addActionListener(new ActionListener() {

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
			});*/
			guiFrame.setSize(600, 150);
			//This will center the JFrame in the middle of the screen
			guiFrame.setLocationRelativeTo(null);
			guiFrame.setVisible(true);
		}
		else if (user.equals(Person.AdminOffice.toString())) {
			
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
