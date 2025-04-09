package hcmute.edu.vn.selfalarm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SendSMSActivity extends AppCompatActivity {

    private EditText txtPhoneNo, txtMessage;
    private Button btnSendSMS;

    private ActivityResultLauncher<String> smsPermissionLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_sms);

        txtPhoneNo = findViewById(R.id.txtPhoneNo);
        txtMessage = findViewById(R.id.txtMessage);
        btnSendSMS = findViewById(R.id.btnSendSMS);

        // Khởi tạo launcher xin quyền
        smsPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        sendSMS(); // Gửi nếu đã được cấp quyền
                    } else {
                        Toast.makeText(this, "Permission denied to send SMS", Toast.LENGTH_SHORT).show();
                    }
                });

        // Gắn sự kiện click gửi tin nhắn
        btnSendSMS.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Xin quyền nếu chưa có
                smsPermissionLauncher.launch(Manifest.permission.SEND_SMS);
            } else {
                // Có quyền rồi thì gửi luôn
                sendSMS();
            }
        });
    }

    private void sendSMS() {
        String phoneNo = txtPhoneNo.getText().toString().trim();
        String message = txtMessage.getText().toString().trim();

        if (phoneNo.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Please enter phone number and message", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show();

            // Trả kết quả OK cho Activity gọi (nếu có)
            setResult(RESULT_OK);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
