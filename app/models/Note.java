package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Note extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5496021554168238796L;

	@ManyToOne
	@Required
	public Etudiant etudiant;
	
	@ManyToOne
	@Required
	public Examen examen;
	
	@MaxSize(4)
	@Required
	public Integer note;
}
