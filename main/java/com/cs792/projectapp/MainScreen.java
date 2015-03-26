package com.cs792.projectapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.app.Activity;
import android.widget.EditText;
import android.support.v7.app.ActionBarActivity;
import android.widget.ProgressBar;

import com.cs792.DB.LoginDBHelper;


public class MainScreen extends Activity {

    /* Constant which is used to refer the buttonType by other activity*/
    public final static String EXTRA_MESSAGE = "com.cs792.projectapp.message";

	Button buttonType_P;
	Button buttonType_DP;
	Button buttonType_CCAC;

	String message;
	private LoginDBHelper loginDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
		loginDBHelper = new LoginDBHelper(this);
		try {
			loginDBHelper.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
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

    /* Called when the user clicks on the Physician Button */
    public void loginPhysician(View view) {
        buttonType_P = (Button) findViewById(R.id.Main_Physician);
        message = buttonType_P.getText().toString();
		new Heavy(MainScreen.this).execute();
    }

    /* Called when the user clicks on the DischargePlanner Button */
    public void loginDischargePlanner(View view) {
        buttonType_DP = (Button) findViewById(R.id.Main_DischargePlanner);
        message = buttonType_DP.getText().toString();
		new Heavy(MainScreen.this).execute();
    }

    /* Called when the user clicks on the CCAC Agent Button */
    public void loginCCACAgent(View view) {
        buttonType_CCAC = (Button) findViewById(R.id.Main_CCAC);
      	message = buttonType_CCAC.getText().toString();
		new Heavy(MainScreen.this).execute();
    }

	private class Heavy extends AsyncTask<Void, Void, String> {

		private Context context;
		private ProgressDialog progressDialog;

		public Heavy(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle("Please wait..");
			progressDialog.setIndeterminate(true);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			return message;
		}

		@Override
		protected void onPostExecute(String s) {
			if(progressDialog.isShowing() || progressDialog!=null) {
				progressDialog.dismiss();
			}
			if(s.equals("Physician")) {
				Intent intent = new Intent(context,LoginScreen.class);
				intent.putExtra(EXTRA_MESSAGE,message);
				startActivity(intent);
			}
			else if(s.equals("DischargePlanner")) {
				Intent intent = new Intent(context,LoginScreen.class);
				intent.putExtra(EXTRA_MESSAGE,message);
				startActivity(intent);
			}
			else {
				Intent intent = new Intent(context,LoginScreen.class);
				intent.putExtra(EXTRA_MESSAGE,message);
				startActivity(intent);
			}
		}
	}
}

