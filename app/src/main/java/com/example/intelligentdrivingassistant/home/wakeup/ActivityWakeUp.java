package com.example.intelligentdrivingassistant.home.wakeup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.intelligentdrivingassistant.R;
import com.baidu.aip.asrwakeup3.core.recog.IStatus;
import com.baidu.aip.asrwakeup3.core.wakeup.MyWakeup;
import com.baidu.aip.asrwakeup3.core.wakeup.listener.IWakeupListener;
import com.baidu.aip.asrwakeup3.core.wakeup.listener.RecogWakeupListener;
import com.example.intelligentdrivingassistant.find.FindActivity;
import com.example.intelligentdrivingassistant.home.ActivityCommon;
import com.baidu.speech.asr.SpeechConstant;
import com.example.intelligentdrivingassistant.music.MusicFragment;
import com.example.intelligentdrivingassistant.navigation.NavigationFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * 集成文档： http://ai.baidu.com/docs#/ASR-Android-SDK/top 集成指南一节
 * 唤醒词功能
 */
public class ActivityWakeUp extends ActivityCommon implements IStatus {

    protected MyWakeup myWakeup;
    private int status = STATUS_NONE;
    private static final String TAG = "ActivityWakeUp";

    public ActivityWakeUp() {
      this(R.raw.normal_wakeup);
    }

    public ActivityWakeUp(int textId) {
        super(textId, R.layout.common_without_setting);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改为 SimpleWakeupListener 后，不依赖handler，但将不会在UI界面上显示
        // 基于DEMO唤醒词集成第1.1, 1.2, 1.3步骤
        IWakeupListener listener = new RecogWakeupListener(handler);
        myWakeup = new MyWakeup(this, listener);
    }

    // 点击“开始识别”按钮
    // 基于DEMO唤醒词集成第2.1, 2.2 发送开始事件开始唤醒
    private void start() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下

        // params.put(SpeechConstant.ACCEPT_AUDIO_DATA,true);
        // params.put(SpeechConstant.IN_FILE,"res:///com/baidu/android/voicedemo/wakeup.pcm");
        // params里 "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        myWakeup.start(params);
    }

    // 基于DEMO唤醒词集成第4.1 发送停止事件
    protected void stop() {
        myWakeup.stop();
    }

    @Override
    protected void initView() {
        super.initView();
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (status) {
                    case STATUS_NONE:
                        start();
                        status = STATUS_WAITING_READY;
                        updateBtnTextByStatus();
                        txtLog.setText("");
                        txtResult.setText("");
                        break;
                    case STATUS_WAITING_READY:
                        stop();
                        status = STATUS_NONE;
                        updateBtnTextByStatus();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
//        switch (msg.what) { // 处理MessageStatusRecogListener中的状态回调
//            case STATUS_FINISHED:
//                if (msg.arg2 == 1) {
//                    txtResult.setText(msg.obj.toString());
//                    String s=txtResult.getText().toString();
//                    if(s.substring(10,19).equals("小杜你好，打开音乐")){
//                        skip(MusicFragment.class);
//                        Log.d(TAG, "music: 打开音乐");
//                    }else if(s.substring(10,19).equals("小杜你好，打开导航")){
//                        skip(NavigationFragment.class);
//                    }else if (s.substring(10,19).equals("小杜你好，打开新闻")){
//                        skip(FindActivity.class);
//                    }
//                }
//                break;
//            case STATUS_NONE:
//            case STATUS_READY:
//            case STATUS_SPEAKING:
//            default:
//                break;
//        }
    }
    public  void skip(Class s){
        Intent intent = new Intent();
        intent.setClass(this, s);
        startActivity(intent);
    }

    private void updateBtnTextByStatus() {
        switch (status) {
            case STATUS_NONE:
                btn.setText("启动唤醒");
                break;
            case STATUS_WAITING_READY:
                btn.setText("停止唤醒");
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        // 基于DEMO唤醒词集成第5 退出事件管理器
        myWakeup.release();
        super.onDestroy();
    }
}
