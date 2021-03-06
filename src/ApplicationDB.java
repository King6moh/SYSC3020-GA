import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * The following class will act as the database controller, to access the applications and professor information (only applications implemented currently)
 * @author Team 4
 *
 */
public class ApplicationDB {	
	/**
	 * 
	 * @param ID the ID of the application we want
	 * @return the application
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static ArrayList<Object> getApplicationbyID(int ID) throws FileNotFoundException, IOException {
		if (ID >= readFromFile())
			return null;
		else {
			return readFromApplicationFile(ID);
		}
	}
	
	/**
	 * 
	 * @return get the application ID we must assign to our application
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static int readFromFile() throws FileNotFoundException, IOException {
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
	 * Used to get all applications corresponding to the field of study entered
	 * @param fos the fieldofstudy we are looking for
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<ArrayList> getApplicationbyFieldOfStudy(FieldOfStudy fos) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader((System.getProperty("user.dir") + "\\Database\\Applications.txt")));
		String line;
		String fosLocal = null;
		String appID;
		int ID;
		ArrayList<ArrayList> arr = new ArrayList<ArrayList>();
		
		while ((line = br.readLine()) != null) {
			int dollarSignCount = 0;
			   for(int i = 0; i < line.length(); i++) {
				   if (line.charAt(i) == '$') {
					   dollarSignCount++;
				   }
				   if (dollarSignCount == 5) { // we know the field we want is after the 5th dollar sign delimeter
					   int index = i+1;
					   for(int k = index; k < line.length(); k++) {
						   if (line.charAt(k) == '$') {
							   fosLocal = line.substring(index, k);
							   //System.out.println("fieldLocal: " + fosLocal);
							   index = k+1;
							   break;
						   }
					   }
					}
				   if (fos.toString().equals(fosLocal) == false && dollarSignCount == 5) {
					   //System.out.println(dollarSignCount);
					   //System.out.println(fos.toString());
					   //System.out.println(fosLocal);
					  // System.out.println("break line 56");
					   break;
				   }
				   if(fos.toString().equals(fosLocal) && dollarSignCount == 5) {
					   //System.out.println("line 63");
					   for(int j = 0; j < line.length(); j++) {
						   ID = 0;
						   if (line.charAt(j) == '$') {
							   appID = line.substring(0, j);
							   int offset = 1;
							   for(int k = 0; k < appID.length()-1; k++) {
								   offset = offset*10;
							   }
							   for(int k = 0; k < appID.length(); k++) {
								   ID = ID + ((appID.charAt(k)-48)*offset);
								   offset = offset /10;
							   }
							   arr.add(getApplicationbyID(ID));
							  // System.out.println("line 77 break");
							   break;
						   }
					   }
					   //System.out.println("Line 81 break");
					   break;
				   }
			   }
		}
		return arr;
	}
	
	public static ArrayList<Object> getApplicationbyName(String name) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader((System.getProperty("user.dir") + "\\Database\\Applications.txt")));
		String line;
		String nameLocal = null;
		String appID;
		int ID;
		ArrayList<Object> arr = new ArrayList<Object>();
		
		while ((line = br.readLine()) != null) {
			int dollarSignCount = 0;
			   for(int i = 0; i < line.length(); i++) {
				   if (line.charAt(i) == '$') {
					   dollarSignCount++;
				   }
				   if (dollarSignCount == 3) { // we know the field we want is after the 5th dollar sign delimeter
					   int index = i+1;
					   for(int k = index; k < line.length(); k++) {
						   if (line.charAt(k) == '$') {
							   nameLocal = line.substring(index, k);
							   index = k+1;
							   break;
						   }
					   }
					}
				   if (name.equals(nameLocal) == false && dollarSignCount == 3) {
					   //System.out.println(dollarSignCount);
					   //System.out.println(fos.toString());
					   //System.out.println(fosLocal);
					  // System.out.println("break line 56");
					   break;
				   }
				   if(name.equals(nameLocal) && dollarSignCount == 3) {
					   //System.out.println("line 63");
					   for(int j = 0; j < line.length(); j++) {
						   ID = 0;
						   if (line.charAt(j) == '$') {
							   appID = line.substring(0, j);
							   int offset = 1;
							   for(int k = 0; k < appID.length()-1; k++) {
								   offset = offset*10;
							   }
							   for(int k = 0; k < appID.length(); k++) {
								   ID = ID + ((appID.charAt(k)-48)*offset);
								   offset = offset /10;
							   }
							   arr = getApplicationbyID(ID);
							  // System.out.println("line 77 break");
							   return arr;
						   }
					   }
					   //System.out.println("Line 81 break");
					   break;
				   }
			   }
		}
		return null;
	}
	
	/**
	 * THe following method will return an array of array lists that will contain all the application that match the term given
	 * @param term the term we are searching for
	 * @return a list of all applications which match the term
	 * @throws IOException
	 */
	public static ArrayList<ArrayList> getApplicationbyTerm(Term term) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader((System.getProperty("user.dir") + "\\Database\\Applications.txt")));
		String line;
		String termLocal = null;
		String appID;
		int ID;
		ArrayList<ArrayList> arr = new ArrayList<ArrayList>();
		
		while ((line = br.readLine()) != null) {
			int dollarSignCount = 0;
			   for(int i = 0; i < line.length(); i++) {
				   if (line.charAt(i) == '$') {
					   dollarSignCount++;
				   }
				   if (dollarSignCount == 4) { // we know the field we want is after the 4th dollar sign delimeter
					   int index = i+1;
					   for(int k = index; k < line.length(); k++) {
						   if (line.charAt(k) == '$') {
							   termLocal = line.substring(index, k);
							   //System.out.println("termLocal: " + termLocal);
							   index = k+1;
							   break;
						   }
					   }
					}
				   if (term.toString().equals(termLocal) == false && dollarSignCount == 4) {
					   //System.out.println(dollarSignCount);
					   //System.out.println(term.toString());
					   //System.out.println(termLocal);
					   //System.out.println("break line 56");
					   break;
				   }
				   if(term.toString().equals(termLocal) && dollarSignCount == 4) {
					  // System.out.println("line 63");
					   for(int j = 0; j < line.length(); j++) {
						   ID = 0;
						   if (line.charAt(j) == '$') {
							   appID = line.substring(0, j);
							   int offset = 1;
							   for(int k = 0; k < appID.length()-1; k++) {
								   offset = offset*10;
							   }
							   for(int k = 0; k < appID.length(); k++) {
								   ID = ID + ((appID.charAt(k)-48)*offset);
								   offset = offset/10;
							   }
							   //System.out.println("Getting ID: " + ID);
							   arr.add(getApplicationbyID(ID));
							   //System.out.println("line 77 break");
							   break;
						   }
					   }
					  // System.out.println("Line 81 break");
					   break;
				   }
			   }
		}
		return arr;
		
	}
	/**
	 * The following method is used to retreive the application with the ID number that is specified
	 * @param ID the ID of the application we are looking for
	 * @return the application in arraylist format
	 * @throws IOException
	 */
	private static ArrayList<Object> readFromApplicationFile(int ID) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader((System.getProperty("user.dir") + "\\Database\\Applications.txt")));
		String line;
		int digits = 0;
		int number = ID;
		/* fields used to store the information about the application */
		int applicationID;
		String state = null;
		String date = null;
		String name = null;
		String term = null;
		String fieldOfStudy = null;
		String fundingRequired = null;
		
		while (number > 0) {
		    number = number / 10;
		    digits++;
		}
		//System.out.println("digits: " + digits);
		char IDinBytes[] = new char[digits];
		number = ID;
		int j = digits;
		while (number > 0) {
			IDinBytes[j-1] = (char)((number % 10)+48);
		    number = number / 10;
		    j--;
		}
		while ((line = br.readLine()) != null) {
		   for(int i = 0; i < line.length(); i++) {
			   if (i < digits) {
				   if (line.charAt(i) != IDinBytes[i] && line.charAt(i) != '$') { // check if the id matches
					   break;
				   }
			   }
			   else if (line.charAt(i) != '$' && i >= digits) {
				   break;
			   }
			   else if (line.charAt(i) == '$') {
				   applicationID = ID;
				   int index = i+1;
				   for(int k = index; k < line.length(); k++) { // get the application state
					   if (line.charAt(k) == '$') {
						   state = line.substring(index, k);
						   //System.out.println("State: " + state);
						   index = k+1;
						   break;
					   }
				   }
				   for(int k = index; k < line.length(); k++) { // get the application date
					   if (line.charAt(k) == '$') {
						   date = line.substring(index, k);
						   //System.out.println("Date: " + date);
						   index = k+1;
						   break;
					   }
				   }
				   for(int k = index; k < line.length(); k++) { // get the name of the applicant
					   if (line.charAt(k) == '$') {
						   name = line.substring(index, k);
						   //System.out.println("Name: " + name);
						   index = k+1;
						   break;
					   }
				   }
				   for(int k = index; k < line.length(); k++) { // get the term for the application
					   if (line.charAt(k) == '$') {
						   term = line.substring(index, k);
						   //System.out.println("Term: " + term);
						   index = k+1;
						   break;
					   }
				   }
				   for(int k = index; k < line.length(); k++) { // get the applicants field of study
					   if (line.charAt(k) == '$') {
						   fieldOfStudy = line.substring(index, k);
						   //System.out.println("FieldOfStudy: " + fieldOfStudy);
						   index = k+1;
						   break;
					   }
				   }
				   for(int k = index; k < line.length(); k++) { // get whether or not the applicant needs funding
					   if (line.charAt(k) == '$') {
						   fundingRequired = line.substring(index, k);
						   //System.out.println("FundingRequired: " + fundingRequired);
						   index = k+1;
						   break;
					   }
				   }
				   ArrayList<Object> application = new ArrayList<Object>();
				   application.add(applicationID);
				   application.add(state);
				   application.add(date);
				   application.add(name);
				   application.add(term);
				   application.add(fieldOfStudy);
				   application.add(fundingRequired);
				   br.close();
				   return application; // return the application information is arraylist format
			   }
			   //System.out.println("didn't find tab");
		   }
		}
		br.close();
		return null;
	}
	/**
	 * This method will replace the contents of the application with an updated application
	 * 
	 * @param ID the application ID of the application being replaced
	 * @param application the new version of the application being replaced
	 * @throws IOException
	 */
	public static void replacebyID(int ID, ArrayList<Object> application) throws IOException
	{
		int digits = 0;
		int number = ID;
		File tempFile = new File((System.getProperty("user.dir") + "\\Database\\TEMPApplications.txt"));
		File file = new File((System.getProperty("user.dir") + "\\Database\\Applications.txt"));
		BufferedReader br = new BufferedReader(new FileReader((System.getProperty("user.dir") + "\\Database\\Applications.txt")));
		FileOutputStream writer = new FileOutputStream(((System.getProperty("user.dir") + "\\Database\\TEMPApplications.txt")));
		String line;
		
		while (number > 0) {
		    number = number / 10;
		    digits++;
		}
		char IDinBytes[] = new char[digits];
		number = ID;
		int j = digits;
		while (number > 0) {
			IDinBytes[j-1] = (char)((number % 10)+48);
		    number = number / 10;
		    j--;
		}
		while ((line = br.readLine()) != null) {
		   for(int i = 0; i < line.length(); i++) {
			   if (i < digits) {
				   if (line.charAt(i) != IDinBytes[i] && line.charAt(i) != '$') { // check if the id matches
					   System.out.println("not right line" + i);
					   String x = "\n";
					   writer.write(line.getBytes());
					   writer.write(x.getBytes());
					   System.out.println(line);
					   break;
				   }
			   }
			   else if (line.charAt(i) != '$' && i >= digits) {
				   System.out.println("not right line" + i);
				   String x = "\n";
				   writer.write(line.getBytes());
				   writer.write(x.getBytes());
				   System.out.println(line);
				   break;
			   }
			   else if (line.charAt(i) == '$') {	//Line found
				   System.out.println("right line");
				   String data = new String( ID + "$" +  application.get(1) + "$" + application.get(2) + "$" + application.get(3) + "$" + application.get(4) + "$" + application.get(5) + "$" + application.get(6)+ "$\n");
				   writer.write(data.getBytes());
				   System.out.println(data);
				   break;
			   }
		   }
		}
		writer.close();
		br.close();
		transferData(tempFile, file);
		tempFile.delete();
		System.out.println("done");
	}
	/**
	 * Transfers the contents of one file to another 
	 * 
	 * @param file1 source file
	 * @param file2 destination file
	 * @throws IOException
	 */
	private static void transferData(File file1, File file2) throws IOException {
		String source = file1.getAbsolutePath();
		String dest = file2.getAbsolutePath();
 
		File fin = new File(source);
		FileInputStream fis = new FileInputStream(fin);
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));
 
		FileWriter fstream = new FileWriter(dest, false);
		BufferedWriter out = new BufferedWriter(fstream);
 
		String aLine = null;
		while ((aLine = in.readLine()) != null) {
			//Process each line and add output to Dest.txt file
			out.write(aLine);
			out.newLine();
		}
 
		// do not forget to close the buffer reader
		in.close();
 
		// close buffer writer
		out.close();
	}
	
	/**
	 * The following main is just to show that our methods defined above work. Everytime it is run, it will append 2 applications to the database
	 * @param args
	 */
	public static void main(String [] args) {
		Application app = new Application("KaisHassanali", Term.FALL, FieldOfStudy.ENGINEERING, false);
		app = new Application("SamsonTruong", Term.WINTER, FieldOfStudy.SCIENCE, true);
		/* Check the Applications file in the Database, and you'll see which ones should be printed here */
		ArrayList<ArrayList> array = new ArrayList<ArrayList>();
		ArrayList<Object> arr = new ArrayList<Object>();
		try {
			array = getApplicationbyFieldOfStudy(FieldOfStudy.ENGINEERING);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print(array + "\n");
		try {
			array = getApplicationbyTerm(Term.WINTER);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print(array+ "\n");
		try {
			arr = getApplicationbyID(4);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(arr);
		System.out.print(array);
		
		try {
			replacebyID(2, getApplicationbyID(1));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
