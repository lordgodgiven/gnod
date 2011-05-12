package controllers;

import models.Enseignant;
import models.Etudiant;
import models.Scolarite;
import controllers.Secure.Security;
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
						Application.indexScolarite();
					}
				} else {
					renderArgs.put("user", enseignant.login);
					Application.indexEnseignant();
				}
			} else {
				renderArgs.put("user", etudiant.login);
				Application.indexEtudiant();
			}
		}
	}

	public static void index(String typeUser) {
		render();
	}

	public static void indexEtudiant() {
		render();
	}

	public static void indexEnseignant() {
		render();
	}

	public static void indexScolarite() {
		render();
	}
}