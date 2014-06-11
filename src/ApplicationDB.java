import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Team 4
 *
 */
public class ApplicationDB {	
	
	public static ArrayList<Object> getApplicationbyID(int ID) throws FileNotFoundException, IOException {
		if (ID >= readFromFile())
			return null;
		else {
			return readFromApplicationFile(ID);
		}
	}

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
				   if (dollarSignCount == 5) {
					   int index = i+1;
					   for(int k = index; k < line.length(); k++) {
						   if (line.charAt(k) == '$') {
							   fosLocal = line.substring(index, k);
							   System.out.println("termLocal: " + fosLocal);
							   index = k+1;
							   break;
						   }
					   }
					}
				   if (fos.toString().equals(fosLocal) == false && dollarSignCount == 5) {
					   System.out.println(dollarSignCount);
					   System.out.println(fos.toString());
					   System.out.println(fosLocal);
					   System.out.println("break line 56");
					   break;
				   }
				   if(fos.toString().equals(fosLocal) && dollarSignCount == 5) {
					   System.out.println("line 63");
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
							   }
							   System.out.println(ID);
							   arr.add(getApplicationbyID(ID));
							   System.out.println("line 77 break");
							   break;
						   }
					   }
					   System.out.println("Line 81 break");
					   break;
				   }
			   }
		}
		return arr;
	}
	
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
				   if (dollarSignCount == 4) {
					   int index = i+1;
					   for(int k = index; k < line.length(); k++) {
						   if (line.charAt(k) == '$') {
							   termLocal = line.substring(index, k);
							   System.out.println("termLocal: " + termLocal);
							   index = k+1;
							   break;
						   }
					   }
					}
				   if (term.toString().equals(termLocal) == false && dollarSignCount == 4) {
					   System.out.println(dollarSignCount);
					   System.out.println(term.toString());
					   System.out.println(termLocal);
					   System.out.println("break line 56");
					   break;
				   }
				   if(term.toString().equals(termLocal) && dollarSignCount == 4) {
					   System.out.println("line 63");
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
							   }
							   System.out.println(ID);
							   arr.add(getApplicationbyID(ID));
							   System.out.println("line 77 break");
							   break;
						   }
					   }
					   System.out.println("Line 81 break");
					   break;
				   }
			   }
		}
		return arr;
		
	}
	
	private static int readFromFile() throws FileNotFoundException, IOException {
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
	private static ArrayList<Object> readFromApplicationFile(int ID) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader((System.getProperty("user.dir") + "\\Database\\Applications.txt")));
		String line;
		int digits = 0;
		int number = ID;
		
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
			   if (i != digits) {
				   if (line.charAt(i) != IDinBytes[i] && line.charAt(i) != '$') {
					   break;
				   }
			   }
			   else if (line.charAt(i) == '$') {
				   applicationID = ID;
				   int index = i+1;
				   for(int k = index; k < line.length(); k++) {
					   if (line.charAt(k) == '$') {
						   state = line.substring(index, k);
						   index = k+1;
						   break;
					   }
				   }
				   for(int k = index; k < line.length(); k++) {
					   if (line.charAt(k) == '$') {
						   date = line.substring(index, k);
						   index = k+1;
						   break;
					   }
				   }
				   for(int k = index; k < line.length(); k++) {
					   if (line.charAt(k) == '$') {
						   name = line.substring(index, k);
						   index = k+1;
						   break;
					   }
				   }
				   for(int k = index; k < line.length(); k++) {
					   if (line.charAt(k) == '$') {
						   term = line.substring(index, k);
						   index = k+1;
						   break;
					   }
				   }
				   for(int k = index; k < line.length(); k++) {
					   if (line.charAt(k) == '$') {
						   fieldOfStudy = line.substring(index, k);
						   index = k+1;
						   break;
					   }
				   }
				   for(int k = index; k < line.length(); k++) {
					   if (line.charAt(k) == '$') {
						   fundingRequired = line.substring(index, k);
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
				   return application;
			   }
			   //System.out.println("didn't find tab");
		   }
		}
		br.close();
		return null;
	}
	public static void main(String [] args) {
		ArrayList<ArrayList> array = new ArrayList<ArrayList>();
		try {
			array = getApplicationbyFieldOfStudy(FieldOfStudy.ENGINEERING);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(array);
	}
}
