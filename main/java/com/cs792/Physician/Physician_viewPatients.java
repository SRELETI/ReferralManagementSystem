package com.cs792.Physician;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cs792.DB.PatientDBAdapter;
import com.cs792.DB.PatientHealthDBAdapter;
import com.cs792.Users.UserSessionManager;
import com.cs792.Utils.PatientListCustomAdapter;
import com.cs792.Utils.PatientRowItem;
import com.cs792.projectapp.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Physician_viewPatients extends Activity  {


	public static String EXTRA_MESSAGE_NAME = "message";
	public static String EXTRA_MESSAGE_STATUS="message";
	public static String EXTRA_MESSAGE_DATE="message";

	private ArrayList<PatientRowItem> rowItems;
	private PatientDBAdapter patients_data;
	private PatientHealthDBAdapter patient_healthData;
	private UserSessionManager user;
	private HashMap<String,String> userMap;
	ListView myListItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_physician_view_patients);
		myListItem = (ListView) findViewById(R.id.Patient_ListView);
		new Heavy(Physician_viewPatients.this).execute();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_physician_view_patients, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.home_Button:
				Intent intent = new Intent(this,PhysicianMain.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(intent);
				return true;
			case R.id.Menu_Logout:
				user.logout();
				return true;
			default:
				return true;
		}

	}

	private class Heavy extends AsyncTask<Void, Void, List<PatientRowItem>> {

		private Context context;
		private ProgressDialog progressDialog;

		private Heavy(Context context) {
			this.context = context;
			progressDialog = new ProgressDialog(context);
		}

		@Override
		protected List<PatientRowItem> doInBackground(Void... params) {
			patients_data = new PatientDBAdapter(context);
			patient_healthData = new PatientHealthDBAdapter(context);
			try {
				patients_data.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				patient_healthData.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			TypedArray photos;
			user = new UserSessionManager(context);
			userMap = user.getUserDetails();
			rowItems = new ArrayList<PatientRowItem>();
			photos = getResources().obtainTypedArray(R.array.photos);
			Cursor cursor = patients_data.getAllPatient(Integer.parseInt(userMap.get(UserSessionManager.USERKEY_ID)));
			while(cursor.moveToNext()) {
				String firstName = cursor.getString(cursor.getColumnIndex(PatientDBAdapter.PATIENT_FIRSTNAME));
				String lastName = cursor.getString(cursor.getColumnIndex(PatientDBAdapter.PATIENT_LASTNAME));
				String status = cursor.getString(cursor.getColumnIndex(PatientDBAdapter.PATIENT_STATUS));
				String date = cursor.getString(cursor.getColumnIndex(PatientDBAdapter.PATIENT_ADMIT_DATE));
				int patient_id = cursor.getInt(cursor.getColumnIndex(PatientDBAdapter.PATIENT_ID));
				Cursor cursor2 = patient_healthData.getPatientProblems(patient_id);
				String problems="";
				if(cursor2.moveToNext()!=false) {
					problems = cursor2.getString(cursor2.getColumnIndex(PatientHealthDBAdapter.PATIENT_ALLERGIES));
				}
				PatientRowItem rowItem = new PatientRowItem(String.valueOf(patient_id),photos.getResourceId(0,-1),firstName+","+lastName,status,problems,date);
				rowItems.add(rowItem);
			}
			return rowItems;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setTitle("Please wait..");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}


		@Override
		protected void onPostExecute(List<PatientRowItem> result) {
			super.onPostExecute(result);
			if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}
			PatientListCustomAdapter adapter = new PatientListCustomAdapter(context,result);
			myListItem.setAdapter(adapter);
			myListItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String name = rowItems.get(position).getName();
					String status = rowItems.get(position).getStatus();
					String date = rowItems.get(position).getDate();
					Intent intent2 = new Intent(getApplicationContext(),Patient_DetailedView.class);
					intent2.putExtra("name",name);
					intent2.putExtra("status",status);
					intent2.putExtra("date",date);
					startActivity(intent2);
				}
			});
		}
	}
}
