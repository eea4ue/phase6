//Team Pepper, {Amas, Larsen, Seid}, Phase 2

package com.cs4720project.phasesix;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Course {

	public String courseID;
	public String courseName;
	public String courseNum;
	public String sectionNum;
	public String courseInstructor;
	public String meetString;
	public String meetRoom;

	public Course(String courseID, String courseName, String courseNum,
			String sectionNum, String courseInstructor, String meetString,
			String meetRoom) {
		super();
		this.courseID = courseID;
		this.courseName = courseName;
		this.courseNum = courseNum;
		this.sectionNum = sectionNum;
		this.courseInstructor = courseInstructor;
		this.meetString = meetString;
		this.meetRoom = meetRoom;
	}

	public Course(JSONObject object) {
		try {
			this.courseID = object.getString("courseID");
			this.courseName = object.getString("courseName");
			this.courseNum = object.getString("courseNum");
			this.sectionNum = object.getString("sectionNum");
			this.courseInstructor = object.getString("courseInstructor");
			this.meetString = object.getString("meetString");
			this.meetRoom = object.getString("meetRoom");

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// User.fromJson(jsonArray);
	public static ArrayList<Course> fromJson(JSONArray jsonObjects) {
		ArrayList<Course> courses = new ArrayList<Course>();
		for (int i = 0; i < jsonObjects.length(); i++) {
			try {
				courses.add(new Course(jsonObjects.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return courses;
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
		   return ((Course)o).courseNum.equals(this.courseNum) && ((Course)o).sectionNum.equals(this.sectionNum);
	}
	public Course() {
		super();
		courseID = "";
		courseName = "";
		courseNum = "";
		sectionNum = "";
		courseInstructor = "";
		meetString = "";
		meetRoom = "";
	}

	@Override
	public String toString() {
		return courseID + ":" + courseName + " (CourseNum:" + courseNum
				+ ", SectionNum: " + sectionNum + ") Instructor: "
				+ courseInstructor + ", Meet Time:" + meetString + ", "
				+ meetRoom;
	}

	public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(String courseNum) {
		this.courseNum = courseNum;
	}

	public String getSectionNum() {
		return sectionNum;
	}

	public void setSectionNum(String sectionNum) {
		this.sectionNum = sectionNum;
	}

	public String getCourseInstructor() {
		return courseInstructor;
	}

	public void setCourseInstructor(String courseInstructor) {
		this.courseInstructor = courseInstructor;
	}

	public String getMeetString() {
		return meetString;
	}

	public void setMeetString(String meetString) {
		this.meetString = meetString;
	}

	public String getMeetRoom() {
		return meetRoom;
	}

	public void setMeetRoom(String meetRoom) {
		this.meetRoom = meetRoom;
	}
}