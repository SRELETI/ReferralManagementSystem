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
public class AssignedListCustomAdapter  extends BaseAdapter{

	Context ctx;
	List<AssignedRowItem> assignedList;

	public AssignedListCustomAdapter(Context ctx, List<AssignedRowItem> rowItems) {
		this.ctx = ctx;
		assignedList = rowItems;
	}

	@Override
	public int getCount() {
		return assignedList.size();
	}

	@Override
	public Object getItem(int position) {
		return assignedList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return assignedList.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView==null) {
			convertView = inflater.inflate(R.layout.assigned_row_item,null);
			holder = new Holder();
			holder.patient_id = (TextView) convertView.findViewById(R.id.Patient_idInvisible);
			holder.photo = (ImageView) convertView.findViewById(R.id.Patient_photo);
			holder.full_Name= (TextView) convertView.findViewById(R.id.Patient_fullName);
			holder.status= (TextView) convertView.findViewById(R.id.Patient_Completestatus);
			holder.discharge_date = (TextView) convertView.findViewById(R.id.Patient_dischargeDate);
			holder.assignedTo = (TextView) convertView.findViewById(R.id.Patient_assignedTo);
			AssignedRowItem row_pos = assignedList.get(position);
			holder.photo.setImageResource(row_pos.getPatient_photo());
			holder.full_Name.setText(row_pos.getName());
			holder.status.setText(row_pos.getStatus());
			holder.discharge_date.setText("Referral Date: "+row_pos.getDate());
			holder.assignedTo.setText("Referral Assigned To: "+row_pos.getAssignedTo());
			holder.patient_id.setText(row_pos.getPatient_id());
			convertView.setTag(holder);

		}
		else holder = (Holder) convertView.getTag();
		return convertView;
	}

	private class Holder {
		TextView patient_id;
		ImageView photo;
		TextView full_Name;
		TextView status;
		TextView discharge_date;
		TextView assignedTo;
	}
}
