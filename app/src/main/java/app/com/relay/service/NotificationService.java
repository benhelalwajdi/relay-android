package app.com.relay.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import app.com.relay.R;
import app.com.relay.activity.OrderActivity;
import app.com.relay.utils.Constant;

public class NotificationService extends Service {

    private int number ;
    private static int numbreOfOrder ;
    Timer timer;
    TimerTask timerTask;
    private RequestQueue queuee;
    private JSONArray jsonArray ;
    String TAG = "Timers";
    int Your_X_SECS = 5;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        startTimer();

        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");


    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();


    }

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();


    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, Your_X_SECS * 1000); //
        //timer.schedule(timerTask, 5000,1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext());

        timerTask = new TimerTask() {

            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        int num = number();
                        System.out.println("hello thread " + numbreOfOrder );
                        System.out.println("hello thread 2 " + number );
                        if((numbreOfOrder-number) != 0) {
                            Intent intent = new Intent(getApplication(), OrderActivity.class );
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0, intent, 0);
                            mBuilder.setContentIntent(pendingIntent);
                            mBuilder.setSmallIcon(R.drawable.about_icon_store_activity);
                            mBuilder.setContentTitle(numbreOfOrder +" new order ");
                            mBuilder.setContentText("You have new order check it please with references ");
                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(001, mBuilder.build());
                        }
                        numbreOfOrder = number ;
                        handler.postDelayed(this, 1000);

                    }
                });
            }
        };
    }


    private int number() {
        final int[] i = {0};
        String URL = Constant.URL_ORDER_STORE+ "54"  ;
        System.out.println(URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    HashMap<String, String> storeMap = new HashMap<>();

                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<HashMap<String, String>> list2 = new ArrayList<>();
                        number = response.length();
                        System.out.println("nombre" + number);
                        System.out.println("nombreoforder " + numbreOfOrder);
                        if(numbreOfOrder < number) {
                            numbreOfOrder = number - numbreOfOrder;
                        }
                        if (numbreOfOrder != 0){
                            jsonArray = response ;
                        }
                        System.out.println("nombre of order " + numbreOfOrder);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Oups Something went wrong !");
                    }
                }
        );
        if (queuee == null) {
            queuee = Volley.newRequestQueue(getApplicationContext());
        }
        queuee.add(jsonArrayRequest);
        return number;

    }
}