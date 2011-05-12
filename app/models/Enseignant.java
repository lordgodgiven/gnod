package models;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

public class Enseignant extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4428972366405599579L;

	@MaxSize(50)
	@Required
	public String login;
	
	@MaxSize(50)
	@Required
	public String password;
	
	@MaxSize(50)
	@Required
	public String nom;
	
	@MaxSize(100)
	@Required
	public String prenom;
	
	@ Required
	public Date dateNaissance;
	
	@OneToMany(cascade = CascadeType.PERSIST)
	public Set<Cours> cours; 

}
