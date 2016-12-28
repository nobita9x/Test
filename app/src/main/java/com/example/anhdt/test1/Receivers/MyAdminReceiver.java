package com.example.anhdt.test1.Receivers;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.anhdt.test1.R;

/**
 * Created by sev_user on 12/24/2016.
 */
public class MyAdminReceiver extends DeviceAdminReceiver {
//
//    private WindowManager mWindowManager;
//    FrameLayout frameLayout;
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Log.d("anhdt1", "intent.getAction() = " + intent.getAction());
//        if ("android.app.action.DEVICE_ADMIN_ENABLED".equalsIgnoreCase(intent.getAction())) {
//            initLayout(context);
//        }
//    }
//
//    private void initLayout(Context mContext) {
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                PixelFormat.TRANSLUCENT);
//        params.gravity = Gravity.LEFT;
//
//        frameLayout = new FrameLayout(mContext);
//
//        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        mWindowManager.addView(frameLayout, params);
//
//        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        layoutInflater.inflate(R.layout.a_dot_in_home_screen, frameLayout);
//    }
}
