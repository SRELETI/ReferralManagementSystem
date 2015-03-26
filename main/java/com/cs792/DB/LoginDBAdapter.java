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
 * Created by sudeelet on 2/27/2015.
 */
public class LoginDBAdapter {


    public static final String LOGIN_TABLE_NAME="login";
    public static final String LOGIN_COLUMN_USERNAME="userName";
    public static final String LOGIN_COLUMN_PASSWORD="password";
    public static final String LOGIN_COLUMN_USER_TYPE="userType";
    public static final String LOGIN_COLUMN_EMAIL="email";
    public static final String LOGIN_OWNER_ID="login_id";
	public static final String LOGIN_CREATION_DATE="creation_date";


    public DatabaseHelper mdbHelper;
    public SQLiteDatabase mdb;

    private final Context context;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context content) {
			super(content,LoginDBHelper.DATABASE_NAME,null,LoginDBHelper.DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}

    public LoginDBAdapter(Context context) {
        this.context = context;
    }

	public LoginDBAdapter open() throws SQLException{
		mdbHelper = new DatabaseHelper(context);
		mdb = this.mdbHelper.getWritableDatabase();
		return this;
	}


    public  void close() {
        mdbHelper.close();
    }

    public  SQLiteDatabase getDatabaseInstance() {
        return mdb;
    }

    public long insertRecord(String user, String password, String userType,String email,Date creation_date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues initialValues = new ContentValues();
        initialValues.put(LOGIN_COLUMN_USERNAME,user);
        initialValues.put(LOGIN_COLUMN_PASSWORD,password);
        initialValues.put(LOGIN_COLUMN_USER_TYPE,userType);
        initialValues.put(LOGIN_COLUMN_EMAIL,email);
		initialValues.put(LOGIN_CREATION_DATE,format.format(creation_date));
        return mdb.insert(LOGIN_TABLE_NAME,null,initialValues);
    }

    public Cursor getRecord(String user, String password, String type) {
        Cursor res = mdb.query(LOGIN_TABLE_NAME,new String[]{LOGIN_OWNER_ID,LOGIN_COLUMN_USERNAME,LOGIN_COLUMN_PASSWORD,LOGIN_COLUMN_USER_TYPE,LOGIN_COLUMN_EMAIL,LOGIN_CREATION_DATE},LOGIN_COLUMN_USERNAME+"=? AND "+LOGIN_COLUMN_PASSWORD+"=? AND "+LOGIN_COLUMN_USER_TYPE+"=?",new String[]{user,password,type},null,null,null,null);
        return res;
    }


}
