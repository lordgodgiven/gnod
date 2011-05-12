package models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;
@Entity
public class Classe extends Model {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5380245769176494375L;
	
	@OneToMany(mappedBy = "classe", cascade = CascadeType.ALL)
	public Set<Etudiant> etudiant;
	
	@OneToMany(mappedBy = "classe", cascade = CascadeType.ALL)
	public Set<Cours> cours;

	public Classe() {
	}
	/*
	 * Méthode pour générer le fichier XML du flux RSS
	 */
}
