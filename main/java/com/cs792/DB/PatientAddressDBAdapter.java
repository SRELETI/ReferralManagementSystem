package com.cs792.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by sudeelet on 3/8/2015.
 */
public class PatientAddressDBAdapter {

	public static final String DATABASE_TABLE = "patientAddress";
	public static final String PATIENT_ID = "_id";
	public static final String PATIENT_ADDRESS = "address";
	public static final String PATIENT_CITY = "city";
	public static final String PATIENT_PROVINCE = "province";
	public static final String PATIENT_POSTALCODE = "postal_code";
	public static final String PATIENT_TELEPHONE = "telephone";
	public static final String PATIENT_ALTTELEPHONE = "altTelephone";

	public static final String PATIENT_ALT_ADDRESS="alt_address";
	public static final String PATIENT_ALT_CITY="alt_city";
	public static final String PATIENT_ALT_PROVINCE="alt_province";
	public static final String PATIENT_ALT_POSTALCODE="alt_postal_code";
	public static final String PATIENT_ALT_TELEPHONE = "alt_telephone";
	public static final String PATIENT_ALT_ALTTELEPHONE="alt_alttelephone";

	private DatabaseHelper mdbHelper;
	private SQLiteDatabase mdb;
	private final Context ctx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, LoginDBHelper.DATABASE_NAME, null, LoginDBHelper.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}

	public PatientAddressDBAdapter(Context context) {
		this.ctx = context;

	}

	public PatientAddressDBAdapter open() throws SQLException {
		this.mdbHelper = new DatabaseHelper(this.ctx);
		this.mdb = this.mdbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.mdbHelper.close();
	}

	public long insertPatientAddress(int id, String address, String city, String province, String postal_Code, long telephone, long altTelephone,
									 String alt_address,String alt_city, String alt_province, String alt_postalCode, long alt_telephone, long alt_altTelephone) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PATIENT_ID, id);
		contentValues.put(PATIENT_ADDRESS, address);
		contentValues.put(PATIENT_CITY, city);
		contentValues.put(PATIENT_PROVINCE, province);
		contentValues.put(PATIENT_POSTALCODE, postal_Code);
		contentValues.put(PATIENT_TELEPHONE, telephone);
		contentValues.put(PATIENT_ALTTELEPHONE, altTelephone);
		contentValues.put(PATIENT_ALT_ADDRESS,alt_address);
		contentValues.put(PATIENT_ALT_CITY,alt_city);
		contentValues.put(PATIENT_ALT_PROVINCE,alt_province);
		contentValues.put(PATIENT_ALT_POSTALCODE,alt_postalCode);
		contentValues.put(PATIENT_ALT_TELEPHONE,alt_telephone);
		contentValues.put(PATIENT_ALT_ALTTELEPHONE,alt_altTelephone);
		return this.mdb.insert(DATABASE_TABLE, null, contentValues);
	}

	public boolean deletePatientAddress(int row) {
		return this.mdb.delete(DATABASE_TABLE,PATIENT_ID+"="+row,null)>0;
	}

	public Cursor getPatientAddress(int row) {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_ADDRESS,PATIENT_CITY,PATIENT_PROVINCE,PATIENT_POSTALCODE,PATIENT_TELEPHONE,PATIENT_ALTTELEPHONE,PATIENT_ALT_ADDRESS,PATIENT_ALT_CITY,PATIENT_ALT_PROVINCE,PATIENT_ALT_POSTALCODE,PATIENT_ALT_TELEPHONE,PATIENT_ALT_ALTTELEPHONE},PATIENT_ID+"="+row,null,null,null,null,null);
		if(cursor!=null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public void updatePatient(int row) {
		// TODO... Left empty for now
	}
}