import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import models.Enseignant;
import models.Etudiant;
import models.Matiere;
import models.NewsFeedGen;
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
		List<Etudiant> etudiant = Etudiant.findAll();
		Etudiant recherche = null;
		assertNotNull(etudiant);
		System.out.println("---------Etudiant-----------" +etudiant.size());
		for (Etudiant etudianttmp : etudiant) {
			System.out.println("Etudiant " +etudianttmp.login);
			if (etudianttmp.login.equals("dulacf")) {
				recherche = etudianttmp;
			}
		}
		assertNotNull(recherche);
		HashMap<Matiere, Float> moyennes = recherche.calculMoyenneDetailee();
		assertEquals(moyennes.get(Matiere.find("byNom","Programmation web").first()), (Float)14f);
		assertEquals(moyennes.get(Matiere.find("byNom","Langage C et compilation").first()), (Float)18f);
		assertEquals(recherche.calculMoyenneGenerale(), (Float)17.2f);
	}
	 @Before
	 public void setup() {
		 new File("public/rss/Enseignant_Sebastien Viardot.xml").delete();
		 new File("public/rss/Etudiant_1A ISI 2.xml").delete();
		 Fixtures.deleteDatabase();
	 }
}
