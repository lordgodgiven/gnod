package models;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

public class Classe extends Model {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5380245769176494375L;

	@OneToMany(mappedBy = "Classe", cascade = CascadeType.ALL)
	public Etudiant etudiant;
	
	@OneToMany(mappedBy = "Cours", cascade = CascadeType.ALL)
	public Cours cours;
	
	/*
	 * Méthode pour générer le fichier XML du flux RSS
	 */
}
