package models;

import javax.persistence.Entity;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

/*
 * Cette classe represente l'acteur personnel de scolarite
 */
@Entity
public class Scolarite extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = -826971617993218396L;

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
}
