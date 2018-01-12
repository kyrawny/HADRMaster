package sg.gov.mindef.hadrmaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:

                    return true;
                case R.id.navigation_communications:

                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser().getEmail(),
                    Toast.LENGTH_LONG)
                    .show();
        } else {
            // not signed in
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder().build(),
                    RC_SIGN_IN);
        }
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_map:
                                selectedFragment = MapFragment.newInstance();
                                break;
                            case R.id.navigation_communications:
                                selectedFragment = CommsFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, MapFragment.newInstance());
        transaction.commit();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        mapView.onStart();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        mapView.onStop();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }


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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            finish();
                        }
                    });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this,
                            "Sign in cancelled.",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this,
                            "The phone is not connected to a network. Please try again later.",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this,
                            "We couldn't sign you in. Please try again later.",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
            }

            Toast.makeText(this,
                    "We couldn't sign you in. Please try again later.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }
}
