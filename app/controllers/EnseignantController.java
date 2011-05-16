package controllers;

import models.Enseignant;
import models.Etudiant;
import models.Scolarite;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
@With(Secure.class)
public class EnseignantController  extends Controller {
	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			Enseignant enseignant = Enseignant.find("byLogin",
					Security.connected()).first();
			if (enseignant == null) {
				System.out.println("User null !!!!");
				render("Application/index.html");
			}
			renderArgs.put("nom", enseignant.nom);
			renderArgs.put("prenom", enseignant.prenom);
			renderArgs.put("status", "connected");
		} else {
			renderArgs.put("status", "disconnected");
		}
	}
	
	public static void index() {
		render("enseignant/index.html");
	}
}
