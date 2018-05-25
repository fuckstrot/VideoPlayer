package mihail.shipulin.videoplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static String RES_PREF = "android.resource://";
    private BroadcastReceiver mMessageReceiver;
    private VideoView videoView;
    private List<String> playList;
    private volatile int currentVideo;
    private static volatile boolean isSopped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        RES_PREF = "android.resource://"+getPackageName()+ "/";
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        currentVideo = 32;
        playList = populatePlayList();
        Log.d(TAG, "requestWindowFeature(Window.FEATURE_NO_TITLE)");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Log.d(TAG, "setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"createBackGroundService()" );
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("message");
                Log.d(TAG, msg);
            }
        };
        Intent intent = new Intent(this, BackGroundService.class);
        startService(intent);
        //startForegroundService(intent) 26 api
        videoView = findViewById(R.id.videoView1);
        videoView.setKeepScreenOn(true);
        //Log.d(TAG,"android.resource://" + getPackageName() + "/" + R.raw.big_buck_bunny );
        videoView.setVideoPath(RES_PREF + R.raw.v32);
        videoView.requestFocus();
        //videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            };
//        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()  {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "currentVideo: "+currentVideo);
                Log.d(TAG, "playList.size(): "+playList.size());
                Toast.makeText(MainActivity.this, "index: "+currentVideo, Toast.LENGTH_LONG).show();
                currentVideo++;
                if(currentVideo>=playList.size())currentVideo = 0;
                videoView.stopPlayback();
                videoView.setVideoPath(playList.get(currentVideo));
                videoView.start();
            }
        });

        Log.d(TAG,"videoView.requestFocus()" );
        videoView.start();
        Log.d(TAG,"videoView.start()" );
    }
    private synchronized int getCurrentPlaylistPosition(List<String> playList){
        int result = 0;
        if(playList!=null && !playList.isEmpty()){

        }
        return result;
    }
    private List<String> populatePlayList() {
        List<String> playList = new LinkedList<>();
        playList.add(RES_PREF + R.raw.v32);
        return playList;
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onResume()" );
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("http-tick"));
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"onPause()" );
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG,"onPause()" );
        super.onStop();

    }
    private static synchronized boolean isActivityStopped(){
        return isSopped;
    }
    private static synchronized boolean setActivityStopped(){
        return isSopped;
    }
}
