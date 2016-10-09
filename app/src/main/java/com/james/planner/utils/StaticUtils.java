package com.james.planner.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;

public class StaticUtils {

    public static float getPixelsFromDp(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int getRelativeLeft(View view) {
        ViewParent parent = view.getParent();
        if (parent == view.getRootView()) return view.getLeft();
        else if (parent instanceof View)
            return view.getLeft() + getRelativeLeft((View) view.getParent());
        else return 0;
    }

    public static int getRelativeTop(View view) {
        ViewParent parent = view.getParent();
        if (view.getParent() == view.getRootView()) return view.getTop();
        else if (parent instanceof View)
            return view.getTop() + getRelativeTop((View) view.getParent());
        else return 0;
    }

    public static boolean isPermissionsGranted(Context context) {
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        if (info.requestedPermissions != null) {
            for (String permission : info.requestedPermissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.wtf("Permission", permission);
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isPermissionsGranted(Activity activity, boolean shouldRequestPermissions, int requestId) {
        PackageInfo info;
        try {
            info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        if (info.requestedPermissions != null) {
            List<String> unrequestedPermissions = new ArrayList<>();
            for (String permission : info.requestedPermissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.wtf("Permission", permission);
                    if (!permission.matches(Manifest.permission.SYSTEM_ALERT_WINDOW) && !permission.matches(Manifest.permission.GET_TASKS))
                        unrequestedPermissions.add(permission);
                }
            }

            if (shouldRequestPermissions) {
                ActivityCompat.requestPermissions(activity, unrequestedPermissions.toArray(new String[unrequestedPermissions.size()]), requestId);
            }
        }

        return isPermissionsGranted(activity);
    }
}
