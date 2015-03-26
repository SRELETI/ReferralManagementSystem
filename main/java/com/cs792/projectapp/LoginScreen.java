package com.cs792.projectapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import com.cs792.CCAC.CCAC_Main;
import com.cs792.DB.LoginDBAdapter;
import com.cs792.DischargePlanner.DischargePlannerMain;
import com.cs792.Physician.PhysicianMain;
import com.cs792.Users.UserSessionManager;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Result;


public class LoginScreen extends Activity {

    /* UserName */
    private EditText userName=null;
    /* Password */
    private EditText password=null;
    /* Login Button */
    private Button login = null;

	Cursor result;
    /* Reference of LoginDBAdapter */
    LoginDBAdapter dbAdapter;
    /* User Type */
    String message;
    /*Shared Preference Reference */
    UserSessionManager shared;

	String type;
	String passWord;
	String user;

    /* Called when this activity is initiated by other activity */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the message from Intent
        Intent intent = getIntent();
        message = intent.getStringExtra(MainScreen.EXTRA_MESSAGE);
		shared = new UserSessionManager(this);
        setContentView(R.layout.activity_login_screen);

        userName = (EditText) findViewById(R.id.Login_userEnteredText);
        password = (EditText) findViewById(R.id.Login_passwordEntered);
        login = (Button) findViewById(R.id.Login_LoginButton);
        //Create a text view
        TextView view = (TextView)findViewById(R.id.Login_loginType);
        view.setText(message);
		dbAdapter = new LoginDBAdapter(this);
        /* Open a DB connection */
		try {
			dbAdapter = dbAdapter.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}



		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				user = userName.getText().toString();
				passWord=password.getText().toString();
				type = message;
				 /* Get details from DB */
				result = dbAdapter.getRecord(user,passWord,type);
				if(result.moveToFirst()==true ) {
					int id = result.getInt(result.getColumnIndex("login_id"));
					String email = result.getString(result.getColumnIndex("email"));
					shared.createUserLoginSession(user,passWord,email,id);
					if(type.equals("Physician")) {
						Intent intent3 = new Intent(getApplicationContext(), PhysicianMain.class);
						startActivity(intent3);
						finish();
					}
					else if(type.equals("DischargePlanner")) {
						Intent intent4 = new Intent(getApplicationContext(), DischargePlannerMain.class);
						startActivity(intent4);
						finish();
					}
					else  {
						Intent intent5 = new Intent(getApplicationContext(), CCAC_Main.class);
						startActivity(intent5);
						finish();
					}
				}
				else {
				/* No User Found */
					Toast.makeText(getApplicationContext(), "No Such User", Toast.LENGTH_SHORT).show();
				}
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void register(View view) {
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
        finish();

    }

    public void destroy() {
        dbAdapter.close();
    }

	private class Heavy extends AsyncTask<Void,Void,Cursor> {
		private Context context;
		private ProgressDialog progressDialog;
		public Heavy(Context context) {
			this.context = context;

		}
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle("Loading");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
		@Override
		protected Cursor doInBackground(Void... params) {

			return result;
		}


		@Override
		protected void onPostExecute(Cursor results) {
				progressDialog.dismiss();
        /* User Found */

		}
		@Override
		public void onCancelled(){
			super.onCancelled();
			if ( progressDialog.isShowing()){
				progressDialog.dismiss();
			}
		}
	}
}
