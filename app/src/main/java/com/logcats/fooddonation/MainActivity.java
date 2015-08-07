package com.logcats.fooddonation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

public class MainActivity extends Activity implements DataCallback {

    public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";
    private SharedPreferences prefs;
    private DataManager dm;

    private boolean loggedIn = false;
    private String SHARED_PREF_USER_LOGGED_IN = "user_logged_in";

    private String welcome_screen_shown = "welcome_screen_shown";
    private Drawer result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dm = new DataManager(this);
        dm.setCallback(this);
        prefs = this.getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE);

        greetUserAtFirstTime();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        result = new DrawerBuilder()
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
                        if (position == 4) {
                            if (loggedIn) {
                                loggedIn = false;
                                result.removeItem(4);
                                result.addItem(new PrimaryDrawerItem().withName(R.string.action_login));
                                prefs.edit().putBoolean(SHARED_PREF_USER_LOGGED_IN, loggedIn);
                                DataManager manager = new DataManager(getApplicationContext());
                                manager.logout();
                                result.closeDrawer();
                            } else {
                                result.removeItem(4);
                                result.addItem(new PrimaryDrawerItem().withName(R.string.action_logout));
                                loggedIn = true;
                                prefs.edit().putBoolean(SHARED_PREF_USER_LOGGED_IN, loggedIn);

                                Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
                                startActivityForResult(intent, UserLoginActivity.ACTION_LOGIN);

                                result.closeDrawer();
                            }
                        }

                        if (position==0){
                            Intent i = new Intent(MainActivity.this, MapsActivity.class);
                            startActivity(i);
                        }
                        if (position==1){
                            Intent i = new Intent(MainActivity.this, DonationListActivity.class);
                            startActivity(i);
                        }
                        if (position==2){
                            Intent i = new Intent(MainActivity.this, CreateDonationActivity.class);
                            startActivity(i);
                        }
                        return true;
                    }
                })
                .build();

        loggedIn = prefs.getBoolean(SHARED_PREF_USER_LOGGED_IN, false);
        result.addItem(new PrimaryDrawerItem().withName(R.string.action_login));
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
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.tutorial);
       // dialog.setTitle(R.string.welcome);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Tap on a marker to view the details of an offer");

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        dialog.show();
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
        // do nothing
    }
}
