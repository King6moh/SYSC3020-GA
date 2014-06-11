import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Kais
 *
 */
public class Application {
	private ArrayList<Object> application;
	private int applicationID;
	
	public Application(String applicantName, Term term, FieldOfStudy fieldOfStudy, boolean fundingRequired) {
		application = new ArrayList<Object>();
		try {
			applicationID = readFromFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writeToFile(applicationID);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		application.add(applicationID);
		application.add(ApplicationState.OPEN.toString());
		application.add(new Date().toString());
		application.add(applicantName);
		application.add(term.toString());
		application.add(fieldOfStudy.toString());
		application.add(fundingRequired);
		try {
			writeApplicationToFile(application);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private int readFromFile() throws FileNotFoundException, IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") + "\\Database\\LastID.txt"));
		int availablebytes = in.available();
		int offset = 1;
		int result = 0;
		for(int i=0; i < availablebytes-1; i++) {
			offset = offset*10;
		}
		for(int i=0; i < availablebytes; i++) {
			result = result + ((in.read()-48) * offset);
			offset = offset/10;
		}
		in.close();
		return result;
	}
	private void writeToFile(int applicationID) throws FileNotFoundException, IOException {
		FileOutputStream out = new FileOutputStream(System.getProperty("user.dir") + "\\Database\\LastID.txt");
		out.write(applicationID+49);
		out.close();
	}
	
	private void writeApplicationToFile(ArrayList<Object> application) throws FileNotFoundException, IOException {
		FileOutputStream out = new FileOutputStream(System.getProperty("user.dir") + "\\Database\\Applications.txt", true);
		String data = new String( application.get(0) + "$" +  application.get(1) + "$" + application.get(2) + "$" + application.get(3) + "$" + application.get(4) + "$" + application.get(5) + "$" + application.get(6)+ "$\n");
		out.write(data.getBytes());
	}
	public static void main(String [] args) {
		Application app = new Application("KaisHassanali", Term.FALL, FieldOfStudy.ARTS, false);
	}
}
