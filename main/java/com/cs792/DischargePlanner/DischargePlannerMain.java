package com.cs792.DischargePlanner;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cs792.Users.UserSessionManager;
import com.cs792.projectapp.R;

public class DischargePlannerMain extends Activity {
	private UserSessionManager user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discharge_planner_main);
		user = new UserSessionManager(this);
		if(!user.checkLogin())
			finish();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_discharge_planner_main, menu);
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

	public void fillForms(View view) {
		Intent intent1 = new Intent(this,DischargePlanner_FillForms.class);
		startActivity(intent1);
	}

	public void submitForms(View view) {
		Intent intent2 = new Intent(this, DischargePlanner_submitForms.class);
		startActivity(intent2);
	}

	public void viewStatus(View view) {
		Intent intent3 = new Intent(this,DischargePlanner_ViewStatus.class);
		startActivity(intent3);
	}

	public void logoutUser(View view) {
		user.logout();
	}
}
