package com.example.week13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.week13.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Vector;
public class MainActivity extends AppCompatActivity {
    final int RC_SMS_RECEIVED = 1;
    MySMSReceiver mySMSReceiver;
    final ArrayList<String> mList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mySMSReceiver = new MySMSReceiver();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, mList);
        binding.List.setAdapter(adapter);


        mySMSReceiver.setOnSmsReceived(m->adapter.add(m));
        //adapter.notifyDataSetChanged();

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
            !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS}, RC_SMS_RECEIVED);
        } //권한 추가
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mySMSReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mySMSReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_SMS_RECEIVED) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 접근 허가
            } else {
                new AlertDialog.Builder(this).setTitle("Permission!")
                        .setMessage("RECEIVE_SMS permission is required to receive SMS.\nPress OK to grant the permission.")
                        .setPositiveButton("OK", ((dialog, which) -> ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.RECEIVE_SMS}, RC_SMS_RECEIVED)))
                        .setNegativeButton("Cancel", null)
                        .create().show();
            }
        }
    }
}
