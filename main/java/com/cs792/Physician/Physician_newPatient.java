package com.cs792.Physician;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cs792.DB.PatientAddressDBAdapter;
import com.cs792.DB.PatientDBAdapter;
import com.cs792.DB.PatientFamilyDBAdapter;
import com.cs792.DB.PatientHealthCardDBAdapter;
import com.cs792.DB.PatientHealthDBAdapter;
import com.cs792.Users.UserSessionManager;
import com.cs792.projectapp.R;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;


public class Physician_newPatient extends Activity {

    private UserSessionManager shared;
	private PatientDBAdapter patient_data;
	private PatientAddressDBAdapter patient_Address_data;
	private PatientFamilyDBAdapter patient_family_data;
	private PatientHealthCardDBAdapter patient_healthCard_data;
	private PatientHealthDBAdapter patient_health_data;

	private EditText firstName;
	private EditText lastName;
	private CheckBox male;
	private CheckBox female;
	private Button dateTextView;

	private EditText address;
	private EditText city;
	private EditText province;
	private EditText postalCode;
	private EditText telephone;
	private EditText altTelephone;

	private EditText alt_address;
	private EditText alt_city;
	private EditText alt_province;
	private EditText alt_postalCode;
	private EditText alt_telephone;
	private EditText alt_altTelephone;

	private EditText relation_name;
	private EditText relation_type;
	private EditText relation_telephone;
	private EditText relation_altTelephone;

