package controllers;

import models.Etudiant;
import models.Scolarite;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
@With(Secure.class)
public class EtudiantController  extends Controller {
	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			Etudiant etudiant = Etudiant.find("byLogin",
					Security.connected()).first();
			if (etudiant == null) {
				System.out.println("User null !!!!");
				render("Application/index.html");
			}
			renderArgs.put("nom", etudiant.nom);
			renderArgs.put("prenom", etudiant.prenom);
			renderArgs.put("status", "connected");
		} else {
			renderArgs.put("status", "disconnected");
		}
	}
	
	public static void index() {
		render("etudiant/index.html");
	}
}
