package models;

import java.io.InputStream;

import org.w3c.dom.Document;


import com.google.gson.JsonElement;

import play.libs.OAuth;
import play.libs.WS;
import play.libs.F.Promise;
import play.libs.OAuth.TokenPair;
import play.libs.WS.HttpResponse;

public class PasserelleGoogleTranslate {
	public static void authenticate() {
		HttpResponse res = WS.url("http://www.google.com").get();
		int status = res.getStatus();
		String type = res.getContentType();
		
		String content = res.getString();
		Document xml = res.getXml();
		JsonElement json = res.getJson();
		InputStream is = res.getStream();
		
		Promise<HttpResponse> futureResponse = WS.url(
			    "http://www.google.com"
			).getAsync();
	    // TWITTER is a OAuth.ServiceInfo object
	    // getUser() is a method returning the current user 
	    if (OAuth.isVerifierResponse()) {
	        // We got the verifier; 
	        // now get the access tokens using the unauthorized tokens
	        TokenPair tokens = OAuth.service(TWITTER).requestAccessToken(
	            getUser().getTokenPair()
	        );
	        // let's store them and go back to index
	        getUser().setTokenPair(tokens);
	        index();
	    }
	    OAuth twitt = OAuth.service(TWITTER);
	    TokenPair tokens = twitt.requestUnauthorizedToken();
	    // We received the unauthorized tokens 
	    // we need to store them before continuing
	    getUser().setTokenPair(tokens);
	    // Redirect the user to the authorization page
	    redirect(twitt.redirectUrl(tokens));
	}
}
