package models;

import java.io.File;
import java.sql.Date;

public class NewsFeedGen {
	private static String BALISE_INTRO =
		new String("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>");
	private static String BALISE_RSS_O = new String("<rss version=\"2.0\">");
	private static String BALISE_RSS_F = new String("</rss>");
	private static String BALISE_CHANNEL_O = new String("<channel>");
	private static String BALISE_CHANNEL_F = new String("</channel>");
	private static String BALISE_TITLE_O = new String("<title>");
	private static String BALISE_TITLE_F = new String("</title>");
	private static String BALISE_DESCR_O = new String("<description>");
	private static String BALISE_DESCR_F = new String("</description>");
	private static String BALISE_LASTBUILD_O = new String("<lastBuildDate>");
	private static String BALISE_LASTBUILD_F = new String("</lastBuildDate>");
	private static String BALISE_LINK_O = new String("<link>");
	private static String BALISE_LINK_F = new String("</link>");
	private static String BALISE_ITEM_O = new String("<item>");
	private static String BALISE_ITEM_F = new String("</item>");
	private static String BALISE_PUBDATE_O = new String("<pubDate>");
	private static String BALISE_PUBDATE_F = new String("</pubDate>");
	/**
	 * 
	 * @param nomFic nom du fichier xml (de la forme /public
	 * @param message message
	 * @param date date de publication
	 */
	public static void generate(String nomFic, String message, Date date){
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
		File file = new File(nomFic); 
	}

}
