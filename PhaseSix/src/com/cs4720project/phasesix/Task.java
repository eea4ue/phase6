//Team Pepper, {Amas, Larsen, Seid}, Phase 2
package com.cs4720project.phasesix;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Task {

	public String projectID;
	public String projectTitle;
	public String taskID;
	public String userID;
	public String taskTitle;
	public String status;

	public Task(String projectID, String projectTitle, String taskID,
			String userID, String taskTitle, String status) {
		super();
		this.projectID = projectID;
		this.projectTitle = projectTitle;
		this.taskID = taskID;
		this.userID = userID;
		this.taskTitle = taskTitle;
		this.status = status;
	}

	public Task(JSONObject object) {
		try {
			this.projectID = object.getString("projectID");
			this.projectTitle = object.getString("projectTitle");
			this.taskID = object.getString("taskID");
			this.userID = object.getString("userID");
			this.taskTitle = object.getString("taskTitle");
			this.status = object.getString("status");

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Task> fromJson(JSONArray jsonObjects) {
		ArrayList<Task> tasks = new ArrayList<Task>();
		for (int i = 0; i < jsonObjects.length(); i++) {
			try {
				tasks.add(new Task(jsonObjects.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return tasks;
	}
	@Override
	public boolean equals(Object o){
		 if (o == null)
		   {
		      return false;
		   }

		   if (this.getClass() != o.getClass())
		   {
		      return false;
		   }
		   return ((Task)o).taskID.equals(this.taskID);
	}
	public Task() {
		super();
		projectID = "";
		projectTitle = "";
		taskID = "";
		userID = "";
		taskTitle = "";
		status = "";
	}

	@Override
	public String toString() {
		return "Task [projectID=" + projectID + ", projectTitle="
				+ projectTitle + ", taskID=" + taskID + ", userID=" + userID
				+ ", taskTitle=" + taskTitle + ", status=" + status + "]";
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public String getProjectTitle() {
		return projectTitle;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	public String getTaskTitle() {
		return taskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}