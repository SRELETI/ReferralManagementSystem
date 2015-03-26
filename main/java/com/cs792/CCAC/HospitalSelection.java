package com.cs792.CCAC;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cs792.DB.HospitalDBAdapter;
import com.cs792.DB.PatientDBAdapter;
import com.cs792.DB.PatientReferralAdapater;
import com.cs792.Users.UserSessionManager;
import com.cs792.Utils.HospitalSelectionAdapter;
import com.cs792.Utils.HospitalSelectionRowItem;
import com.cs792.projectapp.R;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HospitalSelection extends Activity {

	private UserSessionManager user;
	private int patient_id;
	private String wheelChairReq;
	private String interpreterReq;
	private HospitalDBAdapter hospitalList;
	private PatientDBAdapter patient_db;
	private PatientReferralAdapater patient_referral_data;

	private String hos_name;
	private int hos_id;
	private int hos_beds;
	private int hos_distance;
	private String hos_wheelChair;
	private String hos_interpreterReq;
	private ListView myList;
	private List<HospitalSelectionRowItem> rowItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hospital_selection);
		Intent intent = getIntent();
		interpreterReq = intent.getStringExtra("interpreter");
		wheelChairReq = intent.getStringExtra("wheelChair");
		patient_id = intent.getIntExtra("id",0);
		myList = (ListView) findViewById(R.id.Hospital_ListView);
		new HeavyWorker(HospitalSelection.this).execute();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_hospital_selection, menu);
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

	private class HeavyWorker extends AsyncTask<Void, Void, List<HospitalSelectionRowItem>> {

		private Context context;
		private ProgressDialog progressDialog;
		Calendar cal;
		Date current;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		public HeavyWorker(Context context) {
			this.context = context;
			progressDialog = new ProgressDialog(this.context);
			cal = Calendar.getInstance();
			current = cal.getTime();
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
		protected List<HospitalSelectionRowItem> doInBackground(Void... params) {
			user = new UserSessionManager(context);
			hospitalList = new HospitalDBAdapter(context);
			rowItems = new ArrayList<HospitalSelectionRowItem>();
			patient_db = new PatientDBAdapter(context);
			patient_referral_data = new PatientReferralAdapater(context);
			try {
				hospitalList.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				patient_db.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				patient_referral_data.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			Cursor result = hospitalList.getHospitalList(wheelChairReq,interpreterReq);

			while(result.moveToNext()) {
				hos_id = result.getInt(result.getColumnIndex(HospitalDBAdapter.HOSPITAL_ID));
				hos_name = result.getString(result.getColumnIndex(HospitalDBAdapter.HOSPITAL_NAME));
				hos_beds = result.getInt(result.getColumnIndex(HospitalDBAdapter.HOSPITAL_BEDS));
				hos_distance = result.getInt(result.getColumnIndex(HospitalDBAdapter.HOSPITAL_DISTANCE));
				hos_interpreterReq = result.getString(result.getColumnIndex(HospitalDBAdapter.HOSPITAL_INTERPRETER));
				hos_wheelChair = result.getString(result.getColumnIndex(HospitalDBAdapter.HOSPITAL_WHEELCHAIR));

				HospitalSelectionRowItem rowItem = new HospitalSelectionRowItem(hos_id,hos_name,hos_beds,hos_distance,hos_interpreterReq,hos_wheelChair);
				rowItems.add(rowItem);
			}
			return rowItems;
		}

		@Override
		protected void onPostExecute(final List<HospitalSelectionRowItem> hospitalSelectionRowItems) {
			super.onPostExecute(hospitalSelectionRowItems);
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			HospitalSelectionAdapter myadapter = new HospitalSelectionAdapter(context, hospitalSelectionRowItems);
			myList.setAdapter(myadapter);
			myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					int hospital_id = hospitalSelectionRowItems.get(position).getHospital_id();
					String hospital_name = hospitalSelectionRowItems.get(position).getHospital_name();
					int beds_str = hospitalSelectionRowItems.get(position).getBeds();
					Cursor result =patient_db.getPatient(patient_id);
					String lastName="";
					String firstName="";
					if(result.moveToFirst()) {
						lastName = result.getString(result.getColumnIndex(PatientDBAdapter.PATIENT_LASTNAME));
						firstName = result.getString(result.getColumnIndex(PatientDBAdapter.PATIENT_FIRSTNAME));
						patient_referral_data.updateHospitalId(patient_id, hospital_id);
						hospitalList.updateBeds(hospital_id, beds_str - 1);
						patient_db.updatePatient_complete(patient_id,  current,"Completed");
						Toast.makeText(context, firstName + ", " + lastName + " has been allocated a bed in " + hospital_name, Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(context, "Something went wrong, try again", Toast.LENGTH_LONG).show();
					}
					patient_db.close();
					patient_referral_data.close();
					Intent intent = new Intent(context,CCAC_Main.class);
					startActivity(intent);
					finish();
				}
			});
		}
	}
}
