package com.logcats.fooddonation;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class MainActivity extends AppCompatActivity {

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

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Map view"),
                        new PrimaryDrawerItem().withName("List view"),
                        new PrimaryDrawerItem().withName("New donations"),
                        new PrimaryDrawerItem().withName("Manage")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        return true;
                    }
                })
                .build();
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
