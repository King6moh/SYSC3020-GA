import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * The following class will store the application within our database
 * @author Team 4
 * @version June 10 2014
 */
public class Application {
	private ArrayList<Object> application;
	private int applicationID;
	
	public Application(String applicantName, Term term, FieldOfStudy fieldOfStudy, boolean fundingRequired) {
		application = new ArrayList<Object>();
		try {
			applicationID = readFromFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writeToFile(applicationID);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* Create our application */
		application.add(applicationID);
		application.add(ApplicationState.OPEN.toString());
		application.add(new Date().toString());
		application.add(applicantName);
		application.add(term.toString());
		application.add(fieldOfStudy.toString());
		application.add(fundingRequired);
		try { // add it to our database
			writeApplicationToFile(application);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @return get the application ID we must assign to our application
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private int readFromFile() throws FileNotFoundException, IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") + "\\Database\\LastID.txt"));
		int availablebytes = in.available(); // check how big the value is
		int offset = 1;
		int result = 0;
		for(int i=0; i < availablebytes-1; i++) {
			offset = offset*10;
		}
		for(int i=0; i < availablebytes; i++) {
			result = result + ((in.read()-48) * offset); // get the digits of the ID
			offset = offset/10;
		}
		in.close();
		return result;
	}
	/**
	 * The application ID must be updated so we don't have any duplicates
	 * @param applicationID application id we just assigned
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void writeToFile(int applicationID) throws FileNotFoundException, IOException {
		FileOutputStream out = new FileOutputStream(System.getProperty("user.dir") + "\\Database\\LastID.txt");
		// TODO make it write back the ID in a clean format
		out.write(applicationID+49);
		out.close();
	}
	/**
	 * The following method will write our application to the file
	 * @param application the application we are to write to the database
	 * @throws FileNotFoundException if the file we are to write to cannot be found
	 * @throws IOException trouble writing to file
	 */
	private void writeApplicationToFile(ArrayList<Object> application) throws FileNotFoundException, IOException {
		FileOutputStream out = new FileOutputStream(System.getProperty("user.dir") + "\\Database\\Applications.txt", true);
		String data = new String( application.get(0) + "$" +  application.get(1) + "$" + application.get(2) + "$" + application.get(3) + "$" + application.get(4) + "$" + application.get(5) + "$" + application.get(6)+ "$\n");
		out.write(data.getBytes());
	}
}
