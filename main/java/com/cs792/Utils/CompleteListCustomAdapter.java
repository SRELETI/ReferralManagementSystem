package com.cs792.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs792.projectapp.R;

import java.util.List;

/**
 * Created by sudeelet on 3/18/2015.
 */
public class CompleteListCustomAdapter extends BaseAdapter {

	Context ctx;
	List<CompletedRowItem> completeList;

	public CompleteListCustomAdapter(Context ctx, List<CompletedRowItem> rowItems) {
		this.ctx = ctx;
		completeList = rowItems;
	}
	@Override
	public int getCount() {
		return completeList.size();
	}

	@Override
	public Object getItem(int position) {
		return completeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return completeList.indexOf(getItem(position));
	}

	private class Holder {
		TextView patient_id;
		ImageView photo;
		TextView full_Name;
		TextView status;
		TextView complete_date;
		TextView assignedTo;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView==null) {
			convertView = inflater.inflate(R.layout.completed_row_item,null);
			holder = new Holder();
			holder.patient_id = (TextView) convertView.findViewById(R.id.Patient_idInvisible);
			holder.photo = (ImageView) convertView.findViewById(R.id.Patient_photo);
			holder.full_Name= (TextView) convertView.findViewById(R.id.Patient_fullName);
			holder.status= (TextView) convertView.findViewById(R.id.Patient_Completestatus);
			holder.complete_date = (TextView) convertView.findViewById(R.id.Patient_completeDate);
			holder.assignedTo = (TextView) convertView.findViewById(R.id.Patient_completedBy);
			CompletedRowItem row_pos = completeList.get(position);
			holder.photo.setImageResource(row_pos.getPatient_photo());
			holder.full_Name.setText(row_pos.getName());
			holder.status.setText(row_pos.getStatus());
			holder.complete_date.setText("Completion Date: "+row_pos.getDate());
			holder.assignedTo.setText("Completed By: "+row_pos.getAssignedTo());
			holder.patient_id.setText(row_pos.getPatient_id());
			convertView.setTag(holder);

		}
		else holder = (Holder) convertView.getTag();
		return convertView;
	}
}
