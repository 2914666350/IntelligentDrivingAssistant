package com.example.intelligentdrivingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;

import android.os.Looper;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intelligentdrivingassistant.data.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText name;//用户名
    private EditText password;//用户密码
    private Button login;//登录按钮
    private TextView register;//注册
    private TextView forgetNum;//忘记密码
    private static final String TAG = "LoginActivity";
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);




        name = findViewById(R.id.admin_login_activity_name_input);
        password = findViewById(R.id.admin_login_activity_password_input);
        login = findViewById(R.id.admin_login_activity_login);
        register = findViewById(R.id.admin_login_activity_register);
        forgetNum = findViewById(R.id.admin_login_activity_forgetNum);

        //跳转到登录过的管理员界面
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithOkHttp();
            }
            private void sendRequestWithOkHttp(){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String userName = name.getText().toString();
                            String passWord = password.getText().toString();
                            Log.d(TAG, "onCreate: "+userName+","+passWord);
                            String json="{\"userName\": \"1\",\"passWord\":\"2\"}";
                            JSONObject jo=new JSONObject(json);
                            jo.put("userName",userName);
                            jo.put("passWord",passWord);
                            String loginUrl="http://172.19.66.55:8001/deptlogin/login";

                            String responseData=post(loginUrl,jo.toString());
                            Log.d(TAG, "run: "+jo.toString());
                            showLoginResultWithJSONObject(responseData);
                            Log.d(TAG, "run: "+responseData);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            private void showLoginResultWithJSONObject(String responseData) throws JSONException {
                try {

                        String login = responseData.substring(14,18);
                        String message = responseData.substring(28);
//                    for(int i=0; i<jsonArray.length(); i++){
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        String login=jsonObject.getString("status");
//                        String msg=jsonObject.getString("message");
                        if (login.equals("true")) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }

                        Log.d(TAG, "login: " + login);
                        Log.d(TAG, "msg: " + message);
//
                        Log.d(TAG, "showLoginResultWithJSONObject: pass");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        //自定义AlertDialog用于注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                LayoutInflater factory = LayoutInflater.from(LoginActivity.this);
                final View textEntryView = factory.inflate(R.layout.activity_register, null);
                builder.setTitle("用户注册");
                builder.setView(textEntryView);

                final EditText code = (EditText) textEntryView.findViewById(R.id.admin_register_info);
                final EditText name = (EditText) textEntryView.findViewById(R.id.admin_register_name);
                final EditText firstPassword = (EditText) textEntryView.findViewById(R.id.admin_register_first_password);
                final EditText secondPassword = (EditText) textEntryView.findViewById(R.id.admin_register_second_password);


                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nameInfo = name.getText().toString();
                        String firstPasswordInfo = firstPassword.getText().toString();
                        String secondPasswordInfo = secondPassword.getText().toString();
                        String codeInfo = code.getText().toString();
                        //注册码要为10086
                        if (codeInfo.equals("10086")) {


                            //检测密码是否为6个数字
                            if (firstPasswordInfo.matches("[0-9]{6}")) {
                                // 两次密码是否相同
                                if (firstPasswordInfo.equals(secondPasswordInfo)) {
                                        registerRequestWithOkHttp();
                                } else {
                                    Toast.makeText(LoginActivity.this, "两次密码不相同", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "密码为6位纯数字", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(LoginActivity.this, "注册码错误", Toast.LENGTH_SHORT).show();
                        }

                    }
                    private void registerRequestWithOkHttp(){
                        String nameInfo = name.getText().toString();
                        String firstPasswordInfo = firstPassword.getText().toString();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    OkHttpClient client=new OkHttpClient();
                                    String json="{\"userName\": \"1\",\"passWord\":\"2\"}";
                                    JSONObject jo=new JSONObject(json);
                                    jo.put("userName",nameInfo);
                                    jo.put("passWord",firstPasswordInfo);
                                    String loginUrl="http://172.19.90.191:8001/dept/add";
                                    Log.d(TAG, "run: "+nameInfo+","+firstPasswordInfo);
                                    String responseData=post(loginUrl,jo.toString());
                                    Log.d(TAG, "run: "+jo.toString());
                                    Log.d(TAG, "rigsterResponse: "+responseData);
                                    if(responseData.equals("true")){
                                        Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                                        Log.d(TAG, "msg: 注册成功");
                                    }else {
                                        Looper.prepare();
                                        Toast.makeText(LoginActivity.this,"用户名已存在",Toast.LENGTH_LONG).show();
                                        Looper.loop();
                                        Log.d(TAG, "msg: 用户名已存在");
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
//                    private void showRegisterResultWithJSONObject(String responseData){
//                        try{
//                            JSONArray jsonArray = new JSONArray(responseData);
//                            for(int i=0; i<jsonArray.length(); i++){
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                String register=jsonObject.getString("register");
//                                String msg=jsonObject.getString("msg");
//                                if(register.equals("true")){
//                                    Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_LONG).show();
//                                    Log.d(TAG, "msg: "+msg);
//                                }else {
//                                        Toast.makeText(LoginActivity.this,"用户名已存在",Toast.LENGTH_LONG).show();
//                                        Log.d(TAG, "msg: "+msg);
//                                }
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }

                });


                builder.create().show();

            }
        });

        forgetNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "此功能暂不支持", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //okhttp 发送Post请求
    String post(String url, String json) throws IOException {
        OkHttpClient client=new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}

