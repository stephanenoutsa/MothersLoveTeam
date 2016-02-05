package com.motherslove.stephnoutsa.mothersloveteam;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class Home extends AppCompatActivity {

    EditText numberField, lmpField;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        numberField = (EditText) findViewById(R.id.numberField);
        lmpField = (EditText) findViewById(R.id.lmpField);
        dbHandler = new MyDBHandler(this, null, null, 1);
    }

    public void onClickSave(View view) {
        Contact contact = new Contact(numberField.getText().toString(), lmpField.getText().toString());
        dbHandler.addContact(contact);
        numberField.setText("");
        lmpField.setText("");

        // Start service
        Intent service = new Intent(this, MyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, service, 0);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                0, 2 * 1000 * 60,
                pendingIntent);

        // Enable receiver when device boots
        ComponentName receiver = new ComponentName(this, BootReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void onClickDelete(View view) {
        long contactphone = Long.parseLong(numberField.getText().toString());
        dbHandler.deleteContact(contactphone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.go_to_home) {
            Intent i = new Intent(this, Home.class);
            startActivity(i);
        }
        if (id == R.id.go_to_contacts) {
            Intent i = new Intent(this, Contacts.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
