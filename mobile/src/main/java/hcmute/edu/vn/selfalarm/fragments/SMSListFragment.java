package hcmute.edu.vn.selfalarm.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.selfalarm.R;
import hcmute.edu.vn.selfalarm.SendSMSActivity;
import hcmute.edu.vn.selfalarm.adapter.SMSAdapter;
import hcmute.edu.vn.selfalarm.model.SMS;

public class SMSListFragment extends Fragment {

    private RecyclerView smsList;
    private SMSAdapter smsAdapter;
    private List<SMS> smsListData;
    private FloatingActionButton fabSMS;

    // Permission launcher
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    loadSMS();
                } else {
                    Toast.makeText(getContext(), "SMS permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    // Launcher for SendSMSActivity
    private final ActivityResultLauncher<Intent> smsLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    loadSMS(); // refresh khi gửi SMS xong
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms_list, container, false);

        smsList = view.findViewById(R.id.sms_list);
        smsList.setLayoutManager(new LinearLayoutManager(getContext()));

        smsListData = new ArrayList<>();
        smsAdapter = new SMSAdapter(smsListData);
        smsList.setAdapter(smsAdapter);

        fabSMS = view.findViewById(R.id.fab_sms);
        fabSMS.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SendSMSActivity.class);
            smsLauncher.launch(intent);
        });

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_SMS);
        } else {
            loadSMS();
        }

        return view;
    }

    private void loadSMS() {
        smsListData.clear();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED) {

            // Đọc cả inbox và sent
            readSmsFromUri("content://sms/inbox");
            readSmsFromUri("content://sms/sent");

            smsAdapter.notifyDataSetChanged();
        }
    }

    private void readSmsFromUri(String uriStr) {
        Uri uri = Uri.parse(uriStr);
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, "date DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String sender = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String message = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                smsListData.add(new SMS(sender, message));
            }
            cursor.close();
        }
    }
}
