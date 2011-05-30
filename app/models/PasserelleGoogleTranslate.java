package models;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.w3c.dom.Document;


import com.google.gson.JsonElement;

import play.libs.OAuth;
import play.libs.WS;
import play.libs.F.Promise;
import play.libs.OAuth.TokenPair;
import play.libs.WS.HttpResponse;

public class PasserelleGoogleTranslate {
	public static void authenticate() {
		URL url = new URL("https://ajax.googleapis.com/ajax/services/language/translate?" +
        	"v=1.0&q=Hello,%20my%20friend!&langpair=en%7Ces&key=INSERT-YOUR-KEY&userip=INSERT-USER-IP");
		URLConnection connection = url.openConnection();
		connection.addRequestProperty("Referer", /* Enter the URL of your site here */);
		
		String line;
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while((line = reader.readLine()) != null) {
		builder.append(line);
		}
		
		JSONObject json = new JSONObject(builder.toString());

	    }
	}
}
