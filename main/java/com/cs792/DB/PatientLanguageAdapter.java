package com.cs792.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cs792.projectapp.CustomOnItemSelectedListener;

import java.sql.SQLException;

/**
 * Created by sudeelet on 3/15/2015.
 */
public class PatientLanguageAdapter {



	public static String PATIENT_SPEAKS_ENGLISH="speaksEnglish";
	public static String PATIENT_NEEDS_INTERPRETER="needsInterpreter";
	public static String PATIENT_PRIMARY_LANGUAGE="primaryLanguage";
	public static String PATIENT_ID="patient_id";

	public static String DATABASE_TABLE="language";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mdb;
	private Context ctx;

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

	public PatientLanguageAdapter(Context ctx) {
		this.ctx = ctx;
	}

	public PatientLanguageAdapter open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.ctx);
		this.mdb = this.mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.mDbHelper.close();
	}

	public long insertPatientLanguage(int patient_id, String english, String interpreter, String primaryLanguage) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PATIENT_ID,patient_id);
		contentValues.put(PATIENT_SPEAKS_ENGLISH,english);
		contentValues.put(PATIENT_NEEDS_INTERPRETER,interpreter);
		contentValues.put(PATIENT_PRIMARY_LANGUAGE,primaryLanguage);
		return mdb.insert(DATABASE_TABLE,null,contentValues);
	}

	public Cursor getPatientLanguage(int row) {
		Cursor result = mdb.query(true,DATABASE_TABLE,new String[] {PATIENT_ID,PATIENT_SPEAKS_ENGLISH,PATIENT_NEEDS_INTERPRETER,PATIENT_PRIMARY_LANGUAGE},PATIENT_ID+"="+row,null,null,null,null,null);
		return result;
	}

	public boolean updatePatientLanguage(int row,String speaksEnglish, String interpreter, String primaryLang) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PATIENT_SPEAKS_ENGLISH,speaksEnglish);
		contentValues.put(PATIENT_NEEDS_INTERPRETER,interpreter);
		contentValues.put(PATIENT_PRIMARY_LANGUAGE,primaryLang);
		return this.mdb.update(DATABASE_TABLE,contentValues,PATIENT_ID+"="+row,null)>0;
	}
}

