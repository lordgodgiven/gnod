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
								&& !chercherExamen(enseignantTmp, examenTmp)) {
								addExamen(enseignantTmp, examenTmp);
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
								supprimeExamen(classeTmp, examenTmp);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Date stringToDate(String sDate) {
		Date d = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(
					NewsFeedGen.FORMAT_DATE);
			ParsePosition pos = new ParsePosition(0);
			d = formatter.parse(sDate, pos);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * Supprime un news concernant l'examen
	 * 
	 * @param classe
	 *            classe concerne par le flux a mettre a jour
	 * @param examen
	 *            examen a supprimer
	 */
	private static void supprimeExamen(Classe classe, Examen examen) {
		// nom de fichier sous la forme Etudiant_1A ISI 2.xml
		try {
			Document document;
			// On cree une instance de SAXBuilder
			SAXBuilder sxb = new SAXBuilder();
			File file = new File("Etudiant_" + classe.nomClasse + ".xml");
			document = sxb.build(file);

			Element racine = document.getRootElement();
			Element channel = racine.getChild(NewsFeedGen.BALISE_CHANNEL);

			if (stringToDate(
					channel.getAttribute(NewsFeedGen.BALISE_LASTBUILD)
							.getValue()).equals(examen.date)) {
				file.delete();
			} else {
				List<Element> itemTmp = channel
						.getChildren(NewsFeedGen.BALISE_ITEM);
				for (Element elementTmp : itemTmp) {
					// On compare la date de l'examen de l'item a celle de
					// l'examen a supprimer
					if (examen.date.equals(stringToDate(elementTmp.getChild(
							NewsFeedGen.BALISE_PUBDATE).getText()))) {
						elementTmp.removeContent();
						// Recopie du fichier modifie
						XMLOutputter sortie = new XMLOutputter(Format
								.getPrettyFormat());
						// écriture des infos spécifiés dans le file à retourner
						sortie.output(document, new FileOutputStream(file
								.getAbsolutePath()));
					}
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de savoir si un examen est deja dans le fichier RSS ou pas
	 * 
	 * @param enseignant
	 *            enseignant concerne par la recherche
	 * @param examen
	 *            examen recherche dans le fichier XML du flux RSS
	 * @return vrai si l'examen est déjà dans le fichier et faux sinon
	 */
	private static boolean chercherExamen(Enseignant enseignant, Examen examen) {
		File file = new File("Enseignant_" + enseignant.prenom + "_"
				+ enseignant.nom + ".xml");
		if (file.exists()) {
			try {
				Document document;
				// On cree une instance de SAXBuilder
				SAXBuilder sxb = new SAXBuilder();
				document = sxb.build(file);
				Element racine = document.getRootElement();
				Element channel = racine.getChild(NewsFeedGen.BALISE_CHANNEL);
				List<Element> lstItem = channel
						.getChildren(NewsFeedGen.BALISE_ITEM);
				for (Element itemTmp : lstItem) {
					// Si le nom de l'examen apparaît dans la description de l'item, c'est que c'est l'examen
					// que l'on cherche
					if(itemTmp.getChild(NewsFeedGen.BALISE_DESCR).getText().matches(".*"+examen.nom+".*"))
						return true;			
				}
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		} else {
			return false;
		}
	}
	
	private static void addExamen(Enseignant enseignant, Examen examen) {
		NewsFeedGen.generate("public/rss/Enseignant_"+enseignant.prenom+"_"+enseignant.nom+".xml",
				"Examens des" +examen.cours.classe.nomClasse+ "de" +examen.cours.matiere.nom+ "a noter", 
				new String(enseignant.prenom+ " " +enseignant.nom));
	}

}
