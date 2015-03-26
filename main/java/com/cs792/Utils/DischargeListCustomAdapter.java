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
import java.util.zip.Inflater;

/**
 * Created by sudeelet on 3/15/2015.
 */
public class DischargeListCustomAdapter extends BaseAdapter {
	Context context;
	List<DischargeRowItem> dischargeList;

	public DischargeListCustomAdapter(Context context, List<DischargeRowItem> rowItems) {
		this.context = context;
		this.dischargeList = rowItems;
	}

	@Override
	public int getCount() {
		return dischargeList.size();
	}

	@Override
	public Object getItem(int position) {
		return dischargeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return dischargeList.indexOf(dischargeList.get(position));
	}

	private class  Holder2 {
		TextView patient_id;
		ImageView photo;
		TextView full_Name;
		TextView status;
		TextView submit_date;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder2 holder;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView==null) {
			convertView = inflater.inflate(R.layout.dischargeplanner_listrow,null);
			holder = new Holder2();
			holder.patient_id = (TextView) convertView.findViewById(R.id.Patient_idInvisible);
			holder.photo = (ImageView) convertView.findViewById(R.id.Patient_photo);
			holder.full_Name= (TextView) convertView.findViewById(R.id.Patient_fullName);
			holder.status= (TextView) convertView.findViewById(R.id.Patient_Completestatus);
			holder.submit_date = (TextView) convertView.findViewById(R.id.Patient_submitDate);
			DischargeRowItem row_pos = dischargeList.get(position);
			holder.photo.setImageResource(row_pos.getPatient_photo());
			holder.full_Name.setText(row_pos.getName());
			holder.status.setText(row_pos.getStatus());
			holder.submit_date.setText("Submitted On: "+row_pos.getDate());
			holder.patient_id.setText(row_pos.getPatient_id());
			convertView.setTag(holder);
		}
		else holder = (Holder2) convertView.getTag();
		return convertView;
	}
}
