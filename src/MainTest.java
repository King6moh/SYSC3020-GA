import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;


public class MainTest {

	@Test
	public void test() throws FileNotFoundException, IOException {
		new Application("Kais", Term.FALL, FieldOfStudy.ARTS, true);
		
		ArrayList<Object> application = new ArrayList<Object>();
		application.add((int)1);
		application.add(ApplicationState.OPEN.toString());
		application.add(new Date().toString());
		application.add("Kais");
		application.add(Term.FALL.toString());
		application.add(FieldOfStudy.ARTS.toString());
		application.add(true);
		
		ArrayList<Object> storedApp = ApplicationDB.getApplicationbyID(1);
		
		assertTrue((int)application.get(0) == (int)storedApp.get(0));
		assertTrue(((String)application.get(1)).equals((String)storedApp.get(1)));
		//assertTrue(((String)application.get(2)).equals((String)storedApp.get(2)));
		assertTrue(((String)application.get(3)).equals((String)storedApp.get(3)));
		assertTrue(((String)application.get(4)).equals((String)storedApp.get(4)));
		assertTrue(((String)application.get(5)).equals((String)storedApp.get(5)));
		assertTrue(application.get(6).toString().equals(storedApp.get(6).toString()));
		
		storedApp = ApplicationDB.getApplicationbyName("Kais");
		
		assertTrue((int)application.get(0) == (int)storedApp.get(0));
		assertTrue(((String)application.get(1)).equals((String)storedApp.get(1)));
		//assertTrue(((String)application.get(2)).equals((String)storedApp.get(2)));
		assertTrue(((String)application.get(3)).equals((String)storedApp.get(3)));
		assertTrue(((String)application.get(4)).equals((String)storedApp.get(4)));
		assertTrue(((String)application.get(5)).equals((String)storedApp.get(5)));
		assertTrue(application.get(6).toString().equals(storedApp.get(6).toString()));
		
		ArrayList<ArrayList> stored = ApplicationDB.getApplicationbyFieldOfStudy(FieldOfStudy.ARTS);
		
		assertTrue((int)application.get(0) == (int)stored.get(0).get(0));
		assertTrue(((String)application.get(1)).equals((String)stored.get(0).get(1)));
		//assertTrue(((String)application.get(2)).equals((String)stored.get(0).get(2)));
		assertTrue(((String)application.get(3)).equals((String)stored.get(0).get(3)));
		assertTrue(((String)application.get(4)).equals((String)stored.get(0).get(4)));
		assertTrue(((String)application.get(5)).equals((String)stored.get(0).get(5)));
		assertTrue(application.get(6).toString().equals(stored.get(0).get(6).toString()));
		
		stored = ApplicationDB.getApplicationbyTerm(Term.FALL);
		
		assertTrue((int)application.get(0) == (int)stored.get(0).get(0));
		assertTrue(((String)application.get(1)).equals((String)stored.get(0).get(1)));
		//assertTrue(((String)application.get(2)).equals((String)stored.get(0).get(2)));
		assertTrue(((String)application.get(3)).equals((String)stored.get(0).get(3)));
		assertTrue(((String)application.get(4)).equals((String)stored.get(0).get(4)));
		assertTrue(((String)application.get(5)).equals((String)stored.get(0).get(5)));
		assertTrue(application.get(6).toString().equals(stored.get(0).get(6).toString()));
		
		storedApp.set(1, ApplicationState.SUBMITTED.toString());
		application.set(1, ApplicationState.SUBMITTED.toString());
		
		ApplicationDB.replacebyID(1, storedApp);
		
		storedApp = ApplicationDB.getApplicationbyID(1);
		
		assertTrue((int)application.get(0) == (int)storedApp.get(0));
		assertTrue(((String)application.get(1)).equals((String)storedApp.get(1)));
		//assertTrue(((String)application.get(2)).equals((String)storedApp.get(2)));
		assertTrue(((String)application.get(3)).equals((String)storedApp.get(3)));
		assertTrue(((String)application.get(4)).equals((String)storedApp.get(4)));
		assertTrue(((String)application.get(5)).equals((String)storedApp.get(5)));
		assertTrue(application.get(6).toString().equals(storedApp.get(6).toString()));
	}

}
