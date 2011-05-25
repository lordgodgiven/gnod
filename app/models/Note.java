package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Note extends Model {
	@ManyToOne
	@Required
	public Etudiant etudiant;
	
	@ManyToOne
	@Required
	public Examen examen;
	
	@MaxSize(4)
	@Required
	public Integer note;
	
	@Required
	public boolean isDisffused;
	
	public Note(Etudiant etudiant, Examen examen, Integer note) {
		this.etudiant = etudiant;
		this.examen = examen;
		this.note = note;
		this.isDisffused = false;
	}
}
