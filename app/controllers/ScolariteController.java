package controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import models.Classe;
import models.Cours;
import models.Enseignant;
import models.Etudiant;
import models.Matiere;
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
			renderArgs.put("nom", " ");
			renderArgs.put("prenom", scolarite.login);
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
	
    /*********************************************************************************/
    /**********************        PARTIE CLASSE            **************************/
    /*********************************************************************************/
	
	/**
	 * Renvoi les 20 premieres classes et la liste des etudiants sans classe
	 * */
	public static void listeClasses() {
		List<Classe> liste20Classes = Classe.find20Classes();
		// Booleens pour savoir si un appel a previous/next est possible
		renderArgs.put("previous", false);
		renderArgs.put("next", (Classe.findAll().size() > 20));
		List<Etudiant> etudiantsSansClasse = Etudiant.sansClasse();
		render(liste20Classes, etudiantsSansClasse);
	}
	
	/**
	 * Renvoi les 20 prochaines classes dans l'ordre alphabetique
	 * @param id identifiant de la derniere classe affichee
	 */
	public static void listeNextClasses(Long id) {
		List<Classe> liste20Classes = Classe.next20Classes(id);
		renderArgs.put("previous", true);
		// Si la liste n'est pas pleine, c'est qu'on ne peut pas appeler encore un next
		renderArgs.put("next", (liste20Classes.size() == 20));
		render("Scolarite/listeClasses.html", liste20Classes);
	} 
	
	/**
	 * Renvoi les 20 precedente classes dans l'ordre alphabetique
	 * @param id identifiant de la derniere classes affichee
	 */
	public static void listePreviousClasses(Long id) {
		List<Classe> liste20Classes = Classe.previous20Classes(id);
		renderArgs.put("next", true);
		// Si la liste n'est pas pleine, c'est qu'on ne peut pas appeler encore un previous
		renderArgs.put("previous", (liste20Classes.size() == 20));
		render("Scolarite/listeClasses.html", liste20Classes);
	} 

	/**
	 * Supprimer une classe
	 * @param id identifiant de la classe a supprimer
	 */
	public static void supprimerClasse(long id) {
		Classe classe = Classe.findById(id);
		classe._delete();
	}
	
	/**
	 * Creation/modification d'une classe
	 * @param id identifiant de la classe a modifier
	 */
    public static void classeForm(Long id) {
    	// Il faut les etudiants a ajouter
    	List<Etudiant> etudiantsSansClasse = Etudiant.sansClasse();
        if(id != null) {
            Classe classe = Classe.findById(id);
            render(classe, etudiantsSansClasse);
        }
        render(etudiantsSansClasse);
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
    		Classe classePrecharge = null;	 
    		if (id > 0)
    			classePrecharge = Enseignant.findById(id);
    		else
    			classePrecharge = new Classe(nomClasse);
	        render("Scolarite/classeForm.html", classePrecharge, randomID);
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
     * Methode de recherche d'une classe a partir d'une chaine saisie. Appelee par un fichier Javascript
     * @param chaine chaine a rechercher parmis les noms de classe
     * @return une chaine JSon a interpreter dans le fichier Javascipt appelant
     */
    public void rechercheClasseComposition(String chaine) {
    	List<Classe> lstClasses = Classe.cherche(chaine);
    	renderJSON(lstClasses);
    }
    
    /*********************************************************************************/
    /**********************           PARTIE COURS             ***********************/
    /*********************************************************************************/
    
	/**
	 * Renvoi les 20 premieres matieres dans l'ordre alphabetique
	 * */
	public static void listeCours() {
		List<Matiere> liste20Matieres = Matiere.find20Matieres();
		// Booleens pour savoir si un appel a previous/next est possible
		renderArgs.put("previous", false);
		renderArgs.put("next", (Matiere.findAll().size() > 20));
		render(liste20Matieres);
	} 
	
	/**
	 * Renvoi les 20 prochaines matieres dans l'ordre alphabetique
	 * @param id identifiant de la derniere matiere affichee
	 */
	public static void listeNextCours(Long id) {
		List<Matiere> liste20Matieres = Matiere.next20Matieres(id);
		renderArgs.put("previous", true);
		// Si la liste n'est pas pleine, c'est qu'on ne peut pas appeler encore un next
		renderArgs.put("next", (liste20Matieres.size() == 20));
		render("Scolarite/listeCours.html", liste20Matieres);
	} 
	
	/**
	 * Renvoi les 20 precedente matieres dans l'ordre alphabetique
	 * @param id identifiant de la derniere matiere affichee
	 */
	public static void listePreviousCours(Long id) {
		List<Matiere> liste20Matieres = Matiere.previous20Matieres(id);
		renderArgs.put("next", true);
		// Si la liste n'est pas pleine, c'est qu'on ne peut pas appeler encore un previous
		renderArgs.put("previous", (liste20Matieres.size() == 20));
		render("Scolarite/listeCours.html", liste20Matieres);
	} 
    
	/**
	 * Permet d'avoir la liste des cours d'une classe
	 * @param id identifiant d'une classe
	 */
	public static void coursClasse(Long id) {
		Classe classe = Classe.findById(id);
		render(classe);
	}
	
	/**
	 * Supprimer un cours
	 * @param id identifiant du cours a supprimer
	 */
	public static void supprimerCours(long id) {
		Cours cours = Cours.findById(id);
		cours._delete();
	}
	
	/**
	 * Chargement du formulaire de creation/modification d'un cours
	 * @param idCours identifiant du cours a modifier
	 * @param idClasse idenfitiant de la classe (utile car a conserver dans le formulaire)
	 */
    public static void coursForm(Long idCours, Long idClasse) {    	
    	Matiere matiere = Matiere.findById(idClasse);
        if(idCours != null) {
            Cours cours = Cours.findById(idCours);
            // Si on est dans la modification de la matiere, il faut les profs associes
        	List<Enseignant> lstEnseignantMatiere = 
        			Cours.enseignantByMatiere(cours.matiere);
            render(matiere, cours, lstEnseignantMatiere);
        }
        // Sinon, une methode Javascript appelera l'une des deux fonctions suivantes
        render(matiere);
    }
	
    /**
     * Appelee par une methode Javascript
     * Renvoi en Json la liste des enseignants repertories pour la matiere
     * @param id identifiant de la matiere selectionne (dans un menu deroulant par exemple)
     */
    public static void EnseignantByMatiere(Long id) {
    	List<Enseignant> lstEtudiantMatiere = Cours.
    			enseignantByMatiere((Matiere) Matiere.findById(id));
    	renderJSON(lstEtudiantMatiere);
    }
    
    /**
     * Appelee par une methode Javascript
     * Renvoi en Json la liste des enseignants qui n'enseignent pas la matiere selectionne
     */
    public static void allEnseignants() {
    	List<Enseignant> lstEnseignants = Enseignant.findAllEnseignant();
    	renderJSON(lstEnseignants);
    }
    
    /**
     * Creation/Modification d'un cours
     * @param id identifiant du cours si modification
     * @param matiere Matiere enseignee dans le cadre du cours
     * @param enseignant Enseignant etant responsable du cours
     * @param classe classe suivant le cours
     */
    public static void postCours(long id,
	        @Required(message="Une matiere est requise") Matiere matiere, 
	        @Required(message="Un enseignant est requis") Enseignant enseignant,
	        @Required(message="Une classe est requise") Classe classe,
	        		String randomID) {
    	if(validation.hasErrors()) {
	    	flash.error("Formulaire invalide");
	        render("Scolarite/coursForm.html", Cours.findById(id), randomID);
	    }
    	Cours cours = null;
    	if (id > 0) {
    		cours = Cours.findById(id);
    		cours.classe = classe;
    		cours.matiere = matiere;
    		cours.enseignant = enseignant;
    		flash.success("Cours modifié");
    	} else {
    		cours = new Cours(classe, matiere, enseignant);
    		flash.success("Cours ajouté");
    	}
    	cours.save();
    	ScolariteController.listeCours();
    }
    
    /*********************************************************************************/
    /**********************        PARTIE ENSEIGNANT        **************************/
    /*********************************************************************************/
	
    /**
	 * Renvoi les 20 premiers enseignants dans l'ordre alphabetique
	 * */
	public static void listeEnseignants() {
		List<Enseignant> liste20Enseignants = Enseignant.find20Enseignants();
		// Booleens pour savoir si un appel a previous/next est possible
		renderArgs.put("previous", false);
		renderArgs.put("next", (Enseignant.findAll().size() > 20));
		render(liste20Enseignants);
	} 
	
	/**
	 * Renvoi les 20 prochains enseignants dans l'ordre alphabetique
	 * @param id identifiant du dernier enseignant affiche
	 */
	public static void listeNextEnseignants(Long id) {
		List<Enseignant> liste20Enseignants = Enseignant.next20Enseignants(id);
		renderArgs.put("previous", true);
		// Si la liste n'est pas pleine, c'est qu'on ne peut pas appeler encore un next
		renderArgs.put("next", (liste20Enseignants.size() >= 20));
		render("Scolarite/listeEnseignants.html", liste20Enseignants);
	} 
	
	/**
	 * Renvoi les 20 precedents enseignants dans l'ordre alphabetique
	 * @param id identifiant du dernier enseignant affiche
	 */
	public static void listePreviousEnseignants(Long id) {
		List<Enseignant> liste20Enseignants = Enseignant.previous20Enseignants(id);
		renderArgs.put("next", true);
		// Si la liste n'est pas pleine, c'est qu'on ne peut pas appeler encore un previous
		renderArgs.put("previous", (liste20Enseignants.size() >= 20));
		render("Scolarite/listeEnseignants.html", liste20Enseignants);
	} 
	
    /**
     * Methode de recherche d'un enseignant a partir d'une chaine saisie. Appelee par un fichier Javascript
     * @param chaine chaine a rechercher parmis les nom et prenom de l'enseignant
     * @return une chaine JSon a interpreter dans le fichier Javascipt appelant
     */
    public void rechercheEnseignant(String chaine) {
    	List<Enseignant> lstEnseignants = Enseignant.cherche(chaine);
    	renderJSON(lstEnseignants);
    }
	
	/**
	 * Supprimer un enseignant
	 * @param id identifiant de l'enseignant a supprimer
	 */
	public static void supprimerEnseignants(long id) {
		Enseignant enseignant = Enseignant.findById(id);
		enseignant._delete();
	}
	
	/**
	 * Chargement du formulaire de creation/modification d'un enseignant
	 * @param idEnseignant identifiant de l'enseignant a modifier
	 */
    public static void enseignantForm(Long idEnseignant) {    	
        if(idEnseignant != null) {
        	Enseignant enseignant = Enseignant.findById(idEnseignant);
            render(enseignant);
        }
        render();
    }
    
    /**
     * Creation/Modification d'un enseignant
     * @param id identifiant de l'enseignant si modification
     * @param nom nom de l'enseignant
     * @param prenom prenom de l'enseignant
     * @param login login de l'enseignant (doit être unique)
     * @param password mot de passe de l'enseignant
     * @param passwordVerif verification de la saisie du mot de passe
     * @param dateNaissance
     */
    public static void postEnseignants(long id,
	        @Required(message="Un nom est requis") String nom, 
	        @Required(message="Un prenom est requis") String prenom,
	        @Required(message="Un login est requis") String login,
	        @Required(message="Un mot de passe est requis") String password,
	        @Required(message="Un mot de passe est requis") String passwordVerif,
	        @Required(message="Une date de naissance est requise") Date dateNaissance,
	        		String randomID) {
    	if(validation.hasErrors()) {
	    	flash.error("Formulaire invalide");
    		Enseignant enseignantPrecharge = null;	 
    		if (id > 0)
    			enseignantPrecharge = Enseignant.findById(id);
    		else
    			enseignantPrecharge = new Enseignant(login, password, nom, prenom, dateNaissance);
	        render("Scolarite/enseignantForm.html", enseignantPrecharge, randomID);
	    }
    	if (!password.equals(passwordVerif)) {
    		flash.error("Mots de passe non identiques");
    		Enseignant enseignantPrecharge = null;	 
    		if (id > 0)
    			enseignantPrecharge = Enseignant.findById(id);
    		else
    			enseignantPrecharge = new Enseignant(login, password, nom, prenom, dateNaissance);
	        render("Scolarite/enseignantForm.html", enseignantPrecharge, randomID);
    	}
    	if (Enseignant.exist(login)) {
    		flash.error("Le login est déjà utilisé");
    		Enseignant enseignantPrecharge = null;
 
    		if (id > 0)
    			enseignantPrecharge = Enseignant.findById(id);
    		else
    			enseignantPrecharge = new Enseignant(login, password, nom, prenom, dateNaissance);
    		
	        render("Scolarite/enseignantForm.html", enseignantPrecharge, randomID);
    	}
    	Enseignant enseignant = null;
    	if (id > 0) {
    		enseignant = Enseignant.findById(id);
    		enseignant.nom = nom;
    		enseignant.prenom = prenom;
    		enseignant.login = login;
    		enseignant.password = password;
    		enseignant.dateNaissance = dateNaissance;
    		flash.success("Enseignant modifié");
    	} else {
    		enseignant = new Enseignant(login, password, nom, prenom, dateNaissance);
    		flash.success("Enseignant ajouté");
    	}
    	enseignant.save();
    	ScolariteController.listeEnseignants();
    }
    
    /*********************************************************************************/
    /**********************        PARTIE ETUDIANT        ****************************/
    /*********************************************************************************/
	
    /**
	 * Renvoi les 20 premiers etudiants dans l'ordre alphabetique
	 * */
	public static void listeEtudiants() {
		List<Etudiant> liste20Etudiants = Etudiant.find20Etudiants();
		// Booleens pour savoir si un appel a previous/next est possible
		renderArgs.put("previous", false);
		renderArgs.put("next", (Etudiant.findAll().size() > 20));
		render(liste20Etudiants);
	} 
	
	/**
	 * Renvoi les 20 prochains etudiants dans l'ordre alphabetique
	 * @param id identifiant du dernier etudiant affiche
	 */
	public static void listeNextEtudiants(Long id) {
		List<Etudiant> liste20Etudiants = Etudiant.next20Etudiants(id);
		renderArgs.put("previous", true);
		// Si la liste n'est pas pleine, c'est qu'on ne peut pas appeler encore un next
		renderArgs.put("next", (liste20Etudiants.size() >= 20));
		render("Scolarite/listeEtudiants.html", liste20Etudiants);
	} 
	
	/**
	 * Renvoi les 20 precedents etudiants dans l'ordre alphabetique
	 * @param id identifiant du dernier etudiant affiche
	 */
	public static void listePreviousEtudiants(Long id) {
		List<Etudiant> liste20Etudiants = Etudiant.previous20Etudiants(id);
		renderArgs.put("next", true);
		// Si la liste n'est pas pleine, c'est qu'on ne peut pas appeler encore un previous
		renderArgs.put("previous", (liste20Etudiants.size() >= 20));
		render("Scolarite/listeEtudiants.html", liste20Etudiants);
	} 
	
    /**
     * Methode de recherche d'un etudiant a partir d'une chaine saisie. Appelee par un fichier Javascript
     * @param chaine chaine a rechercher parmis les nom et prenom de l'etudiant
     * @return une chaine JSon a interpreter dans le fichier Javascipt appelant
     */
    public void rechercheEtudiant(String chaine) {
    	List<Etudiant> lstEtudiants = Etudiant.cherche(chaine);
    	renderJSON(lstEtudiants);
    }
	
	/**
	 * Supprimer un etudiant
	 * @param id identifiant de l'etudiant a supprimer
	 */
	public static void supprimerEtudiants(long id) {
		Etudiant etudiant = Etudiant.findById(id);
		etudiant._delete();
	}
	
	/**
	 * Chargement du formulaire de creation/modification d'un etudiant
	 * @param idEtudiant identifiant de l'etudiant a modifier
	 */
    public static void etudiantForm(Long idEtudiant) {    	
        if(idEtudiant != null) {
        	Etudiant etudiant = Etudiant.findById(idEtudiant);
            render(etudiant);
        }
        render();
    }
    
    /**
     * Creation/Modification d'un etudiant
     * @param id identifiant de l'etudiant si modification
     * @param nom nom de l'etudiant
     * @param prenom prenom de l'etudiant
     * @param login login de l'etudiant (doit être unique)
     * @param password mot de passe de l'etudiant
     * @param passwordVerif verification de la saisie du mot de passe
     * @param dateNaissance
     */
    public static void postEtudiants(long id,
	        @Required(message="Un nom est requis") String nom, 
	        @Required(message="Un prenom est requis") String prenom,
	        @Required(message="Un login est requis") String login,
	        @Required(message="Un mot de passe est requis") String password,
	        @Required(message="Un mot de passe est requis") String passwordVerif,
	        @Required(message="Une date de naissance est requise") Date dateNaissance,
	        		String randomID) {
    	if(validation.hasErrors()) {
	    	flash.error("Formulaire invalide");
	    	Etudiant etudiantPrecharge = null;	 
    		if (id > 0)
    			etudiantPrecharge = Etudiant.findById(id);
    		else
    			etudiantPrecharge = new Etudiant(login, password, nom, prenom, dateNaissance);
	        render("Scolarite/etudiantForm.html", etudiantPrecharge, randomID);
	    }
    	if (!password.equals(passwordVerif)) {
    		flash.error("Mots de passe non identiques");
    		Etudiant etudiantPrecharge = null;	 
    		if (id > 0)
    			etudiantPrecharge = Etudiant.findById(id);
    		else
    			etudiantPrecharge = new Etudiant(login, password, nom, prenom, dateNaissance);
	        render("Scolarite/etudiantForm.html", etudiantPrecharge, randomID);
    	}
    	if (Etudiant.exist(login)) {
    		flash.error("Le login est déjà utilisé");
    		Etudiant etudiantPrecharge = null;
 
    		if (id > 0)
    			etudiantPrecharge = Etudiant.findById(id);
    		else
    			etudiantPrecharge = new Etudiant(login, password, nom, prenom, dateNaissance);
    		
	        render("Scolarite/etudiantForm.html", etudiantPrecharge, randomID);
    	}
    	Etudiant etudiant = null;
    	if (id > 0) {
    		etudiant = Etudiant.findById(id);
    		etudiant.nom = nom;
    		etudiant.prenom = prenom;
    		etudiant.login = login;
    		etudiant.password = password;
    		etudiant.dateNaissance = dateNaissance;
    		flash.success("Etudiant modifié");
    	} else {
    		etudiant = new Etudiant(login, password, nom, prenom, dateNaissance);
    		flash.success("Etudiant ajouté");
    	}
    	etudiant.save();
    	ScolariteController.listeEtudiants();
    }
    
    /*********************************************************************************/
    /**********************        PARTIE MATIERE        *****************************/
    /*********************************************************************************/
	
    /**
	 * Renvoi les 20 premieres matiere dans l'ordre alphabetique
	 * */
	public static void listeMatieres() {
		List<Matiere> liste20Matieres = Matiere.find20Matieres();
		// Booleens pour savoir si un appel a previous/next est possible
		renderArgs.put("previous", false);
		renderArgs.put("next", (Matiere.findAll().size() > 20));
		render(liste20Matieres);
	} 
	
	/**
	 * Renvoi les 20 prochaines matieres dans l'ordre alphabetique
	 * @param id identifiant de la derniere matiere affiche
	 */
	public static void listeNextMatieres(Long id) {
		List<Matiere> liste20Matieres = Matiere.next20Matieres(id);
		renderArgs.put("previous", true);
		// Si la liste n'est pas pleine, c'est qu'on ne peut pas appeler encore un next
		renderArgs.put("next", (liste20Matieres.size() >= 20));
		render("Scolarite/listeMatieres.html", liste20Matieres);
	} 
	
	/**
	 * Renvoi les 20 precedentes matieres dans l'ordre alphabetique
	 * @param id identifiant de la derniere matiere affiche
	 */
	public static void listePreviousMatieres(Long id) {
		List<Matiere> liste20Matieres = Matiere.previous20Matieres(id);
		renderArgs.put("next", true);
		// Si la liste n'est pas pleine, c'est qu'on ne peut pas appeler encore un previous
		renderArgs.put("previous", (liste20Matieres.size() >= 20));
		render("Scolarite/listeMatieres.html", liste20Matieres);
	} 
	
    /**
     * Methode de recherche d'une matiere a partir d'une chaine saisie. Appelee par un fichier Javascript
     * @param nom chaine a rechercher parmis les noms de matiere
     * @return une chaine JSon a interpreter dans le fichier Javascipt appelant
     */
    public void rechercheMatiere(String nom) {
    	List<Matiere> lstMatieres = Matiere.cherche(nom);
    	renderJSON(lstMatieres);
    }
	
	/**
	 * Supprimer une matiere
	 * @param id identifiant de la matiere a supprimer
	 */
	public static void supprimerMatieres(long id) {
		Matiere matiere = Matiere.findById(id);
		matiere._delete();
	}
	
	/**
	 * Chargement du formulaire de creation/modification d'une matiere
	 * @param idMatiere identifiant de la matiere a modifier
	 */
    public static void matiereForm(Long idMatiere) {    	
        if(idMatiere != null) {
        	Matiere matiere = Matiere.findById(idMatiere);
            render(matiere);
        }
        render();
    }
    
    /**
     * Creation/Modification d'une Matiere
     * @param id identifiant de la matiere si modification
     * @param nom nom de la matiere
     */
    public static void postMatieres(long id,
	        @Required(message="Un nom est requis") String nom, 
	        		String randomID) {
    	if(validation.hasErrors()) {
	    	flash.error("Formulaire invalide");
	    	Matiere matierePrecharge = null;	 
    		if (id > 0)
    			matierePrecharge = Matiere.findById(id);
    		else
    			matierePrecharge = new Matiere(nom);
	        render("Scolarite/matiereForm.html", matierePrecharge, randomID);
	    }
    	if (Matiere.exist(nom)) {
    		flash.error("Le nom de votre matiere est déjà utilisé");
    		Matiere matierePrecharge = null;
 
    		if (id > 0)
    			matierePrecharge = Matiere.findById(id);
    		else
    			matierePrecharge = new Matiere(nom);
    		
	        render("Scolarite/matiereForm.html", matierePrecharge, randomID);
    	}
    	Matiere matiere = null;
    	if (id > 0) {
    		matiere = Matiere.findById(id);
    		matiere.nom = nom;
    		flash.success("Matiere modifiée");
    	} else {
    		matiere = new Matiere(nom);
    		flash.success("Matiere ajoutée");
    	}
    	matiere.save();
    	ScolariteController.listeMatieres();
    }
}
