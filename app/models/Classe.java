package models;

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
     * Methode de recherche d'une classe a partir d'une chaine saisie
     * @param nomClasse chaine a rechercher parmis les noms de classe
     * @return la liste des classes correspondant a la recherche
     */
	public static List<Classe> cherche(String nomClasse) {
		return 	Classe.find("nomClasse like ?", "%" +nomClasse+ "%").fetch();
	}

}
