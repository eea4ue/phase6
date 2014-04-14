package com.cs4720project.phasesix;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Splash extends Activity {

	static String USER_ID = "";
	EditText userNameEditText;
	static String authURL = "http://plato.cs.virginia.edu/~cs4720s14pepper/auth.php";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

//		TextView txtView = (TextView) findViewById(R.id.welcomeTextView);
//		Typeface myTypeface = Typeface.createFromAsset(
//		                          this.getAssets(),
//		                          "font/Robot.otf");
//		txtView.setTypeface(myTypeface);
		
		userNameEditText = (EditText) findViewById(R.id.userNameEditText);

		userNameEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String userIDString = userNameEditText.getText().toString();
				Log.d("DEBUG_TAG", "User set EditText value to " + userIDString);

			}
		});

		Button loginButton = (Button) findViewById(R.id.loginButton);
		Button registerButton = (Button) findViewById(R.id.registerButton);

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new AuthenticateTask(false).execute(authURL);
				//loginIntent.putExtra("session_id", )
				finish();
				
			}

		});

		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
								new AuthenticateTask(true).execute(authURL);
								finish();
			}

		});

	}
	private class AuthenticateTask extends AsyncTask<String, Void, String> {
		private boolean admin;
		private AuthenticateTask(boolean ad){
			super();
			admin=ad;
			
		}
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {
			
			EditText user = (EditText) findViewById(R.id.userNameEditText);
			EditText pass = (EditText) findViewById(R.id.passwordEditText);
			
			StringBuilder userIDBuilder = new StringBuilder();
				String username="";
				String password="";
			if(admin)
			{
				username="admin";
				password="1234";
				Log.d("Admin attempt","Initiated");
			}
			else
			{
				username=user.getText().toString();
				password=pass.getText().toString();
				Log.d("Regular user",username);
			}
			for (String authURL : URL) {
				HttpClient httpClient = HTTPClients.getDefaultHttpClient();
				try {
					HttpPost httpPost = new HttpPost(authURL);
					List<NameValuePair> params = new ArrayList<NameValuePair>(2);
					params.add(new BasicNameValuePair("USERNAME", username));
					params.add(new BasicNameValuePair("PASSWORD", password));
					httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
					
					HttpResponse response = httpClient.execute(httpPost);
					StatusLine searchStatus = response.getStatusLine();

					if (searchStatus.getStatusCode() == 200) {
						HttpEntity entity = response.getEntity();
						InputStream content = entity.getContent();

						// read the data you got
						InputStreamReader inputStreamReader = new InputStreamReader(
								content);
						BufferedReader reader = new BufferedReader(
								inputStreamReader);

						// read one line at a time and append to stringBuilder
						String lineIn;
						
						while ((lineIn = reader.readLine()) != null) {
							userIDBuilder.append(lineIn);
						}
						Log.d("Output from site", userIDBuilder.toString());
						inputStreamReader.close();
						// // THEN TAKE USER TO MAIN ACTIVITY
						// Intent intent = new Intent(MainActivity.this,
						// MainActivity.class);
						// startActivity(intent);
						// adapter.notifyDataSetChanged();
						
					} else
						Log.d("STATUS CODE ERROR", "!= 200");
				} catch (Exception e) {
					Log.d("Exception", "httpClient");
					e.printStackTrace();
				}
				
			}
			return userIDBuilder.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			if(admin){
				Intent registerIntent = new Intent(Splash.this,
						AccountCreate.class);
				registerIntent.putExtra("USER_ID", userNameEditText.getText()
		.toString());
				startActivity(registerIntent);
				finish();

			}
			else
			{
				Intent loginIntent = new Intent(Splash.this,MainActivity.class);
				loginIntent.putExtra("USER_ID", userNameEditText.getText().toString());
				startActivity(loginIntent);
				finish();
			}
			
			
			// TextView temp = new TextView(getActivity());
			// ListView root;
			// root=(ListView) findViewById(R.id.userCourseList);
			// Log.d("Adapter?",((ListView)root).getAdapter().toString());
					}

	}

}
