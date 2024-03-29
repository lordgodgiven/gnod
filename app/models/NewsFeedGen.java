package models;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/*		<?xml version="1.0" encoding="iso-8859-1"?>
<rss version="2.0">
    <channel>
        <title>Mon site</title>
        <description>Ceci est un exemple de flux RSS 2.0</description>
        <lastBuildDate>Sat, 07 Sep 2002 00:00:01 GMT</lastBuildDate>
        <link>http://www.example.org</link>
        <item>
            <title>Actualit� N�1</title>
            <description>Ceci est ma premi�re actualit�</description>
            <pubDate>Sat, 07 Sep 2002 00:00:01 GMT</pubDate>
            <link>http://www.example.org/actu1</link>
        </item>
    </channel>
</rss>
*/
public class NewsFeedGen {
	public static String BALISE_RSS = new String("rss");
	public static String BALISE_CHANNEL = new String("channel");
	public static String BALISE_TITLE = new String("title");
	public static String BALISE_DESCR = new String("description");
	public static String BALISE_LASTBUILD = new String("lastBuildDate");
	public static String BALISE_LINK = new String("link");
	public static String BALISE_ITEM = new String("item");
	public static String BALISE_PUBDATE = new String("pubDate");
	
	public static String FORMAT_DATE = new String("EEE, dd MMM yyyy HH:mm:ss z");

	/**
	 * 
	 * @param nomFic
	 *            nom du fichier xml (de la forme /public/rss et doit contenir
	 *            la chaine "Etudiant" ou "Enseignant"
	 * @param message
	 *            message correspondant a la description de la news
	 * @param date
	 *            date de publication
	 * @param nomClasseEnseignant
	 *            nom de la classe si le flux est un flux etudiant ou nom de
	 *            l'enseignant si le flux est un flux enseignant
	 */
	public static void generate(String nomFic, String message, String nomClasseEnseignant){
		File file = new File(nomFic); 
		if(file.exists()) {
			System.out.println("Fichier existe");
			addItem(file, message);
		} else {
			try{
				file.createNewFile();
				Element racine = new Element(BALISE_RSS);
				racine.setAttribute("version", "2.0");
				Document document = new Document(racine);
				Element channel = new Element(BALISE_CHANNEL);
				Element title1 = new Element(BALISE_TITLE);
				Element descr1 = new Element(BALISE_DESCR);
				Element lastBuildDate = new Element(BALISE_LASTBUILD);
				Element link1 = new Element(BALISE_LINK);
				Element item = new Element(BALISE_ITEM);
				Element title2 = new Element(BALISE_TITLE);
				Element descr2 = new Element(BALISE_DESCR);
				Element pubDate = new Element(BALISE_PUBDATE);
				Element link2 = new Element(BALISE_LINK);
				
				racine.addContent(channel);
				// On regarde si le flux XML est de type etudiant
				
				if (nomFic.matches(new String(".*Etudiant.*"))) {
					title1.setText("Flux RSS des nouvelles notes");
					descr1.setText("Examen de la classe " +nomClasseEnseignant);
				// Sinon est-il de type enseignant ?
				} else if (nomFic.matches(".*Enseignant.*")) {
					title1.setText("Flux RSS des nouveaux examens à noter");
					descr1.setText("Examen " +nomClasseEnseignant);

				}
				channel.addContent(title1);
				channel.addContent(descr1);
				
				// Renseigne la date de a facon qu'il faut pour le flux RSS
				SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DATE);
				lastBuildDate.setText(formatter.format(new GregorianCalendar().getTime()));
				channel.addContent(lastBuildDate);
				// Dans tous les cas, l'utilisateur doit s'identifier
				link1.setText("http://localhost:9000");
				channel.addContent(link1);
				channel.addContent(item);
				title2.setText("Actualité n°1");
				item.addContent(title2);
				descr2.setText(message);
				item.addContent(descr2);
				// La date de publication est la meme que celle de la derniere publication
				pubDate.setText(lastBuildDate.getText());
				item.addContent(pubDate);
				link2.setText(link1.getText());
				item.addContent(link2);
				try
			      {
				     //Affichage classique avec getPrettyFormat()
				     XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
				     //écriture des infos spécifiés dans le file à retourner
				     sortie.output(document, new FileOutputStream(file.getAbsolutePath()));
			      } catch(IOException e) {
			    	  System.out.println("Error while generating XML file");
			      }
			} catch (IOException e) {
				System.out.println("Erreur de creation du fichier");
			}
		}
	}

	/**
	 * Met a jour le fichier xml
	 * 
	 * @param file
	 *            fichier XML a modifier
	 * @param message
	 *            message a ajouter au fichier
	 * @param date
	 *            date du message
	 */
	private static void addItem(File file, String message) {
		Document document;
		//On crÃ©e une instance de SAXBuilder
	    SAXBuilder sxb = new SAXBuilder();
		 //CrÃ©ation d'un nouveau document JDOM avec en argument le fichier XML
        try {
        	document = sxb.build(file);
            Element racine = document.getRootElement();
            Element channel = racine.getChild(BALISE_CHANNEL);
            Element newItem = new Element(BALISE_ITEM);
            Element title = new Element(BALISE_TITLE);
			Element descr = new Element(BALISE_DESCR);
			Element pubDate = new Element(BALISE_PUBDATE);
			Element link = new Element(BALISE_LINK);
            channel.addContent(newItem);
            // On ajoute la taille (et non pas taille + 1) car l'item a ajoute est deja ajoute a channel
            title.setText("Actualité n°" +(channel.getChildren(BALISE_ITEM).size()));
            descr.setText(message);
            SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DATE);
            pubDate.setText(formatter.format(new GregorianCalendar().getTime()));
            link.setText("http://localhost:9000");
            newItem.addContent(title);
            newItem.addContent(descr);
            newItem.addContent(pubDate);
            newItem.addContent(link);
            // MAJ de la derniere date de modification
            Element lastBuildDate = channel.getChild(BALISE_LASTBUILD);
            lastBuildDate.setText(pubDate.getText());
            try
		      {
			     //Affichage classique avec getPrettyFormat()
			     XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			     //écriture des infos spécifiés dans le file à retourner
			     sortie.output(document, new FileOutputStream(file.getAbsolutePath()));
		      } catch(IOException e) {
		    	  System.out.println("Error while generating XML file");
		      }
        } catch(JDOMException e) {
        	System.out.println("Erreur Jdom en ajoutant l'item");
        } catch(IOException e) {
        	System.out.println("Erreur I/O en ajoutant l'item");
        }
	}
	

	/**
	 * Supprime un news concernant l'examen
	 * 
	 * @param classe
	 *            classe concerne par le flux a mettre a jour
	 * @param examen
	 *            examen a supprimer
	 */
	public static void supprimeExamen(Classe classe, Examen examen) {
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
	public static boolean chercherExamen(Enseignant enseignant, Examen examen) {
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

}
