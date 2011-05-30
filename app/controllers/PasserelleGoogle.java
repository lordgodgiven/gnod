package controllers;

import com.sun.org.apache.bcel.internal.generic.NEW;

import models.Enseignant;
import models.Etudiant;
import models.Scolarite;
import controllers.Secure.Security;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

public class PasserelleGoogle  extends Controller {
	
	/**
	 * 
	 */
	public static void translateInEnglish() {
		System.out.println("------------------------------------------------------------------------------------" +
				"---Translate in english-------------------------------------------------------");
		renderJSON(new String("test"));
	}
	
	public static void translateInSpanish() {
		System.out.println("------------------------------------------------------------------------------------" +
				"---Translate in spanish-------------------------------------------------------");
		renderJSON(new String("test"));
	}
	
	public static void translateInDeutsch() {
		System.out.println("------------------------------------------------------------------------------------" +
				"---Translate in deutsch-------------------------------------------------------");
	}
	
	public static void translateInFrench() {
		System.out.println("------------------------------------------------------------------------------------" +
				"---Translate in french-------------------------------------------------------");
	}
}
