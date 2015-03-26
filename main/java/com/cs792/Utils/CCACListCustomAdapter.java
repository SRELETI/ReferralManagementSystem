package com.cs792.Utils;

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

/**
 * Created by sudeelet on 3/16/2015.
 */
public class CCACListCustomAdapter extends BaseAdapter {

	Context ctx;
	List<CCACRowItem> referralList;

	public CCACListCustomAdapter(Context ctx, List<CCACRowItem> rowItems) {
		this.ctx = ctx;
		referralList = rowItems;
	}
	@Override
	public int getCount() {
		return referralList.size();
	}

	@Override
	public Object getItem(int position) {
		return referralList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return referralList.indexOf(getItem(position));
	}

	private class Holder {
		TextView patient_id;
		ImageView photo;
		TextView full_Name;
		TextView status;
		TextView discharge_date;
		TextView completedBy;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView==null) {
			convertView = inflater.inflate(R.layout.ccac_listrow,null);
			holder = new Holder();
			holder.patient_id = (TextView) convertView.findViewById(R.id.Patient_idInvisible);
			holder.photo = (ImageView) convertView.findViewById(R.id.Patient_photo);
			holder.full_Name= (TextView) convertView.findViewById(R.id.Patient_fullName);
			holder.status= (TextView) convertView.findViewById(R.id.Patient_Completestatus);
			holder.discharge_date = (TextView) convertView.findViewById(R.id.Patient_dischargeDate);
			holder.completedBy = (TextView) convertView.findViewById(R.id.Patient_completedBy);
			CCACRowItem row_pos = referralList.get(position);
			holder.photo.setImageResource(row_pos.getPatient_photo());
			holder.full_Name.setText(row_pos.getName());
			holder.status.setText(row_pos.getStatus());
			holder.discharge_date.setText("Referral Date: "+row_pos.getDate());
			holder.completedBy.setText("Referral Submitted By: "+row_pos.getCompletedBy());
			holder.patient_id.setText(row_pos.getPatient_id());
			convertView.setTag(holder);

		}
		else holder = (Holder) convertView.getTag();
		return convertView;
	}
}
