package models;

import groovyjarjarantlr.collections.Stack;

import java.util.Date;

import org.dom4j.dom.DOMDocument;
import org.w3c.dom.Document;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;
import com.sun.org.apache.bcel.internal.generic.NEW;

import controllers.Secure.Security;
import play.libs.WS;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

public class PasserelleGoogle {
	private static Language langue = Language.FRENCH;
	/**
	 * 
	 */
	public static String translateInEnglish(String document) {
		if (langue.equals(Language.ENGLISH)) 
			return (document);
		// Set the HTTP referrer to your website address.
	    Translate.setHttpReferrer("http://localhost:9000");
	    String translatedText = "test";
	    try {
	    	translatedText = Translate.execute(document,
	    			langue, Language.ENGLISH);
	    	langue = Language.ENGLISH;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return (translatedText);
	}
	
	public static String translateInSpanish(String document) {
		if (langue.equals(Language.SPANISH)) 
			return (document);
		// Set the HTTP referrer to your website address.
	    Translate.setHttpReferrer("http://localhost:9000");
	    String translatedText = "test";
	    try {
	    	translatedText = Translate.execute(document,
	    			langue, Language.SPANISH);
	    	langue = Language.SPANISH;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return (translatedText);
	}
	
	public static String translateInDeutsch(String document) {
		if (langue.equals(Language.GERMAN)) 
			return (document);
		System.out.println("------------------------------------------------------------------------------------" +
				"---Translate in deutsch-------------------------------------------------------");
		System.out.println("taille du document :");
		System.out.println(document.length());

		Translate.setHttpReferrer("http://localhost:9000");
	    String translatedText = "test";
	    try {
	    	translatedText = Translate.execute(document,
	    			langue, Language.GERMAN);
	    	langue = Language.GERMAN;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }

	    System.out.println("------TranslateDDD in deutsch-----------");
	    System.out.println("document :");
	    System.out.println(translatedText);
		//String val = new String("test");
		//renderJSON(name);
		return (translatedText);
		//renderJSON(new Etudiant("test", "test", "test", "test", new Date()));
	}
	
	public static String translateInFrench(String document) {
		if (langue.equals(Language.FRENCH)) 
			return (document);
		 Translate.setHttpReferrer("http://localhost:9000");
		    String translatedText = "test";
		    try {
		    	translatedText = Translate.execute(document,
		    			langue, Language.SPANISH);
		    	langue = Language.SPANISH;
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
			return (translatedText);
	}
}
