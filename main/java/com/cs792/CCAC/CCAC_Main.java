package com.cs792.CCAC;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cs792.Users.UserSessionManager;
import com.cs792.projectapp.R;

public class CCAC_Main extends Activity {

	private UserSessionManager user;
	private Button unAssigned;
	private Button assigned;
	private Button logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ccac__main);
		user  = new UserSessionManager(this);
		if(!user.checkLogin()) {
			finish();
		}

		unAssigned = (Button) findViewById(R.id.CCAC_complete);
		assigned = (Button) findViewById(R.id.CCAC_Assign);
		logout = (Button) findViewById(R.id.CCAC_logout);

		unAssigned.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),CCAC_viewAll.class);
				startActivity(intent);
			}
		});

		assigned.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(getApplicationContext(),CCAC_viewAssigned.class);
				startActivity(intent2);
			}
		});

		logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				user.logout();
			}
		});

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_ccac__main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()) {
			case R.id.Menu_Logout:
				user.logout();
				return true;
			case R.id.action_settings:
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void unAssigned(View view) {

	}

	public void assigned(View view) {

	}

	public void logout(View view) {

	}

}
