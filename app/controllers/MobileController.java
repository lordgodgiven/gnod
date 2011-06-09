package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Enseignant;
import models.Etudiant;
import models.Examen;
import models.Note;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
@With(Secure.class)
public class MobileController  extends Controller {
	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			Etudiant etudiant = Etudiant.find("byLogin", Security.connected())
					.first();
			if (etudiant == null) {
				System.out.println("User null !!!!");
				Application.disconnect();
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
		Etudiant etudiant = Etudiant.find("byLogin", Security.connected())
				.first();
		// Avec la classe de l'étudiant, on retrouve les cours, et donc les
		// matieres correspondantes
		render("mobileetudiant/index.html", etudiant);
	}
	
	/**
	 * Affiche les notes diffusees de sa classe pour l'examen selectionne
	 * @param id identifiant de l'examen dont l'etudiant veut les notes
	 */
	public static void diffusionNote(long id) {
		Examen examen = Examen.findById(id);
		List<Note> lstNote = new ArrayList<Note>();
		for (Note noteTmp : examen.notes) {
			if (noteTmp.estDiffusee) {
				lstNote.add(noteTmp);
			}
		}
		// En securite (pour eviter les failles de l'appli mobile, on ajoute la note
		// de l'eleve consultant a la liste de note
		Etudiant etudiant = Etudiant.find("byLogin", Security.connected()).first();
		for (Note noteTmp : etudiant.notes) {
			if (noteTmp.examen == examen && noteTmp.estDiffusee == false) {
				noteTmp.estDiffusee = true;
				noteTmp.save();
				lstNote.add(noteTmp);
			}
		}
		// On renvoit la liste des notes a afficher : possibilite d'acceder
		// au proprietaire 
		render("mobileetudiant/diffusionNote.html", examen, lstNote);
	}
}
