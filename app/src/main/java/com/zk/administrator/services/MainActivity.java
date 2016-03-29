package com.zk.administrator.services;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    // Method to start the service
    public void startService() {
        Intent i =new Intent(getBaseContext(), MyService.class);
        startService(i);
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        switch (action) {
            case MyService.CLOSE_ACTION:
                exit();
                break;
        }
    }

    private void exit() {
        stopService();
        finish();
    }

    public void startService(View view) {
        startService();
    }


}
