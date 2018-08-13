package sg.edu.rp.c346.smsapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etto;
    EditText etcontent;
    Button btnsend;
    Button btnSendMessage;
    BroadcastReceiver br = new MessageReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etto = findViewById(R.id.editTextTo);
        etcontent = findViewById(R.id.editTextContent);
        btnsend = findViewById(R.id.button);
        btnSendMessage = findViewById(R.id.buttonSendMessage);

        checkPermission();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br, filter);


        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String to = etto.getText().toString();
                String content = etcontent.getText().toString();


                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(to, null,content, null, null);
                etcontent.setText(null);

                Toast.makeText(getApplicationContext(),"Message send",Toast.LENGTH_LONG).show();



            }

        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String to = etto.getText().toString();
                String content = etcontent.getText().toString();

                Uri uri = Uri.parse("smsto:"+to);
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", content);
                startActivity(it);

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(br);
    }

    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }
}


















