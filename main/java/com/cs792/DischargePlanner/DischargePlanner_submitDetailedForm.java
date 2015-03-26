package com.cs792.DischargePlanner;

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

import com.cs792.DB.PatientAddressDBAdapter;
import com.cs792.DB.PatientComSerProviderAdapter;
import com.cs792.DB.PatientDBAdapter;
import com.cs792.DB.PatientFamilyDBAdapter;
import com.cs792.DB.PatientHealthCardDBAdapter;
import com.cs792.DB.PatientHealthDBAdapter;
import com.cs792.DB.PatientLanguageAdapter;
import com.cs792.DB.PatientReferralAdapater;
import com.cs792.Physician.PhysicianMain;
import com.cs792.Users.UserSessionManager;
import com.cs792.Utils.PatientDetailedViewCustomAdapter;
import com.cs792.projectapp.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DischargePlanner_submitDetailedForm extends Activity {

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
	private PatientReferralAdapater patient_referral_data;
	private PatientLanguageAdapter patient_language_data;
	private PatientComSerProviderAdapter patient_service_data;

	private TextView name;
	private TextView status_view;
	private TextView date_view;

	private String intent_name;
	private String intent_status;
	private String intent_date;
	private Button discharge;

	private UserSessionManager user;

	private Cursor cursor_address;
	private Cursor cursor_contact;
	private Cursor cursor_healthData;
	private Cursor cursor_healthCard;
	private Cursor cursor_languageData;
	private Cursor cursor_serviceData;
	private Cursor cursor_referralData;


	private String firstName;
	private String lastName;
	private String status;
	private String date;
	private String gender;
	private String submit_date;

	private String address_det;
	private String city;
	private String province;
	private String postalCode;
	private String telephone;
	private String telephone_alt;

	private String alt_address;
	private String alt_city;
	private String alt_province;
	private String alt_postalCode;
	private String alt_telephone;
	private String alt_altTelephone;

	private String personName;
	private String con_telephone;
	private String relation;
	private String con_altTelephone;


	private String cardNo;
	private String cardVer;
	private String card_prv;

	private String issues;
	private String comments;
	private String medical_orders;
	private String infections;
	private String wheel_Chair;

	private String speakEnglish;
	private String interpreter;
	private String primary_lang;

	private String ser_lastName;
	private String ser_firstName;

	private String diagnosis;
	private String reason;
	private String facility;
	private String facility_contact;
	private String completedBy;
	private String completedTitle;
	private String referral_filled_date;
	private String referral_contact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discharge_planner_submit_detailed_form);

		new HeavyWorker(DischargePlanner_submitDetailedForm.this).execute();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_discharge_planner_submit_detailed_form, menu);
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


	private class HeavyWorker extends AsyncTask<Void, Void, Wrapper> {
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
			Intent intent = getIntent();
			intent_date = intent.getStringExtra("date");
			intent_status = intent.getStringExtra("status");
			intent_name = intent.getStringExtra("name");



			photo = (ImageView) findViewById(R.id.Patient_Detailed_photo);
			photo.setImageResource(R.drawable.user);
			detailsView = (ExpandableListView) findViewById(R.id.Patient_DetailedView);
			name = (TextView) findViewById(R.id.Patient_Detailed_Name);
			status_view = (TextView) findViewById(R.id.Patient_Detailed_status);
			date_view = (TextView) findViewById(R.id.Patient_Detailed_SubmitDate);
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
			patient_referral_data = new PatientReferralAdapater(context);
			patient_language_data = new PatientLanguageAdapter(context);
			patient_service_data = new PatientComSerProviderAdapter(context);
			Wrapper w = new Wrapper();
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

			try {
				patient_service_data.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				patient_language_data.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				patient_referral_data.open();
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
			headings.add("Language Requirements");
			headings.add("Community Health Care Provider");
			headings.add("Referral Details");

			String[] names = intent_name.split(",");
			if(names.length!=2) {
				return w;
			}
			String intent_firstName = names[0];
			String intent_lastName = names[1];
			Cursor cursor_personal = patient_data.getPatient_Submit(intent_firstName,intent_lastName,intent_status,intent_date);
			if(cursor_personal.moveToFirst()==false) {
				return w;
			}
			patient_id = cursor_personal.getInt(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_ID));

			cursor_address = patient_Address_data.getPatientAddress(patient_id);
			cursor_contact = patient_family_data.getPatientAddress(patient_id);
			cursor_healthData = patient_health_data.getPatientProblems(patient_id);
			cursor_healthCard = patient_healthCard_data.getPatientAddress(patient_id);
			cursor_languageData = patient_language_data.getPatientLanguage(patient_id);
			cursor_serviceData = patient_service_data.getPatient(patient_id);
			cursor_referralData = patient_referral_data.getReferralDetails(patient_id);

			if(cursor_address.moveToFirst()==false || cursor_contact.moveToFirst()==false || cursor_healthCard.moveToFirst()==false
					|| cursor_healthData.moveToFirst()==false || cursor_languageData.moveToFirst()==false || cursor_referralData.moveToFirst()==false || cursor_serviceData.moveToFirst()==false) {
				return w;
			}

			List<String> personal = new ArrayList<String>();

			firstName = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_FIRSTNAME));
			lastName = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_LASTNAME));
			status = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_STATUS));
			date = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_ADMIT_DATE));
			gender = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_GENDER));
			submit_date=cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_SUBMIT_DATE));

			name.setText(firstName+" "+lastName);
			status_view.setText(status);
			date_view.setText(date);

			personal.add("Name: "+firstName+" "+lastName);
			personal.add("Gender: "+gender);
			personal.add("Date Of Birth: "+date);
			personal.add("Admit Date: "+date);
			personal.add("Treatment Complete Date: "+submit_date);

			if(cursor_address.moveToFirst()==false) {
				return w;
			}

			List<String> address = new ArrayList<String>();

			address_det = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ADDRESS));
			city = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_CITY));
			province = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_PROVINCE));
			postalCode = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_POSTALCODE));
			telephone = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_TELEPHONE));
			telephone_alt = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALTTELEPHONE));

			address.add("Address: "+address_det+", "+city);
			address.add("Province: "+province);
			address.add("Postal Code: "+postalCode);
			address.add("Telephone: "+telephone);
			address.add("Alternate Telephone: "+telephone_alt);

			List<String> altAddress = new ArrayList<String>();

			alt_address = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_ADDRESS));
			alt_city = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_CITY));
			alt_province = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_PROVINCE));
			alt_postalCode = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_POSTALCODE));
			alt_telephone = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_TELEPHONE));
			alt_altTelephone = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_ALTTELEPHONE));

			altAddress.add("Address: "+alt_address+", "+alt_city);
			altAddress.add("Province: "+alt_province);
			altAddress.add("Postal Code: "+alt_postalCode);
			altAddress.add("Telephone: "+alt_telephone);
			altAddress.add("Alternate Telephone: "+alt_altTelephone);

			List<String> family = new ArrayList<String>();
			personName = cursor_contact.getString(cursor_contact.getColumnIndex(PatientFamilyDBAdapter.PATIENT_FAMILY_personName));
			relation = cursor_contact.getString(cursor_contact.getColumnIndex(PatientFamilyDBAdapter.PATIENT_FAMILY_relationShip));
			con_telephone = cursor_contact.getString(cursor_contact.getColumnIndex(PatientFamilyDBAdapter.PATIENT_FAMILY_TELEPHONE));
			con_altTelephone = cursor_contact.getString(cursor_contact.getColumnIndex(PatientFamilyDBAdapter.PATIENT_FAMILY_ALTTELEPHONE));

			family.add("Contact Person: "+personName);
			family.add("RelationShip: "+relation);
			family.add("Telephone: "+con_telephone);
			family.add("Alternate Telephone: "+con_altTelephone);

			List<String> healthCard = new ArrayList<String>();

			cardNo = cursor_healthCard.getString(cursor_healthCard.getColumnIndex(PatientHealthCardDBAdapter.PATIENT_CARDNO));
			cardVer = cursor_healthCard.getString(cursor_healthCard.getColumnIndex(PatientHealthCardDBAdapter.PATIENT_VERSION));
			card_prv = cursor_healthCard.getString(cursor_healthCard.getColumnIndex(PatientHealthCardDBAdapter.PATIENT_PROVINCE));

			healthCard.add(" Card No: "+cardNo);
			healthCard.add(" Card Version: "+cardVer);
			healthCard.add(" Issued Province: "+card_prv);

			List<String> health  = new ArrayList<String>();

			issues = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_ALLERGIES));
			comments = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_COMMENTS));
			medical_orders = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_MEDICAL_ORDERS));
			infections = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_INFECTION));
			wheel_Chair = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_WHEELCHAIR));

			health.add("Issues: "+issues);
			health.add("Medical Orders: "+medical_orders);
			health.add("Infections: "+infections);
			health.add("Comments: "+comments);
			health.add("Require WheelChair: "+wheel_Chair);

			List<String> language = new ArrayList<String>();

			speakEnglish = cursor_languageData.getString(cursor_languageData.getColumnIndex(PatientLanguageAdapter.PATIENT_SPEAKS_ENGLISH));
			interpreter = cursor_languageData.getString(cursor_languageData.getColumnIndex(PatientLanguageAdapter.PATIENT_NEEDS_INTERPRETER));
			primary_lang = cursor_languageData.getString(cursor_languageData.getColumnIndex(PatientLanguageAdapter.PATIENT_PRIMARY_LANGUAGE));

			language.add("Speaks English: "+speakEnglish);
			language.add("Require Interpreter: "+interpreter);
			language.add("Primary Language: "+primary_lang);

			List<String> service = new ArrayList<String>();

			ser_lastName = cursor_serviceData.getString(cursor_serviceData.getColumnIndex(PatientComSerProviderAdapter.PATIENT_PROVIDER_SURNAME));
			ser_firstName = cursor_serviceData.getString(cursor_serviceData.getColumnIndex(PatientComSerProviderAdapter.PATIENT_PROVIDER_FIRSTNAME));

			service.add("Last Name: "+ser_lastName);
			service.add("First Name: "+ser_firstName);

			List<String> referral = new ArrayList<String>();


			diagnosis = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_DIAGNOSIS));
			reason = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_REASON));
			facility = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_FACILITY));
			facility_contact = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_FACILITY_CONTACT));
			completedBy = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_COMPLETED_BY));
			completedTitle = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_COMPLETED_TITLE));
			referral_filled_date = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_DATE));
			referral_contact =cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_CONTACT));

			referral.add("Diagnosis: "+diagnosis);
			referral.add("Reason: "+reason);
			referral.add("Referred Facility/Unit: "+facility);
			referral.add("Facility Contact: "+facility_contact);
			referral.add("Filled By: "+completedBy);
			referral.add("Title: "+completedTitle);
			referral.add("Filled Date: "+referral_filled_date);
			referral.add("Contact Number: "+referral_contact);

			full_details.put(headings.get(0),personal);
			full_details.put(headings.get(1),address);
			full_details.put(headings.get(2),altAddress);
			full_details.put(headings.get(3),family);
			full_details.put(headings.get(4),healthCard);
			full_details.put(headings.get(5),health);
			full_details.put(headings.get(6),language);
			full_details.put(headings.get(7),service);
			full_details.put(headings.get(8),referral);
			w.heads = headings;
			w.full = full_details;
			return w;
		}

		@Override
		protected void onPostExecute(Wrapper wrapper) {
			super.onPostExecute(wrapper);
			if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}

			detailsAdapter = new PatientDetailedViewCustomAdapter(DischargePlanner_submitDetailedForm.this,wrapper.heads,wrapper.full);

			detailsView.setAdapter(detailsAdapter);
			discharge.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean result = patient_data.updatePatientDischarge(patient_id, "Submitted for Discharge", Calendar.getInstance().getTime());
					Intent intent = new Intent(getApplicationContext(),DischargePlannerMain.class);
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