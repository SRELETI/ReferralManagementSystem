package com.cs792.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by sudeelet on 3/15/2015.
 */
public class PatientComSerProviderAdapter {



	public static String PATIENT_ID = "patient_id";
	public static String PATIENT_PROVIDER_SURNAME="surName";
	public static String PATIENT_PROVIDER_FIRSTNAME="firstName";

	public static String DATABASE_TABLE = "patient_serviceProvider";

	private Context ctx;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mdb;
	private class DatabaseHelper extends SQLiteOpenHelper {

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

	public PatientComSerProviderAdapter(Context context) {
		this.ctx = context;
	}

	public PatientComSerProviderAdapter open() throws SQLException{
		this.mDbHelper = new DatabaseHelper(ctx);
		this.mdb = this.mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.mDbHelper.close();
	}

	public long insertPatientServiceProvider(int patient_id, String surName, String firstName) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PATIENT_ID,patient_id);
		contentValues.put(PATIENT_PROVIDER_SURNAME,surName);
		contentValues.put(PATIENT_PROVIDER_FIRSTNAME,firstName);
		return this.mdb.insert(DATABASE_TABLE,null,contentValues);
	}

	public Cursor getPatient(int row) {
		Cursor result = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,PATIENT_PROVIDER_SURNAME,PATIENT_PROVIDER_FIRSTNAME},PATIENT_ID+"="+row,null,null,null,null,null);
		return result;
	}

	public boolean updatePatientServiceProvider(int row, String surName, String firstName) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PATIENT_PROVIDER_SURNAME,surName);
		contentValues.put(PATIENT_PROVIDER_FIRSTNAME,firstName);
		return this.mdb.update(DATABASE_TABLE,contentValues,PATIENT_ID+"="+row,null)>0;
	}
}
