package com.cs792.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sudeelet on 3/7/2015.
 */
public class PatientDBAdapter {

	public static final String PATIENT_ID ="patient_id";
	public static final String PATIENT_FIRSTNAME="firstname";
	public static final String PATIENT_LASTNAME="lastname";
	public static final String PATIENT_GENDER="gender";
	public static final String PATIENT_DOB="dob";
	public static final String PATIENT_STATUS="status";
	public static final String PATIENT_ADMIT_DATE="admit_date";
	public static final String PATIENT_OWNER_ID="owner_id";
	public static final String PATIENT_SUBMIT_DATE="submit_date";
	public static final String PATIENT_DISCHARGE_DATE="discharge_date";
	public static final String PATIENT_COMPLETION_DATE="completion_date";

	public static final String DATABASE_TABLE = "patients";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mdb;

	private final Context ctx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context,LoginDBHelper.DATABASE_NAME,null,LoginDBHelper.DATABASE_VERSION);
		}
		public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}

	public PatientDBAdapter(Context ctx) {
		this.ctx = ctx;
	}

	public PatientDBAdapter open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.ctx);
		this.mdb = this.mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.mDbHelper.close();
	}

	public long insertPatient(String firstName, String lastName, String gender, Date dob, String status, int owner,Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ContentValues content = new ContentValues();
		content.put(PATIENT_FIRSTNAME,firstName);
		content.put(PATIENT_LASTNAME,lastName);
		content.put(PATIENT_GENDER,gender);
		content.put(PATIENT_DOB,dateFormat.format(dob));
		content.put(PATIENT_STATUS,status);
		content.put(PATIENT_ADMIT_DATE,dateFormat.format(date));
		content.put(PATIENT_OWNER_ID,owner);
		return mdb.insert(DATABASE_TABLE,null,content);
	}

	public boolean deletePatient(int row) {
		return this.mdb.delete(DATABASE_TABLE,PATIENT_ID+"="+row,null)>0;
	}

	public Cursor getPatient(int row) {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_FIRSTNAME,PATIENT_LASTNAME,PATIENT_GENDER,PATIENT_DOB,PATIENT_STATUS,PATIENT_ADMIT_DATE,PATIENT_OWNER_ID},PATIENT_ID+"="+row,null,null,null,null,null);
		return cursor;
	}

	public Cursor getPatient(String firstName, String lastName, String status, String date) {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_FIRSTNAME,PATIENT_LASTNAME,PATIENT_GENDER,PATIENT_DOB,PATIENT_STATUS,PATIENT_ADMIT_DATE,PATIENT_OWNER_ID,PATIENT_SUBMIT_DATE,PATIENT_DISCHARGE_DATE},PATIENT_FIRSTNAME+"=? AND "+PATIENT_LASTNAME+"=? AND "+PATIENT_STATUS+"=? AND "+PATIENT_ADMIT_DATE+"=?",new String[]{firstName,lastName,status,date},null,null,null,null,null);
		return cursor;
	}

	public Cursor getPatient_Discharge(String firstName, String lastName, String status, String date) {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_FIRSTNAME,PATIENT_LASTNAME,PATIENT_GENDER,PATIENT_DOB,PATIENT_STATUS,PATIENT_ADMIT_DATE,PATIENT_OWNER_ID,PATIENT_SUBMIT_DATE,PATIENT_DISCHARGE_DATE,PATIENT_COMPLETION_DATE},PATIENT_FIRSTNAME+"=? AND "+PATIENT_LASTNAME+"=? AND "+PATIENT_STATUS+"=? AND "+PATIENT_DISCHARGE_DATE+"=?",new String[]{firstName,lastName,status,date},null,null,null,null,null);
		return cursor;
	}

	public Cursor getPatient_Submit(String firstName, String lastName, String status, String date) {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_FIRSTNAME,PATIENT_LASTNAME,PATIENT_GENDER,PATIENT_DOB,PATIENT_STATUS,PATIENT_ADMIT_DATE,PATIENT_OWNER_ID,PATIENT_SUBMIT_DATE,PATIENT_DISCHARGE_DATE},PATIENT_FIRSTNAME+"=? AND "+PATIENT_LASTNAME+"=? AND "+PATIENT_STATUS+"=? AND "+PATIENT_SUBMIT_DATE+"=?",new String[]{firstName,lastName,status,date},null,null,null,null,null);
		return cursor;
	}

	public Cursor getPatient_Complete(String firstName, String lastName, String status, String date) {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_FIRSTNAME,PATIENT_LASTNAME,PATIENT_GENDER,PATIENT_DOB,PATIENT_STATUS,PATIENT_ADMIT_DATE,PATIENT_OWNER_ID,PATIENT_SUBMIT_DATE,PATIENT_DISCHARGE_DATE,PATIENT_COMPLETION_DATE},PATIENT_FIRSTNAME+"=? AND "+PATIENT_LASTNAME+"=? AND "+PATIENT_STATUS+"=? AND "+PATIENT_COMPLETION_DATE+"=?",new String[]{firstName,lastName,status,date},null,null,null,null,null);
		return cursor;
	}
	public boolean updatePatient(int row, String status, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ContentValues contentValues = new ContentValues();
		contentValues.put(PATIENT_STATUS,status);
		contentValues.put(PATIENT_SUBMIT_DATE,dateFormat.format(date));
		return this.mdb.update(DATABASE_TABLE,contentValues,PATIENT_ID+"="+row,null)>0;
	}

	public boolean updatePatientDischarge(int row, String status, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ContentValues contentValues = new ContentValues();
		contentValues.put(PATIENT_STATUS,status);
		contentValues.put(PATIENT_DISCHARGE_DATE,dateFormat.format(date));
		return this.mdb.update(DATABASE_TABLE,contentValues,PATIENT_ID+"="+row,null)>0;
	}

	public Cursor getAllPatient(int owner_id) {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_FIRSTNAME,PATIENT_LASTNAME,PATIENT_STATUS,PATIENT_ADMIT_DATE,PATIENT_OWNER_ID},PATIENT_OWNER_ID+"="+owner_id+" and "+PATIENT_STATUS+"= 'Admitted'",null,null,null,PATIENT_ADMIT_DATE +" ASC",null);
		return cursor;
	}

	public Cursor getAllPatient() {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_FIRSTNAME,PATIENT_LASTNAME,PATIENT_STATUS,PATIENT_SUBMIT_DATE,PATIENT_OWNER_ID},PATIENT_STATUS+"= 'Treatment Completed'",null,null,null,PATIENT_SUBMIT_DATE +" ASC",null);
		return cursor;
	}

	public Cursor getAllPatient_ReferralReady(int row_id) {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_FIRSTNAME,PATIENT_LASTNAME,PATIENT_STATUS,PATIENT_SUBMIT_DATE,PATIENT_OWNER_ID},PATIENT_STATUS+"= 'Referral Form Ready'"+" and "+PATIENT_ID+"="+row_id,null,null,null,PATIENT_SUBMIT_DATE +" ASC",null);
		return cursor;
	}

	public Cursor getAllPatient_SubmittedDischarge() {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_FIRSTNAME,PATIENT_LASTNAME,PATIENT_STATUS,PATIENT_SUBMIT_DATE,PATIENT_DISCHARGE_DATE,PATIENT_OWNER_ID},PATIENT_STATUS+"= 'Submitted for Discharge'",null,null,null,PATIENT_DISCHARGE_DATE +" ASC",null);
		return cursor;
	}

	public Cursor getAllPatient_SubmittedAssigned() {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_FIRSTNAME,PATIENT_LASTNAME,PATIENT_STATUS,PATIENT_SUBMIT_DATE,PATIENT_DISCHARGE_DATE,PATIENT_OWNER_ID},PATIENT_STATUS+"= 'Assigned'",null,null,null,PATIENT_DISCHARGE_DATE +" ASC",null);
		return cursor;
	}

	public Cursor getAllPatient_Completed() {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_FIRSTNAME,PATIENT_LASTNAME,PATIENT_STATUS,PATIENT_SUBMIT_DATE,PATIENT_DISCHARGE_DATE,PATIENT_COMPLETION_DATE,PATIENT_OWNER_ID},PATIENT_STATUS+"= 'Completed'",null,null,null,PATIENT_COMPLETION_DATE +" ASC",null);
		return cursor;
	}

	public boolean updatePatient_complete(int row, Date date, String status) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ContentValues contentValues = new ContentValues();
		contentValues.put(PATIENT_COMPLETION_DATE,dateFormat.format(date));
		contentValues.put(PATIENT_STATUS,status);
		return this.mdb.update(DATABASE_TABLE,contentValues,PATIENT_ID+"="+row,null)>0;
	}

	public boolean updatePatient(int row, String status) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PATIENT_STATUS,status);
		return this.mdb.update(DATABASE_TABLE,contentValues,PATIENT_ID+"="+row,null)>0;
	}

}
