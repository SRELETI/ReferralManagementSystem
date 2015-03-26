package com.cs792.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by sudeelet on 2/18/2015.
 */
public class LoginDBHelper {

	public static final String DATABASE_NAME="cs792Login.db";
	public static final int DATABASE_VERSION=2;

	public static final String DATABASE_LoginTable_CREATE="create table "+LoginDBAdapter.LOGIN_TABLE_NAME+" (" +LoginDBAdapter.LOGIN_OWNER_ID+" INTEGER PRIMARY KEY ASC, "
			+LoginDBAdapter.LOGIN_COLUMN_USERNAME+ " text not null,"+LoginDBAdapter.LOGIN_COLUMN_PASSWORD+" text not null, "+LoginDBAdapter.LOGIN_COLUMN_USER_TYPE+" text not null, "
			+LoginDBAdapter.LOGIN_COLUMN_EMAIL+" text,"+LoginDBAdapter.LOGIN_CREATION_DATE+" DATE);";


	public static final String DATABASE_Patients_CREATE="create table "+PatientDBAdapter.DATABASE_TABLE+" (" +PatientDBAdapter.PATIENT_ID+" INTEGER PRIMARY KEY ASC, "
			+PatientDBAdapter.PATIENT_FIRSTNAME+" text,"+PatientDBAdapter.PATIENT_LASTNAME+" text, "+PatientDBAdapter.PATIENT_GENDER+" text,"+PatientDBAdapter.PATIENT_DOB+" DATE,"
			+PatientDBAdapter.PATIENT_STATUS+" text,"+PatientDBAdapter.PATIENT_OWNER_ID+" INTEGER,"+ PatientDBAdapter.PATIENT_SUBMIT_DATE+" DATE, "+PatientDBAdapter.PATIENT_DISCHARGE_DATE+" DATE,"+PatientDBAdapter.PATIENT_COMPLETION_DATE+" DATE,"+PatientDBAdapter.PATIENT_ADMIT_DATE+" DATE,"+" FOREIGN KEY("+PatientDBAdapter.PATIENT_OWNER_ID+") REFERENCES "
			+LoginDBAdapter.LOGIN_TABLE_NAME+" ("+LoginDBAdapter.LOGIN_OWNER_ID+")"+")";

	public static final String DATABASE_Patient_Address_CREATE="create table "+PatientAddressDBAdapter.DATABASE_TABLE+" ("+PatientAddressDBAdapter.PATIENT_ID+" INTEGER,"
			+PatientAddressDBAdapter.PATIENT_ADDRESS+" text,"+PatientAddressDBAdapter.PATIENT_CITY+" text,"+PatientAddressDBAdapter.PATIENT_PROVINCE+" text,"
			+PatientAddressDBAdapter.PATIENT_POSTALCODE+" text,"+PatientAddressDBAdapter.PATIENT_TELEPHONE+" INTEGER,"+PatientAddressDBAdapter.PATIENT_ALTTELEPHONE+" INTEGER,"
			+PatientAddressDBAdapter.PATIENT_ALT_ADDRESS+" text,"+PatientAddressDBAdapter.PATIENT_ALT_CITY+" text,"+PatientAddressDBAdapter.PATIENT_ALT_PROVINCE+" text,"
			+PatientAddressDBAdapter.PATIENT_ALT_POSTALCODE+" text,"+PatientAddressDBAdapter.PATIENT_ALT_TELEPHONE+" INTEGER,"+PatientAddressDBAdapter.PATIENT_ALT_ALTTELEPHONE+" INTEGER,"
			+" FOREIGN KEY("+PatientAddressDBAdapter.PATIENT_ID+") REFERENCES "+PatientDBAdapter.DATABASE_TABLE+" ("+PatientDBAdapter.PATIENT_ID+")"+")";

	public static final String DATABASE_Patient_Family_CREATE="create table "+PatientFamilyDBAdapter.DATABASE_TABLE+" ("+PatientFamilyDBAdapter.PATIENT_FAMILY_ID+" INTEGER,"
			+PatientFamilyDBAdapter.PATIENT_FAMILY_personName+" text,"+PatientFamilyDBAdapter.PATIENT_FAMILY_relationShip+" text,"+PatientFamilyDBAdapter.PATIENT_FAMILY_TELEPHONE+" INTEGER,"
			+PatientFamilyDBAdapter.PATIENT_FAMILY_ALTTELEPHONE+" INTEGER,"+" FOREIGN KEY("+PatientFamilyDBAdapter.PATIENT_FAMILY_ID+") REFERENCES "+PatientDBAdapter.DATABASE_TABLE+" ("+PatientDBAdapter.PATIENT_ID+")"+")";

	public static final String DATABASE_Patient_HealthCard_CREATE="create table "+PatientHealthCardDBAdapter.DATABASE_TABLE+" ("+PatientHealthCardDBAdapter.PATIENT_ID+" INTEGER,"
			+PatientHealthCardDBAdapter.PATIENT_CARDNO+" text,"+PatientHealthCardDBAdapter.PATIENT_VERSION+" INTEGER,"+PatientHealthCardDBAdapter.PATIENT_PROVINCE+" text,"+" FOREIGN KEY("+
			PatientHealthCardDBAdapter.PATIENT_ID+") REFERENCES "+PatientDBAdapter.DATABASE_TABLE+" ("+PatientDBAdapter.PATIENT_ID+")"+")";

