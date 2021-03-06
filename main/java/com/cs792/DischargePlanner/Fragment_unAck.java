package com.cs792.DischargePlanner;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cs792.DB.PatientDBAdapter;
import com.cs792.DB.PatientReferralAdapater;
import com.cs792.Users.UserSessionManager;
import com.cs792.Utils.CCACListCustomAdapter;
import com.cs792.Utils.CCACRowItem;
import com.cs792.projectapp.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fragment_unAck extends ListFragment {

	private PatientDBAdapter patients_data;
	private PatientReferralAdapater patient_referral;
	private UserSessionManager user;
	private HashMap<String,String> userMap;
	private List<CCACRowItem> rowItems;
	public Fragment_unAck(){};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_fragment_un_ack, container, false);
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		final List<CCACRowItem> returnedItems = getItems();
		ListView myList = getListView();   //view.findViewById(R.id.list); Will not work, it is not able to find the listView
		CCACListCustomAdapter listAdapter = new CCACListCustomAdapter(getActivity(),returnedItems);
		myList.setAdapter(listAdapter);
		myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String pat_id = returnedItems.get(position).getPatient_id();
				String name = returnedItems.get(position).getName();
				String date = returnedItems.get(position).getDate();
				String completedBy = returnedItems.get(position).getCompletedBy();
				String status = returnedItems.get(position).getStatus();
				Intent intent = new Intent(getActivity(),Patients_UnAck_Details.class);
				intent.putExtra("id",pat_id);
				intent.putExtra("name",name);
				intent.putExtra("date",date);
				intent.putExtra("completedBy",completedBy);
				intent.putExtra("status",status);
				startActivity(intent);
			}
		});
	}


	public List<CCACRowItem> getItems() {

		patients_data = new PatientDBAdapter(getActivity());
		patient_referral = new PatientReferralAdapater(getActivity());
		try {
			patients_data.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			patient_referral.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		TypedArray photos;
		user = new UserSessionManager(getActivity());
		userMap = user.getUserDetails();
		rowItems = new ArrayList<CCACRowItem>();
		photos = getResources().obtainTypedArray(R.array.photos);
		Cursor result = patients_data.getAllPatient_SubmittedDischarge();
		while (result.moveToNext()) {
			int patient_id = result.getInt(result.getColumnIndex(PatientDBAdapter.PATIENT_ID));
			String firstName = result.getString(result.getColumnIndex(PatientDBAdapter.PATIENT_FIRSTNAME));
			String lastName = result.getString(result.getColumnIndex(PatientDBAdapter.PATIENT_LASTNAME));
			String status = result.getString(result.getColumnIndex(PatientDBAdapter.PATIENT_STATUS));
			String date = result.getString(result.getColumnIndex(PatientDBAdapter.PATIENT_DISCHARGE_DATE));

			Cursor result_inner = patient_referral.getReferralDetails(patient_id);
			while (result_inner.moveToNext()) {
				String completedBy = result_inner.getString(result_inner.getColumnIndex(PatientReferralAdapater.REFERRAL_COMPLETED_BY));
				CCACRowItem rowItem = new CCACRowItem(String.valueOf(patient_id), photos.getResourceId(0, -1), firstName + "," + lastName, status, completedBy, date);
				rowItems.add(rowItem);
			}
		}
		return rowItems;
	}


}
