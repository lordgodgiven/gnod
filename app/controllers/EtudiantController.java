package controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;

import models.Cours;
import models.Etudiant;
import models.Examen;
import models.Matiere;
import models.Note;
import models.PasserelleGoogle;
import models.Scolarite;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class EtudiantController extends Controller {
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
		render("etudiant/index.html", etudiant);
	}

	/**
	 * Affiche la page d'information de la matiere
	 * 
	 * @param id
	 *            identifiant du cours duquel l'utilisateur veut afficher sa
	 *            matière
	 */
	public static void afficheMatiere(long id) {
		Cours cours = Cours.findById(id);
		render(cours.matiere);
	}

	/**
	 * Methode appelee par un etudiant pour diffuser sa note
	 * 
	 * @param id
	 *            identificant de la note a diffuser a la classe
	 */
	public static void diffuseNote(long id) {
		Note note = Note.findById(id);
		note.estDiffusee = true;
		note.save();
		// Apres diffusion, on retourne sur la page qui proposent la detail
		// du cours
		EtudiantController.afficheMatiere(note.examen.cours.id);
	}

	/**
	 * Affiche la page de detail de la moyenne
	 */
	public static void detailMoyenne() {
		Etudiant etudiant = Etudiant.find("byLogin", Security.connected())
				.first();
		renderArgs.put("general", etudiant.calculMoyenneGenerale());
		render(etudiant.calculMoyenneDetailee());

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
		render("etudiant/diffusionNote.html", examen, lstNote);
	}
		
	public static void translateInEnglish(String document) {
		PasserelleGoogle.translateInEnglish(document);
	}
	
	public static void translateInSpanish(String document) {
		PasserelleGoogle.translateInSpanish(document);
	}
	
	public static void translateInDeutsch(String document) {
		System.out.println("taille du document dans etudiantcontroller:");
		System.out.println(document.length());
		renderJSON(PasserelleGoogle.translateInDeutsch(document));
	}
	
	public static void translateInFrench(String document) {
		renderText(PasserelleGoogle.translateInFrench(document));
	}
}
