package app.com.relay.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import app.com.relay.R;
import app.com.relay.service.UserService;
import app.com.relay.utils.Constant;
import app.com.relay.utils.DatabaseHelper;

public class LoginActivity extends AppCompatActivity{

    private TextInputEditText mailTxt, passwordTxt;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponent();
    }

    private void initComponent() {
        userService = new UserService(this);
        mailTxt = (TextInputEditText) findViewById(R.id.txt_mail);
        passwordTxt = (TextInputEditText) findViewById(R.id.txt_password);

    }
    public void clickAction(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_sign_in:
                if (!mailTxt.getText().toString().equals("") &&
                        !passwordTxt.getText().toString().equals("")) {
                    String URL = Constant.URL_USERS+ "user/" + mailTxt.getText() + "/" + passwordTxt.getText();
                    System.out.println(URL);
                    userService.tryToLogin(URL);
                }
                break;
            case R.id.txt_sign_up:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

}
