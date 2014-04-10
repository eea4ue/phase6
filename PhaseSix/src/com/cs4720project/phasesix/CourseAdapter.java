package com.cs4720project.phasesix;

import java.util.ArrayList;

import com.cs4720project.phasefour.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CourseAdapter extends ArrayAdapter<Course> {
	public CourseAdapter(Context context, ArrayList<Course> courses) {
		super(context, R.layout.item_course, courses);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Get the data item for this position
		Course course = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_course, null);
		}
		// Lookup view for data population
		TextView course_id_name = (TextView) convertView
				.findViewById(R.id.course_id_name);
		TextView sec_num = (TextView) convertView.findViewById(R.id.sec_num);
		TextView course_num = (TextView) convertView
				.findViewById(R.id.course_num);
		TextView courseInstructorTimeRoom = (TextView) convertView
				.findViewById(R.id.courseInstructorTimeRoom);

		// Populate the data into the template view using the data object
		course_id_name.setText(course.courseID + ":" + course.courseName + " ");
		sec_num.setText(course.sectionNum + " ");
		course_num.setText(course.courseNum + " ");
		courseInstructorTimeRoom.setText(course.courseInstructor + ", "
				+ course.meetString + ", " + course.meetRoom);

		// Return the completed view to render on screen
		return convertView;
	}
}