package models;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(
		columnNames = { "login"} ))
public class Etudiant extends Model {

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

	@OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
	public Set<Note> notes;

	public Etudiant(String prenom, String nom, String login, String password,
			Date dateNaissance) {
		this.prenom = prenom;
		this.nom = nom;
		this.login = login;
		this.password = password;
		this.dateNaissance = dateNaissance;
	}

	public static Etudiant connect(String login, String password) {
		return find("byLoginAndPassword", login, password).first();
	}
}