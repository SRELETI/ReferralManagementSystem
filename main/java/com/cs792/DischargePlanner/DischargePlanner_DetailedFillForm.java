package com.cs792.DischargePlanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.cs792.projectapp.R;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DischargePlanner_DetailedFillForm extends Activity {

	private TextView firstName;
	private TextView lastName;
	private TextView gender;
	private TextView DOB;

	private int patient_id;

	private TextView healthCardNo;
	private TextView healthVersion;
	private TextView issueProvince;

	private TextView address;
	private TextView city;
	private TextView postalCode;
	private TextView province;
	private TextView telephone;
	private TextView altTelephone;

	private TextView alt_address;
	private TextView alt_city;
	private TextView alt_province;
	private TextView alt_postalCode;
	private TextView alt_telephone;
	private TextView alt_altTelephone;

	private TextView contactPerson;
	private TextView relation;
	private TextView relation_telephone;
	private TextView relation_altTelephone;

	private EditText serviceProvider_surName;
	private EditText serviceProvider_firstName;

	private EditText allergies;
	private EditText medicalOrders;
	private EditText infectionControls;
	private EditText comments;
	private CheckBox wheelChairYes;
	private CheckBox wheelChairNo;

	private CheckBox speakYes;
	private CheckBox speakNo;
	private CheckBox interpreterYes;
	private CheckBox interpreterNo;
	private CheckBox languageEnglish;
	private CheckBox languageFrench;
	private CheckBox languageOther;

	private EditText referral_diagnosis;
	private EditText referral_reason;
	private EditText referral_facility;
	private EditText referral_facility_contact;
	private EditText referral_completedBy;
	private TextView referral_completedId;
	private EditText referral_title;
	private EditText referral_submissionDate;
	private EditText referral_contactNumber;

	private UserSessionManager user;
	private Map<String,String> userMap;
	private Intent intent;
	String intent_date;
	String intent_status;
	String intent_name;
	private PatientDBAdapter patient_data;
	private PatientAddressDBAdapter patient_Address_data;
	private PatientFamilyDBAdapter patient_family_data;
	private PatientHealthCardDBAdapter patient_healthCard_data;
	private PatientHealthDBAdapter patient_health_data;
	private PatientComSerProviderAdapter patient_serviceProvider;
	private PatientLanguageAdapter patient_language;
	private PatientReferralAdapater patient_referral;

	private String wheelChair_Str="";
	private String speaksEnglishStr="";
	private String interpreterStr="";
	private String primaryLanguageStr="";
	private String service_lastName="";
	private String service_firstName="";
	private String referral_diagnosisStr="";
	private String referral_reasonStr="";
	private String referral_facilityString="";
	private String referral_facilityContactString="";
	private String referral_titleString="";
	private String referral_contactStr="";

	Cursor cursor_personal;
	Cursor cursor_address;
	Cursor cursor_contact;
	Cursor cursor_healthData;
	Cursor cursor_healthCard;
	Cursor cursor_serviceProvier;
	Cursor cursor_language;
	Cursor cursor_referral;

	String health_issueStr;
	String medicalOrdersStr;
	String infectionControlStr;
	String commentsStr;


	String wheelChairStr;
	String wheelYesStr;
	String wheelNoStr;

	String serviceProviderSurName;
	String serviceProviderFirstName;

	String speaksEnglish;
	String speaksYesStr;
	String speaksNoStr;

	String interpreterReqStr;
	String interpreterYesStr;
	String interpreterNoStr;


	String primaryLangStr;
	String primaryEnglishStr;
	String primaryFrenchStr;
	String primaryOtherStr;

	String referralDiagStr;
	String referralReasonStr;
	String referral_facilityStr;
	long referral_facilityContactStr;
	String referral_completeByStr;
	String referral_completed_Id;
	String referral_titleStr;
	String referral_submittedStr;
	long referral_contact;
	SimpleDateFormat dateFormat;
	Date current;
	Calendar cal;


	String address_det;
	String city_str;
	String province_str;
	String postalCode_str;
	String telephone_str;
	String telephone_alt_str;


	String firstName_str;
	String lastName_str;
	String gender_str;
	String DOB_str;

	String issues;
	String comments_str;
	String medical_orders_str;
	String infections_str;

	String cardNo;
	String cardVer;
	String card_prv;

	String personName;
	String relation_str;
	String con_telephone;
	String con_altTelephone;

	String alt_address_str;
	String alt_city_str;
	String alt_province_str;
	String alt_postalCode_str;
	String alt_telephone_str;
	String alt_altTelephone_str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discharge_planner__detailed_fill_form);
		new HeavyWorker(DischargePlanner_DetailedFillForm.this).execute();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_discharge_planner__detailed_fill_form, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()) {
			case R.id.action_done:
				insertData();
				return true;
			case R.id.action_cancel:
				goBack();
				return true;
			case R.id.Menu_Logout:
				user.logout();
				return true;
			case R.id.action_settings:
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void goBack() {
		Intent intent = new Intent(DischargePlanner_DetailedFillForm.this,DischargePlannerMain.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
		finish();
	}

	public void insertData() {

		health_issueStr = allergies.getText().toString();
		medicalOrdersStr = medicalOrders.getText().toString();
		infectionControlStr = infectionControls.getText().toString();
		commentsStr = comments.getText().toString();


		wheelChairStr ="";
		wheelYesStr = wheelChairYes.getText().toString();
		wheelNoStr = wheelChairNo.getText().toString();

		if(wheelChairYes.isChecked()) {
			wheelChairStr = wheelYesStr;
		}
		else if(wheelChairNo.isChecked()) {
			wheelChairStr = wheelNoStr;
		}

		serviceProviderSurName = serviceProvider_surName.getText().toString();
		serviceProviderFirstName = serviceProvider_firstName.getText().toString();


		speaksEnglish = "";
		speaksYesStr = speakYes.getText().toString();
		speaksNoStr = speakNo.getText().toString();

		if(speakYes.isChecked()) {
			speaksEnglish = speaksYesStr;
		}
		else if(speakNo.isChecked()) {
			speaksEnglish = speaksNoStr;
		}

		interpreterReqStr = "";
		interpreterYesStr=interpreterYes.getText().toString();
		interpreterNoStr = interpreterNo.getText().toString();

		if(interpreterYes.isChecked()) {
			interpreterReqStr = interpreterYesStr;
		}
		else if(interpreterNo.isChecked()) {
			interpreterReqStr=interpreterNoStr;
		}

		primaryLangStr = "";
		primaryEnglishStr = languageEnglish.getText().toString();
		primaryFrenchStr = languageFrench.getText().toString();
		primaryOtherStr = languageOther.getText().toString();

		if(languageEnglish.isChecked()) {
			primaryLangStr = primaryEnglishStr;
		}
		else if(languageFrench.isChecked()) {
			primaryLangStr = primaryFrenchStr;
		}
		else if(languageOther.isChecked()) {
			primaryLangStr = primaryOtherStr;
		}

		referralDiagStr = referral_diagnosis.getText().toString();
		referralReasonStr = referral_reason.getText().toString();
		referral_facilityStr = referral_facility.getText().toString();
		String fac_contact = referral_facility_contact.getText().toString();
		if(fac_contact.equals(""))
			referral_facilityContactStr=0;
		else
			referral_facilityContactStr = Long.parseLong(fac_contact);
		referral_completeByStr = referral_completedBy.getText().toString();
		referral_completed_Id = referral_completedId.getText().toString();
		referral_titleStr = referral_title.getText().toString();
		referral_submittedStr = referral_submissionDate.getText().toString();
		String ref_contact = referral_contactNumber.getText().toString();
		if(ref_contact.equals(""))
			referral_contact=0;
		else
			referral_contact = Long.parseLong(ref_contact);


		if(referralDiagStr.equals("") || referralReasonStr.equals("") || referral_facilityStr.equals("") || referral_facilityContactStr==0 ||
				referral_titleStr.equals("") || referral_contact==0 || primaryLangStr.equals("") || interpreterReqStr.equals("") || speaksEnglish.equals("") ||
				serviceProviderFirstName.equals("") || serviceProviderSurName.equals("") || wheelChairStr.equals("") || allergies.equals("") || medicalOrdersStr.equals("") || infectionControlStr.equals("")
				|| commentsStr.equals("")) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("Alert").setMessage("The Referral form is incomplete. The CCAC agent may return it back").setCancelable(false).setPositiveButton("Proceed",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new HeavyInsertWorker(DischargePlanner_DetailedFillForm.this).execute();
				}
			})
			.setNegativeButton("Fill the form",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			AlertDialog dialog = alertDialog.create();
			dialog.show();
		}
		else {
			new HeavyInsertWorker(this).execute();
		}

	}


	private class HeavyWorker extends AsyncTask {
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
			user  = new UserSessionManager(context);
			userMap = user.getUserDetails();
			intent = getIntent();
			intent_date = intent.getStringExtra("date");
			intent_status = intent.getStringExtra("status");
			intent_name = intent.getStringExtra("name");

			referral_completedBy = (EditText) findViewById(R.id.DischargePlanner_completeBy);
			cal = Calendar.getInstance();

			current = cal.getTime();
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			referral_submissionDate = (EditText) findViewById(R.id.DischargePlanner_submissionDate);

			firstName = (TextView) findViewById(R.id.DischargePlanner_firstNameEntered);
			lastName = (TextView) findViewById(R.id.DischargePlanner_lastNameEntered);
			gender = (TextView) findViewById(R.id.DischargePlanner_gender);
			DOB = (TextView) findViewById(R.id.DischargePlanner_DOB);

			healthCardNo = (TextView) findViewById(R.id.DischargePlanner_cardNo);
			healthVersion = (TextView) findViewById(R.id.DischargePlanner_verionCode);
			issueProvince = (TextView) findViewById(R.id.DischargePlanner_issuedProvince);

			address = (TextView) findViewById(R.id.DischargePlanner_address);
			city = (TextView) findViewById(R.id.DischargePlanner_city);
			province = (TextView) findViewById(R.id.DischargePlanner_Province);
			postalCode = (TextView) findViewById(R.id.DischargePlanner_Postal);
			telephone = (TextView) findViewById(R.id.DischargePlanner_Telephone);
			altTelephone = (TextView) findViewById(R.id.DischargePlanner_AltTelephone);

			alt_address = (TextView) findViewById(R.id.DischargePlanner_AlternateAddress);
			alt_city = (TextView) findViewById(R.id.DischargePlanner_AlternateCity);
			alt_province = (TextView) findViewById(R.id.DischargePlanner_AlternateProvince);
			alt_postalCode = (TextView) findViewById(R.id.DischargePlanner_AlternatePostal);
			alt_telephone = (TextView) findViewById(R.id.DischargePlanner_AlternateTelephone);
			alt_altTelephone = (TextView) findViewById(R.id.DischargePlanner_AlternateAltTelephone);

			contactPerson = (TextView) findViewById(R.id.DischargePlanner_relationPersonName);
			relation = (TextView) findViewById(R.id.DischargePlanner_relationShipType);
			relation_telephone = (TextView) findViewById(R.id.DischargePlanner_RelationTelephone);
			relation_altTelephone = (TextView) findViewById(R.id.DischargePlanner_RelationAltTelephone);

			serviceProvider_firstName = (EditText) findViewById(R.id.DischargePlanner_serProviderFirstName);
			serviceProvider_surName = (EditText) findViewById(R.id.DischargePlanner_serProviderLastName);

			allergies = (EditText) findViewById(R.id.DischargePlanner_Allergies);
			comments = (EditText) findViewById(R.id.DischargePlanner_Comments);
			medicalOrders = (EditText) findViewById(R.id.DischargePlanner_MedicalOrders);
			infectionControls = (EditText) findViewById(R.id.DischargePlanner_InfectionControl);
			wheelChairNo = (CheckBox) findViewById(R.id.DischargePlanner_wheelNo);
			wheelChairYes = (CheckBox) findViewById(R.id.DischargePlanner_wheelYes);

			speakYes = (CheckBox) findViewById(R.id.DischargePlanner_speaksYes);
			speakNo = (CheckBox) findViewById(R.id.DischargePlanner_speaksNo);

			interpreterYes = (CheckBox) findViewById(R.id.DischargePlanner_interpreterYes);
			interpreterNo = (CheckBox) findViewById(R.id.DischargePlanner_interpreterNo);

			languageEnglish = (CheckBox) findViewById(R.id.DischargePlanner_english);
			languageFrench = (CheckBox) findViewById(R.id.DischargePlanner_french);
			languageOther = (CheckBox) findViewById(R.id.DischargePlanner_other);

			referral_diagnosis = (EditText) findViewById(R.id.DischargePlanner_diagnosis);
			referral_reason = (EditText) findViewById(R.id.DischargePlanner_referralReason);
			referral_facility = (EditText) findViewById(R.id.DischargePlanner_referringUnit);
			referral_facility_contact = (EditText) findViewById(R.id.DischargePlanner_facilityContact);
			referral_title = (EditText) findViewById(R.id.DischargePlanner_completedTitle);
			referral_contactNumber = (EditText) findViewById(R.id.DischargePlanner_dischargeContact);
			referral_completedId = (TextView)findViewById(R.id.DischargePlanner_completedId);

		}

		@Override
		protected Void doInBackground(Object[] params) {
			patient_data = new PatientDBAdapter(context);
			patient_Address_data = new PatientAddressDBAdapter(context);
			patient_family_data = new PatientFamilyDBAdapter(context);
			patient_healthCard_data = new PatientHealthCardDBAdapter(context);
			patient_health_data = new PatientHealthDBAdapter(context);
			patient_language = new PatientLanguageAdapter(context);
			patient_serviceProvider = new PatientComSerProviderAdapter(context);
			patient_referral = new PatientReferralAdapater(context);

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
				patient_serviceProvider.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				patient_language.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				patient_referral.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			String[] names = intent_name.split(",");
			if(names.length!=2) {
				return null;
			}
			String intent_firstName = names[0];
			String intent_lastName = names[1];
			cursor_personal = patient_data.getPatient_Submit(intent_firstName,intent_lastName,intent_status,intent_date);
			if(cursor_personal.moveToFirst()==false) {
				return null;
			}

			patient_id = cursor_personal.getInt(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_ID));
			cursor_address = patient_Address_data.getPatientAddress(patient_id);
			cursor_contact = patient_family_data.getPatientAddress(patient_id);
			cursor_healthData = patient_health_data.getPatientProblems(patient_id);
			cursor_healthCard = patient_healthCard_data.getPatientAddress(patient_id);
			cursor_language = patient_language.getPatientLanguage(patient_id);
			cursor_referral = patient_referral.getReferralDetails(patient_id);
			cursor_serviceProvier = patient_serviceProvider.getPatient(patient_id);

			if(cursor_address.moveToFirst()==false || cursor_contact.moveToFirst()==false || cursor_healthCard.moveToFirst()==false
					|| cursor_healthData.moveToFirst()==false) {
				return null;
			}

			firstName_str = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_FIRSTNAME));
			lastName_str = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_LASTNAME));
			gender_str = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_GENDER));
			DOB_str = cursor_personal.getString(cursor_personal.getColumnIndex(PatientDBAdapter.PATIENT_DOB));

			address_det = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ADDRESS));
			city_str = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_CITY));
			province_str = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_PROVINCE));
			postalCode_str = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_POSTALCODE));
			telephone_str = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_TELEPHONE));
			telephone_alt_str = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALTTELEPHONE));

			alt_address_str = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_ADDRESS));
			alt_city_str = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_CITY));
			alt_province_str = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_PROVINCE));
			alt_postalCode_str = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_POSTALCODE));
			alt_telephone_str = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_TELEPHONE));
			alt_altTelephone_str = cursor_address.getString(cursor_address.getColumnIndex(PatientAddressDBAdapter.PATIENT_ALT_ALTTELEPHONE));

			personName = cursor_contact.getString(cursor_contact.getColumnIndex(PatientFamilyDBAdapter.PATIENT_FAMILY_personName));
			relation_str = cursor_contact.getString(cursor_contact.getColumnIndex(PatientFamilyDBAdapter.PATIENT_FAMILY_relationShip));
			con_telephone = cursor_contact.getString(cursor_contact.getColumnIndex(PatientFamilyDBAdapter.PATIENT_FAMILY_TELEPHONE));
			con_altTelephone = cursor_contact.getString(cursor_contact.getColumnIndex(PatientFamilyDBAdapter.PATIENT_FAMILY_ALTTELEPHONE));


			cardNo = cursor_healthCard.getString(cursor_healthCard.getColumnIndex(PatientHealthCardDBAdapter.PATIENT_CARDNO));
			cardVer = cursor_healthCard.getString(cursor_healthCard.getColumnIndex(PatientHealthCardDBAdapter.PATIENT_VERSION));
			card_prv = cursor_healthCard.getString(cursor_healthCard.getColumnIndex(PatientHealthCardDBAdapter.PATIENT_PROVINCE));

			issues = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_ALLERGIES));
			comments_str = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_COMMENTS));
			medical_orders_str = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_MEDICAL_ORDERS));
			infections_str = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_INFECTION));
			wheelChair_Str = cursor_healthData.getString(cursor_healthData.getColumnIndex(PatientHealthDBAdapter.PATIENT_WHEELCHAIR));
			if(cursor_serviceProvier.moveToFirst()) {
				service_lastName = cursor_serviceProvier.getString(cursor_serviceProvier.getColumnIndex(PatientComSerProviderAdapter.PATIENT_PROVIDER_SURNAME));
				service_firstName = cursor_serviceProvier.getString(cursor_serviceProvier.getColumnIndex(PatientComSerProviderAdapter.PATIENT_PROVIDER_FIRSTNAME));
			}
			if(cursor_language.moveToFirst()) {
				speaksEnglishStr = cursor_language.getString(cursor_language.getColumnIndex(PatientLanguageAdapter.PATIENT_SPEAKS_ENGLISH));
				interpreterStr = cursor_language.getString(cursor_language.getColumnIndex(PatientLanguageAdapter.PATIENT_NEEDS_INTERPRETER));
				primaryLanguageStr = cursor_language.getString(cursor_language.getColumnIndex(PatientLanguageAdapter.PATIENT_PRIMARY_LANGUAGE));
			}
			if(cursor_referral.moveToFirst()) {
				referral_diagnosisStr = cursor_referral.getString(cursor_referral.getColumnIndex(PatientReferralAdapater.REFERRAL_DIAGNOSIS));
				referral_reasonStr = cursor_referral.getString(cursor_referral.getColumnIndex(PatientReferralAdapater.REFERRAL_REASON));
				referral_facilityString = cursor_referral.getString(cursor_referral.getColumnIndex(PatientReferralAdapater.REFERRAL_FACILITY));
				referral_facilityContactString = cursor_referral.getString(cursor_referral.getColumnIndex(PatientReferralAdapater.REFERRAL_FACILITY_CONTACT));
				referral_titleString = cursor_referral.getString(cursor_referral.getColumnIndex(PatientReferralAdapater.REFERRAL_COMPLETED_TITLE));
				referral_contactStr = cursor_referral.getString(cursor_referral.getColumnIndex(PatientReferralAdapater.REFERRAL_CONTACT));
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object o) {
			super.onPostExecute(o);
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			firstName.setText("FirstName: "+firstName_str);
			lastName.setText("LastName: "+lastName_str);
			gender.setText("Gender: "+gender_str);
			DOB.setText("Date Of Birth: "+DOB_str);


			address.setText("Address: "+address_det);
			city.setText("City: "+city_str);
			province.setText("Province: "+province_str);
			postalCode.setText("PostcalCode: "+postalCode_str);
			telephone.setText("Telephone: "+telephone_str);
			altTelephone.setText("Alternate Telephone: "+telephone_alt_str);

			alt_address.setText("Address: "+alt_address_str);
			alt_city.setText("City: "+alt_city_str);
			alt_province.setText("Province: "+alt_province_str);
			alt_postalCode.setText("Postcal Code: "+alt_postalCode_str);
			alt_telephone.setText("Telephone: "+alt_telephone_str);
			alt_altTelephone.setText("Alternate Telephone: "+alt_altTelephone_str);


			contactPerson.setText("Contact Person: "+personName);
			relation.setText("Relation: " + relation_str);
			relation_telephone.setText("Telephone: " + con_telephone);
			relation_altTelephone.setText("Alternate Telephone: " + con_altTelephone);


			healthCardNo.setText("Card No: "+cardNo);
			healthVersion.setText("Card Version: "+cardVer);
			issueProvince.setText("Issued Province: "+card_prv);


			allergies.setText(issues);
			comments.setText(comments_str);
			medicalOrders.setText(medical_orders_str);
			infectionControls.setText(infections_str);
			if(wheelChair_Str.equals("Yes")) {
				wheelChairYes.setChecked(true);
			}
			else if(wheelChair_Str.equals("No")){
				wheelChairNo.setChecked(true);
			}
			else {
			}
			if(!service_lastName.equals(""))
				serviceProvider_surName.setText(service_lastName);
			if(!service_firstName.equals(""))
				serviceProvider_firstName.setText(service_firstName);

			if(speaksEnglishStr.equals("Yes")) {
				speakYes.setChecked(true);
			}
			else if(speaksEnglishStr.equals("No")) {
				speakNo.setChecked(true);
			}

			if(primaryLanguageStr.equals("English")) {
				languageEnglish.setChecked(true);
			}
			else if(primaryLanguageStr.equals("French")) {
				languageFrench.setChecked(true);
			}
			else if(primaryLanguageStr.equals("Other")) {
				languageOther.setChecked(true);
			}

			if(interpreterStr.equals("Yes")) {
				interpreterYes.setChecked(true);
			}
			else if(interpreterStr.equals("No")) {
				interpreterNo.setChecked(true);
			}
			if(!referral_diagnosisStr.equals(""))
				referral_diagnosis.setText(referral_diagnosisStr);
			if(!referral_reasonStr.equals(""))
				referral_reason.setText(referral_reasonStr);
			if(!referral_facilityString.equals(""))
				referral_facility.setText(referral_facilityString);
			if(!referral_facilityContactString.equals(""))
				referral_facility_contact.setText(referral_facilityContactString);
			if(!referral_titleString.equals(""))
				referral_title.setText(referral_titleString);
			if(!referral_contactStr.equals(""))
				referral_contactNumber.setText(referral_contactStr);
			referral_completedId.setText(userMap.get(UserSessionManager.USERKEY_ID));
			referral_completedBy.setText(userMap.get(UserSessionManager.USERKEY_NAME));
			referral_submissionDate.setText(dateFormat.format(current));
		}
	}


	private class HeavyInsertWorker extends AsyncTask {
		private Context context;
		private ProgressDialog progressDialog;
		private Calendar cal;

		public HeavyInsertWorker(Context context) {
			this.context = context;
			progressDialog = new ProgressDialog(this.context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setTitle("Inserting..");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			progressDialog.show();
			cal= Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date result = format.parse(referral_submittedStr);
				cal.setTime(result);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Object doInBackground(Object[] params) {

			patient_health_data.updatePatient(patient_id,health_issueStr,medicalOrdersStr,commentsStr,infectionControlStr, wheelChairStr);

			Cursor patient_service = patient_serviceProvider.getPatient(patient_id);
			if(patient_service.moveToFirst()) {
				patient_serviceProvider.updatePatientServiceProvider(patient_id,serviceProviderSurName,serviceProviderFirstName);
			}
			else {
				patient_serviceProvider.insertPatientServiceProvider(patient_id, serviceProviderSurName, serviceProviderFirstName);
			}

			Cursor patient_lang = patient_language.getPatientLanguage(patient_id);
			if(patient_lang.moveToFirst()) {
				patient_language.updatePatientLanguage(patient_id,speaksEnglish,interpreterReqStr,primaryLangStr);
			}
			else {
				patient_language.insertPatientLanguage(patient_id, speaksEnglish, interpreterReqStr, primaryLangStr);
			}

			Cursor patient_ref = patient_referral.getReferralDetails(patient_id);
			if(patient_ref.moveToFirst()) {
				patient_referral.updateReferralDetails(patient_id,referralDiagStr,referralReasonStr,referral_facilityStr,referral_facilityContactStr,referral_completeByStr,Integer.parseInt(referral_completed_Id), referral_titleStr, cal.getTime(), referral_contact);
			}else {
				patient_referral.insertReferralDetails(patient_id, referralDiagStr, referralReasonStr, referral_facilityStr, referral_facilityContactStr, referral_completeByStr, Integer.parseInt(referral_completed_Id), referral_titleStr, cal.getTime(), referral_contact);
			}
			patient_data.updatePatient(patient_id,"Referral Form Ready");

			return null;

		}

		@Override
		protected void onPostExecute(Object o) {
			super.onPostExecute(o);
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			Intent intent3 = new Intent(context,DischargePlannerMain.class);
			intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent3);
		}
	}
}
