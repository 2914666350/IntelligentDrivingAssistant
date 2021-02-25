package com.example.intelligentdrivingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText et_username, et_password;
    private Button btn_login;
    private TextView tv_forgot_pwd,tv_newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_forgot_pwd = (TextView) findViewById(R.id.tv_forgot_pwd);
        tv_newUser = (TextView) findViewById(R.id.tv_newUser);
        LoginOnClickListener loginOnClickListener = new LoginOnClickListener();
        btn_login.setOnClickListener(loginOnClickListener);
    }

    private class LoginOnClickListener implements View.OnClickListener {
        Handler handler=new Handler(){
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        break;
                }

            };
        };
        @Override
        public void onClick(View v) {
            //控件被点击后执行的代码
//            if(v.getId() == R.id.btn_login) { //返回上一级的按钮被单击
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
////                overridePendingTransition(R.anim.down_out, R.anim.down_in);
//            }
             if(v.getId() == R.id.tv_newUser) { //用户注册的按钮或新用户的文本域被单击
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.go_in, R.anim.go_out);
            }
            else if(v.getId() == R.id.tv_forgot_pwd) { //忘记密码的文本域被单击
                Intent intent = new Intent(LoginActivity.this, ForgotPwdActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.go_in, R.anim.go_out);
            }
            else if(v.getId() == R.id.btn_login) { //用户登录的按钮被单击
                final String userName = et_username.getText().toString().trim();
                final String password = et_password.getText().toString().trim();
                if (userName.equals("") || password.equals("")) { //用户名或者密码未填写
                    Toast.makeText(getApplicationContext(), "请将用户名密码填写完全后再登录", Toast.LENGTH_LONG).show();
                } else {
                    //用户名密码均已正确填写
                    //模拟登录过程
                    final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                    pd.setMessage("正在登录...");
                    pd.show();
                    new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                handler.sendEmptyMessage(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            pd.dismiss();
                        } ;
                    }.start();
                /*new Thread() {
						public void run() {
							final String result = LoginService.loginByClientPost(userName, password);
							if(result != null) {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
	Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
	}
	});
	Intent intent = new Intent(LoginActivity.this, MainTabActivity.class);
	startActivity(intent);
							}
	else{//请求失败
	runOnUiThread(new Runnable() {
									@Override
									public void run() {
Toast.makeText(LoginActivity.this, "请求超时...", Toast.LENGTH_LONG).show();
									}
	});
	}
	};
	}.start();*/
//通过android-async-http-1.4.6框架完成与服务器端的交互
                    //1.GET提交方式
					/*AsyncHttpClient client = new AsyncHttpClient();
					String path = "http://172.21.106.59:8080/CoffeeOnlineServer/servlet/LoginServlet?userName="
							+ URLEncoder.encode(userName) + "&password=" + URLEncoder.encode(password);
					client.get(path, new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] response) {
							Toast.makeText(LoginActivity.this, "请求成功:"+new String(response), Toast.LENGTH_LONG).show();
						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] response, Throwable arg3) {
							Toast.makeText(LoginActivity.this, "请求失败:"+new String(response), Toast.LENGTH_LONG).show();
						}
					});*/
                    //2.POST提交方式
					/*AsyncHttpClient client = new AsyncHttpClient();
					String url = IPProvider.IP_ADDRESS+"CoffeeOnlineServer/servlet/LoginServlet";
					RequestParams params = new RequestParams();
					params.put("userName", userName);
					params.put("password", password);
					client.post(url, params, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] response) {
							Toast.makeText(LoginActivity.this, "请求成功:"+new String(response), Toast.LENGTH_LONG).show();
						}
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							Toast.makeText(LoginActivity.this, "请求失败:"+arg3.getMessage(), Toast.LENGTH_LONG).show();
						}
					});*/
                }
            }
        }
    }
}

