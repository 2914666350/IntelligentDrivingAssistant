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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);




        name = (EditText) findViewById(R.id.admin_login_activity_name_input);
        password = (EditText) findViewById(R.id.admin_login_activity_password_input);
        login = (Button) findViewById(R.id.admin_login_activity_login);
        register = (TextView) findViewById(R.id.admin_login_activity_register);
        forgetNum = (TextView) findViewById(R.id.admin_login_activity_forgetNum);
        String userName = name.getText().toString().trim();
        String passWord = password.getText().toString().trim();
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
                            OkHttpClient client=new OkHttpClient();
                            RequestBody requestBody =new FormBody.Builder()
                                    .add("username",userName)
                                    .add("password",passWord)
                                    .build();
                            Request request=new Request.Builder()
                                    .url("http://localhost:8001/login")
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String responseData =response.body().string();
                            showLoginResultWithJSONObject(responseData);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            private void showLoginResultWithJSONObject(String responseData) throws JSONException {
                try{
                    JSONArray jsonArray = new JSONArray(responseData);
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String login=jsonObject.getString("login");
                        String msg=jsonObject.getString("msg");
                        if(login.equals("true")){
                            Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_LONG).show();
                        }
                        Log.d(TAG, "login: "+login);
                        Log.d(TAG, "msg: "+msg);
                    }
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
                                    RequestBody requestBody =new FormBody.Builder()
                                            .add("username",nameInfo)
                                            .add("password",firstPasswordInfo)
                                            .build();
                                    Request request=new Request.Builder()
                                            .url("http://localhost:8001/register")
                                            .post(requestBody)
                                            .build();
                                    Response response = client.newCall(request).execute();
                                    String responseData =response.body().string();
                                    showRegisterResultWithJSONObject(responseData);
                                    Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "register:"+responseData);

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    private void showRegisterResultWithJSONObject(String responseData){
                        try{
                            JSONArray jsonArray = new JSONArray(responseData);
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String register=jsonObject.getString("register");
                                String msg=jsonObject.getString("msg");
                                if(register.equals("true")){
                                    Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "msg: "+msg);
                                }else {
                                    if(msg.equals("notOnly")){
                                        Toast.makeText(LoginActivity.this,"用户名已存在",Toast.LENGTH_LONG).show();
                                        Log.d(TAG, "msg: "+msg);
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

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
}