	private EditText healthCard_number;
	private EditText healthCard_version;
	private EditText healthCard_province;
	private HashMap<String,String> userMap;
	private EditText health_issue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_physician_new_patient);
        shared = new UserSessionManager(this);
        if(!shared.checkLogin()) {
            finish();
        }
		userMap = shared.getUserDetails();

		dateTextView = (Button)findViewById(R.id.start_date);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
        String date = dateFormat.format(cal.getTime());
        dateTextView.setText(date);
		dateTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment dialogFragment = new StartDatePicker();
				dialogFragment.show(getFragmentManager(), "start_date_picker");
					}
				}
		);
		alt_address = (EditText) findViewById(R.id.NewPatient_AlternateAddress);
		alt_city = (EditText) findViewById(R.id.NewPatient_AlternateCity);
		alt_province = (EditText) findViewById(R.id.NewPatient_AlternateProvince);
		alt_postalCode = (EditText) findViewById(R.id.NewPatient_AlternatePostal);
		alt_telephone =(EditText) findViewById(R.id.NewPatient_AlternateTelephone);
		alt_altTelephone = (EditText) findViewById(R.id.NewPatient_AlternateAltTelephone);
		addListenerOnSame();
    }



    @SuppressLint("ValidFragment")
    public class StartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            // Use the current date as the default date in the picker
            Calendar c = Calendar.getInstance();
            int startYear = c.get(Calendar.YEAR);
            int startMonth = c.get(Calendar.MONTH);
            int startDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(Physician_newPatient.this, this, startYear, startMonth, startDay);
            return dialog;
        }
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // Do something with the date chosen by the user
    		Calendar cal = Calendar.getInstance();
			cal.set(year,monthOfYear,dayOfMonth);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
			String date = dateFormat.format(cal.getTime());
            dateTextView.setText(date);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_physician_new_patient, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
			case R.id.action_done:
				new Heavy(Physician_newPatient.this).execute();
				return true;
			case R.id.action_cancel:
				goBack();
				return true;
            case R.id.Menu_Logout:
                shared.logout();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


	private void goBack() {
		Intent intent = new Intent(Physician_newPatient.this,PhysicianMain.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
		finish();
	}

	private class Heavy extends AsyncTask {
		private Context context;
		private ProgressDialog progressDialog;
		private Heavy(Context context) {
			this.context = context;
			progressDialog = new ProgressDialog(context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setTitle("Inserting..");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected Object doInBackground(Object[] params) {
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


			firstName = (EditText)findViewById(R.id.NewPatient_firstNameEntered);
			lastName = (EditText) findViewById(R.id.NewPatient_lastNameEntered);
			male = (CheckBox) findViewById(R.id.NewPatient_checkMale);
			female = (CheckBox) findViewById(R.id.NewPatient_checkFemale);
			//	dateTextView = (Button) findViewById(R.id.start_date);

			String firstNameStr = firstName.getText().toString();
			String lastNameStr = lastName.getText().toString();
			String maleStr = male.getText().toString();
			String femaleStr = female.getText().toString();
			String dateTextViewStr = dateTextView.getText().toString();
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy");
			try {
				Date result = format.parse(dateTextViewStr);
				cal.setTime(result);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			String genderStr="";
			if(male.isChecked())
				genderStr = maleStr;
			else if(female.isChecked())
				genderStr = femaleStr;

			Calendar creation_date = Calendar.getInstance();

			long val = patient_data.insertPatient(firstNameStr,lastNameStr,genderStr,cal.getTime(),"Admitted",Integer.parseInt(userMap.get(UserSessionManager.USERKEY_ID)),creation_date.getTime());


			address = (EditText) findViewById(R.id.NewPatient_address);
			city = (EditText) findViewById(R.id.NewPatient_city);
			province = (EditText) findViewById(R.id.NewPatient_Province);
			postalCode = (EditText) findViewById(R.id.NewPatient_Postal);
			telephone = (EditText) findViewById(R.id.NewPatient_Telephone);
			altTelephone = (EditText) findViewById(R.id.NewPatient_AltTelephone);

			String addressStr = address.getText().toString();
			String cityStr = city.getText().toString();
			String provinceStr = province.getText().toString();
			String postalCodeStr = postalCode.getText().toString();
			long telephoneStr;
			telephoneStr = Long.parseLong(telephone.getText().toString());
			long altTelephoneStr = Long.parseLong(altTelephone.getText().toString());


			alt_address = (EditText) findViewById(R.id.NewPatient_AlternateAddress);
			alt_city = (EditText) findViewById(R.id.NewPatient_AlternateCity);
			alt_province = (EditText) findViewById(R.id.NewPatient_AlternateProvince);
			alt_postalCode = (EditText) findViewById(R.id.NewPatient_AlternatePostal);
			alt_telephone =(EditText) findViewById(R.id.NewPatient_AlternateTelephone);
			alt_altTelephone = (EditText) findViewById(R.id.NewPatient_AlternateAltTelephone);

			String alt_addressStr = alt_address.getText().toString();
			String alt_cityStr = alt_city.getText().toString();
			String alt_provinceStr = alt_province.getText().toString();
			String alt_postalCodeStr = alt_postalCode.getText().toString();
			long alt_telephoneStr = Long.parseLong(alt_telephone.getText().toString());
			long alt_altTelephoneStr = Long.parseLong(alt_altTelephone.getText().toString());

			patient_Address_data.insertPatientAddress((int) val, addressStr, cityStr, provinceStr, postalCodeStr, telephoneStr, altTelephoneStr, alt_addressStr,
					alt_cityStr, alt_provinceStr, alt_postalCodeStr, altTelephoneStr, alt_altTelephoneStr);


			relation_name = (EditText) findViewById(R.id.NewPatient_relationPersonName);
			relation_type = (EditText) findViewById(R.id.NewPatient_relationShipType);
			relation_telephone = (EditText) findViewById(R.id.NewPatient_RelationTelephone);
			relation_altTelephone = (EditText) findViewById(R.id.NewPatient_RelationAltTelephone);

			String relation_nameStr = relation_name.getText().toString();
			String relation_typeStr = relation_type.getText().toString();
			Long relation_telephoneStr = Long.parseLong(relation_telephone.getText().toString());
			Long relation_altTelephoneStr = Long.parseLong(relation_altTelephone.getText().toString());

			patient_family_data.insertPatientFamilyInfo((int)val,relation_nameStr,relation_typeStr,relation_telephoneStr,relation_altTelephoneStr);

			healthCard_number = (EditText) findViewById(R.id.NewPatient_cardNo);
			healthCard_version = (EditText) findViewById(R.id.NewPatient_verionCode);
			healthCard_province = (EditText) findViewById(R.id.NewPatient_issuedProvince);

			String healthCard_numberStr = healthCard_number.getText().toString();
			int healthCard_versionStr = Integer.parseInt(healthCard_version.getText().toString());
			String healthCard_provinceStr = healthCard_province.getText().toString();

			patient_healthCard_data.insertPatientAddress((int)val,healthCard_numberStr,healthCard_versionStr,healthCard_provinceStr);
			health_issue = (EditText) findViewById(R.id.NewPatient_Allergies);

			String health_issueStr = health_issue.getText().toString();

			patient_health_data.insertPatientAddress((int)val,health_issueStr,"","","","");

			return null;
		}

		@Override
		protected void onPostExecute(Object o) {
			super.onPostExecute(o);
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			Intent intent = new Intent(context,PhysicianMain.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
	}


	private void addListenerOnSame() {
		final CheckBox sameAddress = (CheckBox) findViewById(R.id.NewPatient_sameAddress);
		sameAddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(((CheckBox)v).isChecked()) {
					alt_address.setText(((EditText)findViewById(R.id.NewPatient_address)).getText().toString());
					alt_city.setText(((EditText) findViewById(R.id.NewPatient_city)).getText().toString());
					alt_province.setText(((EditText) findViewById(R.id.NewPatient_Province)).getText().toString());
					alt_postalCode.setText(((EditText) findViewById(R.id.NewPatient_Postal)).getText().toString());
					alt_telephone.setText(((EditText) findViewById(R.id.NewPatient_Telephone)).getText().toString());
					alt_altTelephone.setText(((EditText) findViewById(R.id.NewPatient_AltTelephone)).getText().toString());
				}
			}
		});
	}
}
