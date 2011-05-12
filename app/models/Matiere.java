package models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

public class Matiere extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4111153225748967659L;
	
	@Required 
	@MaxSize(100)
	public String nom;
	
	@OneToMany(mappedBy = "Matiere", cascade = CascadeType.PERSIST)
	public Set<Cours> cours;
}
