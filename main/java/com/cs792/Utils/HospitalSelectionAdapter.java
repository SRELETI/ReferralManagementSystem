package com.cs792.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cs792.projectapp.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by sudeelet on 3/17/2015.
 */
public class HospitalSelectionAdapter extends BaseAdapter {

	private Context context;
	private List<HospitalSelectionRowItem> rowItems;

	public HospitalSelectionAdapter(Context context, List<HospitalSelectionRowItem> rowItems) {
		this.context = context;
		this.rowItems = rowItems;
	}
	@Override
	public int getCount() {
		return this.rowItems.size();
	}

	@Override
	public Object getItem(int position) {
		return this.rowItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return this.rowItems.indexOf(getItem(position));
	}

	private class Holder {
		TextView hospital_name;
		TextView hospital_id;
		TextView beds;
		TextView distance;
		TextView wheelChair;
		TextView interpreter;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView==null) {
			convertView = inflater.inflate(R.layout.hospital_selection_listrow,null);
			holder = new Holder();
			holder.hospital_id = (TextView) convertView.findViewById(R.id.Hospital_Id);
			holder.hospital_name = (TextView) convertView.findViewById(R.id.Hospital_Name);
			holder.beds = (TextView) convertView.findViewById(R.id.Hospital_Beds);
			holder.distance = (TextView) convertView.findViewById(R.id.Hospital_Distance);
			holder.wheelChair = (TextView) convertView.findViewById(R.id.Hospital_wheelChair);
			holder.interpreter = (TextView) convertView.findViewById(R.id.Hospital_interpreter);
			HospitalSelectionRowItem rowItem = rowItems.get(position);

			holder.hospital_id.setText(Integer.toString(rowItem.getHospital_id()));
			holder.hospital_name.setText(rowItem.getHospital_name());
			holder.beds.setText("Beds Available: "+rowItem.getBeds());
			holder.distance.setText("Distance: "+rowItem.getDistance());
			holder.interpreter.setText("Interpreter: "+rowItem.getInterpreter());
			holder.wheelChair.setText("Wheel Chair: "+rowItem.getWheelChair());
			convertView.setTag(holder);
		}
		else holder = (Holder) convertView.getTag();
		return convertView;
	}
}
