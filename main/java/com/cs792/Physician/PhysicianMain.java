package com.cs792.Physician;

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

public class PhysicianMain extends Activity {

    UserSessionManager user;
    Button newPatient;
    Button viewPatient;
    Button dischargePatient;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physician_main);
        user = new UserSessionManager(this);
        if(!user.checkLogin())
            finish();

        newPatient = (Button) findViewById(R.id.Phy_dash_Add);
        viewPatient = (Button) findViewById(R.id.Phy_dash_View);
        dischargePatient = (Button) findViewById(R.id.Phy_dash_discharge);
        logout = (Button) findViewById(R.id.Phy_dash_logout);

        newPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),Physician_newPatient.class);
                startActivity(intent1);
            }
        });

        viewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(),Physician_viewPatients.class);
                startActivity(intent2);
            }
        });

        dischargePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(),Physician_viewPatients.class);
                startActivity(intent3);
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
        getMenuInflater().inflate(R.menu.menu_physician_main, menu);
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

    public void newPatient() {
        Intent intent = new Intent(this,Physician_newPatient.class);
    }
}
