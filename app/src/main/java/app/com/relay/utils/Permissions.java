package app.com.relay.utils;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class Permissions {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 13;

    public static void askForExternalStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

    }
}
