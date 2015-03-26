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
public class PatientFamilyDBAdapter {

	public static final String DATABASE_TABLE = "patientFamilyData";
	public static final String PATIENT_FAMILY_ID = "_id";
	public static final String PATIENT_FAMILY_personName = "personName";
	public static final String PATIENT_FAMILY_relationShip = "relationShip";
	public static final String PATIENT_FAMILY_TELEPHONE = "telephone";
	public static final String PATIENT_FAMILY_ALTTELEPHONE = "altTelephone";

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

	public PatientFamilyDBAdapter(Context context) {
		this.ctx = context;

	}

	public PatientFamilyDBAdapter open() throws SQLException {
		this.mdbHelper = new DatabaseHelper(this.ctx);
		this.mdb = this.mdbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.mdbHelper.close();
	}

	public long insertPatientFamilyInfo(int id, String personName, String relation, long telephone, long altTelephone) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PATIENT_FAMILY_ID, id);
		contentValues.put(PATIENT_FAMILY_personName,personName);
		contentValues.put(PATIENT_FAMILY_relationShip, relation);
		contentValues.put(PATIENT_FAMILY_TELEPHONE, telephone);
		contentValues.put(PATIENT_FAMILY_ALTTELEPHONE, altTelephone);
		return this.mdb.insert(DATABASE_TABLE, null, contentValues);
	}

	public boolean deletePatientAddress(int row) {
		return this.mdb.delete(DATABASE_TABLE,PATIENT_FAMILY_ID+"="+row,null)>0;
	}

	public Cursor getPatientAddress(int row) {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_FAMILY_ID, PATIENT_FAMILY_personName,PATIENT_FAMILY_relationShip, PATIENT_FAMILY_TELEPHONE, PATIENT_FAMILY_ALTTELEPHONE},PATIENT_FAMILY_ID+"="+row,null,null,null,null,null);
		if(cursor!=null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public void updatePatient(int row) {
		// TODO... Left empty for now
	}
}
