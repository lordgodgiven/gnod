package models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * @author julien
 *
 */
@Entity
public class Cours extends Model {	
	@ManyToOne
	@Required 
	public Classe classe;
	
	@ManyToOne
	@Required 
	public Matiere matiere;
	
	@ManyToOne
	@Required 
	public Enseignant enseignant;
	
	@OneToMany(mappedBy = "cours", cascade = CascadeType.ALL)
	public Set<Examen> examen;

	public Cours(Classe classe, Matiere matiere, Enseignant enseignant) {
		this.classe = classe;
		this.matiere = matiere;
		this.enseignant = enseignant;
	}	
}
