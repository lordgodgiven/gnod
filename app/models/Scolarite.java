package models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

/*
 * Cette classe represente l'acteur personnel de scolarite
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(
		columnNames = { "login"} ))
public class Scolarite extends Model {

	@MaxSize(50)
	@Required
	public String login;

	@MaxSize(50)
	@Required
	public String password;

	public Scolarite(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public static Scolarite connect(String login, String password) {
		return find("byLoginAndPassword", login, password).first();
	}
	
	/**
	 * Permet de savoir si un login existe deja dans la base de donnees
	 * @param login login a rechercher dans la BD
	 * @return true si le login est deja pris, false sinon
	 */
	public static boolean exist(String login) {
		return (Etudiant.find("byLogin", login) != null 
				&& Enseignant.find("byLogin", login) != null
				&& Scolarite.find("byLogin", login) != null);
	}
}
