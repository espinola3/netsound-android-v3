package edu.upc.eetac.dsa.dsaqt1314g4.netsound;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.User;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.utils.Utils;

public class MainActivity extends Activity implements AsyncResponse{

	public static final String BASE_URL = "http://192.168.1.108:8000/netsound-api/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Netsound");
		
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		Button playSong = (Button) findViewById(R.id.play);
		 playSong.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              play( "http://www.w3schools.com/tags/horse.ogg");
            }
        });
		 
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		
		 
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	 public void login(View view) {	
		 
		 EditText usernameText = (EditText) findViewById(R.id.username);
		 String username = usernameText.getText().toString(); 
		 
		 EditText passwordText = (EditText) findViewById(R.id.password);
		 String password = passwordText.getText().toString(); 
		
		 String urlString = BASE_URL +"profile/"+username;
		 
		 String toEncode = username +":" +password;
		 String basic = "Basic " + Base64.encodeToString(toEncode.getBytes(),0);

		 new CallAPI(this).execute(urlString, basic, CallAPI.LOGIN_OPERATION);		 
		  
	}
	 
	 public void goToHome(User user){
		 
		Intent i = new Intent(getApplicationContext(), HomeActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		i.putExtra("user", user);
		startActivity(i);
	 }
	 
	public void printError(String error){
		 Utils.createDialog(this, "Try again", "Your username or password is wrong", false); 
	}

	@Override
	public void printContent(List<Object> stingList) {
		// TODO Auto-generated method stub
		
	}
	 
	public void signup(View view){
		Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
		startActivity(i);
	}

	@Override
	public void goToLogin() {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isPLAYING = false;
	private MediaPlayer mp;
	public void play(String url) {
	
	    if (!isPLAYING) {
	        isPLAYING = true;
	        mp  = new MediaPlayer();
	        try {
	            mp.setDataSource(url);
	            mp.prepare();
	            mp.start();
	            mp.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						Button playSong = (Button) findViewById(R.id.play);
				        playSong.setBackgroundResource(R.drawable.ic_action_play);
						
					}
				});
	            Button playSong = (Button) findViewById(R.id.play);
	            playSong.setBackgroundResource(R.drawable.ic_action_pause);
	        } catch (IOException e) {
	            System.out.print("Error on play --> my songs activity " + e.getMessage());
	        }
	    } else {
	        isPLAYING = false;
	        Button playSong = (Button) findViewById(R.id.play);
	        playSong.setBackgroundResource(R.drawable.ic_action_play);
	        stopPlaying();
	    }
	}
	
	private void stopPlaying() {
	    mp.release();
	    mp = null;
	}

}
