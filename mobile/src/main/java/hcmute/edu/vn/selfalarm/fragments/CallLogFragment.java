package hcmute.edu.vn.selfalarm.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hcmute.edu.vn.selfalarm.R;
import hcmute.edu.vn.selfalarm.adapter.SMSAdapter;
import hcmute.edu.vn.selfalarm.model.SMS;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CallLogFragment extends Fragment {
    private RecyclerView callLogList;
    private SMSAdapter callLogAdapter;
    private List<SMS> callLogData;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    loadCallLog();
                } else {
                    Toast.makeText(getContext(), "Call Log permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_log, container, false);

        callLogList = view.findViewById(R.id.call_log_list);
        callLogList.setLayoutManager(new LinearLayoutManager(getContext()));

        callLogData = new ArrayList<>();
        callLogAdapter = new SMSAdapter(callLogData);
        callLogList.setAdapter(callLogAdapter);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG);
        } else {
            loadCallLog();
        }

        return view;
    }

    private void loadCallLog() {
        callLogData.clear();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG)
                == PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                    long date = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
                    String message = "Call at: " + new Date(date).toString();
                    callLogData.add(new SMS(number, message));
                }
                cursor.close();
            }
            callLogAdapter.notifyDataSetChanged();
        }
    }
}