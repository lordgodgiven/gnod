package controllers;

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
					}
				} else {
					typeUser = new String("enseignant");
					renderArgs.put("user", enseignant.login);
				}
			} else {
				typeUser = new String("etudiant");
				renderArgs.put("user", etudiant.login);
			}
			renderArgs.put("status", "connected");
			System.out.println("status a connected");
		} else {
			typeUser = null;
			System.out.println("status a disconnected");
			renderArgs.put("status", "disconnected");
		}
	}

	public static void index() {
		System.out.println("index");
		render();
	}
	
	public static void disconnect() {
		System.out.println("disconnect");
		//renderArgs.put("status", "disconnected");
		//renderArgs.put("user", null);
		//renderArgs.data.clear();
		renderArgs = null;
		typeUser = null;
		try {
			Secure.logout();
			if(Security.isConnected()) {
				System.out.println("déconnection nok");
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		Application.index();
	}

	public static void identifiate(String login, String password) {
		Etudiant etudiant = Etudiant.connect(login, password);
		if (etudiant == null) {
			Enseignant enseignant = Enseignant.connect(login, password);
			if (enseignant == null) {
				Scolarite scolarite = Scolarite.connect(login, password);
				if (scolarite == null) {
					// Page d'erreur : utilisateur inconnu
					render("Application/index.html");
				} else {
					System.out.println("User de type scolarite");
					renderArgs.put("user", scolarite.login);
					//render("Scolarite/index.html");
					ScolariteController.index();
				}
			} else {
				renderArgs.put("user", enseignant.login);
				render("Enseignant/index.html");
			}
		} else {
			renderArgs.put("user", etudiant.login);
			render("Etudiant/index.html");
		}
	}
	
	@After
	static void verifConnectedUser() {
		if (typeUser != null) {
			if(typeUser.equals("etudiant"))
				typeUser = typeUser;
			if(typeUser.equals("enseignant"))
				typeUser = typeUser;
			if (typeUser.equals("scolarite"))
				ScolariteController.index();
		}
	}
	
}