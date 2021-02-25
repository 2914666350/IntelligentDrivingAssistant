package com.example.intelligentdrivingassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private EditText etPasswordAgain;

    private Button btnBack;

    private TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.CustomTheme);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_register_username);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        etPasswordAgain = (EditText) findViewById(R.id.et_passwordAgain);

        btnBack = (Button) findViewById(R.id.btn_first);

        tvTitle = (TextView) findViewById(R.id.tv_title);

        btnBack.setBackgroundResource(R.drawable.img_back);

        tvTitle.setText("注册");
        //实例化注册1页面的监听器对象
        Register1OnClickListener register1OnClickListener = new Register1OnClickListener();
        //为可能点击的按钮绑定监听器
        btnBack.setOnClickListener(register1OnClickListener);
    }
    class Register1OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //返回上一级的按钮被单击
            if(v.getId() == R.id.btn_first) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.back_out, R.anim.back_in);
            }
//下一步按钮被单击
            else if (v.getId() == R.id.btn_second) {
                String userName = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String passwordAgain = etPasswordAgain.getText().toString();

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
                }
            }

        }
    }
}
