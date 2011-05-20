package models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;
@Entity
public class Classe extends Model {
	
	@OneToMany(mappedBy = "classe", cascade = CascadeType.ALL)
	public Set<Etudiant> etudiant;
	
	@OneToMany(mappedBy = "classe", cascade = CascadeType.ALL)
	public Set<Cours> cours;

	public Classe() {
	}
	/*
	 * Methode pour generer le fichier XML du flux RSS
	 */
}
