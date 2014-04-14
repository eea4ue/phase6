package com.cs4720project.phasesix;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccountCreate extends Activity {

	EditText userNameEditText;
	EditText passwordEditText;

	Button createAccountButton;
	static String addUserURL = "http://plato.cs.virginia.edu/~cs4720s14pepper/admin/addUser/";
	static String user = "";
	static String usernameString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		Intent i = getIntent();
		user = i.getStringExtra("USER_ID");
		userNameEditText = (EditText) findViewById(R.id.userNameEditText);

		if (user != null)
			userNameEditText.setText(user);

		createAccountButton = (Button) findViewById(R.id.createAccountButton);
		createAccountButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addUser(v);
			}
		});

	}

	public void addUser(View view) {

		userNameEditText = (EditText) findViewById(R.id.userNameEditText);
		passwordEditText = (EditText)findViewById(R.id.passwordEditText);
		usernameString = userNameEditText.getText().toString();
		Context context = getApplicationContext();
		CharSequence userCreationErrorText = "Error! Could not create user: "
				+ usernameString;
		CharSequence noInputErrorText = "Error! You left the user field empty.";
		int duration = Toast.LENGTH_LONG;

		Toast.makeText(context, "Creating User: " + usernameString, Toast.LENGTH_SHORT)
				.show();
		Toast.makeText(context, "Welcome, " + usernameString + "!", duration).show();

		// TODO Passwords / PHASE 5
		// passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		// String passwordString = passwordEditText.getText().toString();

		if (usernameString.length() > 0) {
			try {
				String encodedSearch = URLEncoder.encode(usernameString,
						"UTF-8");
				String searchURL = addUserURL + encodedSearch;
				Log.d("usernameString", usernameString);
				Log.d("searchURL", searchURL);

				/*** EXECUTE ***/
				new AddUserTask().execute(searchURL);
				new LogoutTask().execute("http://plato.cs.virginia.edu/~cs4720s14pepper/logout.php");
			
			
			} catch (Exception e) {
				Toast.makeText(context, userCreationErrorText, duration).show();
				e.printStackTrace();
			}

		} else
			Toast.makeText(context, noInputErrorText, duration).show();
	}

	private class AddUserTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {

			StringBuilder userIDBuilder = new StringBuilder();

			for (String searchURL : URL) {
				HttpClient httpClient = HTTPClients.getDefaultHttpClient();
				try {
					HttpPost httpPost = new HttpPost(searchURL);
					List<NameValuePair> pass = new ArrayList<NameValuePair>(2);
					//Log.d("Password Plaintext:",passwordEditText.getText().toString());
					pass.add(new BasicNameValuePair("PASSWORD", passwordEditText.getText().toString()));
					//Log.d("Inside keypair",pass.get(0).getValue());
					httpPost.setEntity(new UrlEncodedFormEntity(pass));
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
						inputStreamReader.close();

					} else
						Log.d("STATUS CODE ERROR !=200", String.valueOf(searchStatus.getStatusCode()));
				} catch (Exception e) {
					Log.d("Exception", "httpClient");
					e.printStackTrace();
				}

			}
			return userIDBuilder.toString();
		}

	}
	private class LogoutTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {

			
			for (String logoutURL : URL) {
				HttpClient httpClient = new DefaultHttpClient();
				try {
					HttpGet httpGet = new HttpGet(logoutURL);
					HttpResponse response = httpClient.execute(httpGet);
					StatusLine searchStatus = response.getStatusLine();
					InputStream is = response.getEntity().getContent();
					is.close();
					if (searchStatus.getStatusCode() == 200) {
						HTTPClients.clearClient();
						
					} else
						Log.d("STATUS CODE ERROR", "!= 200");
				} catch (Exception e) {
					Log.d("Exception", "httpClient");
					e.printStackTrace();
				}

			}
			return "Completed";
		}
		protected void onPostExecute(String result){
			HTTPClients.clearClient();
			Intent intent = new Intent(AccountCreate.this,
					Splash.class);
			intent.putExtra("USER_ID", usernameString);
			startActivity(intent);
		}
	}


}