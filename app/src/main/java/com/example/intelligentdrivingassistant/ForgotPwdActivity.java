package com.example.intelligentdrivingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*
 *@ClassName:ForgetPwdActivity
 * @Description:找回密码
 *  */
public class ForgotPwdActivity extends AppCompatActivity {
    private EditText etPhoneNumber;
    private EditText etNewPassword;
    private EditText etCaptcha;
    private Button btnVerify;
    private Button btnFinish;
//    private ButtonTimeCount btnTimeCount;

    private Button btnBack;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(R.style.CustomTheme);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_forgot_pwd);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
//
//        etPhoneNumber = (EditText) findViewById(R.id.et_phoneNumber);
//        etNewPassword = (EditText) findViewById(R.id.et_newPassword);
//        etCaptcha = (EditText) findViewById(R.id.et_captcha);
//        btnVerify = (Button) findViewById(R.id.btn_verify);
//        btnFinish = (Button) findViewById(R.id.btn_finish);
//        btnBack = (Button) findViewById(R.id.btn_first);
//        tvTitle = (TextView) findViewById(R.id.tv_title);
//
//        btnBack.setBackgroundResource(R.drawable.img_back);
//        tvTitle.setText(R.string.find_password);
//        btnTimeCount = new ButtonTimeCount(60000, 1000);
//
//        btnBack.setOnClickListener(new ButtonBackClickListener());
//        btnVerify.setOnClickListener(new ButtonVerifyClickListener());
//    }
//
//    /**
//     * @ClassName:ButtonBackClickListener
//     * @Description:返回到登录界面
//     */
//    class ButtonBackClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(ForgetPwdActivity.this, LoginActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.back_out, R.anim.back_in);
//        }
//    }
//
//    /**
//     * @author yan
//     * @ClassName:ButtonVerifyClickListener
//     * @Description:获取验证码的按钮监听器
//     */
//    class ButtonVerifyClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            btnTimeCount.start();
//        }
//    }
//
//    /**
//     * @author yan
//     * @ClassName:ButtonTimeCount
//     * @Description:找回密码时验证手机号码
//     */
//    class ButtonTimeCount extends CountDownTimer {
//
//        /**
//         * @param millisInFuture    总时长
//         * @param countDownInterval 计时的时间间隔
//         */
//        public ButtonTimeCount(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);
//        }
//
//        /**
//         * 计时完成时触发
//         */
//        @Override
//        public void onFinish() {
//            btnVerify.setText(R.string.verify_phone);
//            btnVerify.setClickable(true);
//        }
//        /**
//         * 计时过程显示
//         */
//        @Override
//        public void onTick(long millisUntilFinished) {
//            btnVerify.setClickable(false);
//            btnVerify.setTextColor(Color.WHITE);
//            btnVerify.setText((millisUntilFinished / 1000) + "秒");
//        }
    }
}

