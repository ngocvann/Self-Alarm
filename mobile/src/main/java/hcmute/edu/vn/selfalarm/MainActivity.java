package hcmute.edu.vn.selfalarm;

import android.app.AlertDialog;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import hcmute.edu.vn.selfalarm.fragments.BatteryFragment;
import hcmute.edu.vn.selfalarm.fragments.MusicFragment;
import hcmute.edu.vn.selfalarm.fragments.SMSFragment;
import hcmute.edu.vn.selfalarm.fragments.ScheduleFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_music) {
                selectedFragment = new MusicFragment();
            } else if (itemId == R.id.nav_sms) {
                selectedFragment = new SMSFragment();
            } else if (itemId == R.id.nav_schedule) {
                selectedFragment = new ScheduleFragment();
            } else if (itemId == R.id.nav_battery) {
                selectedFragment = new BatteryFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MusicFragment())
                .commit();

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    Toast.makeText(MainActivity.this, "Incoming: " + phoneNumber, Toast.LENGTH_SHORT).show();
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            showDarkModeDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDarkModeDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Settings")
                .setMessage("Toggle Dark Mode")
                .setPositiveButton("Enable Dark Mode", (dialog, which) -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    recreate();
                })
                .setNegativeButton("Disable Dark Mode", (dialog, which) -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    recreate();
                })
                .setNeutralButton("Cancel", null)
                .show();
    }
}