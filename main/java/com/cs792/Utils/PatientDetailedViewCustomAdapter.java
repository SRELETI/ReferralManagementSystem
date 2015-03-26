package com.cs792.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cs792.projectapp.R;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by sudeelet on 3/12/2015.
 */
	public class PatientDetailedViewCustomAdapter extends BaseExpandableListAdapter {

	private Activity context;
	private Map<String,List<String>> listDetails;
	private List<String> details;

	public PatientDetailedViewCustomAdapter(Activity context, List<String> details, Map<String,List<String>> listDetails) {
		this.context = context;
		this.details = details;
		this.listDetails = listDetails;
	}



	@Override
	public int getGroupCount() {
		return details.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return listDetails.get(details.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return details.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return listDetails.get(details.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String detail_heading = (String) getGroup(groupPosition);
		if(convertView==null) {
			LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.group_item,null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.group_heading);
		item.setTypeface(null, Typeface.BOLD);
		item.setText(detail_heading);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		String detail_subHeading = (String) getChild(groupPosition,childPosition);
		if(convertView==null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.group_child_item,null);
		}
		TextView child_item = (TextView) convertView.findViewById(R.id.child_item);
		child_item.setText(detail_subHeading);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}
