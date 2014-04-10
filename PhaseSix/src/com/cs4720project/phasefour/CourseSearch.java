//Team Pepper, {Amas, Larsen, Seid}
package com.cs4720project.phasefour;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CourseSearch extends Activity {

	public static String EXTRA_MESSAGE = "";
	public static String USER_ID;
	static String searchWebServiceURL = "http://plato.cs.virginia.edu/~cs4720s14pepper/view/CLAS/";
	static String addCourseURL = "http://plato.cs.virginia.edu/~cs4720s14pepper/user/";
	static String userName;
	static boolean workAround = false;
	private ListView courseTableScrollView;
	
	private EditText courseIDEditText;
	private TextView userIDTextView;
	private TextView course_id_name;
	private TextView course_num;
	private TextView sec_num;
	ArrayList<Course> courseObjectArray = new ArrayList<Course>();
	CourseAdapter adapter;
	
	Button enterCourseIDButton;
	Button clearListButton;
	Button viewCourseListButton;
	CheckBox checkbox;

	public void setUser() {
		Intent i = getIntent();
		String user = i.getStringExtra("USER_ID");
		if (user != null && !workAround) {
			userName = user;
			addCourseURL += userName + "/add/";
			workAround = true;
		}
		if (user == null && !workAround) {
			userName = "TestAppUser";
			addCourseURL += "TestAppUser/add/";
			workAround = true;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_search);
		setUser();

		courseIDEditText = (EditText) findViewById(R.id.courseIDEditText);
		courseTableScrollView = (ListView) findViewById(R.id.courseTableScrollView);

		userIDTextView = (TextView) findViewById(R.id.userNameTextView);
		userIDTextView.setText(userName);

		enterCourseIDButton = (Button) findViewById(R.id.searchButton);
		clearListButton = (Button) findViewById(R.id.clearListButton);
		viewCourseListButton = (Button) findViewById(R.id.viewCourseListButton);
		viewCourseListButton.setOnClickListener(viewCourseListListener);

		courseObjectArray = new ArrayList<Course>();
		adapter = new CourseAdapter(this, courseObjectArray);
		courseTableScrollView.setAdapter(adapter);
	}

	public void onCheckboxClicked(View view) {
		ViewGroup par = (ViewGroup) view.getParent();
		checkbox = (CheckBox) par.findViewById(R.id.checkbox);
		boolean checked = ((CheckBox) view).isChecked();

		if (checked) {

			// ALERT DIALOG: CONFIRM ADD COURSE
			course_id_name = (TextView) par.findViewById(R.id.course_id_name);
			sec_num = (TextView) par.findViewById(R.id.sec_num);
			course_num = (TextView) par.findViewById(R.id.course_num);
			sec_num = (TextView) par.findViewById(R.id.sec_num);
			EXTRA_MESSAGE = course_id_name.getText().toString() + " (Section: "
					+ sec_num.getText().toString() + " Course#: "
					+ course_num.getText().toString() + ")";

			AlertDialog.Builder builder = new AlertDialog.Builder(
					CourseSearch.this);
			builder.setTitle(EXTRA_MESSAGE);
			builder.setMessage(R.string.add_to_user_course_list);
			builder.setPositiveButton(R.string.OK_button,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							String courseNumValue = course_num.getText()
									.toString();
							String secNumValue = sec_num.getText().toString();

							if (courseNumValue.length() > 0) {
								try {
									String encodedSearch1 = URLEncoder.encode(
											courseNumValue, "UTF-8");
									String encodedSearch2 = URLEncoder.encode(
											secNumValue, "UTF-8");
									String searchURL = addCourseURL
											+ encodedSearch1 + "/"
											+ encodedSearch2;

									Log.d("searchURL", searchURL);

									/*** EXECUTE ***/
									new AddToCourseListTask()
											.execute(searchURL);

									checkbox.toggle();

									// clear the view
									courseIDEditText.setText("");
									courseObjectArray.clear();
									adapter.notifyDataSetChanged();

									Toast.makeText(getApplicationContext(),
											"Adding Course#:" + courseNumValue
													+ " Section#: "
													+ secNumValue
													+ " to 'My Schedule'",
											Toast.LENGTH_LONG);

								} catch (Exception e) {
									Toast.makeText(
											getApplicationContext(),
											"Error adding course to user's list",
											Toast.LENGTH_LONG).show();
									e.printStackTrace();
								}

							}

						}
					});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							checkbox.toggle();
							adapter.notifyDataSetChanged();
						}
					});

			AlertDialog theAlert = builder.create();
			theAlert.show();
		}
	}

	public void searchCourseID(View view) {

		courseIDEditText = (EditText) findViewById(R.id.courseIDEditText);
		String searchTerm = courseIDEditText.getText().toString();

		if (searchTerm.length() > 0) {

			// force close keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(courseIDEditText.getWindowToken(), 0);
			adapter.clear();
			adapter.notifyDataSetChanged();
			try {
				String encodedSearch = URLEncoder.encode(searchTerm, "UTF-8");
				String searchURL = searchWebServiceURL + encodedSearch;
				Log.d("searchTerm", searchTerm);
				Log.d("searchURL", searchURL);
				new GetCoursesTask().execute(searchURL);

			} catch (Exception e) {
				Log.d("searchCourseID Exception", "");
				Toast.makeText(getApplicationContext(),
						"Error: Don't Panic! Problem with searchURL",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		} else
			Toast.makeText(getApplicationContext(), "Enter a search query!",
					Toast.LENGTH_SHORT).show();
	}

	public OnClickListener viewCourseListListener = new OnClickListener() {

		public void onClick(View v) {

			// new activity - new intent
			Intent intent = new Intent(CourseSearch.this, MainActivity.class);
			intent.putExtra("USER_ID", userName);
			String fragment = "My Schedule";
			intent.putExtra("fragment", fragment);

			startActivity(intent);
		}
	};

	public void clearList(View view) {

		// blank search bar
		courseIDEditText.setText("");

		// clear the displayed results (list)
		courseObjectArray.clear();
		adapter.notifyDataSetChanged();

		// let the user know something
		Toast.makeText(getApplicationContext(),
				"Please enter a new search term.", Toast.LENGTH_SHORT).show();
	}

	private class GetCoursesTask extends AsyncTask<String, Void, String> {

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
				Log.d("JSONArray", courseArray.toString());
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

					courseObjectArray.add(course);
				}
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "Error: onPostExecute",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

			if (courseObjectArray != null) {
				// update View with found courses
				adapter.notifyDataSetChanged();
			} else
				Toast.makeText(getApplicationContext(),
						"Error: no matching courses found", Toast.LENGTH_SHORT)
						.show();
		}
	}

	private class AddToCourseListTask extends AsyncTask<String, Void, String> {

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
	}
}