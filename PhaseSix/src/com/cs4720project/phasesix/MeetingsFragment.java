package com.cs4720project.phasesix;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MeetingsFragment extends Fragment implements
		OnItemSelectedListener {

	LinearLayout spinners;
	Button submitButton;
	Button clearButton;
	Spinner libSpinner;
	Spinner sectionSpinner;
	Spinner timeSpinner;
	Spinner daySpinner;
	TextView resultsTextView;
	ProgressBar progressNoise;
	ProgressBar progressCrowd;
	
	HashMap libraryHM = new HashMap();
	HashMap dayHM = new HashMap();
	
	static String libTimeDay = "http://plato.cs.virginia.edu/~pel5xq/library/";

	static String userName;
	static boolean tempWorkAround = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		/*
		 * Set up the hash map of (section title, section url) codes to form valid url
		 */
		libraryHM.put("East Wing", "EastWing");
		libraryHM.put("West Wing", "WestWing");
		libraryHM.put("McGregor Room", "McGregor");
		libraryHM.put("Cafe", "Cafe");
		libraryHM.put("Reading Room", "Reading");
		libraryHM.put("Stacks", "Stacks");
		libraryHM.put("1st Floor", "1");
		libraryHM.put("2nd Floor", "2");
		libraryHM.put("3rd Floor", "3");
		libraryHM.put("4th Floor", "4");
		libraryHM.put("5th Floor", "5");
		libraryHM.put("Computer Lab", "CompLab");
		dayHM.put("Sunday", 1);
		dayHM.put("Monday", 2);
		dayHM.put("Tuesday", 3);
		dayHM.put("Wednesday", 4);
		dayHM.put("Thursday", 5);
		dayHM.put("Friday", 6);
		dayHM.put("Saturday", 7);
		
		
		View rootView = inflater.inflate(R.layout.fragment_meetings, container,
				false);

		TextView userLabel = (TextView) rootView
				.findViewById(R.id.userNametextView);
		userLabel.setText(userName);
		
		resultsTextView = (TextView) rootView
				.findViewById(R.id.resultsTextView);

		progressNoise = (ProgressBar) rootView.findViewById(R.id.progressNoise);
		progressCrowd = (ProgressBar) rootView.findViewById(R.id.progressCrowd);
		
		Context context = getActivity();
		spinners = (LinearLayout) rootView.findViewById(R.id.spinners);

		// LIBRARY SPINNER
		libSpinner = (Spinner) rootView.findViewById(R.id.libSpinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		libSpinner.setOnItemSelectedListener((OnItemSelectedListener) this);
		ArrayAdapter<CharSequence> libAdapter = ArrayAdapter
				.createFromResource(context, R.array.lib_array,
						android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		libAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		libSpinner.setAdapter(libAdapter);

		sectionSpinner = (Spinner) rootView.findViewById(R.id.sectionSpinner);

		// TIME SPINNER
		timeSpinner = (Spinner) rootView.findViewById(R.id.timeSpinner);
		ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter
				.createFromResource(context, R.array.time_array,
						android.R.layout.simple_spinner_item);
		timeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeSpinner.setAdapter(timeAdapter);

		// DAY SPINNER
		daySpinner = (Spinner) rootView.findViewById(R.id.daySpinner);
		ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter
				.createFromResource(context, R.array.day_array,
						android.R.layout.simple_spinner_item);
		dayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		daySpinner.setAdapter(dayAdapter);

		resultsTextView = (TextView) rootView
				.findViewById(R.id.resultsTextView);

		/* BUILD THE URL */

		// libTimeDay =
		// http://plato.cs.virginia.edu/~pel5xq/library/@lib/timespan/@minutes/day/@day
		libTimeDay += "Alderman" + "/day/" + "1";
		// "/timespan/" + "10" +

		/** SUBMIT BUTTON **/
		View submitButton = rootView.findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/* MAKE THE ASYNC CALL WITH THE BUILT URL */

				String libraryURL = makeURL();
				//new SearchLibraryStatus().execute(libraryURL);
				
				try {
					//Log.d("searchTerm", libTimeDay);
					Log.d("Library URL", libraryURL);
					new GetDetailsTask().execute(libraryURL);
					//new GetDetailsTask().execute(libTimeDay);
				} catch (Exception e) {
					Log.d("libTimeDay Exception", "");
					e.printStackTrace();
				}

				/*
				 * SET resultsTextView bug: the below is temp text that gets
				 * replaced after a few seconds. Web service might just be a
				 * little slow to respond.
				 */
				resultsTextView.setText("Gathering results. Please standby...");

			}

		});

		View clearButton = rootView.findViewById(R.id.clearButton);
		clearButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// edittext1.setText("");
				resultsTextView.setText("Results: ");
				Toast.makeText(getActivity(),
						"Please select new criteria above.", Toast.LENGTH_SHORT)
						.show();

			}
		});

		return rootView;
	}

	public void setUser(String user) {
		// Log.d("from setUser",user);
		if (user == null && !tempWorkAround)
			userName = "TestAppUser";
		if (user != null && !tempWorkAround)
			userName = user.trim();
		if (!tempWorkAround) {
			// userViewCoursesURL += userName + "/";
			// userDeleteCourseURL += userName + "/delete/";
			tempWorkAround = true;
			Log.d("from setUser in SchedulesFrag", userName);
		}
	}
	
	public String makeURL(){
		String url = "";
		//hard code in a working url
		/*
		 * Set the library, section, time, and day string for url testing
		 */
		
		String library = libSpinner.getSelectedItem().toString();
		// didn't add all libraries to hashmap, only Comm School seems different
		if (library.equals("Commerce School")){
			library="Commerce";
		}
		
		String section = sectionSpinner.getSelectedItem().toString();
		if (section.length()>0){
			section = (libraryHM.get(section)).toString();
		}
		String time = timeSpinner.getSelectedItem().toString();
		if (time.length()>0){
			time = time.substring(0,2);
		}
		String day = daySpinner.getSelectedItem().toString();
		if (day.length()>0){
			day = (dayHM.get(day)).toString();
		}
		
		
		String infoSelected = library + ", " + section + ", "+time+", "+day;
		Log.d("Inputs Selected", infoSelected);
		
		/*
		 * Logic: given default library
		 * section
				day
				timespan
					day
			timespan
				day
			day

		 */
		if (section.length()>0){
			if ((day.length()>0) && (time.length()==0)){
				//lib, section, day
				//http://plato.cs.virginia.edu/~pel5xq/library/@lib/section/@sec/day/@day 
				url = "http://plato.cs.virginia.edu/~pel5xq/library/"+library+"/section/"+section+"/day/"+day;
			}
			else if (time.length()>0){
				if (day.length()>0){
					//lib, section, time, day
					//http://plato.cs.virginia.edu/~pel5xq/library/@lib/section/@sec/timespan/@minutes/day/@day 
					url = "http://plato.cs.virginia.edu/~pel5xq/library/"+library+"/section/"+section+"/timespan/"+time+"/day/"+day ;
				}
				else{
					//lib, section, time
					//http://plato.cs.virginia.edu/~pel5xq/library/@lib/section/@sec/timespan/@minutes
					//if empty: http://plato.cs.virginia.edu/~pel5xq/library/@lib/section/@sec/timespan/@minutes/day
					url="http://plato.cs.virginia.edu/~pel5xq/library/"+library+"/section/"+section+"/timespan/"+time;
				}
			}
			else{
				//lib, section
				//http://plato.cs.virginia.edu/~pel5xq/library/@lib/section/@sec/day 
				url="http://plato.cs.virginia.edu/~pel5xq/library/"+library+"/section/"+section+"/day";
			}
		}
		else if (time.length()>0){
			if (day.length()>0){
				//lib, time, day
				//http://plato.cs.virginia.edu/~pel5xq/library/@lib/timespan/@minutes/day/@day
				url="http://plato.cs.virginia.edu/~pel5xq/library/"+library+"/timespan/"+time+"/day/"+day;
			}
			else{
				//lib, time
				//http://plato.cs.virginia.edu/~pel5xq/library/@lib/timespan/@minutes 
				url="http://plato.cs.virginia.edu/~pel5xq/library/"+library+"/timespan/"+time;
			}
		}
		else if (day.length()>0){
			//lib, day
			//http://plato.cs.virginia.edu/~pel5xq/library/@lib/day/@day 
			url="http://plato.cs.virginia.edu/~pel5xq/library/"+library+"/day/"+day;
		}
		else{
			//lib
			//http://plato.cs.virginia.edu/~pel5xq/library/@lib/day 
			url="http://plato.cs.virginia.edu/~pel5xq/library/"+library+"/day";
		}
		
		
		Log.d("Library Search URL", url);
				
		return url;
	}
	
	private class SearchLibraryStatus extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {

			StringBuilder userIDBuilder = new StringBuilder();

			for (String searchURL : URL) {
				HttpClient httpClient = new DefaultHttpClient();
				try {
					HttpGet httpGet = new HttpGet(searchURL);
					HttpResponse response = httpClient.execute(httpGet);
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


					} else
						Log.d("STATUS CODE ERROR", "!= 200");
				} catch (Exception e) {
					Log.d("Exception", "httpClient");
					e.printStackTrace();
				}

			}
			return userIDBuilder.toString();
		}

		protected void onPostExecute(String result) {
			
			try {
				
				//show result
				resultsTextView.setText("testing");
				
				JSONObject statusObject = new JSONObject(result);
				String noise = statusObject.getString("noise");
				String crowd = statusObject.getString("crowd");
				
				resultsTextView.setText("Noise: "+noise + " and Crowd: "+crowd);
				
				Log.d("Noise Level", statusObject.getString("noise"));
				Log.d("Crowd Level", statusObject.getString("crowd"));
								
				Log.d("JSONObject for Library Status", statusObject.toString());

				
			} catch (Exception e) {
				Log.d("Library Status URL Exception", "");
				Toast.makeText(getActivity(),
						"Error: Don't Panic! Problem with libraryURL",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

		}

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		if (arg0.equals(libSpinner)) {
			sectionSpinner.setEnabled(true);

			if (libSpinner.getSelectedItem().equals("Alderman")) {
				ArrayAdapter<CharSequence> adapter0 = ArrayAdapter
						.createFromResource(getActivity(),
								R.array.alderman_array,
								android.R.layout.simple_spinner_item);
				adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sectionSpinner.setAdapter(adapter0);

			} else if (libSpinner.getSelectedItem().equals("Clark")) {

				ArrayAdapter<CharSequence> adapter1 = ArrayAdapter
						.createFromResource(getActivity(), R.array.clark_array,
								android.R.layout.simple_spinner_item);
				adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sectionSpinner.setAdapter(adapter1);
			} else if (libSpinner.getSelectedItem().equals("Clemons")) {

				ArrayAdapter<CharSequence> adapter2 = ArrayAdapter
						.createFromResource(getActivity(),
								R.array.clemons_array,
								android.R.layout.simple_spinner_item);
				adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sectionSpinner.setAdapter(adapter2);
			} else if (libSpinner.getSelectedItem().equals("Commerce School")) {

				ArrayAdapter<CharSequence> adapter3 = ArrayAdapter
						.createFromResource(getActivity(),
								R.array.commerce_school_array,
								android.R.layout.simple_spinner_item);
				adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sectionSpinner.setAdapter(adapter3);
			}

			else if (libSpinner.getSelectedItem().equals("Rice")) {

				ArrayAdapter<CharSequence> adapter4 = ArrayAdapter
						.createFromResource(getActivity(), R.array.rice_array,
								android.R.layout.simple_spinner_item);
				adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sectionSpinner.setAdapter(adapter4);
			} else if (libSpinner.getSelectedItem().equals("Thornton")) {

				ArrayAdapter<CharSequence> adapter5 = ArrayAdapter
						.createFromResource(getActivity(),
								R.array.thornton_array,
								android.R.layout.simple_spinner_item);
				adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sectionSpinner.setAdapter(adapter5);
			} else if (libSpinner.getSelectedItem().equals("Wilsdorf")) {

				ArrayAdapter<CharSequence> adapter6 = ArrayAdapter
						.createFromResource(getActivity(),
								R.array.wilsdorf_array,
								android.R.layout.simple_spinner_item);
				adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sectionSpinner.setAdapter(adapter6);
			}
			// default:
			// return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// nothing

	}

	private class GetDetailsTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {

			StringBuilder sb = new StringBuilder();

			for (String deleteURL : URL) {
				HttpClient httpClient = new DefaultHttpClient();
				try {
					HttpGet httpGet = new HttpGet(deleteURL);
					HttpResponse response = httpClient.execute(httpGet);
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
							sb.append(lineIn);
						}
						Log.d("Output from site", sb.toString());

					} else
						Log.d("STATUS CODE ERROR", "!= 200");
				} catch (Exception e) {
					Log.d("Exception", "httpClient");
					e.printStackTrace();
				}

			}
			return sb.toString();
		}

		@Override
		protected void onPostExecute(String result) {

			// adapter.notifyDataSetChanged();
			//resultsTextView.setText(result);
			
			JSONObject statusObject;
			try {
				statusObject = new JSONObject(result);
				
				String noise = statusObject.getString("noise");
				int noiseVal=0;
				if (noise.length()>1){
					String[] noiseDec = noise.split("\\.");
					noiseVal = Integer.parseInt(noiseDec[0]);
				}
				progressNoise.setProgress(noiseVal);
				
				String crowd = statusObject.getString("crowd");
				int crowdVal=0;
				if (crowd.length()>1){
					String[] crowdDec = noise.split("\\.");
					crowdVal = Integer.parseInt(crowdDec[0]);
				}
				progressCrowd.setProgress(crowdVal);
				
				
				resultsTextView.setText("Noise: "+noise + " and Crowd: "+crowd);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}

	}

}
