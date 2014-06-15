import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

/**
 * @author Kais
 *
 */
public class UI {
	private static JFrame frameCC;
	private static JButton login;
	private static JButton exit;
	private static JTextArea username;
	private static JTextArea password;
	private static String user;
	private static char[] pass;
	private static String usertype;

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
        
        //As the JOptionPane accepts an object as the message
        //it allows us to use any component we like - in this case 
        //a JPanel containing the dialog components we want
        JOptionPane.showConfirmDialog(guiFrame, userPanel, "Enter your password:"
                            ,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        pass = passwordFld.getPassword();
        user = username.getText();
        
        Authenticate(user, new String(pass));
        
        System.out.println(user);
        System.out.println(new String(pass));
        System.out.println("The user is a(n): " + usertype);
        guiFrame.dispose();
    }

    private static void Authenticate(String username, String password) throws IOException {
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
    			System.out.println("The user has been authenticated");
    			return;
    		}
    	}
    	System.out.println("The user could not be authenticated");
    }

	public static void main(String[] args) {
		frameCC = new JFrame("Hello, welcome to Carleton Central");
		login = new JButton("Login");
		exit = new JButton("Exit");

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
		frameCC.setSize(300, 200);
		frameCC.setVisible(true);
	}

}
