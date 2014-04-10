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

import com.cs4720project.phasefour.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskList extends Activity {

	static String addNewTaskURL = "http://peppernode.azurewebsites.net/task/add/"; // +pid
	static String editTaskURL = "http://peppernode.azurewebsites.net/task/edit/"; // [taskStatus]/[taskID]";
	static String deleteTaskURL = "http://peppernode.azurewebsites.net/task/delete/"; // +taskID
	static String assignTaskURL = "http://peppernode.azurewebsites.net/task/assign/"; // +USER_ID/[taskID]";
	static String viewUserTasksURL = "http://peppernode.azurewebsites.net/task/view/user/"; // +USER_ID";
	static String viewProjectTasksURL = "http://peppernode.azurewebsites.net/task/view/project/"; // +PID

	private EditText taskTitleEditText;
	private EditText taskStatusEditText;
	private TextView projectTextView;
	private ListView taskListView;

	Button addNewTaskButton;
	//ImageButton is a child of View, not of Button
	View deleteTaskButton;
	Button assignTaskButton;

	public static String USER_ID;
	public static String PID;
	public static String P_TITLE;
	static String user;
	static String pid;
	static String pTitle;
	static boolean tempWorkAround = false;

	// ArrayList<Task> userTaskArray = new ArrayList<Task>();
	ArrayList<Task> projectTaskArray = new ArrayList<Task>();
	TaskAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		// setUser();

		Intent i = getIntent();
		user = i.getStringExtra("USER_ID");
		pid = i.getStringExtra("PID");
		pTitle = i.getStringExtra("P_TITLE");
		// pid = (pid.replace("(", " ")).replace(")", " ");
		// pid = pid.trim();
		// Log.d("Edited pid:", pid);
		Log.d("Unedited pid:", pid);

		// pid ="4";
		// user="eea4ue";

		taskTitleEditText = (EditText) findViewById(R.id.taskTitleEditText);
		taskStatusEditText = (EditText) findViewById(R.id.taskStatusEditText);

		// TextView taskTextView = (TextView) findViewById(R.id.taskTextView);
		// TextView taskIDTextView = (TextView)
		// findViewById(R.id.taskIDTextView);
		projectTextView = (TextView) findViewById(R.id.projectTextView);
		projectTextView.setText(pTitle);

		taskListView = (ListView) findViewById(R.id.taskListView);
		adapter = new TaskAdapter(this, projectTaskArray);
		taskListView.setAdapter(adapter);

		addNewTaskButton = (Button) findViewById(R.id.addNewTaskButton);
		deleteTaskButton = (View) findViewById(R.id.deleteTaskButton);
		assignTaskButton = (Button) findViewById(R.id.assignTaskButton);

		addNewTaskButton.setOnClickListener(addNewTaskListener);
		// deleteTaskButton.setOnClickListener(deleteTaskListener);
		// assignTaskButton.setOnClickListener(assignTaskListener);

		try {

			String tempProjTaskURL = viewProjectTasksURL;
			if (pid != null)
				tempProjTaskURL += pid;
			else
				tempProjTaskURL += "4";
			Log.d("viewProjectTasksURL", tempProjTaskURL);
			new GetProjectTasks().execute(tempProjTaskURL);
		} catch (Exception e) {
			Log.d("viewProjectTasksURL Exception", "");
			Toast.makeText(
					getApplicationContext(),
					"Error: Don't Panic! Problem with getting " + pTitle
							+ " tasks.", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

		addNewTaskButton = (Button) findViewById(R.id.addNewTaskButton);
		//deleteTaskButton = (Button) findViewById(R.id.deleteTaskButton);
		assignTaskButton = (Button) findViewById(R.id.assignTaskButton);

		addNewTaskButton.setOnClickListener(addNewTaskListener);
		// deleteTaskButton.setOnClickListener(deleteTaskListener);
		//assignTaskButton.setOnClickListener(assignTaskListener);

		taskListView.setAdapter(adapter);

	}

	// public void setUser(String user) {
	// Log.d("from setUser", user);
	// if (user == null)
	// user = "TestAppUser";
	// if (user != null && !tempWorkAround)
	// this.user = user.trim();
	// if (!tempWorkAround) {
	// /**** UPDATE THESE ****/
	// // userViewProjectsURL += user + "/";
	// // userDeleteCourseURL += user + "/delete/";
	// tempWorkAround = true;
	// }
	// }

	public void onTaskImageButtonClick(View v) {
		ViewGroup par = (ViewGroup) v.getParent();
		TextView taskID = (TextView) par.findViewById(R.id.taskIDTextView);

		// adapter = (TaskAdapter) ((ListView) par).getAdapter();
		String taskString = taskID.getText().toString().trim();
		taskString = taskString.substring(taskString.indexOf(":") + 1,
				taskString.length());
		taskString = taskString.trim();
		adapter.remove(new Task(PID, null, taskString, null, null, null));
		try {
			String tempurl = deleteTaskURL + taskString;
			Log.d("deleteTask", tempurl);
			/** EXECUTE !! GET USER'S COURSE LIST **/
			new DeleteTask().execute(tempurl);
			// adapter.notifyDataSetChanged();
			((View) par.getParent()).invalidate();
		} catch (Exception e) {
			Log.d("userViewCoursesURL Exception", "");
			e.printStackTrace();
		}

	}

	public OnClickListener addNewTaskListener = new OnClickListener() {

		public void onClick(View v) {
			// parsed Data

			taskTitleEditText = (EditText) findViewById(R.id.taskTitleEditText);
			taskStatusEditText = (EditText) findViewById(R.id.taskStatusEditText);

			String taskTitle = taskTitleEditText.getText().toString();
			String taskStatus = taskStatusEditText.getText().toString();
			Task temp = new Task();
			temp.setProjectID(pid);
			temp.setProjectTitle(taskTitle);
			temp.setStatus(taskStatus);
			if (adapter != null)
				adapter.add(temp);
			if (taskTitle.length() > 0 && taskStatus.length() > 0) {
				try {
					String encodedSearch1 = URLEncoder.encode(pid, "UTF-8");
					String encodedSearch2 = URLEncoder.encode(taskTitle,
							"UTF-8");
					String encodedSearch3 = URLEncoder.encode(taskStatus,
							"UTF-8");

					// /task/add/[projectID]/[taskTitle]/[taskStatus]
					String searchURL = addNewTaskURL + encodedSearch1 + "/"
							+ encodedSearch2 + "/" + encodedSearch3;

					Log.d("searchURL", searchURL);

					/*** EXECUTE ***/
					new AddProjectTasks().execute(searchURL);

					taskTitleEditText.setText("");
					taskStatusEditText.setText("");

					String refreshURL = viewProjectTasksURL + pid;
					adapter.clear();
					new GetProjectTasks().execute(refreshURL);

					Toast.makeText(getApplicationContext(), "Adding Task:"
							+ taskTitle + " Status: " + taskStatus
							+ " to ProjectID: " + pid, Toast.LENGTH_LONG);

					// adapter.notifyDataSetChanged();

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"Error adding task to this project",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

			} else
				Toast.makeText(getApplicationContext(),
						"You must enter both a title and status!",
						Toast.LENGTH_SHORT).show();

		}
	};

	// public OnClickListener deleteTaskListener = new OnClickListener() {
	//
	// public void onClick(View v) {
	//
	// taskIDTextView = (TextView) findViewById(R.id.taskIDTextView);
	// taskTextView = (TextView) findViewById(R.id.taskTextView);
	//
	// String taskID = taskIDTextView.getText().toString();
	// String taskText = taskTextView.getText().toString();
	//
	// if (taskID.length() > 0) {
	// try {
	// String encodedSearch1 = URLEncoder.encode(taskID, "UTF-8");
	//
	// // /task/delete/[taskID]";
	// String searchURL = deleteTaskURL + encodedSearch1;
	//
	// Log.d("searchURL", searchURL);
	//
	// /*** EXECUTE ***/
	// new DeleteTask().execute(searchURL);
	//
	// Toast.makeText(getApplicationContext(), "Deleting TaskID:"
	// + taskID + " | " + taskText, Toast.LENGTH_LONG);
	//
	// } catch (Exception e) {
	// Toast.makeText(getApplicationContext(),
	// "Error deleting task...", Toast.LENGTH_SHORT)
	// .show();
	// e.printStackTrace();
	// }
	//
	// }
	//
	// }
	// };

	// Edit: /task/edit/[taskStatus]/[taskID]
	public void editTask(View view) {
		ViewGroup par = (ViewGroup) view.getParent();
		final ViewGroup par2 = (ViewGroup) (par).getParent();
		adapter = (TaskAdapter) ((ListView) par2).getAdapter();

		TextView taskTextView = (TextView) par.findViewById(R.id.taskTextView);
		TextView taskIDTextView = (TextView) par
				.findViewById(R.id.taskIDTextView);

		String taskString = taskTextView.getText().toString().trim();
		final String taskIDString = taskIDTextView.getText().toString().trim();

		AlertDialog.Builder builder = new AlertDialog.Builder(TaskList.this);
		LayoutInflater inflater = TaskList.this.getLayoutInflater();

		final View DialogView = inflater
				.inflate(R.layout.dialog_edittask, null);
		final EditText editTaskStatusEditText = (EditText) DialogView
				.findViewById(R.id.editTaskStatusEditText);

		builder.setTitle(taskIDString + " | " + taskString);
		builder.setView(DialogView);

//<<<<<<< HEAD
//		public void onClick(View v) {
////			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////			LayoutInflater inflater = getActivity().getLayoutInflater();
//			AlertDialog.Builder builder = new AlertDialog.Builder(TaskList.this);
//			LayoutInflater inflater = TaskList.this.getLayoutInflater();
//			View view = inflater.inflate(R.layout.dialog_assigntask, null);
//			builder.setView(view);
//			
////			builder.setView(inflater.inflate(R.layout.dialog_assigntask, null));
//=======
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				TextView taskIDTextView = (TextView) findViewById(R.id.taskIDTextView);
				//String taskIDString = taskIDTextView.getText().toString().trim();
//>>>>>>> b142105965503a5e42295d334016757d7ca26053

				String param = editTaskStatusEditText.getText().toString();

				Context context = getApplicationContext();
				CharSequence taskEditErrorText = "Error! Could not edit task status";
				CharSequence noInputErrorText = "Error! No text was entered.";
				int duration = Toast.LENGTH_SHORT;

				if (param.length() > 0) {
					try {
						String encodedSearch = URLEncoder
								.encode(param, "UTF-8");

						String searchURL = editTaskURL + encodedSearch + "/"
								+ taskIDString;

						Log.d("param", encodedSearch);
						Log.d("editTaskURL", searchURL);

						new EditTask().execute(searchURL);
						((View) par2.getParent()).invalidate();

						String refreshURL = viewProjectTasksURL + pid;
						adapter.clear();
						new GetProjectTasks().execute(refreshURL);

					} catch (Exception e) {
						Toast.makeText(context, taskEditErrorText, duration)
								.show();
						e.printStackTrace();
					}

				} else
					Toast.makeText(context, noInputErrorText, duration).show();
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

		// MAKE THE ALERT DIALOG AND SHOW IT
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		Log.d("EDIT TASK", "TASK EDITED");

		// try {
		// String tempurl = editTaskURL + taskStatus + "/" + taskIDString;
		// Log.d("assignTask", tempurl);
		//
		// /** EXECUTE !! ASSIGN TASK TO USER **/
		// new EditTask().execute(tempurl);
		// adapter.notifyDataSetChanged();
		// Toast.makeText(getApplicationContext(),
		// "Assigning Task: " + taskIDString + " | " + taskString + " to User: "
		// + user, Toast.LENGTH_LONG).show();
		//
		// ((View) par2.getParent()).invalidate();
		// } catch (Exception e) {
		// Log.d("assignTaskURL Exception", "");
		// e.printStackTrace();
		// }

	}

	// Assign: /task/assign/[userID]/[taskID]
	// public void assignTask(View view) {
	// ViewGroup par = (ViewGroup) view.getParent();
	// ViewGroup par2 = (ViewGroup) (par).getParent();
	// adapter = (TaskAdapter) ((ListView) par2).getAdapter();
	//
	// TextView taskTextView = (TextView) par.findViewById(R.id.taskTextView);
	// TextView taskIDTextView = (TextView) par
	// .findViewById(R.id.taskIDTextView);
	//
	// String taskString = taskTextView.getText().toString().trim();
	//
	// String taskIDString = taskIDTextView.getText().toString().trim();
	//
	// AlertDialog.Builder builder = new AlertDialog.Builder(TaskList.this);
	// LayoutInflater inflater = TaskList.this.getLayoutInflater();
	//
	// final View DialogView = inflater
	// .inflate(R.layout.dialog_assigntask, null);
	// final EditText assignToUserEditText = (EditText) DialogView
	// .findViewById(R.id.assignToUserEditText);
	//
	// builder.setTitle(taskIDString + " | " + taskString);
	// builder.setView(DialogView);
	//
	// builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	//
	// // TextView taskIDTextView = (TextView)
	// findViewById(R.id.taskIDTextView);
	// // String taskIDString = taskIDTextView.getText().toString().trim();
	//
	// String param = assignToUserEditText.getText().toString().trim();
	// Log.d("assignToUser", param);
	//
	// Context context = getApplicationContext();
	// CharSequence taskEditErrorText = "Error! Could not edit task status";
	// CharSequence noInputErrorText = "Error! No text was entered.";
	// int duration = Toast.LENGTH_SHORT;
	//
	// // adapter.remove(new Project(pIDString, null));
	// try {
	// String tempurl = assignTaskURL + param + "/" + taskIDString;
	// Log.d("assignTask", tempurl);
	//
	// /** EXECUTE !! ASSIGN TASK TO USER **/
	// new AssignTasks().execute(tempurl);
	// adapter.notifyDataSetChanged();
	// Toast.makeText(
	// getApplicationContext(),
	// "Assigning Task: " + taskIDString + " | " + taskString
	// + " to User: " + user, Toast.LENGTH_LONG).show();
	//
	// ((View) par2.getParent()).invalidate();
	// } catch (Exception e) {
	// Log.d("assignTaskURL Exception", "");
	// e.printStackTrace();
	// }
	//
	// }

	// public OnClickListener assignTaskListener = new OnClickListener() {
	public void assignTask(View view) {

		// ViewGroup par = (ViewGroup) DialogView.getParent();
		// ViewGroup par2 = (ViewGroup) (par).getParent();
		final TextView taskTextView = (TextView) findViewById(R.id.taskTextView);
		final TextView taskIDTextView = (TextView) findViewById(R.id.taskIDTextView);
		final String taskID = taskIDTextView.getText().toString();
				
		AlertDialog.Builder builder = new AlertDialog.Builder(TaskList.this);
		LayoutInflater inflater = TaskList.this.getLayoutInflater();

		final View DialogView = inflater.inflate(R.layout.dialog_assigntask,
				null);
		final EditText assignToUserEditText = (EditText) DialogView
				.findViewById(R.id.assignToUserEditText);

		builder.setTitle("Assign This Task");
		builder.setView(DialogView);

		final Button assignTaskToMeButton = (Button) DialogView
				.findViewById(R.id.assignTaskToMeButton);
		final Button assignTaskToOtherButton = (Button) DialogView
				.findViewById(R.id.assignTaskToOtherButton);

		assignTaskToMeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// url/userID/taskID
				Context context = getApplicationContext();
				CharSequence noInputErrorText = "Error! No input!";
				int duration = Toast.LENGTH_SHORT;

				// adapter = (TaskAdapter) ((ListView) par2).getAdapter();

				//taskID = taskIDTextView.getText().toString();

				if (user.length() > 0) {
					try {
						String encodedSearch1 = URLEncoder
								.encode(user, "UTF-8");
						String encodedSearch2 = URLEncoder.encode(taskID,
								"UTF-8");

						String searchURL = assignTaskURL + encodedSearch1 + "/"
								+ encodedSearch2;

						Log.d("param1", encodedSearch1);
						Log.d("param2", encodedSearch2);
						Log.d("searchURL", searchURL);

						new AssignTasks().execute(searchURL);

						adapter.notifyDataSetChanged();
						Toast.makeText(
								getApplicationContext(),
								"Assigning Task: " + taskID + " | "
										+ taskTextView.getText().toString()
										+ " to User: " + user,
								Toast.LENGTH_LONG).show();

						// ((View) par2.getParent()).invalidate();
					} catch (Exception e) {
						Log.d("assignTaskURL Exception", "");
						e.printStackTrace();
					}
				} else
					Toast.makeText(context, noInputErrorText, duration).show();
			}
		});

		assignTaskToOtherButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Context context = getApplicationContext();
				CharSequence noInputErrorText = "Error! No input!";
				int duration = Toast.LENGTH_SHORT;

				// adapter = (TaskAdapter) ((ListView) par2).getAdapter();

				//taskID = taskIDTextView.getText().toString();

				String assignToUserString = assignToUserEditText.getText()
						.toString();

				if (assignToUserString.length() > 0) {
					try {
						String encodedSearch1 = URLEncoder.encode(
								assignToUserString, "UTF-8");
						String encodedSearch2 = URLEncoder.encode(taskID,
								"UTF-8");

						String searchURL = assignTaskURL + encodedSearch1 + "/"
								+ encodedSearch2;

						Log.d("param1", encodedSearch1);
						Log.d("param2", encodedSearch2);
						Log.d("searchURL", searchURL);

						new AssignTasks().execute(searchURL);
						adapter.notifyDataSetChanged();
						Toast.makeText(
								getApplicationContext(),
								"Assigning Task: " + taskID + " | "
										+ taskTextView.getText().toString()
										+ " to User: " + assignToUserString,
								Toast.LENGTH_LONG).show();

					} catch (Exception e) {
						Toast.makeText(
								context,
								"Error assigning task: " + taskID + "to user:"
										+ assignToUserString, duration).show();
						e.printStackTrace();
					}
				} else
					Toast.makeText(context, noInputErrorText, duration).show();
			}

		});
		builder.setPositiveButton("Done",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		// MAKE THE ALERT DIALOG AND SHOW IT
		AlertDialog alertDialog = builder.create();
		alertDialog.show();

	};

	private class AddProjectTasks extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {

			StringBuilder taskBuilder = new StringBuilder();

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
							taskBuilder.append(lineIn);
						}
					} else
						Log.d("STATUS CODE", "!= 200");

				} catch (Exception e) {
					Log.d("Exception", "");
					e.printStackTrace();
				}
			}
			return taskBuilder.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			
			
			
			if (projectTaskArray != null) {
				adapter.notifyDataSetChanged();
			} else
				Toast.makeText(getApplicationContext(),
						"Error creating tasks...", Toast.LENGTH_SHORT).show();
		}
	}

	private class DeleteTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {

			StringBuilder taskIDBuilder = new StringBuilder();

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
							taskIDBuilder.append(lineIn);
						}
						Log.d("Output from site", taskIDBuilder.toString());

					} else
						Log.d("STATUS CODE ERROR", "!= 200");
				} catch (Exception e) {
					Log.d("Exception", "httpClient");
					e.printStackTrace();
				}

			}
			return taskIDBuilder.toString();
		}

		@Override
		protected void onPostExecute(String result) {

			adapter.notifyDataSetChanged();
		}

	}

	private class EditTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {

			StringBuilder taskBuilder = new StringBuilder();

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
							taskBuilder.append(lineIn);
						}
					} else
						Log.d("STATUS CODE", "!= 200");

				} catch (Exception e) {
					Log.d("Exception", "");
					e.printStackTrace();
				}
			}
			return taskBuilder.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			adapter.notifyDataSetChanged();
