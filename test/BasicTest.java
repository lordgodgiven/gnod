import java.text.SimpleDateFormat;

import models.Enseignant;
import models.Etudiant;
import models.Scolarite;

import org.junit.Before;
import org.junit.Test;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import play.test.Fixtures;
import play.test.UnitTest;

public class BasicTest extends UnitTest {

	@Test
	public void creeEtChercheEnseignant() {
		try {
			new Enseignant("ChabanasM", "secret", "Chabanas", "Matthieu", 
			    		new SimpleDateFormat("dd/MM/yyyy").parse("31/02/1975")).save();
		    // Retrouver l'enseignant à partir de son login
		    Enseignant mat = Enseignant.find("byLogin", "ChabanasM").first();
		    
		    // Test 
		    assertNotNull(mat);
		    assertEquals("ChabanasM", mat.login);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void creeEtChercheEtudiant() {
		try {
			new Etudiant("Romain", "Durant", "Durantr", "secret",  
			    		new SimpleDateFormat("dd/MM/yyyy").parse("31/02/1987")).save();
		    // Retrouver l'étudiant à partir de son login
			Etudiant etudiant = Etudiant.find("byLogin", "Durantr").first();
		    
		    // Test 
		    assertNotNull(etudiant);
		    assertEquals("Durantr", etudiant.login);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void creeEtChercheScolarite() {
		new Scolarite("lecapona", "secret").save();
		// Retrouver l'enseignant à partir de son login
		Scolarite scolarite = Scolarite.find("byLogin", "lecapona").first();
		    
		// Test 
		assertNotNull(scolarite);
		assertEquals("lecapona", scolarite.login);
	}
	
	 @Before
	 public void setup() {
		 Fixtures.deleteDatabase();
	 }
}
