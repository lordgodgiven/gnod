import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import models.Cours;
import models.Enseignant;
import models.Etudiant;
import models.Examen;
import models.Matiere;
import models.NewsFeedGen;
import models.Note;
import models.Scolarite;

import org.h2.constant.SysProperties;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Before;
import org.junit.Test;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import controllers.Secure.Security;

import play.test.Fixtures;
import play.test.UnitTest;

public class BasicTest extends UnitTest {
	@Test
	public void fullTest() {
	    Fixtures.loadModels("data.yml");
	    
	    // Count things
	    assertEquals(1, Enseignant.count());
	    assertEquals(2, Etudiant.count());
	    assertEquals(2, Scolarite.count());
	 
	    // Try to connect as users
	    assertNotNull(Enseignant.connect("viardots", "secret"));
	    assertNotNull(Scolarite.connect("marysc", "secret"));
	    assertNotNull(Etudiant.connect("dulacf", "secret"));
	    assertNull(Enseignant.connect("viardots", "badpassword"));
	    assertNull(Scolarite.connect("viardots", "secret"));
	    Enseignant sViardots = Enseignant.find("byLogin", "viardots").first();
	    assertNotNull(sViardots);
	    Etudiant fdulac = Etudiant.find("byLogin", "dulacf").first();
	    assertNotNull(fdulac);
	    // les cours de sebastien viardot
	    List<Cours> svCours = Cours.find("enseignant.login", "viardots").fetch();
	    assertEquals(2, svCours.size());
	 
	    // les examens de sebastien viardot
	    List<Examen> svExamen = Examen.find("cours.enseignant.login", "viardots").fetch();
	    assertEquals(5, svExamen.size());
	 
	    // les notes données par sebastien viardot
	    List<Note> svnote = Note.find("examen.cours.enseignant.login", "viardots").fetch();
	    assertEquals(8, svnote.size());
	}
	
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
		    assertTrue(Enseignant.exist("ChabanasM"));
		    assertTrue(Etudiant.exist("ChabanasM"));
		    assertTrue(Scolarite.exist("ChabanasM"));
		    
		    assertNull(Enseignant.find("byLogin", "ChabanasMV").first());		    
		    assertNull(Etudiant.find("byLogin", "ChabanasMV").first());
		    assertNull(Scolarite.find("byLogin", "ChabanasMV").first());

		    assertFalse(Enseignant.exist("ChabanasMV"));
		    assertFalse(Etudiant.exist("Chabanas"));
		    assertFalse(Scolarite.exist("Chaban"));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void creeEtChercheEtudiant() {
		try {
			new Etudiant("durantr", "secret", "Durant", "Romain",   
			    		new SimpleDateFormat("dd/MM/yyyy").parse("31/02/1987")).save();
		    // Retrouver l'�tudiant � partir de son login
			Etudiant etudiant = Etudiant.find("byLogin", "durantr").first();
		    
		    // Test 
		    assertNotNull(etudiant);
		    assertEquals("durantr", etudiant.login);
		    assertTrue(Enseignant.exist("durantr"));
		    assertTrue(Etudiant.exist("durantr"));
		    assertTrue(Scolarite.exist("durantr"));
		    assertFalse(Enseignant.exist("durantrV"));
		    assertFalse(Etudiant.exist("durant"));
		    assertFalse(Scolarite.exist("dura"));
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
	    assertTrue(Enseignant.exist("lecapona"));
	    assertTrue(Etudiant.exist("lecapona"));
	    assertTrue(Scolarite.exist("lecapona"));
	    assertFalse(Enseignant.exist("lecaponaV"));
	    assertFalse(Etudiant.exist("lecapon"));
	    assertFalse(Scolarite.exist("lecap"));
	}
	
	@Test
	public void generateJDomEtudiant() {
		NewsFeedGen.generate("public/rss/Etudiant_1A ISI 2.xml", "notes de SGBD disponibles", "1A ISI Gpe 1");
		NewsFeedGen.generate("public/rss/Etudiant_1A ISI 2.xml", "notes de SAP disponibles", "1A ISI Gpe 1");
		
		File fileEtudiant = new File("public/rss/Etudiant_1A ISI 2.xml");
		
		
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
		NewsFeedGen.generate("public/rss/Enseignant_Sebastien Viardot.xml", "Examens des 1A ISI Gpe1 a noter", "Sebastien Viardot");
		File fileEnseignant = new File("public/rss/Enseignant_Sebastien Viardot.xml");
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
		
	@Test
	public void calculMoyenne() {
		Fixtures.loadModels("data.yml");
		//Etudiant etudiant = Etudiant.find("byLogin", "dulacf").first();
		Etudiant fdulac = Etudiant.find("byLogin", "dulacf").first();
		assertNotNull(fdulac);
		//assertNotNull(recherche=etudiant);
		HashMap<Matiere, Float> moyennes = fdulac.calculMoyenneDetailee();
		Matiere progWeb = Matiere.find("byNom","Programmation web").first();
		Matiere langageC = Matiere.find("byNom","Langage C et compilation").first();
		assertNotNull(progWeb);
		assertNotNull(langageC);
		assertEquals(moyennes.get(progWeb), (Float) 14f);
		assertEquals(moyennes.get(langageC), (Float)18f);
		assertEquals(fdulac.calculMoyenneGenerale(), (Float)17.2f);
	}
	
	@Test
	public void fonctionsRecherche() {
		new Enseignant("login", "password", "Robert", "Jean Jacques", new Date()).save();
		assertEquals(1, Enseignant.cherche("Robert").size());
		assertEquals( "Jean Jacques", Enseignant.cherche("ober").get(0).prenom);
		assertEquals(1, Enseignant.cherche("acque").size());
		assertEquals(1, Enseignant.cherche("acque Robert").size());
		assertEquals(1, Enseignant.cherche("JEAN").size());
		assertEquals(1, Enseignant.cherche("Jean Jacques Robert").size());
		
		new Etudiant("login", "password", "Dulac", "Quentin", new Date()).save();
		new Enseignant("login2", "password", "Dupond", "Jean", new Date()).save();
		new Enseignant("login3", "password", "Dupond", "Jean Jacques", new Date()).save();
		assertEquals(1, Etudiant.cherche("Quentin").size());
		assertEquals(1, Etudiant.cherche("QueNtin dulac").size());
		assertEquals(3, Enseignant.cherche("Jean").size());
		assertEquals(2, Enseignant.cherche("Jean JAcques").size());
	}
	
	 @Before
	 public void setup() {
		 new File("public/rss/Enseignant_Sebastien Viardot.xml").delete();
		 new File("public/rss/Etudiant_1A ISI 2.xml").delete();
		 Fixtures.deleteDatabase();
	 }
}
