package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

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

	public Etudiant(String login, String password, String nom, String prenom, 
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
	
	/**
	 * Calcul la moyenne generale d'un etudiant
	 * @return la valeur de cette moyenne
	 */
	public Float calculMoyenneGenerale() {
		float cptCoefficient = 0, totalNote = 0;
		GregorianCalendar dateActuelle = new GregorianCalendar();
		GregorianCalendar dateDebutAnnee = null;
		
		if (dateActuelle.get(Calendar.MONTH) < Calendar.SEPTEMBER) {
			dateDebutAnnee = new GregorianCalendar(dateActuelle.get(Calendar.YEAR) 
					- 1, Calendar.SEPTEMBER,15);
		} else {
			dateDebutAnnee = new GregorianCalendar(dateActuelle.get(Calendar.YEAR),
					Calendar.SEPTEMBER,15);
		}

		for (Note noteTmp : notes) {
			// La note doit etre pour cette annee est validee
			if (noteTmp.examen.date.after(dateDebutAnnee.getTime()) 
					&& noteTmp.examen.noteValidee) {
				totalNote = totalNote + (noteTmp.note * noteTmp.examen.coef);
				cptCoefficient += noteTmp.examen.coef;
			}
		}
		return (Float) ((float) totalNote / (float)cptCoefficient);
	}
	
	/**
	 * Calcul la moyenne generale d'un etudiant
	 * @return la moyenne pour chaque matiere ou l'etudiant a passe des examens
	 */
	public HashMap<Matiere, Float> calculMoyenneDetailee() {
		HashMap<Matiere, Float> retour = new HashMap<Matiere, Float>();
		GregorianCalendar dateActuelle = new GregorianCalendar();
		GregorianCalendar dateDebutAnnee = null;
		
		if (dateActuelle.get(Calendar.MONTH) < Calendar.SEPTEMBER) {
			dateDebutAnnee = new GregorianCalendar(dateActuelle.get(Calendar.YEAR) 
					- 1, Calendar.SEPTEMBER,15);
		} else {
			dateDebutAnnee = new GregorianCalendar(dateActuelle.get(Calendar.YEAR),
					Calendar.SEPTEMBER,15);
		}
		for (Note noteTmp : notes) {
			System.out.println("Note " +noteTmp.note);
			float cptCoefficient = 0, totalNote = 0;
			// Si la note est validee qu'elle est de cette annee
			// et que la moyenne de sa matiere n'a pas encore ete calculee :
			if (noteTmp.examen.noteValidee && noteTmp.examen.date.after(dateDebutAnnee.getTime())
					&& !retour.containsKey(noteTmp.examen.cours.matiere)) {
				System.out.println("Calcul de la moyenne de la matiere " +noteTmp.examen.cours.matiere.nom);
				for (Note noteTmp2 : notes) {
					if (noteTmp2.examen.date.after(dateDebutAnnee.getTime()) 
							&& noteTmp2.examen.noteValidee && noteTmp2.examen.cours.matiere.nom.equals(
									noteTmp.examen.cours.matiere.nom)) {
						totalNote = totalNote +(noteTmp2.note * noteTmp2.examen.coef);
						cptCoefficient += noteTmp2.examen.coef;
					}
				}
				System.out.println("Total " +totalNote);
				System.out.println("cptCoeff " +cptCoefficient);
				retour.put(noteTmp.examen.cours.matiere, (Float) ((float) totalNote / (float)cptCoefficient));
			}
		}
		System.out.println("------Liste des matières----------");
		for (Matiere matiere : retour.keySet()) {
			System.out.println("Matiere " +matiere.nom+ ", moyenne : " +retour.get(matiere));
		}
		System.out.println("------FIN liste des matières----------");
		return retour;
	}
	
	/**
	 * Return les etudiants sans classe
	 */
	public static List<Etudiant> sansClasse() {		
		return Etudiant.find("classe is null").fetch();
	}
	
	/**
	 * met a jour les etudiants qui n'ont pas de classe
	 * @param lstEtudiantSC liste des etudiants sans classe
	 */
	public static void saveEtudiantSC(List<Etudiant> lstEtudiantSC) {
		for (Etudiant etudiantTmp : lstEtudiantSC) {
			// Maj de la classe
			// TODO : tester sans les deux lignes suivantes, normalement, ca marche
			etudiantTmp.classe.etudiant.remove(etudiantTmp);
			etudiantTmp.classe.save();
			// Maj de l'etudiant
			etudiantTmp.classe = null;
			etudiantTmp.save();
		}
	}
	
	
	
	/**
	 * Permet d'obtenir la liste des 20 premiers etudiants dans l'ordre alphabetique
	 * @return la liste de maximum 20 etudiant
	 */
	public static List<Etudiant> find20Etudiants() {
		List<Etudiant> allEtudiants = Etudiant.find("order by nom, prenom asc").fetch();
		// On veut les 20 premiers
		List<Etudiant> etudiants = new ArrayList<Etudiant>();
		Iterator<Etudiant> itEtudiant = allEtudiants.iterator();
		int cpt = 0;
		while (cpt < 20 && itEtudiant.hasNext()) {
			etudiants.add(itEtudiant.next());
			cpt++;
		}
		return etudiants;
	}
	
	/**
	 * Permet d'obtenir la liste des 20 prochains Etudiants dans l'ordre alphabetique
	 * @param idLastEtudiant dernier etudiant de la page actuelle
	 * @return la liste de maximum 20 etudiants
	 */
	public static List<Etudiant> next20Etudiants(Long idLastEtudiant) {
		List<Etudiant> allEtudiants = Etudiant.find("order by nom, prenom asc").fetch();
		// On veut les 20 premiers
		List<Etudiant> etudiants = new ArrayList<Etudiant>();
		Iterator<Etudiant> itEtudiant = allEtudiants.iterator();
		int cptEtudiant = 0;
		// A vrai lorsque l'etudiant en paramtre est trouve dans la liste
		boolean trouve = false;
		while (cptEtudiant < 20 && itEtudiant.hasNext()) {
			Etudiant etudiantTmp = itEtudiant.next();
			if (!trouve && etudiantTmp.id == idLastEtudiant) {
				trouve = true;
				etudiants.add(itEtudiant.next());
				cptEtudiant++;
			} else if (trouve) {
				etudiants.add(itEtudiant.next());
				cptEtudiant++;
			}
		}
		return etudiants;
	}
	
	/**
	 * Permet d'obtenir la liste des 20 etudiants precedents dans l'ordre alphabetique
	 * @param idPremEtudiant premier etudiant de la page actuelle
	 * @return la liste de maximum 20 etudiants
	 */
	public static List<Etudiant> previous20Etudiants(Long idPremEtudiant) {
		List<Etudiant> allEtudiants = Etudiant.find("order by nom, prenom asc").fetch();
		// On veut les 20 premiers
		List<Etudiant> etudiants = new ArrayList<Etudiant>();
		int cptAll = -1, cptEtudiant = 0;
		// A vrai lorsque l'enseignant en paramtre est trouve dans la liste
		boolean trouve = false;
		while (!trouve && cptAll < allEtudiants.size() - 1) {
			cptAll++;
			trouve = (allEtudiants.get(cptAll).id == idPremEtudiant);
		}
		// On veut garder l'ordre de la liste initiale
		if (cptAll < 19) {
			cptEtudiant = cptAll;
		} else {
			cptEtudiant = 19;
		}
		
		// On rempli la liste de retour avec les enseignants precedents
		while (cptEtudiant <= 0) {
			etudiants.set(cptEtudiant, allEtudiants.get(cptAll)) ;
			cptEtudiant--;
			cptAll--;
		}
		return etudiants;
	}
	
	/**
	 * Permet de savoir si un login existe deja dans la base de donnees
	 * @param login login a rechercher dans la BD
	 * @return true si le login est deja pris, false sinon
	 */
	public static boolean exist(String login) {
		return (Etudiant.find("byLogin", login).first() != null 
				|| Enseignant.find("byLogin", login).first() != null
				|| Scolarite.find("byLogin", login).first() != null);
	}
	
	  /**
     * Methode de recherche d'un Etudiant a partir d'une chaine saisie
     * @param chaine chaine a rechercher parmis les noms/prenoms des etudiants
     * @return la liste des etudiants correspondant a la recherche
     */
	public static List<Etudiant> cherche(String chaine) {
		List<Etudiant> lstEtudiants = new ArrayList<Etudiant>();
		StringTokenizer st = new StringTokenizer(chaine, " ", false);
		String [] tabToken = new String[st.countTokens()];
		int cpt = 0;
		while (st.hasMoreElements()) {
			tabToken[cpt] = st.nextToken();
			cpt++;
		}
		// saisie du nom ou du prenom
		if (st.countTokens() == 1) {
			lstEtudiants = Etudiant.find("byPrenomLike", "%" +tabToken[0]+ "%").fetch();
			if (lstEtudiants.size() <= 0) {
				lstEtudiants = Etudiant.find("byNomLike", "%" +tabToken[1]+ "%").fetch();
			}
		}
		// saisie du type nom prenom ou prenom nom
		else if (st.countTokens() == 2) {
			lstEtudiants = Etudiant.find("byNomLikeAndPrenomLike", "%" +tabToken[0]+ "%", "%" +tabToken[1]+ "%").fetch();
			if (lstEtudiants.size() <= 0) {
				lstEtudiants = Etudiant.find("byNomLikeAndPrenomLike", "%" +tabToken[1]+ "%", "%" +tabToken[0]+ "%").fetch();
				if (lstEtudiants.size() <= 0) {
					// Disons qu'on a un nom ou un prenom compose (mais pas les deux)
					lstEtudiants = Etudiant.find("byNomLike", "%" +chaine+ "%").fetch();
					if (lstEtudiants.size() <= 0) {
						lstEtudiants = Etudiant.find("byPrenomLike", "%" +chaine+ "%").fetch();
					}
				}
			}
		}
		// On a affaire a un prenom ou un nom compose (pour simplifier, on fait la recherche que sur un champ
		// Sinon il faudrait decomposer toutes les combinaisons possibles en 2 puissance nombre de tokens)
		else {
			// Disons qu'on a un nom ou un prenom compose (mais pas les deux)
			lstEtudiants = Etudiant.find("byNomLike", "%" +chaine+ "%").fetch();
			if (lstEtudiants.size() <= 0) {
				lstEtudiants = Etudiant.find("byPrenomLike", "%" +chaine+ "%").fetch();
			}
		}
		return lstEtudiants;
	}
}