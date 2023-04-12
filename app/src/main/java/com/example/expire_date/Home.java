package com.example.expire_date;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.time.LocalDateTime;

public class Home extends AppCompatActivity {

    BottomNavigationView bnv;


    public Fragment selectedFregment;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.bn_list_add:
                    selectedFregment = new List_Add();
                    break;
                case R.id.bn_list_view:
                    selectedFregment = new List_View();
                    break;
                case R.id.bn_account:
                    selectedFregment = new Fragment_Account();
                    break;
            }
            if (selectedFregment == null) {

                selectedFregment = new List_Add();
                bnv.setSelectedItemId(R.id.bn_list_add);
                getSupportFragmentManager().beginTransaction().replace(R.id.fregment_container, selectedFregment).commit();
            } else {

                getSupportFragmentManager().beginTransaction().replace(R.id.fregment_container, selectedFregment).commit();
            }


            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        if (savedInstanceState == null) {

            bnv.setSelectedItemId(R.id.bn_list_add);
            getSupportFragmentManager().beginTransaction().replace(R.id.fregment_container, selectedFregment).commit();
        }


    }

    /*private void ScanBarCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(integrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fregment_container); // Replace "fragment_container" with your container ID
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void init() {
        //Set Bottom Navigation View In Activity
        bnv = findViewById(R.id.boottom_navigation);
        bnv.setOnNavigationItemSelectedListener(navListener);
    }

    /*public void Notify(){

        LocalDateTime currentDateTime = LocalDateTime.now();

        String cmpdate = currentDateTime.getDayOfMonth()+"/"+currentDateTime.getMonthValue()+"/"+currentDateTime.getYear();
        Toast.makeText(this, cmpdate, Toast.LENGTH_LONG).show();

        int notify_ID=1;
        String CHANNEL_ID = "my_channel_id";
        NotificationManager mnotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;

        notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Barcode Scanned")
                .setContentText("rs.getContents()")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();

        mnotificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"New Channel", NotificationManager.IMPORTANCE_HIGH));

        mnotificationManager.notify(notify_ID, notification);
    }*/
}