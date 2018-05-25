package mihail.shipulin.videoplayer;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BackGroundService extends Service {
    private NotificationManager mNotificationManager;
    private static final String TAG = "BackGroundService";
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private RequestQueue httpRequestQueue;
    private String WS_URL ="http://dp1app:50200/irj/portal";


    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        // Instantiate the RequestQueue.

        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage");
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            long endTime = System.currentTimeMillis() + 30*1000;
            Log.i(TAG, "endTime "+endTime);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, WS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Log.d(TAG, "response "+response.substring(0,500));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //mTextView.setText("That didn't work!");
                            //sendMessage("error: "+error.getMessage());//TODO
//                            if (error instanceof NetworkError) {
//                            } else if (error instanceof ServerError) {
//                            } else if (error instanceof AuthFailureError) {
//                            } else if (error instanceof ParseError) {
//                            } else if (error instanceof NoConnectionError) {
//                            } else if (error instanceof TimeoutError) {
//                                Toast.makeText(getContext(),
//                                        "Oops. Timeout error!",
//                                        Toast.LENGTH_LONG).show();
//                            }
                            Log.e(TAG, "send http onResponse ", error);
                        }
            });
            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            while (true) {
                synchronized (this) {
                    try {
                        wait(10000);
                        Log.i(TAG, "send http request "+System.currentTimeMillis());
                        // Add the request to the RequestQueue.
                        httpRequestQueue.add(stringRequest);
                        Log.i(TAG,"app is in background?: " +isAppIsInBackground(BackGroundService.this));
                        if(isAppIsInBackground(BackGroundService.this)){
                            Log.i(TAG,"app is in background or stopped");
                            bringAppToLive(BackGroundService.this);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "send http request error: "+e.getMessage());
                    }
                }
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg1);
        }
    }
    /*Запускаем приложение*/
    public static void bringAppToLive(BackGroundService backGroundService) {
        Intent i = new Intent(backGroundService, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        backGroundService.startActivity(i);
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        httpRequestQueue = Volley.newRequestQueue(this);
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    private void sendMessage(String msg) {
        // The string "my-integer" will be used to filer the intent
        Intent intent = new Intent("http-tick");
        // Adding some data
        intent.putExtra("message", msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_LONG).show();
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        //msg.arg2 = false;
        mServiceHandler.sendMessage(msg);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }
    public boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
}
