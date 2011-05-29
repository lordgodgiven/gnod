package controllers;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import models.Enseignant;
import models.Etudiant;
import models.Scolarite;
import controllers.Secure.Security;
import play.data.validation.Required;
import play.mvc.After;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Application extends Controller {
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

	public static void index() {
		render();
	}
	/*
	 * Utilisateur n'est plus loggé
	 */
	public static void disconnect() {
		renderArgs = null;
		typeUser = null;
		try {
			Secure.logout();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Action d'appel du web Service google translate pour la traduction en anglais
	 */
	public static void traduitSiteAnglais() {
		
	}
	
	/**
	 * Action d'appel du web Service google translate pour la traduction en allemand
	 */
	public static void traduitSiteAllemand() {
		
	}
	
	/**
	 * Action d'appel du web Service google translate pour la traduction en francais
	 */
	public static void traduitSiteFrancais() {
		
	}
	
	@After
	static void verifConnectedUser() {
		if (typeUser != null) {
			if(typeUser.equals("etudiant"))
				EtudiantController.index();
			if(typeUser.equals("enseignant"))
				EnseignantController.index();
			if (typeUser.equals("scolarite"))
				ScolariteController.index();
		}
	}
	
}