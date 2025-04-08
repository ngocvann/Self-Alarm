package hcmute.edu.vn.selfalarm.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import hcmute.edu.vn.selfalarm.R;

public class BatteryFragment extends Fragment {
    private TextView batteryStatusTextView, batteryLevelTextView, tipsTextView;
    private BroadcastReceiver batteryReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_battery, container, false);

        batteryStatusTextView = view.findViewById(R.id.battery_status);
        batteryLevelTextView = view.findViewById(R.id.battery_level);
        tipsTextView = view.findViewById(R.id.tips_text);

        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

                float batteryPct = level * 100 / (float) scale;
                batteryLevelTextView.setText("Battery Level: " + (int) batteryPct + "%");

                String statusString;
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        statusString = "Charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        statusString = "Discharging";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        statusString = "Full";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        statusString = "Not Charging";
                        break;
                    default:
                        statusString = "Unknown";
                        break;
                }
                batteryStatusTextView.setText("Battery Status: " + statusString);

                StringBuilder tips = new StringBuilder();
                if (batteryPct < 20) {
                    tips.append("- Enable battery saver mode.\n");
                    tips.append("- Reduce screen brightness.\n");
                    tips.append("- Close unused apps.\n");
                } else if (batteryPct < 50) {
                    tips.append("- Turn off Wi-Fi and Bluetooth if not in use.\n");
                    tips.append("- Disable background app refresh.\n");
                } else {
                    tips.append("- Battery is in good condition.\n");
                }
                tipsTextView.setText(tips.toString());
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getContext().registerReceiver(batteryReceiver, filter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(batteryReceiver);
    }
}