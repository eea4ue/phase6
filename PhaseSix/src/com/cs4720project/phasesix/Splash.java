package com.cs4720project.phasesix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
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
				Intent loginIntent = new Intent(Splash.this, MainActivity.class);
				loginIntent.putExtra("USER_ID", userNameEditText.getText()
						.toString());
				startActivity(loginIntent);
			}

		});

		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent registerIntent = new Intent(Splash.this,
						AccountCreate.class);
				registerIntent.putExtra("USER_ID", userNameEditText.getText()
						.toString());
				startActivity(registerIntent);
			}

		});

	}
}
