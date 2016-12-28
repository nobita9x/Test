package com.example.anhdt.test1.Services;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.example.anhdt.test1.Activities.MainActivity;
import com.example.anhdt.test1.Listeners.OnToggleClickListener;
import com.example.anhdt.test1.R;
import com.example.anhdt.test1.Receivers.MyAdminReceiver;
import com.example.anhdt.test1.Receivers.UnlockedDoubleTapHomeScreenReceiver;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by DTA on 12/25/2016.
 */

public class UnlockedDoubleTapHomeScreenService extends Service {

    private static BroadcastReceiver mUnlockedScreenReceiver = null;
    private static BroadcastReceiver mDoubleTapHomeScreenReceiver = null;

    // check whether current service alive or not to avoid re-create
    private static UnlockedDoubleTapHomeScreenService mUnlockedDoubleTapHomeScreenService = null;

    public static boolean isCreated() {
        return mUnlockedDoubleTapHomeScreenService != null;
    }

    private static WeakReference<MainActivity> mActivityRef;

    public static void updateActivity(MainActivity activity) {
        mActivityRef = new WeakReference<>(activity);
    }

    // for double tap homescreen
    private WindowManager mWindowManager;
    FrameLayout frameLayout;
    private static long THRESHOLD_TIME_DETECT_DOUBLE_TAP = 250;
    public SharedPreferences mSharedPreferencesSaveStateToggleBtn;
    final String PREF_NAME = "saveStateToggleBtn";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("anhdt1", "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("anhdt1", "UnlockedDoubleTapHomeScreenService onCreate");

        super.onCreate();
        if (mActivityRef != null)
            mActivityRef.get().setOnToggleClickListener(mOnToggleClickListener);
        mUnlockedDoubleTapHomeScreenService = this;

        SharedPreferences sharedPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (mUnlockedScreenReceiver == null && sharedPrefs.getBoolean("tgbtn_unlocked_quick_answer_state", false)) {
            mUnlockedScreenReceiver = new UnlockedDoubleTapHomeScreenReceiver();
            registerReceiver(mUnlockedScreenReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
        }

        if (mDoubleTapHomeScreenReceiver == null && sharedPrefs.getBoolean("tgbtn_double_tap_to_lock_screen_state", false)) {
            mDoubleTapHomeScreenReceiver = new MyAdminReceiver();
            registerReceiver(mDoubleTapHomeScreenReceiver, new IntentFilter("android.app.action.DEVICE_ADMIN_ENABLED"));
            showHeader(getApplicationContext());
        }

        boolean a = sharedPrefs.getBoolean("tgbtn_unlocked_quick_answer_state", false);
        boolean b = sharedPrefs.getBoolean("tgbtn_double_tap_to_lock_screen_state", false);
        Log.d("anhdt1", "Service onCreate, mUnlockedScreenReceiver = " + mUnlockedScreenReceiver + ", toggle = " + a);
        Log.d("anhdt1", "Service onCreate, mDoubleTapHomeScreenReceiver = " + mDoubleTapHomeScreenReceiver + ", toggle = " + b);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("anhdt1", "UnlockedDoubleTapHomeScreenService onStartCommand");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("anhdt1", "UnlockedDoubleTapHomeScreenService onDestroy");

        super.onDestroy();
    }

    OnToggleClickListener mOnToggleClickListener = new OnToggleClickListener() {
        @Override
        public void isEnabled(int id, boolean isEnabled) {
            SharedPreferences sharedPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            switch (id) {
                case R.id.tgbtn_unlocked_quick_answer:
                    Log.d("anhdt1", "tgbtn_unlocked_quick_answer, isEnabled = " + isEnabled);

                    if (isEnabled) {
                        if (mUnlockedScreenReceiver == null && sharedPrefs.getBoolean("tgbtn_unlocked_quick_answer_state", false)) {
                            mUnlockedScreenReceiver = new UnlockedDoubleTapHomeScreenReceiver();
                            registerReceiver(mUnlockedScreenReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
                        }
                    } else {
                        unregisterReceiver(mUnlockedScreenReceiver);
                        mUnlockedScreenReceiver = null;
                    }
                    break;
                case R.id.tgbtn_double_tap_to_lock_screen:
                    Log.d("anhdt1", "tgbtn_double_tap_to_lock_screen, isEnabled = " + isEnabled);

                    if (isEnabled) {
                        if (mDoubleTapHomeScreenReceiver == null && sharedPrefs.getBoolean("tgbtn_double_tap_to_lock_screen_state", false)) {
                            mDoubleTapHomeScreenReceiver = new MyAdminReceiver();
                            registerReceiver(mDoubleTapHomeScreenReceiver, new IntentFilter("android.app.action.DEVICE_ADMIN_ENABLED"));
                        }
                        showHeader(getApplicationContext());
                    } else {
                        removeHeader();
                        unregisterReceiver(mDoubleTapHomeScreenReceiver);
                        mDoubleTapHomeScreenReceiver = null;
                    }
                    break;
                default:
            }
        }
    };


    private void showHeader(Context mContext) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;

        frameLayout = new FrameLayout(mContext);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.a_dot_in_home_screen, frameLayout);
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        mWindowManager.addView(frameLayout, params);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            private long lastTouchTime = -1;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("anhdt1", "onTouch, e = " + motionEvent.toString());

                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    long thistime = System.currentTimeMillis();
                    if (thistime - lastTouchTime < THRESHOLD_TIME_DETECT_DOUBLE_TAP) {
                        // is double click down
                        lastTouchTime = -1;

                        // need sure the device admin has been granted
                        if (isTopLauncher(getApplicationContext())) {
                            DevicePolicyManager mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                            mDevicePolicyManager.lockNow();
                        }
                    } else {
                        lastTouchTime = thistime;
                    }
                }
                return true;
            }
        });
    }

    private void removeHeader() {
        mWindowManager.removeView(frameLayout);
    }

    private boolean isTopLauncher(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_HOME);

        ResolveInfo currentLauncherResolveInfo = getPackageManager().resolveActivity(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
        ComponentName currentLauncherComponentName = new ComponentName(currentLauncherResolveInfo.activityInfo.applicationInfo.packageName, currentLauncherResolveInfo.activityInfo.name);
        ComponentName topActivityComponentName = activityManager.getRunningTasks(1).get(0).topActivity;
        if (currentLauncherComponentName.equals(topActivityComponentName)) {
            Log.d("anhdt1", "currentLauncherComponentName = " + currentLauncherComponentName + ", topActivityComponentName = " + topActivityComponentName);
            return true;
        }
        return false;
    }
}
