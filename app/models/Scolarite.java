package models;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

/*
 * Cette classe repr�sente l'acteur personnel de scolarit�
 */
public class Scolarite extends Model{
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
}
