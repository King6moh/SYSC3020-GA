import java.awt.*;
import java.awt.event.*;

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

	/**
	 * @param args
	 */
	//handles the creation of the JFrame and
    //all it's components
    private static void createLoginFrame()
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
        
        System.out.println(user);
        System.out.println(pass);
        guiFrame.dispose();
    }
	public static void main(String[] args) {
		frameCC = new JFrame("Hello, welcome to Carleton Central");
		login = new JButton("Login");
		exit = new JButton("Exit");

		login.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				frameCC.dispose();
				createLoginFrame();
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
