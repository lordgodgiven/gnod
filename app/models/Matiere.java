package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
@Entity
@Table(uniqueConstraints = @UniqueConstraint(
		columnNames = {"nom"} ))
public class Matiere extends Model {
	@Required 
	@MaxSize(100)
	public String nom;
	
	@OneToMany(mappedBy = "matiere", cascade = CascadeType.PERSIST)
	public Set<Cours> cours;

	public Matiere(String nom) {
		this.nom = nom;
	}
	
	/**
	 * Permet d'obtenir la liste des 20 premiers matieres dans l'ordre alphabetique
	 * @return la liste de maximum 20 matiere
	 */
	public static List<Matiere> find20Matiere() {
		List<Matiere> allMatieres = Matiere.find("order by nom asc").fetch();
		// On veut les 20 premieres
		List<Matiere> matieres = new ArrayList<Matiere>();
		Iterator<Matiere> itMatiere = allMatieres.iterator();
		int cpt = 0;
		while (cpt < 20 && itMatiere.hasNext()) {
			matieres.add(itMatiere.next());
			cpt++;
		}
		return matieres;
	}
	
	/**
	 * Permet d'obtenir la liste des 20 prochains matieres dans l'ordre alphabetique
	 * @param idLastMatiere derniere matiere de la page actuelle
	 * @return la liste de maximum 20 matieres
	 */
	public static List<Matiere> next20Matieres(Long idLastMatiere) {
		List<Matiere> allMatieres = Matiere.find("order by nom asc").fetch();
		// On veut les 20 premiers
		List<Matiere> matieres = new ArrayList<Matiere>();
		Iterator<Matiere> itMatiere = allMatieres.iterator();
		int cptMatiere = 0;
		// A vrai lorsque la matiere en paramtre est trouvee dans la liste
		boolean trouve = false;
		while (cptMatiere < 20 && itMatiere.hasNext()) {
			Matiere matiereTmp = itMatiere.next();
			if (!trouve && matiereTmp.id == idLastMatiere) {
				trouve = true;
				matieres.add(itMatiere.next());
				cptMatiere++;
			} else if (trouve) {
				matieres.add(itMatiere.next());
				cptMatiere++;
			}
		}
		return matieres;
	}
	
	/**
	 * Permet d'obtenir la liste des 20 Matieres precedentes dans l'ordre alphabetique
	 * @param idPremMatiere premiere matiere de la page actuelle
	 * @return la liste de maximum 20 matieres
	 */
	public static List<Matiere> previous20Matieres(Long idPremMatiere) {
		List<Matiere> allMatieres = Matiere.find("order by nom asc").fetch();
		// On veut les 20 premiers
		List<Matiere> matieres = new ArrayList<Matiere>();
		int cptAll = -1, cptMatiere = 0;
		// A vrai lorsque la matiere en paramtre est trouvee dans la liste
		boolean trouve = false;
		while (!trouve && cptAll < allMatieres.size() - 1) {
			cptAll++;
			trouve = (allMatieres.get(cptAll).id == idPremMatiere);
		}
		// On veut garder l'ordre de la liste initiale
		if (cptAll < 19) {
			cptMatiere = cptAll;
		} else {
			cptMatiere = 19;
		}
		
		// On rempli la liste de retour avec les matieres precedentes
		while (cptMatiere <= 0) {
			matieres.set(cptMatiere, allMatieres.get(cptAll)) ;
			cptMatiere--;
			cptAll--;
		}
		return matieres;
	}
	
	/**
	 * Permet de savoir si un nom existe deja dans la base de donnees
	 * @param nom nom a rechercher dans la BD
	 * @return true si le nom est deja pris, false sinon
	 */
	public static boolean exist(String nom) {
		return (Matiere.find("byNom", nom) != null);
	}
}
