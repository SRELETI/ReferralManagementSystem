package com.cs792.projectapp;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import com.cs792.DB.LoginDBAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register extends Activity implements AdapterView.OnItemSelectedListener{

    private Spinner spin;
    private Button btnSubmit;
    LoginDBAdapter db;
    EditText user;
    EditText password;
    EditText confirmPass;
    EditText email;
    String userType;

    // OnCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        spin = (Spinner)findViewById(R.id.Reg_typeDropdown);
        spin.setOnItemSelectedListener(this);

        // Adds the items in the drop down
        List<String> list = new ArrayList<String>();
        list.add("Physician");
        list.add("DischargePlanner");
        list.add("CCAC");

        // Adds the list to a adapter
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        // Attach the adapter to the spinner
        spin.setAdapter(dataAdapter);

        // Creates an instance of the dbHelper
        db=new LoginDBAdapter(this);
        // Opens a database connection
		try {
			db = db.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        // Adds a listener to the Register button
        addListenerOnButton();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userType=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void addListenerOnButton() {
        // Reads the values entered by the user.
        user= (EditText) findViewById(R.id.Reg_userEnteredText);
        password= (EditText)findViewById(R.id.Reg_passwordEntered);
        spin = (Spinner) findViewById(R.id.Reg_typeDropdown);
        confirmPass = (EditText) findViewById(R.id.Reg_confirmPassEntered);
        btnSubmit = (Button) findViewById(R.id.Reg_RegisterSubmit);
        email = (EditText) findViewById(R.id.Reg_EmailEntered);
        // Called when the button is clicked
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userStr=user.getText().toString();
                String passStr=password.getText().toString();
                String typeStr=spin.getSelectedItem().toString();
                String confStr=confirmPass.getText().toString();
                String emailId=email.getText().toString();

                if (!isValidEmail(emailId)) {
                    email.setError("Invalid Email");
                    return;
                }
                // checks if username,password,usertype are empty and display error if so.
                else if(userStr.equals("") || passStr.equals("") || typeStr.equals("")) {
                    Toast.makeText(Register.this,"Fields Vacant",Toast.LENGTH_SHORT).show();
                    return;
                }
                // checks if both the passwords are matching
                else if(!passStr.equals(confStr)) {
                    Toast.makeText(getApplicationContext(),"Passwords does not match",Toast.LENGTH_SHORT).show();
                    return;
                }
                // Inserts the record into the database.
                else {
                    db.insertRecord(userStr,passStr,typeStr,emailId, Calendar.getInstance().getTime());
                    db.close();
                    Toast.makeText(getApplicationContext(),"Account created successfully",Toast.LENGTH_LONG).show();
                    // Redirects to the login page by clearing everything that is opened here.
                    Intent intent2 = new Intent(Register.this,MainScreen.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent2);
                    finish();
                }
            }
        });
    }
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
