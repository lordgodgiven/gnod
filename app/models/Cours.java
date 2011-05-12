package models;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

public class Cours extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6300426313966272333L;
	
	@ManyToOne
	@Required 
	public Classe classe;
	
	@ManyToOne
	@Required 
	public Matiere matiere;
	
	@ManyToOne
	@Required 
	public Enseignant enseignant;
	
	@OneToMany(mappedBy = "Cours", cascade = CascadeType.ALL)
	public Examen examen;
	
		
}
