 package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Cours;
import models.Enseignant;
import models.Examen;
import models.Matiere;
import models.NewsFeedGen;
import models.Note;
import models.PasserelleGoogle;
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
		Set<String> matieres = new HashSet<String> ();
		for (Cours coursTmp : enseignant.cours){
			if (!matieres.contains(coursTmp.matiere.nom))matieres.add(coursTmp.matiere.nom);
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
	    render("enseignant/afficheExamens.html", cours, randomID);
	}
	
	/**
	 * Affiche le formulaire d'ajout d'examens pour un cours selectionne.
	 * @param id identifiant du cours (<=> show(id) pour un post )dans le tuto play
	 * => Selection de l'id a ajouter dans la vue
	 */
	public static void ajouteExamens(
			long id, 
	        String randomID) {
		Cours cours = Cours.findById(id);
	    render("enseignant/ajouteExamens.html", cours, randomID);
	}
	
	/**
	 * @param id identifiant du cours (<=> show(id) pour un post )dans le tuto play
	 * => Selection de l'id a ajouter dans la vue
	 */
	 public static void creeExamen(
			 	long id,
		        @Required(message="A name is required") String nom, 
		        @Required(message="A day is required") String day, 
		        @Required(message="A month is required") String month, 
		        @Required(message="A year is required") String year, 
		        @Required(message="A coefficient is required") Float coef, 
		        String randomID) {
		 	Cours cours = Cours.findById(id);
		    if(validation.hasErrors()) {
		    	
		    	 for(play.data.validation.Error error : validation.errors()) {
		             System.out.println(error.message());
		         }
		    	 flash.error("Formulaire invalide");
		        render("Enseignant/ajouteExamens.html", cours, randomID);
		    }
		    
		    DateFormat formatter ; 
		    Date dateExam ;
		    String date = day + "-" + month + "-" + year;
		     formatter = new SimpleDateFormat("dd-MM-yy");
		     try {
		    dateExam = (Date)formatter.parse(date);
		    System.out.println(dateExam);
		    Examen examen = new Examen(nom, dateExam, coef, cours);
		    examen.save();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				 flash.error("Erreur : " + e);
			     render("Enseignant/ajouteExamens.html", cours, randomID);
			}
		    
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
		public static void afficheResultats(long idcours, long idexam) {
			Cours cours = Cours.findById(idcours);
		    Examen examen = Examen.findById(idexam);
		    String randomID = Codec.UUID();
		    render("Enseignant/notesForm.html", cours, examen, randomID);
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
		public static void modifResultats(long idcours, long idexam,
				List<Integer> valNotes, 
		        String randomID) {
			Cours cours = Cours.findById(idcours);
		    Examen examen = Examen.findById(idexam);
			if(validation.hasErrors()) {
		        render("Enseignant/notesForm.html", examen, randomID);
		    }
		    // Note validees non modifiables
			if (examen.noteValidee) {
				flash.error("Vos notes sont déjà validées");
		    } else if (examen.notes != null && examen.notes.size() > 0){
		    	int cpt = 0;
		    	for (Note note : examen.notes) {
		    		note.note = (Integer) valNotes.get(cpt);
		    		note.save();
		    		cpt++;
		    	}
		    } else {
		    	// Les notes sont a créer, comment renseigner l'etudiant ?
		    	/*Note note = new Note(etudiant, examen, valNote);
		    	note.save();*/
		    }
			afficheResultats(idcours,idexam);
		}
		
		/**
		 * @param id identifiant de l'examen (<=> show(id) pour un post )dans le tuto play
		 * => Selection de l'id a ajouter dans la vue
		 */
		public static void valideResultats(long idcours, long idexam) {
			Cours cours = Cours.findById(idcours);
		    Examen examen = Examen.findById(idexam);
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
				afficheResultats(idcours,idexam);
			}			
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
