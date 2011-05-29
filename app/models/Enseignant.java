package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(
		columnNames = { "login"} ))
public class Enseignant extends Model {

	@MaxSize(50)
	@Required
	public String login;

	@MaxSize(50)
	@Required
	public String password;

	@MaxSize(50)
	@Required
	public String nom;

	@MaxSize(50)
	@Required
	public String prenom;

	@Required
	public Date dateNaissance;

	@OneToMany(mappedBy="enseignant", cascade = CascadeType.PERSIST)
	public Set<Cours> cours;

	public Enseignant(String login, String password, String nom, String prenom,
			Date dateNaissance) {
		this.login = login;
		this.password = password;
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaissance = dateNaissance;
	}

	public static Enseignant connect(String login, String password) {
		return find("byLoginAndPassword", login, password).first();
	}
	
	/**
	 * Permet d'obtenir la liste des 20 premiers enseignant dans l'ordre alphabetique
	 * @return la liste de maximum 20 enseignants
	 */
	public static List<Enseignant> find20Enseignants() {
		List<Enseignant> allEnseignants = Enseignant.find("order by nom, prenom asc").fetch();
		// On veut les 20 premiers
		List<Enseignant> enseignants = new ArrayList<Enseignant>();
		Iterator<Enseignant> itEnseignant = allEnseignants.iterator();
		int cpt = 0;
		while (cpt < 20 && itEnseignant.hasNext()) {
			enseignants.add(itEnseignant.next());
			cpt++;
		}
		return enseignants;
	}
	
	/**
	 * Permet d'obtenir la liste des 20 prochains enseignants dans l'ordre alphabetique
	 * @param lastEnseignant dernier enseignant de la page actuelle
	 * @return la liste de maximum 20 enseignants
	 */
	public static List<Enseignant> next20Enseignants(Long idLastEnseignant) {
		List<Enseignant> allEnseignants = Enseignant.find("order by nom, prenom asc").fetch();
		// On veut les 20 premiers
		List<Enseignant> enseignants = new ArrayList<Enseignant>();
		Iterator<Enseignant> itEnseignant = allEnseignants.iterator();
		int cptEnseignant = 0;
		// A vrai lorsque l'enseignant en paramtre est trouve dans la liste
		boolean trouve = false;
		while (cptEnseignant < 20 && itEnseignant.hasNext()) {
			Enseignant enseignantTmp = itEnseignant.next();
			if (!trouve && enseignantTmp.id == idLastEnseignant) {
				trouve = true;
				enseignants.add(itEnseignant.next());
				cptEnseignant++;
			} else if (trouve) {
				enseignants.add(itEnseignant.next());
				cptEnseignant++;
			}
		}
		return enseignants;
	}
	
	/**
	 * Permet d'obtenir la liste des 20 enseignants precedents dans l'ordre alphabetique
	 * @param premEnseignant premier enseignant de la page actuelle
	 * @return la liste de maximum 20 enseignants
	 */
	public static List<Enseignant> previous20Enseignants(Long idPremEnseignant) {
		List<Enseignant> allEnseignants = Enseignant.find("order by nom, prenom asc").fetch();
		// On veut les 20 premiers
		List<Enseignant> enseignants = new ArrayList<Enseignant>();
		int cptAll = -1, cptEnseignant = 0;
		// A vrai lorsque l'enseignant en paramtre est trouve dans la liste
		boolean trouve = false;
		while (!trouve && cptAll < allEnseignants.size() - 1) {
			cptAll++;
			trouve = (allEnseignants.get(cptAll).id == idPremEnseignant);
		}
		// On veut garder l'ordre de la liste initiale
		if (cptAll < 19) {
			cptEnseignant = cptAll;
		} else {
			cptEnseignant = 19;
		}
		
		// On rempli la liste de retour avec les enseignants precedents
		while (cptEnseignant <= 0) {
			enseignants.set(cptEnseignant, allEnseignants.get(cptAll)) ;
			cptEnseignant--;
			cptAll--;
		}
		return enseignants;
	}
	
	public static List<Enseignant> findAllEnseignant() {
		return Enseignant.find("order by nom, prenom asc").fetch();
	}
	
	/**
	 * Permet de savoir si un login existe deja dans la base de donnees
	 * @param login login a rechercher dans la BD
	 * @return true si le login est deja pris, false sinon
	 */
	public static boolean exist(String login) {
		return (Etudiant.find("byLogin", login).first() != null 
				|| Enseignant.find("byLogin", login).first() != null
				|| Scolarite.find("byLogin", login).first() != null);
	}
		
	/**
     * Methode de recherche d'un enseignant a partir d'une chaine saisie
     * @param chaine chaine a rechercher parmis les noms/prenoms des enseignants
     * @return la liste des enseignants correspondant a la recherche
     */
	public static List<Enseignant> cherche(String chaine) {
		List<Enseignant> lstEnseignants = Enseignant.findAll();
		List<Enseignant> enseignantsMatch = new ArrayList<Enseignant>();
		for (Enseignant enseignantTmp : lstEnseignants) {
			String prenomNom = enseignantTmp.prenom+" "+enseignantTmp.nom;
			if (prenomNom.toLowerCase().matches(".*" +chaine.toLowerCase()+ ".*"))
				enseignantsMatch.add(enseignantTmp);
		}
		if (enseignantsMatch.size() > 0) 
			return enseignantsMatch;		
		
		// Sinon, on verifie la correspondance de recherche avec les sous chaines de la saisie
		String [] tabToken = chaine.split("\\s");
		// Seulement s'il y a des sous chaines
		if (tabToken.length > 1) {
			for (Enseignant enseignantTmp : lstEnseignants) {
				String prenomNom = enseignantTmp.prenom+" "+enseignantTmp.nom;
				int cptToken = 0;
				while (cptToken < tabToken.length) {
					if (prenomNom.toLowerCase().matches(".*" +tabToken[cptToken].toLowerCase()+ ".*")) {
						enseignantsMatch.add(enseignantTmp);
						cptToken = tabToken.length;
					}
					cptToken++;
				}
			}
		}
		return enseignantsMatch;		
	}
}
