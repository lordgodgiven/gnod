package controllers;

import models.Cours;
import models.Etudiant;
import models.Matiere;
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
			renderArgs.put("typeUser", "etudiant");
		} else {
			renderArgs.put("status", "disconnected");
		}
	}
	
	/**
	 * Page d'accueil de l'espace etudiant
	 */
	public static void index() {
		Etudiant etudiant = Etudiant.find("byLogin",
				Security.connected()).first();
		// Avec la classe de l'étudiant, on retrouve les cours, et donc les matieres correspondantes
		render("etudiant/index.html", etudiant.classe);
	}
	
	/**
	 * Affiche la page d'information de la matiere
	 * @param id identifiant du cours duquel l'utilisateur veut afficher sa matière 
	 */
	public static void afficheMatiere(long id) {
		Cours cours = Cours.findById(id);
		render(cours.matiere);
	}
	
	/**
	 * Affiche la page de detail de la moyenne 
	 */
	public static void detailMoyenne() {
		Etudiant etudiant = Etudiant.find("byLogin",
				Security.connected()).first();		
		renderArgs.put("general", etudiant.calculMoyenneGenerale());
		render(etudiant.calculMoyenneDetailee());
	}
}
