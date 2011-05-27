 package controllers;

import groovyjarjarantlr.collections.List;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import models.Cours;
import models.Enseignant;
import models.Examen;
import models.Matiere;
import models.NewsFeedGen;
import models.Note;
import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class EnseignantController extends Controller {
	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			Enseignant enseignant = Enseignant.find("byLogin",
					Security.connected()).first();
			if (enseignant == null) {
				System.out.println("User null !!!!");
				Application.disconnect();
			}
			renderArgs.put("nom", enseignant.nom);
			renderArgs.put("prenom", enseignant.prenom);
			renderArgs.put("status", "connected");
			renderArgs.put("typeUser", "enseignant");
		} else {
			renderArgs.put("status", "disconnected");
		}
	}


	public static void index() {
		Enseignant enseignant = Enseignant.find("byLogin", Security.connected()).first();
		Set<Matiere> matieres = new HashSet<Matiere> ();
		for (Cours coursTmp : enseignant.cours){
			if (!matieres.contains(coursTmp.matiere))matieres.add(coursTmp.matiere);
		}
		// Permet d'afficher sur la page d'accueil les cours de l'enseignant
		render("enseignant/index.html", enseignant, matieres);
	}
	
	/**
	 * Affiche les examens pour un cours selectionne. A partie du cours, on peut trouver 
	 * les examens par acces direct dans la vue
	 * @param id identifiant du cours (<=> show(id) pour un post )dans le tuto play
	 * => Selection de l'id a ajouter dans la vue
	 */
	public static void afficheExamens(long id) {
		Cours cours = Cours.findById(id);
	    String randomID = Codec.UUID();
	    render(cours, randomID);
	}
	
	/**
	 * @param id identifiant du cours (<=> show(id) pour un post )dans le tuto play
	 * => Selection de l'id a ajouter dans la vue
	 */
	 public static void creeExamen(
			 	long id,
		        @Required(message="A name is required") String nom, 
		        @Required(message="A date is required") Date date, 
		        @Required(message="A coefficient is required") Float coef, 
		        String randomID) {
		 	Cours cours = Cours.findById(id);
		    if(validation.hasErrors()) {
		    	flash.error("Formulaire invalide");
		        render("Enseignant/examenForm.html", cours, randomID);
		    }
		    Examen examen = new Examen(nom, date, coef, cours);
		    examen.save();
		    flash.success("Examen ajouté");
		    Cache.delete(randomID);
		    // On revient sur la page d'affichage des examens du cours
		    afficheExamens(id);
		}
	 
	 
		/**
		 * Affiche les résultats pour un examen selectionne. les résultats sont modifiables
		 * si la validation n'a pas été demandée. 
		 * A partie de l'examen, on peut trouver les notes par acces direct dans la vue
		 * 
		 * @param id identifiant de l'examen (<=> show(id) pour un post )dans le tuto play
		 * => Selection de l'id a ajouter dans la vue
		 */
		public static void afficheResultats(long id) {
			Examen examen = Examen.findById(id);
		    String randomID = Codec.UUID();
		    render("Enseignant/notesForm.html", examen, randomID);
		}
		
		/**
		 * Affiche les resultats pour un cours selectionne. les resultats sont modifiables
		 * si la validation n'a pas été demandée. 
		 * A partie de l'examen, on peut trouver les notes par acces direct dans la vue
		 * 
		 * @param id identifiant de l'examen (<=> show(id) pour un post )dans le tuto play
		 * => Selection de l'id a ajouter dans la vue
		 */
		// TODO : gérer le nombre dynamique de note a entrer
		// Apparemment, des listes groovy existent : a faire a deux
		public static void modifResultats(long id,
				List valNotes, 
		        String randomID) {
			Examen examen = Examen.findById(id);
			if(validation.hasErrors()) {
		        render("Enseignant/notesForm.html", examen, randomID);
		    }
		    // Note validees non modifiables
			if (examen.noteValidee) {
				flash.error("Vos notes sont déjà validées");
		    } else if (examen.notes != null && examen.notes.size() > 0){
		    	int cpt = 0;
		    	for (Note note : examen.notes) {
		    		note.note = (Integer) valNotes.elementAt(cpt);
		    		note.save();
		    		cpt++;
		    	}
		    } else {
		    	// Les notes sont a créer, comment renseigner l'etudiant ?
		    	/*Note note = new Note(etudiant, examen, valNote);
		    	note.save();*/
		    }
			afficheResultats(id);
		}
		
		/**
		 * @param id identifiant de l'examen (<=> show(id) pour un post )dans le tuto play
		 * => Selection de l'id a ajouter dans la vue
		 */
		public static void valideResultats(long id) {
			Examen examen = Examen.findById(id);
			if (examen.notes != null && examen.notes.size() > 0) {
				examen.noteValidee = true;
				// Mise a jours du flux RSS Etudiant a la validation du resultat
				NewsFeedGen.generate("public/rss/Etudiant_"+examen.cours.classe.nomClasse+".xml",
						"notes de " +examen.nom+ " disponibles", examen.cours.classe.nomClasse);
				
				// Mise a jour du flux RSS Enseignant : suppression de la news correspondant
				// a l'examen a noter
				NewsFeedGen.supprimeExamen(examen.cours.classe, examen);
			} else {
				flash.error("Vos notes ne sont pas renseignées");
				afficheResultats(id);
			}			
		}
}
