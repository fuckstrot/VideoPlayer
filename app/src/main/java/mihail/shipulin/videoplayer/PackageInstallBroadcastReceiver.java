package mihail.shipulin.videoplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PackageInstallBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "PackageInstalled";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = null;
        if (intent != null) {
            action = intent.getAction();
        }

        if (action != null && Intent.ACTION_PACKAGE_ADDED.equals(action)) {
            String dataString = intent.getDataString();
            Log.d(TAG, "dataString: "+dataString);
            if (dataString != null && dataString.equals("mihail.shipulin.videoplayer")) {
//                Intent i = new Intent(this, MainActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                backGroundService.startActivity(i);
                  Log.i(TAG, "PackageInstalled");
            }
        }
    }
}
