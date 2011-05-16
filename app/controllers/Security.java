package controllers;
 
import models.Enseignant;
import models.Etudiant;
import models.Scolarite;
 
public class Security extends Secure.Security {
	
    static boolean authenticate(String login, String password) {        
        return (Etudiant.connect(login, password) != null ||
        		Enseignant.connect(login, password) != null ||
        		Scolarite.connect(login, password) != null);
    }
    
    static void onDisconnected() {
    }
    
    static void onAuthenticated() {
    }
}
   