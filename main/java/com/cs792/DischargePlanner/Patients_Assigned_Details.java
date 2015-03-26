package com.cs792.DischargePlanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cs792.DB.PatientAddressDBAdapter;
import com.cs792.DB.PatientComSerProviderAdapter;
import com.cs792.DB.PatientDBAdapter;
import com.cs792.DB.PatientFamilyDBAdapter;
import com.cs792.DB.PatientHealthCardDBAdapter;
import com.cs792.DB.PatientHealthDBAdapter;
import com.cs792.DB.PatientLanguageAdapter;
import com.cs792.DB.PatientReferralAdapater;
import com.cs792.Users.UserSessionManager;
import com.cs792.Utils.PatientDetailedViewCustomAdapter;
import com.cs792.projectapp.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Patients_Assigned_Details extends Activity {


	private UserSessionManager user;
	private TextView progress;
	private ProgressBar progressBar;

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
	private TextView assignedTo;
	private TextView date_view;

	private String intent_name;
	private String intent_date;
	private String intent_assignedTo;
	private String intent_id;
	private String intent_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patients__assigned__details);

		Intent intent = getIntent();
		intent_status = intent.getStringExtra("status");
		intent_assignedTo = intent.getStringExtra("assigned");
		intent_date = intent.getStringExtra("date");
		intent_name = intent.getStringExtra("name");
		intent_id = intent.getStringExtra("id");

		user = new UserSessionManager(this);
		progress = (TextView) findViewById(R.id.acknowledged);
		progressBar = (ProgressBar) findViewById(R.id.progress);

		photo = (ImageView) findViewById(R.id.Patient_Detailed_photo);
		photo.setImageResource(R.drawable.user);
		detailsView = (ExpandableListView) findViewById(R.id.Patient_DetailedView);
		name = (TextView) findViewById(R.id.Patient_Detailed_Name);
		assignedTo = (TextView) findViewById(R.id.Patient_Detailed_status);
		date_view = (TextView) findViewById(R.id.Patient_Detailed_SubmitDate);

		new HeavyWorker(Patients_Assigned_Details.this).execute();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_patients__assigned__details, menu);
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

	public void prepareData() {


	}

	private class HeavyWorker extends AsyncTask<Void, Void, Wrapper_Assinged> {

		private Context context;
		private ProgressDialog progressDialog;
		private String firstName;
		private String lastName;
		private String discharge_date;
		private String assignedToStr;

		public HeavyWorker( Context context) {
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
		protected Wrapper_Assinged doInBackground(Void... params) {
			patient_data = new PatientDBAdapter(context);
			patient_Address_data = new PatientAddressDBAdapter(context);
			patient_family_data = new PatientFamilyDBAdapter(context);
			patient_healthCard_data = new PatientHealthCardDBAdapter(context);
			patient_health_data = new PatientHealthDBAdapter(context);
			patient_referral_data = new PatientReferralAdapater(context);
			patient_language_data = new PatientLanguageAdapter(context);
			patient_service_data = new PatientComSerProviderAdapter(context);

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
			Wrapper_Assinged wrapper_assinged = new Wrapper_Assinged();
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
				return wrapper_assinged;
			}
			String intent_firstName = names[0];
			String intent_lastName = names[1];
			Cursor cursor_personal = patient_data.getPatient_Discharge(intent_firstName,intent_lastName,intent_status,intent_date);
			if(cursor_personal.moveToFirst()==false) {
				return wrapper_assinged;
			}
			patient_id = cursor_personal.getInt(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_ID));

			Cursor cursor_address = patient_Address_data.getPatientAddress(patient_id);
			Cursor cursor_contact = patient_family_data.getPatientAddress(patient_id);
			Cursor cursor_healthData = patient_health_data.getPatientProblems(patient_id);
			Cursor cursor_healthCard = patient_healthCard_data.getPatientAddress(patient_id);
			Cursor cursor_languageData = patient_language_data.getPatientLanguage(patient_id);
			Cursor cursor_serviceData = patient_service_data.getPatient(patient_id);
			Cursor cursor_referralData = patient_referral_data.getReferralDetails(patient_id);

			if(cursor_address.moveToFirst()==false || cursor_contact.moveToFirst()==false || cursor_healthCard.moveToFirst()==false
					|| cursor_healthData.moveToFirst()==false || cursor_languageData.moveToFirst()==false || cursor_referralData.moveToFirst()==false || cursor_serviceData.moveToFirst()==false) {
				return wrapper_assinged;
			}

			List<String> personal = new ArrayList<String>();
			firstName = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_FIRSTNAME));
			lastName = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_LASTNAME));
			String status = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_STATUS));
			String date = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_ADMIT_DATE));
			String gender = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_GENDER));
			String submit_date=cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_SUBMIT_DATE));
			discharge_date = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_DISCHARGE_DATE));

			personal.add("Name: "+firstName+" "+lastName);
			personal.add("Gender: "+gender);
			personal.add("Date Of Birth: "+date);
			personal.add("Admit Date: "+date);
			personal.add("Treatment Completion Date: "+submit_date);
			personal.add("Referral Submitted Date: "+discharge_date);
			if(cursor_address.moveToFirst()==false) {
				return wrapper_assinged;
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
			String wheel_Chair = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_WHEELCHAIR));

			health.add("Issues: "+issues);
			health.add("Medical Orders: "+medical_orders);
			health.add("Infections: "+infections);
			health.add("Comments: "+comments);
			health.add("Require WheelChair: "+wheel_Chair);

			List<String> language = new ArrayList<String>();

			String speakEnglish = cursor_languageData.getString(cursor_languageData.getColumnIndex(PatientLanguageAdapter.PATIENT_SPEAKS_ENGLISH));
			String interpreter = cursor_languageData.getString(cursor_languageData.getColumnIndex(PatientLanguageAdapter.PATIENT_NEEDS_INTERPRETER));
			String primary_lang = cursor_languageData.getString(cursor_languageData.getColumnIndex(PatientLanguageAdapter.PATIENT_PRIMARY_LANGUAGE));

			language.add("Speaks English: "+speakEnglish);
			language.add("Require Interpreter: "+interpreter);
			language.add("Primary Language: "+primary_lang);

			List<String> service = new ArrayList<String>();

			String ser_lastName = cursor_serviceData.getString(cursor_serviceData.getColumnIndex(PatientComSerProviderAdapter.PATIENT_PROVIDER_SURNAME));
			String ser_firstName = cursor_serviceData.getString(cursor_serviceData.getColumnIndex(PatientComSerProviderAdapter.PATIENT_PROVIDER_FIRSTNAME));

			service.add("Last Name: "+ser_lastName);
			service.add("First Name: "+ser_firstName);

			List<String> referral = new ArrayList<String>();

			String diagnosis = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_DIAGNOSIS));
			String reason = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_REASON));
			String facility = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_FACILITY));
			String facility_contact = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_FACILITY_CONTACT));
			String completedByStr = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_COMPLETED_BY));
			String completedTitle = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_COMPLETED_TITLE));
			String referral_filled_date = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_DATE));
			String referral_contact =cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_CONTACT));
			assignedToStr = cursor_referralData.getString(cursor_referralData.getColumnIndex(PatientReferralAdapater.REFERRAL_ASSIGNED_TO));

			referral.add("Diagnosis: "+diagnosis);
			referral.add("Reason: "+reason);
			referral.add("Referred Facility/Unit: "+facility);
			referral.add("Facility Contact: "+facility_contact);
			referral.add("Filled By: "+completedByStr);
			referral.add("Title: "+completedTitle);
			referral.add("Filled Date: "+referral_filled_date);
			referral.add("Contact Number: "+referral_contact);
			referral.add("Assigned To: "+assignedToStr);


			full_details.put(headings.get(0),personal);
			full_details.put(headings.get(1),address);
			full_details.put(headings.get(2),altAddress);
			full_details.put(headings.get(3),family);
			full_details.put(headings.get(4),healthCard);
			full_details.put(headings.get(5),health);
			full_details.put(headings.get(6),language);
			full_details.put(headings.get(7),service);
			full_details.put(headings.get(8),referral);

			wrapper_assinged.heads = headings;
			wrapper_assinged.full = full_details;
			return wrapper_assinged;
		}

		@Override
		protected void onPostExecute(Wrapper_Assinged wrapper_assinged) {
			super.onPostExecute(wrapper_assinged);
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
			name.setText(firstName+" "+lastName);
			date_view.setText("Submitted On: "+discharge_date);
			assignedTo.setText("Assigned To: "+assignedToStr);
			detailsAdapter = new PatientDetailedViewCustomAdapter(Patients_Assigned_Details.this,wrapper_assinged.heads,wrapper_assinged.full);

			detailsView.setAdapter(detailsAdapter);
		}
	}
}

class Wrapper_Assinged {
	public static List<String> heads;
	public static Map<String,List<String>> full;
}
