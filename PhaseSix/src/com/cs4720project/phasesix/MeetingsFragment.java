package com.cs4720project.phasesix;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MeetingsFragment extends Fragment {

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

		libSpinner = (Spinner) rootView.findViewById(R.id.libSpinner);
		sectionSpinner = (Spinner) rootView.findViewById(R.id.sectionSpinner);
		timeSpinner = (Spinner) rootView.findViewById(R.id.timeSpinner);
		daySpinner = (Spinner) rootView.findViewById(R.id.daySpinner);

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