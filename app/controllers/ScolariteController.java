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
					//Security.connected()).first();
					Security.connected()).first();
			//renderArgs.put("user", renderArgs.get("user"));
		}
	}
	
	public static void index() {
		System.out.println("Entree dans la zone scolarité"+renderArgs.get("user"));
		render("scolarite/index.html");
	}
}
