package controllers;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import models.Cours;
import models.Enseignant;
import models.Etudiant;
import models.Examen;
import models.NewsFeedGen;
import models.Scolarite;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

import controllers.Secure.Security;
@With(Secure.class)
public class Export extends Controller  {
	private static String typeUser;
	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			Etudiant etudiant = Etudiant.find("byLogin", Security.connected())
					.first();
			if (etudiant == null) {
				Enseignant enseignant = Enseignant.find("byLogin",
						Security.connected()).first();
				if (enseignant == null) {
					Scolarite scolarite = Scolarite.find("byLogin",
							Security.connected()).first();
					if (scolarite == null) {
						// Page d'erreur : utilisateur inconnu
						System.out.println("User inconnu");
					} else {
						typeUser = new String("scolarite");
						renderArgs.put("user", scolarite.login);
						renderArgs.put("typeUser", typeUser);
						
					}
				} else {
					typeUser = new String("enseignant");
					renderArgs.put("user", enseignant.login);
					renderArgs.put("typeUser", typeUser);
				}
			} else {
				typeUser = new String("etudiant");
				renderArgs.put("user", etudiant.login);
				renderArgs.put("typeUser", typeUser);
			}
			renderArgs.put("status", "connected");
		} else {
			typeUser = null;
			renderArgs.put("status", "disconnected");
		}
	}
	/**
	 * Fichier d'abonnement a un flux RSS
	 */
	public static void rss() {
		File file = null;
		if (renderArgs.get("typeUser").equals("enseignant")) {
			Enseignant enseignant = Enseignant.find("byLogin",
					Security.connected()).first();
			file = new File("public/rss/Enseignant_" +enseignant.prenom+ 
					"_" +enseignant.nom+ ".xml");
		} else if (renderArgs.get("typeUser").equals("etudiant")) {
			Etudiant etudiant = Etudiant.find("byLogin",
					Security.connected()).first();
			file = new File("public/rss/Etudiant_" +etudiant.classe.nomClasse+ ".xml");
		} else {
			flash.error("aucun flux RSS pour votre profil de connexion");
			Application.index();
		}
		
		if (file == null || !file.exists()) {			
			if (renderArgs.get("typeUser").equals("enseignant")) {
				Enseignant enseignant = Enseignant.find("byLogin",
						Security.connected()).first();
				for (Cours coursTmp : enseignant.cours) {
					for (Examen examenTmp : coursTmp.examen) {
						if (!examenTmp.noteValidee) {
							Date today = new Date();
							if (today.after(examenTmp.date)
									&& !NewsFeedGen.chercherExamen(enseignant,
											examenTmp)) {
								NewsFeedGen.generate("public/rss/Enseignant_"
										+ enseignant.prenom + "_"
										+ enseignant.nom + ".xml", "Examens des"
										+ examenTmp.cours.classe.nomClasse + "de"
										+ examenTmp.cours.matiere.nom + "a noter",
										new String(enseignant.prenom + "_"
												+ enseignant.nom));
							}
						}
					}
				}
				// Aucune new A ajouter pour l'enseignant => creation factice
				if (!file.exists()) {
					NewsFeedGen.generate("public/rss/Enseignant_"
										+ enseignant.prenom + "_"
										+ enseignant.nom + ".xml", "Création du flux d'actualité",
										new String(enseignant.prenom + "_"
												+ enseignant.nom));
				}
			} else if (renderArgs.get("typeUser").equals("etudiant")) {
				Etudiant etudiant = Etudiant.find("byLogin",
						Security.connected()).first();
				NewsFeedGen.generate("public/rss/Etudiant_"
						+ etudiant.classe.nomClasse + ".xml", "Création du flux d'actualité",
						etudiant.classe.nomClasse);
			} else {
				flash.error("aucun flux RSS pour votre profil de connexion");
				System.out.println("Fic null ou n'existe pas");
				Application.index();
			}
		}
        Document document = null;
		try {	
			// création d'une fabrique de documents
			DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
			
			// création d'un constructeur de documents
			DocumentBuilder constructeur = fabrique.newDocumentBuilder();
			document = constructeur.parse(file);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			flash.error(e.toString());
			Application.index();
		} catch (IOException e) {
			e.printStackTrace();
			flash.error(e.toString());
			Application.index();
		} catch (SAXException e) {
			e.printStackTrace();
			flash.error(e.toString());
			Application.index();
		}
		if (document == null) {
			flash.error("erreur en parsant le document");
			Application.index();
		}
		renderXml(document);
	}
}
