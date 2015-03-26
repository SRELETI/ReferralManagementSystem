package com.cs792.Physician;

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
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs792.DB.PatientAddressDBAdapter;
import com.cs792.DB.PatientDBAdapter;
import com.cs792.DB.PatientFamilyDBAdapter;
import com.cs792.DB.PatientHealthCardDBAdapter;
import com.cs792.DB.PatientHealthDBAdapter;
import com.cs792.Users.UserSessionManager;
import com.cs792.Utils.PatientDetailedViewCustomAdapter;
import com.cs792.projectapp.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Patient_DetailedView extends Activity {

	private ImageView photo;
	private ExpandableListView detailsView;
	private List<String> headings;
	private Map<String,List<String>> full_details;
	private PatientDetailedViewCustomAdapter detailsAdapter;
	private int patient_id;
	private PatientDBAdapter patient_data;
	private PatientAddressDBAdapter patient_Address_data;
	private PatientFamilyDBAdapter patient_family_data;
	private PatientHealthCardDBAdapter patient_healthCard_data;
	private PatientHealthDBAdapter patient_health_data;
	private TextView name;
	private TextView status_view;
	private TextView date_view;
	private String intent_name;
	private String intent_status;
	private String intent_date;
	private Button discharge;
	private UserSessionManager user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient__detailed_view);
		new HeavyWork(Patient_DetailedView.this).execute();

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_patient__detailed_view, menu);
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

	private class HeavyWork extends AsyncTask<Void, Void, Wrapper> {

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

			Intent intent = getIntent();
			intent_date = intent.getStringExtra("date");
			intent_status = intent.getStringExtra("status");
			intent_name = intent.getStringExtra("name");

			if(!intent_status.equals("Admitted")) {
				discharge.setVisibility(View.GONE);
			}
			photo = (ImageView) findViewById(R.id.Patient_Detailed_photo);
			photo.setImageResource(R.drawable.user);
			detailsView = (ExpandableListView) findViewById(R.id.Patient_DetailedView);
			name = (TextView) findViewById(R.id.Patient_Detailed_Name);
			status_view = (TextView) findViewById(R.id.Patient_Detailed_status);
			date_view = (TextView) findViewById(R.id.Patient_Detailed_AdmitDate);
			discharge = (Button) findViewById(R.id.DischargeButton);
		}

		@Override
		protected Wrapper doInBackground(Void... params) {
			user = new UserSessionManager(context);
			patient_data = new PatientDBAdapter(context);
			patient_Address_data = new PatientAddressDBAdapter(context);
			patient_family_data = new PatientFamilyDBAdapter(context);
			patient_healthCard_data = new PatientHealthCardDBAdapter(context);
			patient_health_data = new PatientHealthDBAdapter(context);

			try {
				patient_data.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				patient_Address_data.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				patient_family_data.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				patient_healthCard_data.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				patient_health_data.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			headings = new ArrayList<String>();
			full_details = new HashMap<String,List<String>>();

			headings.add("Person Details");
			headings.add("Address");
			headings.add("Alternate Address");
			headings.add("Contact Person");
			headings.add("HealthCard Details");
			headings.add("Health Issues");
			Wrapper w = new Wrapper();
			String[] names = intent_name.split(",");
			if(names.length!=2) {
				return w;
			}
			String intent_firstName = names[0];
			String intent_lastName = names[1];
			Cursor cursor_personal = patient_data.getPatient(intent_firstName,intent_lastName,intent_status,intent_date);
			if(cursor_personal.moveToFirst()==false) {
				return w;
			}
			patient_id = cursor_personal.getInt(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_ID));

			Cursor cursor_address = patient_Address_data.getPatientAddress(patient_id);
			Cursor cursor_contact = patient_family_data.getPatientAddress(patient_id);
			Cursor cursor_healthData = patient_health_data.getPatientProblems(patient_id);
			Cursor cursor_healthCard = patient_healthCard_data.getPatientAddress(patient_id);

			if(cursor_address.moveToFirst()==false || cursor_contact.moveToFirst()==false || cursor_healthCard.moveToFirst()==false
					|| cursor_healthData.moveToFirst()==false) {
				return w;
			}

			List<String> personal = new ArrayList<String>();
			String firstName = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_FIRSTNAME));
			String lastName = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_LASTNAME));
			String status = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_STATUS));
			String date = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_ADMIT_DATE));
			String gender = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_GENDER));

			name.setText(firstName+" "+lastName);
			status_view.setText(status);
			date_view.setText(date);

			personal.add("Name: "+firstName+" "+lastName);
			personal.add("Gender: "+gender);
			personal.add("Date Of Birth: "+date);

			if(cursor_address.moveToFirst()==false) {
				return w;
			}

			List<String> address = new ArrayList<String>();
			String address_det = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ADDRESS));
			String city = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_CITY));
			String province = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_PROVINCE));
			String postalCode = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_POSTALCODE));
			String telephone = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_TELEPHONE));
			String telephone_alt = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALTTELEPHONE));

			address.add("Address: "+address_det+", "+city);
			address.add("Province: "+province);
			address.add("Telephone: "+telephone);
			address.add("Alternate Telephone: "+telephone_alt);

			List<String> altAddress = new ArrayList<String>();

			String alt_address = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_ADDRESS));
			String alt_city = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_CITY));
			String alt_province = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_PROVINCE));
			String alt_postalCode = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_POSTALCODE));
			String alt_telephone = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_TELEPHONE));
			String alt_altTelephone = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_ALTTELEPHONE));

			altAddress.add("Address: "+alt_address+", "+alt_city);
			altAddress.add("Province: "+alt_province);
			altAddress.add("Telephone: "+alt_telephone);
			altAddress.add("Alternate Telephone: "+alt_altTelephone);

			List<String> family = new ArrayList<String>();

			String personName = cursor_contact.getString(cursor_contact.getColumnIndex(PatientFamilyDBAdapter.PATIENT_FAMILY_personName));
			String relation = cursor_contact.getString(cursor_contact.getColumnIndex(PatientFamilyDBAdapter.PATIENT_FAMILY_relationShip));
			String con_telephone = cursor_contact.getString(cursor_contact.getColumnIndex(PatientFamilyDBAdapter.PATIENT_FAMILY_TELEPHONE));
			String con_altTelephone = cursor_contact.getString(cursor_contact.getColumnIndex(PatientFamilyDBAdapter.PATIENT_FAMILY_ALTTELEPHONE));

			family.add("Contact Person: "+personName);
			family.add("RelationShip: "+relation);
			family.add("Telephone: "+con_telephone);
			family.add("Alternate Telephone: "+con_altTelephone);

			List<String> healthCard = new ArrayList<String>();

			String cardNo = cursor_healthCard.getString(cursor_healthCard.getColumnIndex(PatientHealthCardDBAdapter.PATIENT_CARDNO));
			String cardVer = cursor_healthCard.getString(cursor_healthCard.getColumnIndex(PatientHealthCardDBAdapter.PATIENT_VERSION));
			String card_prv = cursor_healthCard.getString(cursor_healthCard.getColumnIndex(PatientHealthCardDBAdapter.PATIENT_PROVINCE));

			healthCard.add(" Card No: "+cardNo);
			healthCard.add(" Card Version: "+cardVer);
			healthCard.add(" Issued Province: "+card_prv);

			List<String> health  = new ArrayList<String>();

			String issues = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_ALLERGIES));
			String comments = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_COMMENTS));
			String medical_orders = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_MEDICAL_ORDERS));
			String infections = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_INFECTION));

			health.add("Issues: "+issues);
			health.add("Medical Orders: "+medical_orders);
			health.add("Infections: "+infections);
			health.add("Comments: "+comments);

			full_details.put(headings.get(0),personal);
			full_details.put(headings.get(1),address);
			full_details.put(headings.get(2),altAddress);
			full_details.put(headings.get(3),family);
			full_details.put(headings.get(4),healthCard);
			full_details.put(headings.get(5),health);

			w.heads = headings;
			w.full = full_details;
			return w;
		}

		@Override
		protected void onPostExecute(Wrapper wrapper) {
			super.onPostExecute(wrapper);
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			detailsAdapter = new PatientDetailedViewCustomAdapter(Patient_DetailedView.this,wrapper.heads,wrapper.full);

			detailsView.setAdapter(detailsAdapter);

			discharge.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean result = patient_data.updatePatient(patient_id,"Treatment Completed", Calendar.getInstance().getTime());
					Intent intent = new Intent(getApplicationContext(),PhysicianMain.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
					startActivity(intent);
				}
			});
		}
	}


}

class Wrapper {
	public static List<String> heads;
	public static Map<String,List<String>> full;
}
