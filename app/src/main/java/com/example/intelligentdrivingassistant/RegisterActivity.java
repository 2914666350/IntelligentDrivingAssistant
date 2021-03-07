package com.example.intelligentdrivingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText regUsername;
    private EditText regPassword;
    private EditText regPasswordAgain;

    private Button register;
    private ImageButton btnBack;
//    private TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(R.style.CustomTheme);
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_register);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        regUsername = (EditText) findViewById(R.id.et_username);
        regPassword = (EditText) findViewById(R.id.et_password);
        regPasswordAgain = (EditText) findViewById(R.id.reg_password_again);
        btnBack =  findViewById(R.id.btn_back);
        //实例化注册1页面的监听器对象
        BackOnClickListener backOnClickListener = new BackOnClickListener();
        //为可能点击的按钮绑定监听器
        btnBack.setOnClickListener(backOnClickListener);
        register=findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                {
                    String userName = regUsername.getText().toString();
                    String password = regPassword.getText().toString();
                    String passwordAgain = regPasswordAgain.getText().toString();

                    if(userName.equals("")) { //如果用户名为空
                        Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_LONG).show();
                    }
                    else if(password.equals("")) { //如果密码为空
                        Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_LONG).show();
                    }
                    else if(passwordAgain.equals("")) { //如果确认密码为空
                        Toast.makeText(getApplicationContext(), "确认密码不能为空", Toast.LENGTH_LONG).show();
                    }
                    else if(!password.equals(passwordAgain)) { //如果两次输入的密码不相同
                        Toast.makeText(getApplicationContext(), "两次输入密码必须相同", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"注册成功,请返回重新登录！",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

//        tvTitle = (TextView) findViewById(R.id.tv_title);

//        btnBack.setBackgroundResource(R.drawable.img_back);

//        tvTitle.setText("注册");

    }
    class BackOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //返回上一级的按钮被单击
            if(v.getId() == R.id.btn_back) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.back_out, R.anim.back_in);
            }

        }
    }

}
