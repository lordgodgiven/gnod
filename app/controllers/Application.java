package controllers;

import models.Enseignant;
import models.Etudiant;
import models.Scolarite;
import controllers.Secure.Security;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Application extends Controller {

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
						renderArgs.put("user", scolarite.login);
					}
				} else {
					renderArgs.put("user", enseignant.login);
				}
			} else {
				renderArgs.put("user", etudiant.login);
			}
		}
	}

	public static void index(String typeUser) {
		render();
	}

	public static void identifiate(String login, String password) {
		Etudiant etudiant = Etudiant.find("byLogin", login).first();
		if (etudiant == null) {
			Enseignant enseignant = Enseignant.find("byLogin",
					login).first();
			if (enseignant == null) {
				Scolarite scolarite = Scolarite.find("byLogin",
						login).first();
				if (scolarite == null) {
					// Page d'erreur : utilisateur inconnu
					render("Application/index.html");
				} else {
					renderArgs.put("user", scolarite.login);
					render("Scolarite/index.html");
				}
			} else {
				renderArgs.put("user", enseignant.login);
			}
		} else {
			renderArgs.put("user", etudiant.login);
			render("Etudiant/index.html");
		}
	}
}