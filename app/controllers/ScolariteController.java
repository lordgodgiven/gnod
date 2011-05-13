package controllers;

import models.Scolarite;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class ScolariteController extends Controller {
	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			Scolarite scolarite = Scolarite.find("byLogin",
					Security.connected()).first();
			renderArgs.put("user", scolarite.login);
		}
	}
	
	public static void index() {
		System.out.println("Entree dans la zone scolarité");
	}
}
