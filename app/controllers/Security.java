package controllers;
 
import models.Enseignant;
import models.Etudiant;
import models.Scolarite;
 
public class Security extends Secure.Security {
	private static String typeUser;
	
    static boolean authenticate(String login, String password) {
        boolean identifie = true;
        
        if (Etudiant.connect(login, password) != null) {
        	typeUser = new String("etudiant");
        	identifie = true;
        } else if (Enseignant.connect(login, password) != null) {
        	typeUser = new String("enseignant");
        } else if (Scolarite.connect(login, password) != null) {
        	typeUser = new String("scolarite");
        } else {
        	identifie = false;
        }
    	
    	return identifie;
    }
    
    static void onDisconnected() {
    }
    
    static void onAuthenticated() {
    	if (typeUser.equals("scolarite")) {
    		ScolariteController.index();
        } else {
        	System.out.println("Probleme de connexion");
        }
    }
    
}
   