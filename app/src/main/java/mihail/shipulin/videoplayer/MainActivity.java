package mihail.shipulin.videoplayer;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity {
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
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
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.big_buck_bunny);
        videoView.requestFocus();
        videoView.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }
//    }
}
