package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;
@Entity
public class Classe extends Model {
	 @Required
	 public String nomClasse;
	
	@OneToMany(mappedBy = "classe", cascade = CascadeType.ALL)
	public Set<Etudiant> etudiant;
	
	@OneToMany(mappedBy = "classe", cascade = CascadeType.ALL)
	public Set<Cours> cours;

	public Classe(String nomClasse) {
		this.nomClasse = nomClasse;
	}

	/**
	 * Permet d'obtenir la liste des 20 premieres classes dans l'ordre alphabetique
	 * @return la liste de maximum 20 classes
	 */
	public static List<Classe> find20Classes() {
		List<Classe> allClasses = Classe.find("order by nomClasse asc").fetch();
		// On veut les 20 premieres
		List<Classe> classes = new ArrayList<Classe>();
		Iterator<Classe> itClasse = allClasses.iterator();
		int cpt = 0;
		while (cpt < 20 && itClasse.hasNext()) {
			classes.add(itClasse.next());
			cpt++;
		}
		return classes;
	}
	
	/**
	 * Permet d'obtenir la liste des 20 prochains classes dans l'ordre alphabetique
	 * @param idLastClasse derniere classe de la page actuelle
	 * @return la liste de maximum 20 classes
	 */
	public static List<Classe> next20Classes(Long idLastClasse) {
		List<Classe> allClasses = Classe.find("order by nomClasse asc").fetch();
		// On veut les 20 premieres
		List<Classe> classes = new ArrayList<Classe>();
		Iterator<Classe> itClasse = allClasses.iterator();
		int cptClasse = 0;
		// A vrai lorsque la classe en paramtre est trouvee dans la liste
		boolean trouve = false;
		while (cptClasse < 20 && itClasse.hasNext()) {
			Classe classeTmp = itClasse.next();
			if (!trouve && classeTmp.id == idLastClasse) {
				trouve = true;
				classes.add(itClasse.next());
				cptClasse++;
			} else if (trouve) {
				classes.add(itClasse.next());
				cptClasse++;
			}
		}
		return classes;
	}
	
	/**
	 * Permet d'obtenir la liste des 20 classes precedentes dans l'ordre alphabetique
	 * @param idPremClasse premiere classe de la page actuelle
	 * @return la liste de maximum 20 classes
	 */
	public static List<Classe> previous20Classes(Long idPremClasse) {
		List<Classe> allClasses = Classe.find("order by nomClasse asc").fetch();
		// On veut les 20 premiers
		List<Classe> classes = new ArrayList<Classe>();
		int cptAll = -1, cptClasse = 0;
		// A vrai lorsque la classe en paramtre est trouvee dans la liste
		boolean trouve = false;
		while (!trouve && cptAll < allClasses.size() - 1) {
			cptAll++;
			trouve = (allClasses.get(cptAll).id == idPremClasse);
		}
		// On veut garder l'ordre de la liste initiale
		if (cptAll < 19) {
			cptClasse = cptAll;
		} else {
			cptClasse = 19;
		}
		
		// On rempli la liste de retour avec les classes precedentes
		while (cptClasse <= 0) {
			classes.set(cptClasse, allClasses.get(cptAll)) ;
			cptClasse--;
			cptAll--;
		}
		return classes;
	}
	
    /**
     * Methode de recherche d'une classe a partir d'une chaine saisie
     * @param nomClasse chaine a rechercher parmis les noms de classe
     * @return la liste des classes correspondant a la recherche
     */
	public static List<Classe> cherche(String nomClasse) {
		return 	Classe.find("nomClasse like ?", "%" +nomClasse+ "%").fetch();
	}

}
