package com.example.anhdt.test1.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anhdt.test1.R;


/**
 * Created by sev_user on 12/24/2016.
 */
public class UnlockedDoubleTapHomeScreenReceiver extends BroadcastReceiver implements View.OnClickListener {

    private WindowManager mWindowManager;
    private RelativeLayout mRelativeLayoutUnlocked;
    private LinearLayout ln0_unlocked_choice_remember_word, ln1_unlocked_answer1,
            ln2_unlocked_answer2, ln3_unlocked_answer3, ln4_unlocked_answer4;
    private TextView tv_unlocked_word, tv_unlocked_word_type,
            tv_unlocked_answer_1,
            tv_unlocked_answer_2,
            tv_unlocked_answer_3,
            tv_unlocked_answer_4;
    private CheckBox cb_unlocked_remember_word;

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (Intent.ACTION_USER_PRESENT.equalsIgnoreCase(intent.getAction())) {
            initLayout(context);
            itemClicked();
        }
    }

    @Override
    public void onClick(View view) {
        int question = 1;
        boolean isCorrected;

        switch (view.getId()) {
            case R.id.ln1_unlocked_answer1:

                isCorrected = isCorrectAnser(question, 1);
                processAnswer(isCorrected, R.id.ln1_unlocked_answer1);
                Toast.makeText(mContext, "You right!", Toast.LENGTH_SHORT).show();
                removeLayout();

                break;
            case R.id.ln2_unlocked_answer2:

                isCorrected = isCorrectAnser(question, 2);
                processAnswer(isCorrected, R.id.ln2_unlocked_answer2);

                break;
            case R.id.ln3_unlocked_answer3:

                isCorrected = isCorrectAnser(question, 3);
                processAnswer(isCorrected, R.id.ln3_unlocked_answer3);

                break;
            case R.id.ln4_unlocked_answer4:

                isCorrected = isCorrectAnser(question, 4);
                processAnswer(isCorrected, R.id.ln4_unlocked_answer4);

                break;

            case R.id.cb_unlocked_remember_word:
                if (cb_unlocked_remember_word.isChecked())
                    Toast.makeText(mContext, "Good", Toast.LENGTH_SHORT).show();
                break;

            default:
        }

    }

    private void processAnswer(boolean isCorrected, int index_choosen) {
        if (isCorrected) {
            mRelativeLayoutUnlocked.findViewById(index_choosen).setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_light));
        } else {
            mRelativeLayoutUnlocked.findViewById(index_choosen).setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_red_light));
        }
    }

    private void initLayout(Context context) {

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        mRelativeLayoutUnlocked = (RelativeLayout) layoutInflater.inflate(R.layout.unlocked_screen_layout, null);

        ln1_unlocked_answer1 = (LinearLayout) mRelativeLayoutUnlocked.findViewById(R.id.ln1_unlocked_answer1);
        ln2_unlocked_answer2 = (LinearLayout) mRelativeLayoutUnlocked.findViewById(R.id.ln2_unlocked_answer2);
        ln3_unlocked_answer3 = (LinearLayout) mRelativeLayoutUnlocked.findViewById(R.id.ln3_unlocked_answer3);
        ln4_unlocked_answer4 = (LinearLayout) mRelativeLayoutUnlocked.findViewById(R.id.ln4_unlocked_answer4);

        tv_unlocked_word = (TextView) mRelativeLayoutUnlocked.findViewById(R.id.tv_unlocked_word);
        tv_unlocked_word_type = (TextView) mRelativeLayoutUnlocked.findViewById(R.id.tv_unlocked_word_type);
        tv_unlocked_answer_1 = (TextView) mRelativeLayoutUnlocked.findViewById(R.id.tv_unlocked_answer_1);
        tv_unlocked_answer_2 = (TextView) mRelativeLayoutUnlocked.findViewById(R.id.tv_unlocked_answer_2);
        tv_unlocked_answer_3 = (TextView) mRelativeLayoutUnlocked.findViewById(R.id.tv_unlocked_answer_3);
        tv_unlocked_answer_4 = (TextView) mRelativeLayoutUnlocked.findViewById(R.id.tv_unlocked_answer_4);

        cb_unlocked_remember_word = (CheckBox) mRelativeLayoutUnlocked.findViewById(R.id.cb_unlocked_remember_word);

        mWindowManager.addView(mRelativeLayoutUnlocked, params);
    }

    private void removeLayout() {
        mWindowManager.removeView(mRelativeLayoutUnlocked);
    }

    private void itemClicked() {
        ln1_unlocked_answer1.setOnClickListener(this);
        ln2_unlocked_answer2.setOnClickListener(this);
        ln3_unlocked_answer3.setOnClickListener(this);
        ln4_unlocked_answer4.setOnClickListener(this);
        cb_unlocked_remember_word.setOnClickListener(this);
    }


    private void flipLayoutAnimation(boolean isCorrected, int i) {
        // TODO
    }

    private boolean isCorrectAnser(int question, int i) {
        // TODO
        if (question == 1) {
            if (i == 1) {
                return true;
            }
        }
        return false;
    }
}