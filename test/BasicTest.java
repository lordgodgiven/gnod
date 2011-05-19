import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import models.Enseignant;
import models.Etudiant;
import models.NewsFeedGen;
import models.Scolarite;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
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
		    // Retrouver l'enseignant � partir de son login
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
		    // Retrouver l'�tudiant � partir de son login
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
		// Retrouver l'enseignant � partir de son login
		Scolarite scolarite = Scolarite.find("byLogin", "lecapona").first();
		    
		// Test 
		assertNotNull(scolarite);
		assertEquals("lecapona", scolarite.login);
	}
	
	@Test
	public void generateJDomEtudiant() {
		NewsFeedGen.generate("Etudiant_1A_ISI_2.xml", "notes de SGBD disponibles", "1A ISI Gpe 1");
		NewsFeedGen.generate("Etudiant_1A_ISI_2.xml", "notes de SAP disponibles", "1A ISI Gpe 1");
		
		File fileEtudiant = new File("Etudiant_1A_ISI_2.xml");
		
		
		SAXBuilder sxb = new SAXBuilder();
		Document document = null;
		try {
			document = sxb.build(fileEtudiant);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Element racine = document.getRootElement();
        Element channel = racine.getChild("channel");
		// Test 
		assertNotNull(channel);
		assertEquals(channel.getChildren("item").size(), 2);
	}
	
	@Test
	public void generateJDomEnseignant() {
		NewsFeedGen.generate("Enseignant_Sebastien_Viardot.xml", "Examens des 1A ISI Gpe1 a noter", "Sebastien Viardot");
		File fileEnseignant = new File("Enseignant_Sebastien_Viardot.xml");
		SAXBuilder sxb = new SAXBuilder();
		Document document = null;
		try {
			document = sxb.build(fileEnseignant);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Element racine = document.getRootElement();
        Element channel = racine.getChild("channel");
		// Test 
		assertNotNull(channel);
		assertEquals(channel.getChildren("item").size(), 1);
	}
	
	 @Before
	 public void setup() {
		 Fixtures.deleteDatabase();
	 }
}
