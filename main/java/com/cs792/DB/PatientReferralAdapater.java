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
 * Created by sudeelet on 3/15/2015.
 */
public class PatientReferralAdapater {

	public static String PATIENT_ID="patient_id";
	public static String REFERRAL_DIAGNOSIS="diagnosis";
	public static String REFERRAL_REASON="reason";
	public static String REFERRAL_FACILITY="facility";
	public static String REFERRAL_FACILITY_CONTACT="facility_contact";
	public static String REFERRAL_COMPLETED_BY="completed_by";
	public static String REFERRAL_COMPLETED_ID="discharge_id";
	public static String REFERRAL_COMPLETED_TITLE="title";
	public static String REFERRAL_DATE="referral_fill_date";
	public static String REFERRAL_CONTACT="referral_contact_number";
	public static String REFERRAL_ASSIGNED_TO="referral_assigned_ccac";
	public static String REFERRAL_HOSPITAL_ID="referral_hospital_id";

	public static String DATABASE_TABLE = "referral_details";

	private Context ctx;
	private SQLiteDatabase mdb;
	private DatabaseHelper mDbHelper;

	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context,LoginDBHelper.DATABASE_NAME,null,LoginDBHelper.DATABASE_VERSION);
		}

		public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
			super(context,name,factory,version);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}

	public PatientReferralAdapater(Context context) {
		ctx = context;
	}

	public PatientReferralAdapater open() throws SQLException{
		this.mDbHelper = new DatabaseHelper(ctx);
		this.mdb = this.mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.mdb.close();
	}

	public long insertReferralDetails(int patient_id, String diagnosis, String reason, String facility, long facility_contact, String completed_by, int completed_id,String title, Date date, long contact) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ContentValues contentValues = new ContentValues();
		contentValues.put(PATIENT_ID,patient_id);
		contentValues.put(REFERRAL_DIAGNOSIS,diagnosis);
		contentValues.put(REFERRAL_REASON,reason);
		contentValues.put(REFERRAL_FACILITY,facility);
		contentValues.put(REFERRAL_FACILITY_CONTACT,facility_contact);
		contentValues.put(REFERRAL_COMPLETED_BY,completed_by);
		contentValues.put(REFERRAL_COMPLETED_TITLE,title);
		contentValues.put(REFERRAL_DATE,dateFormat.format(date));
		contentValues.put(REFERRAL_CONTACT,contact);
		contentValues.put(REFERRAL_COMPLETED_ID,completed_id);
		return mdb.insert(DATABASE_TABLE,null,contentValues);
	}

	public Cursor getReferralDetails(int row) {
		Cursor result = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,REFERRAL_DIAGNOSIS,REFERRAL_REASON,REFERRAL_FACILITY,REFERRAL_FACILITY_CONTACT,REFERRAL_COMPLETED_BY,REFERRAL_COMPLETED_ID,REFERRAL_COMPLETED_TITLE,REFERRAL_CONTACT,REFERRAL_DATE,REFERRAL_ASSIGNED_TO},PATIENT_ID+"="+row,null,null,null,null,null);
		return result;
	}

	public Cursor getReferral_CompletedBy(int completed_id) {
		Cursor result = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,REFERRAL_DIAGNOSIS,REFERRAL_REASON,REFERRAL_FACILITY,REFERRAL_FACILITY_CONTACT,REFERRAL_COMPLETED_BY,REFERRAL_COMPLETED_ID,REFERRAL_COMPLETED_TITLE,REFERRAL_CONTACT,REFERRAL_DATE},REFERRAL_COMPLETED_ID+"="+completed_id,null,null,null,null,null);
		return result;
	}

	public Cursor getReferralDetails(int row, String assignedTo) {
		Cursor result = this.mdb.query(true,DATABASE_TABLE,new String[]{PATIENT_ID,REFERRAL_DIAGNOSIS,REFERRAL_REASON,REFERRAL_FACILITY,REFERRAL_FACILITY_CONTACT,REFERRAL_COMPLETED_BY,REFERRAL_COMPLETED_ID,REFERRAL_COMPLETED_TITLE,REFERRAL_CONTACT,REFERRAL_DATE},PATIENT_ID+"=?"+row+" and "+REFERRAL_ASSIGNED_TO+"=?",new String[]{assignedTo},null,null,null,null,null);
		return result;
	}

	public boolean updateAssignedTo(int row, String assignedTo) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(REFERRAL_ASSIGNED_TO,assignedTo);
		return this.mdb.update(DATABASE_TABLE,contentValues,PATIENT_ID+"="+row,null)>0;
	}

	public boolean updateHospitalId(int row, int hospital) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(REFERRAL_HOSPITAL_ID,hospital);
		return this.mdb.update(DATABASE_TABLE,contentValues,PATIENT_ID+"="+row,null)>0;
	}

	public boolean updateReferralDetails(int row, String diagnosis, String reason, String facility, long fac_contact, String completedBy, int complete_id, String title, Date date, long contact) {
		SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
		ContentValues contentValues = new ContentValues();
		contentValues.put(REFERRAL_DIAGNOSIS,diagnosis);
		contentValues.put(REFERRAL_REASON,reason);
		contentValues.put(REFERRAL_FACILITY,facility);
		contentValues.put(REFERRAL_FACILITY_CONTACT,fac_contact);
		contentValues.put(REFERRAL_COMPLETED_BY,completedBy);
		contentValues.put(REFERRAL_COMPLETED_ID,complete_id);
		contentValues.put(REFERRAL_COMPLETED_TITLE,title);
		contentValues.put(REFERRAL_DATE,dateForm.format(date));
		contentValues.put(REFERRAL_CONTACT,contact);
		return this.mdb.update(DATABASE_TABLE,contentValues,PATIENT_ID+"="+row,null)>0;
	}


}
