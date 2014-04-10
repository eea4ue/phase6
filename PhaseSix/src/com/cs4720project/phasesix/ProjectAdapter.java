package com.cs4720project.phasesix;

import java.util.ArrayList;

import com.cs4720project.phasefour.R;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ProjectAdapter extends ArrayAdapter<Project> {
	public ProjectAdapter(Context context, ArrayList<Project> projects) {
		super(context, R.layout.item_project, projects);
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
	
		// Get the data item for this position
		Project project = getItem(position);
		
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_project, null);
		}
		// Lookup view for data population
		LinearLayout nested=(LinearLayout)convertView.findViewById(R.id.nested_layout);
		TextView project_title = (TextView) nested.findViewById(R.id.project_title);
		TextView project_id = (TextView) nested.findViewById(R.id.project_id);
	
		//CheckBox radiobutton = (CheckBox) convertView.findViewById(R.id.checkBox1);
		ImageButton deleteProjectButton = (ImageButton) convertView.findViewById(R.id.deleteProjectButton);

		
		// Populate the data into the template view using the data object
		project_title.setText(project.projectTitle);
		project_id.setText(project.projectID);
		//radiobutton.setChecked(false);
		
		//RadioGroup projectRG = (RadioGroup) convertView.findViewById(R.id.radioProjectGroup);

        //LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT); 
		
		//projectRG.addView(radiobutton, linLayoutParam);

		// Return the completed view to render on screen
		return convertView;
	}
}
