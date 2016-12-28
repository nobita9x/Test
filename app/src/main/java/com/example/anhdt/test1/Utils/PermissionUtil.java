package com.example.anhdt.test1.Utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by DTA on 12/25/2016.
 */

public class PermissionUtil {

    private static List<String> listPermissionsNeeded = new ArrayList<>();
    private static int isPermissionGranted = PackageManager.PERMISSION_GRANTED;

    // This function for the permission which has defined in Manifest ramresource
    public static boolean checkAndRequestPermissions(Activity activity, String[] permissions, int REQUEST_ID_PERMISSIONS) {
        for (String permission : permissions) {
            int a = ContextCompat.checkSelfPermission(activity, permission);
            if (ContextCompat.checkSelfPermission(activity, permission) != isPermissionGranted)
                listPermissionsNeeded.add(permission);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_PERMISSIONS);
            return false;
        }
        return true;
    }

    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != isPermissionGranted) {
                return false;
            }
        }
        return true;
    }

    private static void requestPermissions(Activity activity, String[] permissions, int REQUEST_ID_PERMISSIONS) {
        if (isNeedGoToSetting(activity, permissions)) {

            if (enabledDrawOverlays(activity.getApplicationContext()))
                return;

            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
            ActivityCompat.startActivityForResult(activity, intent, REQUEST_ID_PERMISSIONS, null);

        } else {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_ID_PERMISSIONS);
        }
    }

    private static boolean isNeedGoToSetting(Activity activity, String[] permissions) {
        if (Arrays.asList(permissions).contains(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            return true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static boolean enabledDrawOverlays(Context context) {
        return Settings.canDrawOverlays(context);
    }
}
