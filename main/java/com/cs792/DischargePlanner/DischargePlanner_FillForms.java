package com.cs792.DischargePlanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cs792.DB.PatientDBAdapter;
import com.cs792.DB.PatientHealthDBAdapter;
import com.cs792.Physician.PhysicianMain;
import com.cs792.Users.UserSessionManager;
import com.cs792.Utils.DischargeListCustomAdapter;
import com.cs792.Utils.DischargeRowItem;
import com.cs792.Utils.PatientRowItem;
import com.cs792.projectapp.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DischargePlanner_FillForms extends Activity {

	private PatientDBAdapter patients_data;
	private PatientHealthDBAdapter patient_healthData;
	private UserSessionManager user;
	private HashMap<String,String> userMap;
	private List<DischargeRowItem> rowItems;
	ListView myList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discharge_planner__fill_forms);
		new HeavyWork(DischargePlanner_FillForms.this).execute();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_discharge_planner__fill_forms, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.home_Button:
				Intent intent = new Intent(this, DischargePlannerMain.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(intent);
				return true;
			case R.id.Menu_Logout:
				user.logout();
				return true;
			default:
				return true;
		}
	}

	private class HeavyWork extends AsyncTask<Void, Void, List<DischargeRowItem>> {
		private Context context;
		private ProgressDialog progressDialog;

		public HeavyWork(Context context) {
			this.context = context;
			progressDialog = new ProgressDialog(context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setTitle("Please wait..");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			progressDialog.show();
			myList = (ListView) findViewById(R.id.Discharge_ListView);
		}

		@Override
		protected List<DischargeRowItem> doInBackground(Void... params) {
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
			rowItems = new ArrayList<DischargeRowItem>();
			photos = getResources().obtainTypedArray(R.array.photos);
			Cursor cursor = patients_data.getAllPatient();
			while(cursor.moveToNext()) {
				String firstName = cursor.getString(cursor.getColumnIndex(PatientDBAdapter.PATIENT_FIRSTNAME));
				String lastName = cursor.getString(cursor.getColumnIndex(PatientDBAdapter.PATIENT_LASTNAME));
				String status = cursor.getString(cursor.getColumnIndex(PatientDBAdapter.PATIENT_STATUS));
				String date = cursor.getString(cursor.getColumnIndex(PatientDBAdapter.PATIENT_SUBMIT_DATE));
				int patient_id = cursor.getInt(cursor.getColumnIndex(PatientDBAdapter.PATIENT_ID));
				DischargeRowItem rowItem = new DischargeRowItem(String.valueOf(patient_id), photos.getResourceId(0, -1), firstName + "," + lastName, status, date);
				rowItems.add(rowItem);
			}
			return rowItems;
		}

		@Override
		protected void onPostExecute(List<DischargeRowItem> row) {
			super.onPostExecute(row);
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			DischargeListCustomAdapter myAdapter = new DischargeListCustomAdapter(context,row);
			myList.setAdapter(myAdapter);
			myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String name = rowItems.get(position).getName();
					String status = rowItems.get(position).getStatus();
					String date = rowItems.get(position).getDate();
					Intent intent2 = new Intent(getApplicationContext(),DischargePlanner_DetailedFillForm.class);
					intent2.putExtra("name",name);
					intent2.putExtra("status",status);
					intent2.putExtra("date",date);
					startActivity(intent2);
				}
			});
		}
	}


}
