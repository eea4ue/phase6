package com.cs4720project.phasesix;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cs4720project.phasefour.R;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleFragment extends Fragment {

	Button addNewCourse;
	ListView userCourseList;

	static String userViewCoursesURL = "http://plato.cs.virginia.edu/~cs4720s14pepper/user/";
	static String userDeleteCourseURL = "http://plato.cs.virginia.edu/~cs4720s14pepper/user/";
	static String userName;
	static boolean tempWorkAround = false;
	ArrayList<Course> userCourses = new ArrayList<Course>();
	CourseListAdapter adapter;

	public void setUser(String user) {
		// Log.d("from setUser",user);
		if (user == null && !tempWorkAround)
			userName = "TestAppUser";
		if (user != null && !tempWorkAround)
			userName = user.trim();
		if (!tempWorkAround) {
			userViewCoursesURL += userName + "/";
			userDeleteCourseURL += userName + "/delete/";
			tempWorkAround = true;
			Log.d("from setUser in SchedulesFrag", userName);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_schedule, container,
				false);
		// Intent i = getActivity().getIntent();
		// user=i.getStringExtra("USER_ID");
		// Log.d("Latetest", i.getStringExtra("USER_ID"));
		TextView userLabel = (TextView) rootView.findViewById(R.id.userNametextView);
		userLabel.setText(userName);

		Context context = getActivity();
		userCourseList = (ListView) rootView.findViewById(R.id.userCourseList);
		// View imageButton = rootView.findViewById(R.id.imageButton);
		// imageButton.setOnClickListener(listener);

		userCourses = new ArrayList<Course>();
		adapter = new CourseListAdapter(context, userCourses);
		userCourseList.setAdapter(adapter);
		Log.d("onCreate Adapter:", adapter.toString());

		try {
			Log.d("searchTerm", userViewCoursesURL);
			/** EXECUTE !! GET USER'S COURSE LIST **/

			new GetUserCoursesTask().execute(userViewCoursesURL);

		} catch (Exception e) {
			Log.d("userViewCoursesURL Exception", "");
			Toast.makeText(context,
					"Error: Don't Panic! Problem with searchURL",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

		/** ADD COURSE BUTTON **/
		View addCourseButton = rootView.findViewById(R.id.addNewCourse);
		addCourseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CourseSearch.class);
				intent.putExtra("USER_ID", userName);
				startActivity(intent);
			}

		});

		return rootView;
	}

	private class GetUserCoursesTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {

			StringBuilder courseIDBuilder = new StringBuilder();

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
							courseIDBuilder.append(lineIn);
						}
					} else
						Log.d("STATUS CODE", "!= 200");

				} catch (Exception e) {
					Log.d("Exception", "");
					e.printStackTrace();
				}
			}
			return courseIDBuilder.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONArray courseArray = new JSONArray(result);
				// Log.d("JSONArray", courseArray.toString());
				for (int i = 0; i < courseArray.length(); i++) {
					JSONObject courseObject = courseArray.getJSONObject(i);

					Course course = new Course();
					course.setCourseID(courseObject.getString("courseID"));
					course.setCourseName(courseObject.getString("courseName"));
					course.setCourseNum(courseObject.getString("courseNum"));
					course.setSectionNum(courseObject.getString("sectionNum"));
					course.setCourseInstructor(courseObject
							.getString("courseInstructor"));
					course.setMeetString(courseObject.getString("meetString"));
					course.setMeetRoom(courseObject.getString("meetRoom"));

					userCourses.add(course);
				}
			} catch (JSONException e) {
				Toast.makeText(getActivity(), "Error: onPostExecute",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

			if (userCourses != null) {
				// update ListView with results
				// Log.d("Updater adapter", adapter.toString());
				adapter.notifyDataSetChanged();
			} else
				Toast.makeText(getActivity(),
						"User has no courses! Try adding some.",
						Toast.LENGTH_SHORT).show();
		}
	}

	public void onImageButtonClick(View view) {
		ViewGroup par = (ViewGroup) view.getParent();
		ViewGroup par2 = (ViewGroup) (par).getParent();
		// Log.d("Adapter in clickhandler", ((ListView)
		// par2).getAdapter().toString());
		adapter = (CourseListAdapter) ((ListView) par2).getAdapter();

		TextView sec_num = (TextView) par.findViewById(R.id.sec_num);
		TextView course_num = (TextView) par.findViewById(R.id.course_num);
		String secString = sec_num.getText().toString().trim();
		String coursenumString = course_num.getText().toString().trim();
		adapter.remove(new Course(null, null, coursenumString, secString, null,
				null, null));
		try {
			String tempurl = userDeleteCourseURL + coursenumString + "/"
					+ secString;
			Log.d("deleteTerm", tempurl);
			/** EXECUTE !! GET USER'S COURSE LIST **/
			new DeleteCourseTask().execute(tempurl);
			// adapter.notifyDataSetChanged();
			((View) par2.getParent()).invalidate();
		} catch (Exception e) {
			Log.d("userViewCoursesURL Exception", "");
			e.printStackTrace();
		}

	}

	private class DeleteCourseTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {

			StringBuilder userIDBuilder = new StringBuilder();

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
							userIDBuilder.append(lineIn);
						}
						Log.d("Output from site", userIDBuilder.toString());
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
			// TextView temp = new TextView(getActivity());
			// ListView root;
			// root=(ListView) findViewById(R.id.userCourseList);
			// Log.d("Adapter?",((ListView)root).getAdapter().toString());
			adapter.notifyDataSetChanged();
		}

	}

}