	public static final String DATABASE_Patient_Health_CREATE="create table "+PatientHealthDBAdapter.DATABASE_TABLE+" ("+PatientHealthDBAdapter.PATIENT_ID+" INTEGER,"+PatientHealthDBAdapter.PATIENT_ALLERGIES+" text,"
			+PatientHealthDBAdapter.PATIENT_INFECTION+" text,"+PatientHealthDBAdapter.PATIENT_MEDICAL_ORDERS+" text,"+PatientHealthDBAdapter.PATIENT_WHEELCHAIR+" INTEGER,"+PatientHealthDBAdapter.PATIENT_COMMENTS+" text,"
			+" FOREIGN KEY("+PatientHealthDBAdapter.PATIENT_ID+") REFERENCES "+PatientDBAdapter.DATABASE_TABLE+" ("+PatientDBAdapter.PATIENT_ID+")"+")";


	public static final String DATABASE_Referral_CREATE="create table "+PatientReferralAdapater.DATABASE_TABLE+" ("+PatientReferralAdapater.PATIENT_ID+" INTEGER,"+PatientReferralAdapater.REFERRAL_DIAGNOSIS+" text,"
			+PatientReferralAdapater.REFERRAL_REASON+" text,"+PatientReferralAdapater.REFERRAL_FACILITY+" text,"+PatientReferralAdapater.REFERRAL_FACILITY_CONTACT+" INTEGER,"+PatientReferralAdapater.REFERRAL_COMPLETED_BY+" text,"
			+PatientReferralAdapater.REFERRAL_COMPLETED_TITLE+" text,"+PatientReferralAdapater.REFERRAL_DATE+" DATE,"+PatientReferralAdapater.REFERRAL_CONTACT+" INTEGER,"+PatientReferralAdapater.REFERRAL_COMPLETED_ID+" text,"+PatientReferralAdapater.REFERRAL_ASSIGNED_TO+" TEXT,"+PatientReferralAdapater.REFERRAL_HOSPITAL_ID+" INTEGER,"+
			" FOREIGN KEY("+PatientReferralAdapater.PATIENT_ID+") REFERENCES "+PatientDBAdapter.DATABASE_TABLE+" ("+PatientDBAdapter.PATIENT_ID+")"+")";

	public static final String DATABASE_Patient_Language_CREATE="create table "+PatientLanguageAdapter.DATABASE_TABLE+" ("+PatientLanguageAdapter.PATIENT_ID+" INTEGER,"+PatientLanguageAdapter.PATIENT_SPEAKS_ENGLISH+" text,"
			+PatientLanguageAdapter.PATIENT_NEEDS_INTERPRETER+" text,"+PatientLanguageAdapter.PATIENT_PRIMARY_LANGUAGE+" text,"
			+" FOREIGN KEY("+PatientLanguageAdapter.PATIENT_ID+") REFERENCES "+PatientDBAdapter.DATABASE_TABLE+" ("+PatientDBAdapter.PATIENT_ID+")"+")";


	public static final String DATABASE_Patient_ComSerProviderAdapter_CREATE="create table "+PatientComSerProviderAdapter.DATABASE_TABLE+" ("+PatientComSerProviderAdapter.PATIENT_ID+" INTEGER,"+PatientComSerProviderAdapter.PATIENT_PROVIDER_SURNAME+" text,"
			+PatientComSerProviderAdapter.PATIENT_PROVIDER_FIRSTNAME+" text,"+" FOREIGN KEY("+PatientComSerProviderAdapter.PATIENT_ID+") REFERENCES "+PatientDBAdapter.DATABASE_TABLE+" ("+PatientDBAdapter.PATIENT_ID+")"+")";


	public static final String DATABASE_HospitalList_CREATE="create table "+HospitalDBAdapter.DATABASE_TABLE+" ("+HospitalDBAdapter.HOSPITAL_ID+" INTEGER PRIMARY KEY ASC,"+HospitalDBAdapter.HOSPITAL_NAME+" text,"
			+HospitalDBAdapter.HOSPITAL_BEDS+" INTEGER,"+HospitalDBAdapter.HOSPITAL_DISTANCE+" INTEGER,"
			+HospitalDBAdapter.HOSPITAL_WHEELCHAIR+" text,"+HospitalDBAdapter.HOSPITAL_INTERPRETER+" text"+")";


	private final Context context;
	private DatabaseHelper mdbHelper;
	private SQLiteDatabase mdb;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context2) {
			super(context2,DATABASE_NAME,null,DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_LoginTable_CREATE);
			db.execSQL(DATABASE_Patients_CREATE);
			db.execSQL(DATABASE_Patient_Address_CREATE);
			db.execSQL(DATABASE_Patient_Family_CREATE);
			db.execSQL(DATABASE_Patient_HealthCard_CREATE);
			db.execSQL(DATABASE_Patient_Health_CREATE);
			db.execSQL(DATABASE_Referral_CREATE);
			db.execSQL(DATABASE_Patient_Language_CREATE);
			db.execSQL(DATABASE_Patient_ComSerProviderAdapter_CREATE);
			db.execSQL((DATABASE_HospitalList_CREATE));
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}
	public LoginDBHelper(Context ctx) {
		this.context = ctx;
		mdbHelper = new DatabaseHelper(context);
	}


   public LoginDBHelper open() throws Exception {
	   this.mdb = mdbHelper.getWritableDatabase();
	   return this;
   }

	public void close() {
		this.mdbHelper.close();
	}






}