//			projectTaskArray.clear();
//			try {
//
//				String tempProjTaskURL = viewProjectTasksURL;
//				if (pid != null)
//					tempProjTaskURL += pid;
//				else
//					tempProjTaskURL += "4";
//				Log.d("viewProjectTasksURL", tempProjTaskURL);
//				new GetProjectTasks().execute(tempProjTaskURL);
//			} catch (Exception e) {
//				Log.d("viewProjectTasksURL Exception", "");
//				Toast.makeText(
//						getApplicationContext(),
//						"Error: Don't Panic! Problem with getting " + pTitle
//								+ " tasks.", Toast.LENGTH_SHORT).show();
//				e.printStackTrace();
//			}
		}

	}

	private class AssignTasks extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {

			StringBuilder taskBuilder = new StringBuilder();

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
							taskBuilder.append(lineIn);
						}
					} else
						Log.d("STATUS CODE", "!= 200");

				} catch (Exception e) {
					Log.d("Exception", "");
					e.printStackTrace();
				}
			}
			return taskBuilder.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			if (projectTaskArray != null) {
				adapter.notifyDataSetChanged();
			} else
				Toast.makeText(getApplicationContext(), "no tasks!",
						Toast.LENGTH_SHORT).show();
		}

	}

	private class GetProjectTasks extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {

			StringBuilder taskBuilder = new StringBuilder();

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
							taskBuilder.append(lineIn);
						}
					} else
						Log.d("STATUS CODE", "!= 200");

				} catch (Exception e) {
					Log.d("Exception", "");
					e.printStackTrace();
				}
			}
			return taskBuilder.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONArray taskArray = new JSONArray(result);
				Log.d("JSONArray", taskArray.toString());
				for (int i = 0; i < taskArray.length(); i++) {
					JSONObject taskObject = taskArray.getJSONObject(i);

					Task task = new Task();
					task.setProjectID(taskObject.getString("projectID"));
					task.setProjectTitle(taskObject.getString("projectTitle"));
					task.setTaskID(taskObject.getString("taskID"));
					task.setTaskTitle(taskObject.getString("taskTitle"));
					task.setStatus(taskObject.getString("status"));

					projectTaskArray.add(task);
				}
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "Error: onPostExecute",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

			if (projectTaskArray != null) {
				adapter.notifyDataSetChanged();
			} else
				Toast.makeText(getApplicationContext(),
						"Error: no matching tasks found", Toast.LENGTH_SHORT)
						.show();
		}
	}
}