package models;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
	private static String BALISE_RSS = new String("rss");
	private static String BALISE_CHANNEL = new String("channel");
	private static String BALISE_TITLE = new String("title");
	private static String BALISE_DESCR = new String("description");
	private static String BALISE_LASTBUILD = new String("lastBuildDate");
	private static String BALISE_LINK = new String("link");
	private static String BALISE_ITEM = new String("item");
	private static String BALISE_PUBDATE = new String("pubDate");

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
				} else if (nomFic.equals(".*Enseignant.*")) {
					title1.setText("Flux RSS des nouveaux examens à noter");
					descr1.setText("Examen " +nomClasseEnseignant);

				}
				channel.addContent(title1);
				channel.addContent(descr1);
				
				// Renseigne la date de a facon qu'il faut pour le flux RSS
				SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
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
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
            pubDate.setText(formatter.format(new GregorianCalendar().getTime()));
            link.setText("http://localhost:9000");
            newItem.addContent(title);
            newItem.addContent(descr);
            newItem.addContent(pubDate);
            newItem.addContent(link);
            System.out.println("everything is all right");
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

}
