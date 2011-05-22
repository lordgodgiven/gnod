import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import models.Classe;
import models.Cours;
import models.Enseignant;
import models.Examen;
import models.NewsFeedGen;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import play.jobs.Job;
import play.jobs.On;

/**
 * Taches de mise à jour des fichiers XML des flux RSS pour des tâches ayant un
 * rapport avec les dates
 */
// Tout les jours à minuit
@On("0 0 12 * * ?")
public class MiseAJourRSS extends Job {
	public void doJob() {
		// Job pour les professeurs
		// Pour tous les professeurs, s'ils sont abonnes au flux RSS
		// Pour tous les examens, si leur date est passée et que l'examen
		// n'a pas de notes validées et qu'il n'apparaît pas dans le fichier XML
		// du professeur : Ajout de l'item
		List<Enseignant> lstEnseignant = Enseignant.findAll();
		for (Enseignant enseignantTmp : lstEnseignant) {
			for (Cours coursTmp : enseignantTmp.cours) {
				for (Examen examenTmp : coursTmp.examen) {
					if (!examenTmp.noteValidee) {
						Date today = new Date();
						if (today.after(examenTmp.date)
								&& !NewsFeedGen.chercherExamen(enseignantTmp,
										examenTmp)) {

							NewsFeedGen.generate("public/rss/Enseignant_"
									+ enseignantTmp.prenom + "_"
									+ enseignantTmp.nom + ".xml", "Examens des"
									+ examenTmp.cours.classe.nomClasse + "de"
									+ examenTmp.cours.matiere.nom + "a noter",
									new String(enseignantTmp.prenom + " "
											+ enseignantTmp.nom));
						}
					}
				}
			}
		}

		// Job pour les etudiants
		// Pour toutes les classes pour tous les examens de la classe,
		// s'ils datent de plus d'un an, on supprime l'item du flux RSS
		List<Classe> lstClasses = Classe.findAll();
		for (Classe classeTmp : lstClasses) {
			// Si aucun fichier n'a ete cree pour cette classe, inutile de
			// continuer
			if (new File("Etudiant_" + classeTmp.nomClasse + ".xml").exists()) {
				try {
					for (Cours coursTmp : classeTmp.cours) {
						for (Examen examenTmp : coursTmp.examen) {
							// Pour tout les examens, on regarde s'il date de
							// plus d'un an
							GregorianCalendar calendar = new java.util.GregorianCalendar();
							calendar.setTime(examenTmp.date);
							calendar.add(Calendar.YEAR, 1);
							// date courante
							Date date = new Date();
							// Si la date du jour est apres la date cree : la
							// date de l'examen
							// est superieure a 1 an
							if (date.after(calendar.getTime())) {
								// Suppression de la ligne correspondant
								NewsFeedGen
										.supprimeExamen(classeTmp, examenTmp);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
