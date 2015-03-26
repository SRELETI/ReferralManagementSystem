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
public class PatientHealthCardDBAdapter {

	public static final String DATABASE_TABLE = "patientHealthCardDetails";
	public static final String PATIENT_ID = "_id";
	public static final String PATIENT_CARDNO = "cardNo";
	public static final String PATIENT_VERSION = "version";
	public static final String PATIENT_PROVINCE = "province";

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

	public PatientHealthCardDBAdapter(Context context) {
		this.ctx = context;

	}

	public PatientHealthCardDBAdapter open() throws SQLException {
		this.mdbHelper = new DatabaseHelper(this.ctx);
		this.mdb = this.mdbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.mdbHelper.close();
	}

	public long insertPatientAddress(int id, String cardNo, int version, String province) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PATIENT_ID, id);
		contentValues.put(PATIENT_CARDNO, cardNo);
		contentValues.put(PATIENT_VERSION, version);
		contentValues.put(PATIENT_PROVINCE, province);
		return this.mdb.insert(DATABASE_TABLE, null, contentValues);
	}

	public boolean deletePatientAddress(int row) {
		return this.mdb.delete(DATABASE_TABLE,PATIENT_ID+"="+row,null)>0;
	}

	public Cursor getPatientAddress(int row) {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_CARDNO,PATIENT_VERSION,PATIENT_PROVINCE},PATIENT_ID+"="+row,null,null,null,null,null);
		if(cursor!=null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public void updatePatient(int row) {
		// TODO... Left empty for now
	}
}
