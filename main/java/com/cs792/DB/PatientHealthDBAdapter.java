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
public class PatientHealthDBAdapter {

	public static final String DATABASE_TABLE = "patientHealth";
	public static final String PATIENT_ID = "_id";
	public static final String PATIENT_ALLERGIES = "allergies";
	public static final String PATIENT_INFECTION = "infection_Control";
	public static final String PATIENT_MEDICAL_ORDERS = "medical_Orders";
	public static final String PATIENT_WHEELCHAIR = "wheelChair";
	public static final String PATIENT_COMMENTS="comments";

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

	public PatientHealthDBAdapter(Context context) {
		this.ctx = context;

	}

	public PatientHealthDBAdapter open() throws SQLException {
		this.mdbHelper = new DatabaseHelper(this.ctx);
		this.mdb = this.mdbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.mdbHelper.close();
	}

	public long insertPatientAddress(int id, String allergies, String infection, String medical_orders, String wheel_Chair,String comments) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PATIENT_ID, id);
		contentValues.put(PATIENT_ALLERGIES, allergies);
		contentValues.put(PATIENT_INFECTION, infection);
		contentValues.put(PATIENT_MEDICAL_ORDERS, medical_orders);
		contentValues.put(PATIENT_WHEELCHAIR, wheel_Chair);
		contentValues.put(PATIENT_COMMENTS,comments);
		return this.mdb.insert(DATABASE_TABLE, null, contentValues);
	}

	public boolean deletePatientAddress(int row) {
		return this.mdb.delete(DATABASE_TABLE,PATIENT_ID+"="+row,null)>0;
	}

	public Cursor getPatientProblems(int row) {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_ALLERGIES,PATIENT_INFECTION,PATIENT_MEDICAL_ORDERS,PATIENT_WHEELCHAIR,PATIENT_COMMENTS},PATIENT_ID+"="+row,null,null,null,null,null);
		return cursor;
	}

	public boolean updatePatient(int row,String all, String med, String com, String inf, String wheel) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PATIENT_ALLERGIES,all);
		contentValues.put(PATIENT_COMMENTS,com);
		contentValues.put(PATIENT_MEDICAL_ORDERS,med);
		contentValues.put(PATIENT_INFECTION,inf);
		contentValues.put(PATIENT_WHEELCHAIR,wheel);
		return this.mdb.update(DATABASE_TABLE,contentValues,PATIENT_ID+"="+row,null)>0;

	}

}
