package com.cs4720project.phasesix;

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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectsFragment extends Fragment {

	ArrayList<Project> projectObjectArray = new ArrayList<Project>();
	public ProjectAdapter adapter;
	private ListView projectTableScrollView;

	static String projectURL = "http://project-task-service.herokuapp.com/project/view/details/";
	private static String addNewProjectURL = "http://project-task-service.herokuapp.com/project/add/";
	private static String deleteProjectURL = "http://project-task-service.herokuapp.com/project/delete/"; // +pid
	private static String userName;
	static boolean tempWorkAround = false;
	private static Context context;
	private static String deleteProjPID;
	private static String deleteProjPTITLE;

	public void setUser(String user) {
		
		if (user == null && !tempWorkAround)
			userName = "TestAppUser";
		if (user != null && !tempWorkAround)
			userName = user.trim();
		if (!tempWorkAround) {
			projectURL += userName;
			tempWorkAround = true;
			Log.d("from setUser in ProjectsFrag", userName);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (context != null)
			context = activity;
	}

	public void setActivity(Activity activity) {
		if (context != null)
			context = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (getActivity() != null)
			context = getActivity();
		final View rootView = inflater.inflate(R.layout.fragment_project_list,
				container, false);

		Button addProjectButton = (Button) rootView
				.findViewById(R.id.addNewProjectButton);

		addProjectButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				LayoutInflater inflater = getActivity().getLayoutInflater();
				final View view = inflater.inflate(R.layout.dialog_addproject,
						null);
				builder.setView(view);

				// Set the positive OK button
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								final EditText projectTitleEditText = (EditText) view
										.findViewById(R.id.projectTitleEditText);
								String param = projectTitleEditText.getText()
										.toString();

								Context context = getActivity();
								CharSequence projectCreationErrorText = "Error! Could not create project: "
										+ param;
								CharSequence noInputErrorText = "Error! You didn't give your project a title";
								int duration = Toast.LENGTH_SHORT;

								if (param.length() > 0) {
									try {
										String encodedSearch = URLEncoder
												.encode(param, "UTF-8");
										String addProjectURL = addNewProjectURL
												+ encodedSearch + "/"
												+ userName;
										Log.d("param", param);
										Log.d("addProjectURL", addProjectURL);

										new AddToProjectListTask()
												.execute(addProjectURL);

										// new
										// GetCoursesTask().execute(searchURL);
									} catch (Exception e) {
										Toast.makeText(context,
												projectCreationErrorText,
												duration).show();
										e.printStackTrace();
									}

								} else
									Toast.makeText(context, noInputErrorText,
											duration).show();
							}
						});

				// Set the negative Cancel button
				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
				/*
				 * Use dialog_addproject.xml edit field:
				 */

				// MAKE THE ALERT DIALOG AND SHOW IT
				AlertDialog alertDialog = builder.create();
				alertDialog.show();
				Log.d("ADD_PROJECT", "Add Project clicked");

			}

		});

		// set content of a tab host .setContent(int viewID)
		// set the my course given default user of kml5aa
		projectTableScrollView = (ListView) rootView
				.findViewById(R.id.projectView);
		projectObjectArray = new ArrayList<Project>();
		// adapter = new CourseAdapter(this, courseObjectArray);
		adapter = new ProjectAdapter(getActivity(), projectObjectArray);
		projectTableScrollView.setAdapter(adapter);

		// String username = "kml5aa";
		// searchURL=searchURL+username;

		//userName = i.getStringExtra("USER_ID");
