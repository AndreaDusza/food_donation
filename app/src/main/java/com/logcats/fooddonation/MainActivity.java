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

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.List;


public class MainActivity extends Activity implements DataCallback {

    public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";
    private static final String CLASS_NAME = "com.logcats.fooddonation.MainActivity";
    private SharedPreferences prefs;
    private DataManager dm;

    private boolean loggedIn = false;
    private String SHARED_PREF_USER_LOGGED_IN = "user_logged_in";

    private String welcome_screen_shown = "welcome_screen_shown";
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dm = new DataManager(this);
        prefs = this.getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE);

        greetUserAtFirstTime();

        loggedIn = prefs.getBoolean(SHARED_PREF_USER_LOGGED_IN, false);

        Button button = (Button) findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
                startActivityForResult(intent, UserLoginActivity.ACTION_LOGIN);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_logout);
        if (!loggedIn && menuItem != null) {
            menuItem.setVisible(false);
        }

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
            case R.id.action_logout:
                loggedIn = false;
                menu.findItem(R.id.action_logout).setVisible(false);
                prefs.edit().putBoolean(SHARED_PREF_USER_LOGGED_IN, loggedIn);
                DataManager manager = new DataManager(this);
                manager.logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void greetUserAtFirstTime() {
        boolean welcomeScreenShown = prefs.getBoolean(welcome_screen_shown, false);
        if (!welcomeScreenShown) {
            showWelcomeScreen();
            prefs.edit().putBoolean(welcome_screen_shown, true).commit();
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
                loggedIn = true;
                prefs.edit().putBoolean(SHARED_PREF_USER_LOGGED_IN, loggedIn);
                MenuItem menuItem = menu.findItem(R.id.action_logout);
                menuItem.setVisible(true);
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("MainActivity", "User login was cancelled");
            }
        }
    }

    @Override
    public void onOffersReceived(List<Offer> offers) {
        // Do nothing
    }

    @Override
    public void onUsersReceived(List<User> users) {
        // Do nothing
    }

    @Override
    public void onAuthStateChanged(AuthData authData) {
        if (authData != null) {
            loggedIn = true;
            prefs.edit().putBoolean(SHARED_PREF_USER_LOGGED_IN, loggedIn);
            if (menu != null) {
                menu.findItem(R.id.action_logout).setVisible(true);
            }
        }
    }
}
