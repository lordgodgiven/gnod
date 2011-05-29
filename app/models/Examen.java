package models;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
@Entity
public class Examen extends Model{	
	@MaxSize(100)
	@Required
	public String nom;
	
	@Required
	public Date date;
	
	@Required
	public Float coef;
	
	@ManyToOne
	@Required
	public Cours cours; 
	
	@OneToMany(mappedBy="examen", cascade=CascadeType.ALL)
	public Set<Note> notes;
	
	@Required
	public boolean noteValidee;

	public Examen(String nom, Date date, Float coef, Cours cours) {
		this.nom = nom;
		this.date = date;
		this.cours = cours;
		this.coef = coef;
		noteValidee = false;
	}
	
	
}
