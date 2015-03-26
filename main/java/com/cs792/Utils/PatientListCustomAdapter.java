package com.cs792.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs792.projectapp.R;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by sudeelet on 3/8/2015.
 */
public class PatientListCustomAdapter extends BaseAdapter {
	Context context;
	List<PatientRowItem> rowItems;

	public PatientListCustomAdapter(Context context,List<PatientRowItem> rowItems) {
		this.context = context;
		this.rowItems = rowItems;
	}
	@Override
	public int getCount() {
		return rowItems.size();
	}

	@Override
	public Object getItem(int position) {
		return rowItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return rowItems.indexOf(rowItems.get(position));
	}

	private class Holder {
		TextView patient_id;
		ImageView photo;
		TextView full_Name;
		TextView status;
		TextView problems;
		TextView admit_date;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if(convertView==null) {
			convertView = mInflater.inflate(R.layout.list_row,null);
			holder = new Holder();
			holder.patient_id = (TextView) convertView.findViewById(R.id.Patient_idInvisible);
			holder.photo = (ImageView) convertView.findViewById(R.id.Patient_photo);
			holder.full_Name= (TextView) convertView.findViewById(R.id.Patient_fullName);
			holder.status= (TextView) convertView.findViewById(R.id.Patient_Completestatus);
			holder.admit_date = (TextView) convertView.findViewById(R.id.Patient_admitDate);
			holder.problems = (TextView) convertView.findViewById(R.id.Patient_problems);
			PatientRowItem row_pos = rowItems.get(position);
			holder.photo.setImageResource(row_pos.getPatient_photo());
			holder.full_Name.setText(row_pos.getName());
			holder.status.setText(row_pos.getStatus());
			holder.problems.setText(row_pos.getProblems());
			holder.admit_date.setText("Admitted On: "+row_pos.getDate());
			holder.patient_id.setText(row_pos.getPatient_id());
			convertView.setTag(holder);

		}
		else {
			holder = (Holder) convertView.getTag();
		}
		return convertView;
	}
}
