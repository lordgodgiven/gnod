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
public class Etudiant extends Model {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9011663606100748007L;

	@MaxSize(50)
	@Required
	 public String login;
	 
	@MaxSize(50)
	 @Required
	 public String password;
	
	 @MaxSize(50)
	 @Required
	 public String prenom;
	 
	 @MaxSize(50)
	 @Required
	 public String nom;
	
	 @Required
	 public Date dateNaissance;
	
	 @ManyToOne
	 public Classe classe;
	 
	 @OneToMany(mappedBy="Etudiant", cascade=CascadeType.ALL)
	 public Set<Note> notes;	 
}