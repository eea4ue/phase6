package com.cs4720project.phasesix;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter<Task> {
	public TaskAdapter(Context context, ArrayList<Task> tasks) {
		super(context, R.layout.task_listrow_details, tasks);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Get the data item for this position
		Task task = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.task_listrow_details, null);
		}
		// Lookup view for data population
		TextView taskTextView = (TextView) convertView
				.findViewById(R.id.taskTextView);
		
		TextView taskIDTextView = (TextView) convertView
				.findViewById(R.id.taskIDTextView);

		// CheckBox taskCheckbox = (CheckBox) convertView
		// .findViewById(R.id.taskCheckbox);

		// Populate the data into the template view using the data object
		taskTextView.setText("(ProjectID: " + task.projectID + ")  |  Task: " + task.taskTitle
				+ "  |  Status: "
				+ task.status);
		taskIDTextView.setText(task.taskID);

		
		// Return the completed view to render on screen
		return convertView;
	}
}