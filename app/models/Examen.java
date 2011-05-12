package models;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

public class Examen extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3696608795108901374L;
	
	@MaxSize(100)
	@Required
	public String nom;
	
	@Required
	public Date date;
	
	@ManyToOne
	@Required
	public Cours cours; 
	
	@OneToMany(mappedBy="Examen", cascade=CascadeType.ALL)
	public Set<Note> notes;
}
