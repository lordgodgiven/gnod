package models;

import java.util.Date;
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
		columnNames = { "login"} ))
public class Enseignant extends Model {

	@MaxSize(50)
	@Required
	public String login;

	@MaxSize(50)
	@Required
	public String password;

	@MaxSize(50)
	@Required
	public String nom;

	@MaxSize(50)
	@Required
	public String prenom;

	@Required
	public Date dateNaissance;

	@OneToMany(mappedBy="enseignant", cascade = CascadeType.PERSIST)
	public Set<Cours> cours;

	public Enseignant(String login, String password, String nom, String prenom,
			Date dateNaissance) {
		this.login = login;
		this.password = password;
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaissance = dateNaissance;
	}

	public static Enseignant connect(String login, String password) {
		return find("byLoginAndPassword", login, password).first();
	}
	
	/**
	 * Permet d'obtenir la liste des 20 premiers enseignant dans l'ordre alphabetique
	 * @return
	 */
	public static List<Enseignant> find20Enseignant() {
		
	}

}
