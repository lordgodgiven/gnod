package controllers;

import groovyjarjarantlr.collections.Stack;

import java.util.Date;

import org.dom4j.dom.DOMDocument;
import org.w3c.dom.Document;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;
import com.sun.org.apache.bcel.internal.generic.NEW;

import models.Enseignant;
import models.Etudiant;
import models.Scolarite;
import controllers.Secure.Security;
import play.libs.WS;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

public class PasserelleGoogle  extends Controller {
	static String test = new String("Json");
	/**
	 * 
	 */
	public static void translateInEnglish(Document document) {
		System.out.println("------------------------------------------------------------------------------------" +
		"---Translate in english 1 -------------------------------------------------------");
		// Set the HTTP referrer to your website address.
	    Translate.setHttpReferrer("http://localhost:9000");
	    String translatedText = "test";
	    try {
	    	translatedText = Translate.execute("Bonjour le monde",
	            Language.FRENCH, Language.ENGLISH);
	    System.out.println(translatedText);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	 	System.out.println("------------------------------------------------------------------------------------" +
				"---Translate in english 2 -------------------------------------------------------"+document.toString());
		renderJSON(translatedText);
	}
	
	public static void translateInSpanish(String chaine) {
		System.out.println("------------------------------------------------------------------------------------" +
				"---Translate in spanish-------------------------------------------------------" +chaine+" __àè");
		renderJSON(new String("test"));
	}
	
	public static void translateInDeutsch(String doc/*String name, String location*/) {
		System.out.println("------------------------------------------------------------------------------------" +
				"---Translate in deutsch-------------------------------------------------------");
		System.out.println("document :");
		System.out.println(doc);
		//String val = new String("test");
		//renderJSON(name);
		renderText("test");
		//renderJSON(new Etudiant("test", "test", "test", "test", new Date()));
	}
	
	
	
	public static void translateInFrench() {
		System.out.println("------------------------------------------------------------------------------------" +
				"---Translate in french-------------------------------------------------------");
		renderJSON(new String("test"));
	}
}
