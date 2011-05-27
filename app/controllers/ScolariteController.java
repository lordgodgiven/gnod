package controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import models.Classe;
import models.Cours;
import models.Enseignant;
import models.Etudiant;
import models.Scolarite;
import play.data.validation.Required;
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
	
	/**
	 * Renvoi les classes et la liste des etudiants sans classe
	 * */
	public static void listeClasses() {
		List<Classe> classes = Classe.findAll();
		List<Etudiant> etudiantsSansClasse = Etudiant.sansClasse();
		render(classes, etudiantsSansClasse);
	}
	
	/**
	 * Creation/modification d'une classe
	 * @param id identifiant de la classe a modifier
	 */
    public static void classeForm(Long id) {
        if(id != null) {
            Classe classe = Classe.findById(id);
            render(classe);
        }
        render();
    }
    
    /**
     * Creation/Modification d'une classe
     * @param id identifiant de la classe si modification
     * @param nomClasse nom de la classe a creer/modifier
     * @param lstEtudiants liste des etudiants de la classe
     * @param lstEtudiantsSC liste des etudiants qui n'appartienne a aucune classe
     */
    public static void postClasse(long id,
	        @Required(message="A name is required") String nomClasse, 
	        @Required(message="Des étudiants doivents être sélectionné") 
	        		List<Etudiant> lstEtudiants,
	        		List<Etudiant> lstEtudiantsSC,
	        		String randomID) {
    	if(validation.hasErrors()) {
	    	flash.error("Formulaire invalide");
	        render("Scolarite/classeForm.html", Classe.findById(id), randomID);
	    }
    	Classe classe = null;
    	if (id > 0) {
    		classe = Classe.findById(id);
    		classe.nomClasse = nomClasse;
    	} else {
    		classe = new Classe(nomClasse);
    	}
    	classe.etudiant = new HashSet<Etudiant>(lstEtudiants);
    	classe.save();
    	Etudiant.saveEtudiantSC(lstEtudiantsSC);
    	flash.success("Classe " +classe.nomClasse+ " ajoutée");
    	ScolariteController.listeClasses();
    }
    
	/**
	 * Renvoi les 20 premiers enseignants dans l'ordre alphabetique
	 * */
	public static void listeEnseignant() {
		List<Enseignant> liste20Enseignants = Cours.find20Enseignant(null);
		render(liste20Enseignants);
	} 
    
}