//		Intent intent = new Intent(getActivity(), ProjectsFragment.class);
//		userName = intent.getStringExtra("USER_ID");
//		setUser(userName);
		
		TextView userLabel = (TextView) rootView
				.findViewById(R.id.showUsername);

		// Log.d("Projects user",user);

		userLabel.setText(userName);

		try {
			Log.d("projectURL", projectURL);
			new GetProjectsTask().execute(projectURL);
		} catch (Exception e) {
			Log.d("userViewCoursesURL Exception", "");
			Toast.makeText(context,
					"Error: Don't Panic! Problem with projectURL",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		return rootView;
	}

	public void doOnProjectDeleteConfirm() {

	}

	public void deleteProjectButton(View view) {
		ViewGroup par = (ViewGroup) view.getParent();
		ViewGroup par2 = (ViewGroup) (par).getParent();
		adapter = (ProjectAdapter) ((ListView) par2).getAdapter();

		TextView project_title = (TextView) par
				.findViewById(R.id.project_title);
		TextView project_id = (TextView) par.findViewById(R.id.project_id);
		deleteProjPTITLE = project_title.getText().toString().trim();
		deleteProjPID = project_id.getText().toString().trim();

		String EXTRA_MESSAGE = deleteProjPTITLE + deleteProjPID;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(EXTRA_MESSAGE);
		builder.setMessage(R.string.project_delete_confirm);
		builder.setPositiveButton(R.string.OK_button,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						try {
							String tempurl = deleteProjectURL + deleteProjPID;
							Log.d("deleteTerm", tempurl);
							if (adapter != null)
								adapter.remove(new Project(deleteProjPID, null));
							/** EXECUTE !! DELETE PROJECT **/
							new DeleteProject().execute(tempurl);
							adapter.notifyDataSetChanged();
							Toast.makeText(
									context,
									"Deleting Project: " + deleteProjPTITLE
											+ " ID: " + deleteProjPID,
									Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
							Toast.makeText(context, "Error deleting Project",
									Toast.LENGTH_LONG).show();

							Log.d("deleteProjectURL Exception", "");
							e.printStackTrace();
						}
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		AlertDialog theAlert = builder.create();
		theAlert.show();

		// adapter.remove(new Project(pIDString, null));

	}

	public void onProjectClick(View v) {
		ViewGroup par = (ViewGroup) v.getParent();
		TextView projectTitle = (TextView) par.findViewById(R.id.project_title);
		TextView projectID = (TextView) par.findViewById(R.id.project_id);

		// if (isAdded()) {

		Log.d("Project ID selected:", projectID.getText().toString());
		Intent intent = new Intent(context, TaskList.class);
		intent.putExtra("USER_ID", userName);
		intent.putExtra("PID", projectID.getText().toString());
		intent.putExtra("P_TITLE", projectTitle.getText().toString());
		context.startActivity(intent);
		// }
		// else
		// {

		// }
	}

	private class GetProjectsTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {

			StringBuilder courseIDBuilder = new StringBuilder();

			for (String projectURL : URL) {
				HttpClient httpClient = new DefaultHttpClient();
				try {

					HttpGet httpGet = new HttpGet(projectURL);
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
						inputStreamReader.close();
						Log.d("Output from Project site:",courseIDBuilder.toString());
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
				
				JSONArray projectArray = new JSONArray(result);
				//Log.d("JSONArray", projectArray.toString());

				int lastID = 0;

				for (int i = 0; i < projectArray.length(); i++) {

					JSONObject projectObject = projectArray.getJSONObject(i);
					Project project = new Project();

					project.setProjectID(projectObject.getString("projectID"));

					project.setProjectTitle(projectObject
							.getString("projectTitle"));

					if ((Integer.parseInt(projectObject.getString("projectID"))) > lastID) {

						projectObjectArray.add(project);

						lastID = Integer.parseInt(projectObject
								.getString("projectID"));
					}

				}
			} catch (JSONException e) {
				Toast.makeText(getActivity(), "Error: onPostExecute",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

			if (projectObjectArray != null) {
				// update View with found courses
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(getActivity(), "Error: no projects found",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class AddToProjectListTask extends AsyncTask<String, Void, String> {

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
						inputStreamReader.close();
						// // THEN TAKE USER TO MAIN ACTIVITY
						// Intent intent = new Intent(MainActivity.this,
						// MainActivity.class);
						// startActivity(intent);

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
			projectObjectArray.clear();

			// new ProjectsFragment();
			try {
				Log.d("projectURL", projectURL);
				new GetProjectsTask().execute(projectURL);
			} catch (Exception e) {
				Log.d("userViewCoursesURL Exception", "");
				Toast.makeText(getActivity(),
						"Error: Don't Panic! Problem with projectURL",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

		}

	}

	private class DeleteProject extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {

			StringBuilder pIDBuilder = new StringBuilder();

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
							pIDBuilder.append(lineIn);
						}
						inputStreamReader.close();
						
						// // THEN TAKE USER TO MAIN ACTIVITY
						// Intent intent = new Intent(MainActivity.this,
						// MainActivity.class);
						// startActivity(intent);

					} else
						Log.d("STATUS CODE ERROR", "!= 200");
				} catch (Exception e) {
					Log.d("Exception", "httpClient");
					e.printStackTrace();
				}

			}
			return pIDBuilder.toString();
		}

		protected void onPostExecute(String result) {
			projectObjectArray.clear();

			// new ProjectsFragment();
			try {
				Log.d("projectURL", projectURL);
				new GetProjectsTask().execute(projectURL);
			} catch (Exception e) {
				Log.d("userViewCoursesURL Exception", "");
				Toast.makeText(getActivity(),
						"Error: Don't Panic! Problem with projectURL",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

		}

	}

}
