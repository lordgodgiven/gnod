package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
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
		List<Etudiant> lstEtudiantSC = new ArrayList<Etudiant>();
		List<Etudiant> lstEtudiants = Etudiant.findAll();
		for (Etudiant etudiantTmp : lstEtudiants) {
			if (etudiantTmp.classe == null) 
				lstEtudiantSC.add(etudiantTmp);
		}
		return lstEtudiantSC;
	}
	
	/**
	 * met a jour les etudiants qui n'ont pas de classe
	 * @param lstEtudiantSC liste des etudiants sans classe
	 */
	public static void saveEtudiantSC(List<Etudiant> lstEtudiantSC) {
		for (Etudiant etudiantTmp : lstEtudiantSC) {
			// Maj de la classe
			etudiantTmp.classe.etudiant.remove(etudiantTmp);
			etudiantTmp.classe.save();
			// Maj de l'etudiant
			etudiantTmp.classe = null;
			etudiantTmp.save();
		}
	}
}