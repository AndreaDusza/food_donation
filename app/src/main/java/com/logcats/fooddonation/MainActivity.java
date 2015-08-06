package com.logcats.fooddonation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";
    private static final String CLASS_NAME = "com.logcats.fooddonation.MainActivity";
    private SharedPreferences prefs;
    private DataManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dm = new DataManager(this);
        prefs = this.getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE);

        greetUserAtFirstTime();

        Button button = (Button) findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
                intent.putExtra(UserLoginActivity.REDIRECT_CLASS_STRING, CLASS_NAME);
                startActivityForResult(intent, UserLoginActivity.ACTION_LOGIN);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            //case R.id.action_logout:
              //  return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void greetUserAtFirstTime(){
        String welcome_screen_shown = "welcome_screen_shown";
        boolean welcomeScreenShown = prefs.getBoolean(welcome_screen_shown, false);
        if (!welcomeScreenShown) {
            showWelcomeScreen();
            prefs.edit().putBoolean(welcome_screen_shown,true).commit();
        }
    }

    private void showWelcomeScreen() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.welcome))
                .setMessage(getString(R.string.tutorial_text))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UserLoginActivity.ACTION_LOGIN) {
            if (resultCode == RESULT_OK) {
                Log.d("MainActivity", "User logged in!");
                User user = (User) data.getSerializableExtra(UserLoginActivity.DATA_USER);
                Log.d("MainActivity", "Got user: " + user.getName());
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("MainActivity", "User login was cancelled");
            }
        }
    }
}
