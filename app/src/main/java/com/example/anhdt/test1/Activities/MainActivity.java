package com.example.anhdt.test1.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.anhdt.test1.Listeners.OnToggleClickListener;
import com.example.anhdt.test1.R;
import com.example.anhdt.test1.Receivers.MyAdminReceiver;
import com.example.anhdt.test1.Services.UnlockedDoubleTapHomeScreenService;
import com.example.anhdt.test1.Utils.PermissionUtil;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String description = "Sample Administrator description";
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;

    private static final int REQUEST_CODE_SYSTEM_ALERT_WINDOW = 1;
    private static final int REQUEST_CODE_ENABLE_ADMIN = 2;

    private static String[] SYSTEM_ALERT_WINDOW_PERMISSION = {Manifest.permission.SYSTEM_ALERT_WINDOW/*,
            Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_PHONE_STATE*/};

    ToggleButton tgbtn_double_tap_to_lock_screen, tgbtn_unlocked_quick_answer;
    Button btnEnableAdmin, btnDisableAdmin, btnLock;

    private static OnToggleClickListener mOnToggleClickListener;

    public void setOnToggleClickListener(OnToggleClickListener listener) {
        mOnToggleClickListener = listener;
    }

    public SharedPreferences mSharedPreferencesSaveStateToggleBtn;
    final String PREF_NAME = "saveStateToggleBtn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UnlockedDoubleTapHomeScreenService.updateActivity(this);

        initLayout();

        // for Unlock screen for quick answer  and  double tap on home to lock screen
        if (!UnlockedDoubleTapHomeScreenService.isCreated()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                startService(new Intent(getApplicationContext(), UnlockedDoubleTapHomeScreenService.class));
            } else {
                // granted or not will be processd on  onActivityResult method

                // for handle unlock screen
                PermissionUtil.checkAndRequestPermissions(MainActivity.this, SYSTEM_ALERT_WINDOW_PERMISSION, REQUEST_CODE_SYSTEM_ALERT_WINDOW);

                // for handle double tap to lock screen
                checkAndRequestDeviceAdminPermission();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tgbtn_unlocked_quick_answer:
                if (mOnToggleClickListener != null) {
                    boolean state = tgbtn_unlocked_quick_answer.isChecked();
                    mOnToggleClickListener.isEnabled(R.id.tgbtn_unlocked_quick_answer, state);

                    SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean("tgbtn_unlocked_quick_answer_state", state); // value to store
                    editor.commit();

                    Log.d("anhdt1", "tgbtn_unlocked_quick_answer_state = " + state);

                }
                break;
            case R.id.tgbtn_double_tap_to_lock_screen:
                if (mOnToggleClickListener != null) {
                    boolean state = tgbtn_double_tap_to_lock_screen.isChecked();
                    mOnToggleClickListener.isEnabled(R.id.tgbtn_double_tap_to_lock_screen, state);

                    SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean("tgbtn_double_tap_to_lock_screen_state", state); // value to store
                    editor.commit();

                    Log.d("anhdt1", "tgbtn_double_tap_to_lock_screen_state = " + state);

                }
                break;

            case R.id.btnLock:
                boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);
                if (isAdmin) {
                    mDevicePolicyManager.lockNow();
                } else {
                    Toast.makeText(getApplicationContext(), "Not Registered as admin", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("anhdt1", "requestCode = " + requestCode + ", resultCode = " + resultCode + ", isEnableDrawOverlays = " + isEnableDrawOverlays());
        if (requestCode == REQUEST_CODE_SYSTEM_ALERT_WINDOW && resultCode == RESULT_OK || isEnableDrawOverlays()) {
            if (!UnlockedDoubleTapHomeScreenService.isCreated()) {
                startService(new Intent(getApplicationContext(), UnlockedDoubleTapHomeScreenService.class));
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isEnableDrawOverlays() {
        return Settings.canDrawOverlays(this);
    }

    private void checkAndRequestDeviceAdminPermission() {
        if (mDevicePolicyManager != null && mDevicePolicyManager.isAdminActive(mComponentName)) {
            return;
        }

        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, MyAdminReceiver.class);

        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, description);
        startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
    }

    private void initLayout() {

        btnEnableAdmin = (Button) findViewById(R.id.btnEnable);
        btnDisableAdmin = (Button) findViewById(R.id.btnDisable);
        btnLock = (Button) findViewById(R.id.btnLock);
        tgbtn_double_tap_to_lock_screen = (ToggleButton) findViewById(R.id.tgbtn_double_tap_to_lock_screen);
        tgbtn_unlocked_quick_answer = (ToggleButton) findViewById(R.id.tgbtn_unlocked_quick_answer);

        btnEnableAdmin.setOnClickListener(this);
        btnDisableAdmin.setOnClickListener(this);
        btnLock.setOnClickListener(this);
        tgbtn_double_tap_to_lock_screen.setOnClickListener(this);
        tgbtn_unlocked_quick_answer.setOnClickListener(this);

        SharedPreferences sharedPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (sharedPrefs != null) {
            tgbtn_unlocked_quick_answer.setChecked(sharedPrefs.getBoolean("tgbtn_unlocked_quick_answer_state", false));
            tgbtn_double_tap_to_lock_screen.setChecked(sharedPrefs.getBoolean("tgbtn_double_tap_to_lock_screen_state", false));
        }
    }
}