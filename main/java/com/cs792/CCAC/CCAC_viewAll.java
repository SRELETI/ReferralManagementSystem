package com.cs792.CCAC;

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
import com.cs792.DB.PatientReferralAdapater;
import com.cs792.DischargePlanner.DischargePlannerMain;
import com.cs792.DischargePlanner.DischargePlanner_DetailedFillForm;
import com.cs792.Users.UserSessionManager;
import com.cs792.Utils.CCACListCustomAdapter;
import com.cs792.Utils.CCACRowItem;
import com.cs792.Utils.DischargeRowItem;
import com.cs792.projectapp.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CCAC_viewAll extends Activity {


	private PatientDBAdapter patients_data;
	private PatientReferralAdapater patient_referral;
	private UserSessionManager user;
	private HashMap<String,String> userMap;
	private List<CCACRowItem> rowItems;
	private ListView myList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ccac_view_all);
		new HeavyWorker(CCAC_viewAll.this).execute();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_ccac_view_all, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.home_Button:
				Intent intent = new Intent(this, CCAC_Main.class);
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


	private class HeavyWorker extends AsyncTask<Void, Void, List<CCACRowItem>> {

		private Context context;
		private ProgressDialog progressDialog;

		public HeavyWorker(Context context) {
			this.context = context;
			progressDialog = new ProgressDialog(this.context);
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
		protected List<CCACRowItem> doInBackground(Void... params) {

			patients_data = new PatientDBAdapter(context);
			patient_referral = new PatientReferralAdapater(context);
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
			user = new UserSessionManager(context);
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
				while(result_inner.moveToNext()) {
					String completedBy = result_inner.getString(result_inner.getColumnIndex(PatientReferralAdapater.REFERRAL_COMPLETED_BY));
					CCACRowItem rowItem = new CCACRowItem(String.valueOf(patient_id), photos.getResourceId(0, -1), firstName + "," + lastName, status,completedBy,date);
					rowItems.add(rowItem);
				}
			}
			return rowItems;
		}

		@Override
		protected void onPostExecute(final List<CCACRowItem> result) {
			super.onPostExecute(result);
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			myList = (ListView)findViewById(R.id.CCAC_ListView);
			CCACListCustomAdapter listAdapter = new CCACListCustomAdapter(CCAC_viewAll.this,result);
			myList.setAdapter(listAdapter);
			myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String name = result.get(position).getName();
					String status = result.get(position).getStatus();
					String date = result.get(position).getDate();
					String completedBy = result.get(position).getCompletedBy();
					Intent intent2 = new Intent(getApplicationContext(),CCAC_DetailedViewAll.class);
					intent2.putExtra("name",name);
					intent2.putExtra("status",status);
					intent2.putExtra("date",date);
					startActivity(intent2);
				}
			});
		}
	}
}
