package com.zk.administrator.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 3/27/2016.
 */



public class MyService extends Service {
    long timerTime;
    private NotificationManager mNotificationManager = null;
    private final NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this);
    private static final int NOTIFICATION = 1;
    public static final String CLOSE_ACTION = "close";

    int i=0;
    @Override
    public IBinder onBind(Intent arg0) {
        timerTime = arg0.getExtras().getLong("Timer");
        //{"UserId":35,"BillId":614,"OrderDetails":[{"ItemComments":"","ItemId":15,"ItemQuaintity":1,"UnitPrice":290}]}
        //String json = ""
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupNotifications();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        callAsynchronousTask();
        showNotification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        timer.cancel();
        mNotificationManager.cancel(NOTIFICATION);
    }

    final Handler handler = new Handler();
    Timer timer = new Timer();
    public void callAsynchronousTask() {

        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            /*PerformBackgroundTask performBackgroundTask = new PerformBackgroundTask();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            performBackgroundTask.execute();*/
                            //i++;
                            sendOrder();
                            //Toast.makeText(MyService.this, "Service Timer Task "+i, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 10000); //execute in every 50000 ms
    }

    public MyService(){

    }

    private void setupNotifications() { //called in onCreate()
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0);
        PendingIntent pendingCloseIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .setAction(CLOSE_ACTION),
                0);
        mNotificationBuilder
                .setSmallIcon(R.drawable.technology)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(getText(R.string.app_name))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel,
                        getString(R.string.action_exit), pendingCloseIntent)
                .setOngoing(true);
    }

    private void showNotification() {
        mNotificationBuilder
                .setTicker(getText(R.string.service_connected))
                .setContentText(getText(R.string.service_connected));
        if (mNotificationManager != null) {
            mNotificationManager.notify(NOTIFICATION, mNotificationBuilder.build());
        }
    }
    protected void sendOrder() {
        List<NameValuePair> nameValuePairs = new ArrayList<>(
                2);
        nameValuePairs.add(new BasicNameValuePair("item",
                getJObj().toString()));
        NetworkManagerOld httpMan = new NetworkManagerOld(MyService.this,
                NetworkManagerOld.EnCallType.POSTFORLOGIN, NetworkManagerOld.ReturnTypeForReponse.String, nameValuePairs,
                new TaskCompleted() {

                    @Override
                    public void onTaskFailed() {
                        // TODO Auto-generated method stub
                        Log.e("Menyoo","Error");
                    }

                    @Override
                    public void onTaskCompletedSuccessfully(Object obj) {
                        // TODO Auto-generated method stub
                        Log.e("Menyoo", obj.toString());
                    }
                });

        String url = "GetUserOrder";
        String[] params = {getString(R.string.url_offline) + url};
        httpMan.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    //595B5C
    public JSONObject getJObj() {

        JSONObject obj = null;
        try {
            obj = new JSONObject();
            String arr = "{\"UserId\":35,\"BillId\":616,\"OrderDetails\":[{\"ItemComments\":\"Large Mayoo\",\"ItemId\":15,\"ItemQuaintity\":4,\"UnitPrice\":290}]}";

            obj = new JSONObject(arr);
            Log.e("JsonObj", obj.toString());
            return obj;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}