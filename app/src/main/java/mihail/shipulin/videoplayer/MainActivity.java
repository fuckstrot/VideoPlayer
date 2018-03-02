package mihail.shipulin.videoplayer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import mihail.shipulin.videoplayer.services.BackGroundService;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.d(TAG, "requestWindowFeature(Window.FEATURE_NO_TITLE)");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Log.d(TAG, "setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"createBackGroundService()" );
        Intent intent = new Intent(this, BackGroundService.class);
        startService(intent);
        videoView = (VideoView)findViewById(R.id.videoView1);
        //Creating MediaController
        //MediaController mediaController= new MediaController(this);
        // mediaController.setAnchorView(videoView);
        //specify the location of media file
        //Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/media/1.mp4");
        //Setting MediaController and URI, then starting the videoView
        //videoView.setMediaController(mediaController);
        videoView.setKeepScreenOn(true);
        //videoView.setVideoURI(uri);
        Log.d(TAG,"android.resource://" + getPackageName() + "/" + R.raw.big_buck_bunny );
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.big_buck_bunny);
        videoView.requestFocus();
        Log.d(TAG,"videoView.requestFocus()" );
        videoView.start();
        Log.d(TAG,"videoView.start()" );
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onResume()" );
        super.onResume();
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"onPause()" );
        super.onPause();
        videoView.pause();
    }

}
