package com.cs792.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.ContextThemeWrapper;

import java.sql.SQLException;

/**
 * Created by sudeelet on 3/17/2015.
 */
public class HospitalDBAdapter {

	public static String HOSPITAL_ID="hospital_id";
	public static String HOSPITAL_NAME="hospital_name";
	public static String HOSPITAL_BEDS="beds";
	public static String HOSPITAL_DISTANCE="distance";
	public static String HOSPITAL_WHEELCHAIR="wheelChair";
	public static String HOSPITAL_INTERPRETER = "interpreter";

	public static String DATABASE_TABLE="hospitals";

	private Context context;

	private SQLiteDatabase mdb;
	private DatabaseHelper mdbHelper;

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

	public HospitalDBAdapter(Context context) {
		this.context = context;
	}

	public HospitalDBAdapter open() throws SQLException{
		this.mdbHelper = new DatabaseHelper(this.context);
		this.mdb = this.mdbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.mdbHelper.close();
	}

	public Cursor getHospitalList(String wheelChair, String interpreter) {
		Cursor cursor = this.mdb.query(true,DATABASE_TABLE,new String[]{HOSPITAL_ID,HOSPITAL_NAME,HOSPITAL_BEDS,HOSPITAL_DISTANCE,HOSPITAL_WHEELCHAIR,HOSPITAL_INTERPRETER},HOSPITAL_INTERPRETER+"=? AND "+HOSPITAL_WHEELCHAIR+"=?",new String[] {interpreter,wheelChair},null,null,HOSPITAL_DISTANCE+" ASC",null);
		return cursor;
	}

	public boolean updateBeds(int hospital, int beds) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(HOSPITAL_BEDS,beds);
		return this.mdb.update(DATABASE_TABLE,contentValues,HOSPITAL_ID+"="+hospital,null)>0;
	}
}
