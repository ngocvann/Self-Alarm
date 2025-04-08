package hcmute.edu.vn.selfalarm.fragments;

import android.Manifest;
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
import hcmute.edu.vn.selfalarm.R;
import hcmute.edu.vn.selfalarm.adapter.SMSAdapter;
import hcmute.edu.vn.selfalarm.model.SMS;
import java.util.ArrayList;
import java.util.List;

public class SMSListFragment extends Fragment {
    private RecyclerView smsList;
    private SMSAdapter smsAdapter;
    private List<SMS> smsListData;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    loadSMS();
                } else {
                    Toast.makeText(getContext(), "SMS permission denied", Toast.LENGTH_SHORT).show();
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
            Uri uri = Uri.parse("content://sms/inbox");
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String sender = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    String message = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                    smsListData.add(new SMS(sender, message));
                }
                cursor.close();
            }
            smsAdapter.notifyDataSetChanged();
        }
    }
}