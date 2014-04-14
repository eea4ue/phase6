package com.cs4720project.phasesix;

import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;

public class MeetingsFragment extends Fragment implements
		OnItemSelectedListener {

	LinearLayout spinners;
	Button submitButton;
	Spinner libSpinner;
	Spinner sectionSpinner;
	Spinner timeSpinner;
	Spinner daySpinner;
	TextView resultsTextView;

	static String userName;
	static boolean tempWorkAround = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_meetings, container,
				false);

		TextView userLabel = (TextView) rootView
				.findViewById(R.id.userNametextView);
		userLabel.setText(userName);

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

		// try {
		// Log.d("searchTerm", userViewCoursesURL);
		// /** EXECUTE !! GET USER'S COURSE LIST **/
		//
		// new GetUserCoursesTask().execute(userViewCoursesURL);
		//
		// } catch (Exception e) {
		// Log.d("userViewCoursesURL Exception", "");
		// Toast.makeText(context,
		// "Error: Don't Panic! Problem with searchURL",
		// Toast.LENGTH_SHORT).show();
		// e.printStackTrace();
		// }
		//
		/** SUBMIT BUTTON **/
		View submitButton = rootView.findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/* MAKE THE ASYNC CALL WITH THE BUILT URL */
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

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		if (arg0.equals(libSpinner)) {
				sectionSpinner.setEnabled(true);

		if (libSpinner.getSelectedItem().equals("Alderman")) {
			ArrayAdapter<CharSequence> adapter0 = ArrayAdapter
					.createFromResource(getActivity(), R.array.alderman_array,
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
					.createFromResource(getActivity(), R.array.clemons_array,
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
					.createFromResource(getActivity(), R.array.thornton_array,
							android.R.layout.simple_spinner_item);
			adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sectionSpinner.setAdapter(adapter5);
		} else if (libSpinner.getSelectedItem().equals("Wilsdorf")) {

			ArrayAdapter<CharSequence> adapter6 = ArrayAdapter
					.createFromResource(getActivity(), R.array.wilsdorf_array,
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

	// private class GetUserCoursesTask extends AsyncTask<String, Void, String>
	// {
	//
	// @Override
	// protected void onPreExecute() {
	// }
	//
	// @Override
	// protected String doInBackground(String... URL) {
	//
	// StringBuilder courseIDBuilder = new StringBuilder();
	//
	// for (String searchURL : URL) {
	// HttpClient httpClient = new DefaultHttpClient();
	// try {
	// HttpGet httpGet = new HttpGet(searchURL);
	// HttpResponse response = httpClient.execute(httpGet);
	// StatusLine searchStatus = response.getStatusLine();
	//
	// if (searchStatus.getStatusCode() == 200) {
	// HttpEntity entity = response.getEntity();
	// InputStream content = entity.getContent();
	//
	// // read the data you got
	// InputStreamReader inputStreamReader = new InputStreamReader(
	// content);
	// BufferedReader reader = new BufferedReader(
	// inputStreamReader);
	//
	// // read one line at a time and append to stringBuilder
	// String lineIn;
	// while ((lineIn = reader.readLine()) != null) {
	// courseIDBuilder.append(lineIn);
	// }
	// } else
	// Log.d("STATUS CODE", "!= 200");
	//
	// } catch (Exception e) {
	// Log.d("Exception", "");
	// e.printStackTrace();
	// }
	// }
	// return courseIDBuilder.toString();
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// try {
	// JSONArray courseArray = new JSONArray(result);
	// Log.d("JSONArray", courseArray.toString());
	// for (int i = 0; i < courseArray.length(); i++) {
	// JSONObject courseObject = courseArray.getJSONObject(i);
	//
	// Course course = new Course();
	// course.setCourseID(courseObject.getString("courseID"));
	// course.setCourseName(courseObject.getString("courseName"));
	// course.setCourseNum(courseObject.getString("courseNum"));
	// course.setSectionNum(courseObject.getString("sectionNum"));
	// course.setCourseInstructor(courseObject
	// .getString("courseInstructor"));
	// course.setMeetString(courseObject.getString("meetString"));
	// course.setMeetRoom(courseObject.getString("meetRoom"));
	//
	// userCourses.add(course);
	// }
	// } catch (JSONException e) {
	// Toast.makeText(getActivity(), "Error: onPostExecute",
	// Toast.LENGTH_SHORT).show();
	// e.printStackTrace();
	// }
	//
	// if (userCourses != null) {
	// // update ListView with results
	// // Log.d("Updater adapter", adapter.toString());
	// adapter.notifyDataSetChanged();
	// } else
	// Toast.makeText(getActivity(),
	// "User has no courses! Try adding some.",
	// Toast.LENGTH_SHORT).show();
	// }
	// }
	//
	// private class DeleteCourseTask extends AsyncTask<String, Void, String> {
	//
	// @Override
	// protected void onPreExecute() {
	// }
	//
	// @Override
	// protected String doInBackground(String... URL) {
	//
	// StringBuilder userIDBuilder = new StringBuilder();
	//
	// for (String deleteURL : URL) {
	// HttpClient httpClient = new DefaultHttpClient();
	// try {
	// HttpGet httpGet = new HttpGet(deleteURL);
	// HttpResponse response = httpClient.execute(httpGet);
	// StatusLine searchStatus = response.getStatusLine();
	//
	// if (searchStatus.getStatusCode() == 200) {
	// HttpEntity entity = response.getEntity();
	// InputStream content = entity.getContent();
	//
	// // read the data you got
	// InputStreamReader inputStreamReader = new InputStreamReader(
	// content);
	// BufferedReader reader = new BufferedReader(
	// inputStreamReader);
	//
	// // read one line at a time and append to stringBuilder
	// String lineIn;
	// while ((lineIn = reader.readLine()) != null) {
	// userIDBuilder.append(lineIn);
	// }
	// Log.d("Output from site", userIDBuilder.toString());
	// // // THEN TAKE USER TO MAIN ACTIVITY
	// // Intent intent = new Intent(MainActivity.this,
	// // MainActivity.class);
	// // startActivity(intent);
	// // adapter.notifyDataSetChanged();
	// } else
	// Log.d("STATUS CODE ERROR", "!= 200");
	// } catch (Exception e) {
	// Log.d("Exception", "httpClient");
	// e.printStackTrace();
	// }
	//
	// }
	// return userIDBuilder.toString();
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// // TextView temp = new TextView(getActivity());
	// // ListView root;
	// // root=(ListView) findViewById(R.id.userCourseList);
	// // Log.d("Adapter?",((ListView)root).getAdapter().toString());
	// adapter.notifyDataSetChanged();
	// }
	//
	// }

}
