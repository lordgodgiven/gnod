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
			if (scolarite == null) {
				System.out.println("User null !!!!");
				Application.disconnect();
			}
			renderArgs.put("user", scolarite.login);
			renderArgs.put("status", "connected");
			renderArgs.put("typeUser", "scolarite");
		} else {
			renderArgs.put("status", "disconnected");
		}
	}
	
	public static void index() {
		render("scolarite/index.html");
	}
}
