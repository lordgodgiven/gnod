package models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
@Entity
@Table(uniqueConstraints = @UniqueConstraint(
		columnNames = {"nom"} ))
public class Matiere extends Model {
	@Required 
	@MaxSize(100)
	public String nom;
	
	@OneToMany(mappedBy = "matiere", cascade = CascadeType.PERSIST)
	public Set<Cours> cours;

	public Matiere(String nom) {
		this.nom = nom;
	}
	
}
