//Team Pepper, {Amas, Larsen, Seid}, Phase 2
package com.cs4720project.phasesix;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Project {

	public String projectID;
	public String projectTitle;
	
	
	public Project() {
		super();
		projectID = "";
		projectTitle = "";
	}

	public Project(String projectID, String projectTitle) {
		super();
		this.projectID = projectID;
		this.projectTitle = projectTitle;
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

	@Override
	public String toString() {
		return "Project [projectID=" + projectID + ", projectTitle="
				+ projectTitle + "]";
	}

	public Project(JSONObject object) {
		try {
			this.projectID = object.getString("projectID");
			this.projectTitle = object.getString("projectTitle");

		} catch (JSONException e) {
			e.printStackTrace();
		}
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
		   return ((Project)o).projectID.equals(this.projectID);
	}
	public static ArrayList<Project> fromJson(JSONArray jsonObjects) {
		ArrayList<Project> projects = new ArrayList<Project>();
		for (int i = 0; i < jsonObjects.length(); i++) {
			try {
				projects.add(new Project(jsonObjects.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return projects;
	}

	
}