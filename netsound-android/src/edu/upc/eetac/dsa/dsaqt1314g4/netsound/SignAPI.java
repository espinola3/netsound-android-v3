package edu.upc.eetac.dsa.dsaqt1314g4.netsound;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.User;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.utils.Utils;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.CallLog;

public class SignAPI extends AsyncTask<String, String, String> {

	public AsyncResponse callback=null;
	String username =null;
	
	public SignAPI(AsyncResponse callback) {
		super();
		this.callback = callback;
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		 String urlString=params[0]; // URL to call
		 String urlParameters = params[1];
		 
		 InputStream in = null;
		 JSONObject json;
		 try {
		  		 
			 URL myURL = new URL(urlString);
			 HttpURLConnection myURLConnection = (HttpURLConnection)myURL.openConnection();		
			 myURLConnection.setRequestMethod("POST");
			 myURLConnection.setRequestProperty("Content-Language", "es-ES");
			 myURLConnection.setRequestProperty("Content-Type", "application/vnd.netsound.api.user+json; charset=UTF-8");
			 myURLConnection.setRequestProperty("Content-Length", ""+urlParameters.getBytes().length);
			 myURLConnection.setRequestProperty("Accept", "*/*");
			 myURLConnection.setUseCaches(false);
			 myURLConnection.setDoInput(true);
			 myURLConnection.setDoOutput(true);
			 
			DataOutputStream wr = new DataOutputStream(myURLConnection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			 
			in = myURLConnection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
		    String jsonText = readAll(rd);
		    json = new JSONObject(jsonText);		     
		    
		    
		    username = (String) json.get("username");				
				
			  
		 } catch (Exception e ) {		  
			 System.out.println(e.getMessage());		  			 		  
		 } finally {
			 try { in.close(); } 
		     catch (Exception e) {
		    	 System.out.println(e.getMessage());
		     };	
		 }
		 		 			
		 return ""; 
	}
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	protected void onPostExecute(String operation) {
		if(username!=null){
			callback.goToLogin();
		}
		else{
			callback.printError("Error on registration process, please try again");
		}
	}

}
