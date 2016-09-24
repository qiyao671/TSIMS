package com.example.slmgroup.tsims.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.slmgroup.tsims.R;
import com.example.slmgroup.tsims.model.HttpResult;
import com.example.slmgroup.tsims.model.UserData;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {
    @Bind(R.id.ivLogo)
    ImageView ivLogo;
    private SharedPreferences sp;
    private static final String KEY_SP_USER_INFO = "userInfo";
    private static final String KEY_IS_REMEMBER_PASSWORD = "isRmbPwd";
    private static final String KEY_IS_AUTO_LOGIN = "autoLogin";
    private static final String KEY_ACCOUNT = "account";
    private static final String KEY_PASSWORD = "pwd";
    public static final String KEY_IS_SWITCH = "isSwitch";

    @Bind(R.id.etAccount)
    EditText etAccount;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.btLogin)
    Button btLogin;
    @Bind(R.id.cbRmbPwd)
    CheckBox cbRmbPwd;
    @Bind(R.id.cbAutoLogin)
    CheckBox cbAutoLogin;
    private Animation rotate;

    @Override
    protected void initData() {
        sp = this.getSharedPreferences(KEY_SP_USER_INFO, Activity.MODE_PRIVATE);
        rotate = AnimationUtils.loadAnimation(this, R.anim.anim_loading_rotate);

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_login);
        setTitle("登录");
        if (getSupportActionBar() != null && UserData.getUserNum() == null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        getIbLogin().setVisibility(View.GONE);

        etAccount.setText(sp.getString(KEY_ACCOUNT, ""));
        if (UserData.getUserNum() == null) {    //内存中无用户数据
            //若为记住密码
            if (sp.getBoolean(KEY_IS_REMEMBER_PASSWORD, false)) {
                cbRmbPwd.setChecked(true);
                etPassword.setText(sp.getString(KEY_PASSWORD, ""));
                //若自动登陆
                if (sp.getBoolean(KEY_IS_AUTO_LOGIN, false)) {
                    cbAutoLogin.setChecked(true);
                    checkLogin(etAccount.getText().toString(), etPassword.getText().toString());
                }
            }
        } else {
            boolean isSwitch = getIntent().getBooleanExtra(KEY_IS_SWITCH, false);
            if (!isSwitch) {
                startActivity(MainActivity.class);
                this.finish();
            }
        }

    }

    @Override
    protected void initEvents() {
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin(etAccount.getText().toString(), etPassword.getText().toString());
            }
        });

        cbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //当选中自动登陆时，自动选中记住密码
                if (b) {
                    cbRmbPwd.setChecked(true);
                }
            }
        });

        cbRmbPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //当取消记住密码时，自动取消选中自动登陆
                if (!b) {
                    cbAutoLogin.setChecked(false);
                }
            }
        });
    }

    private void loadFail() {
        btLogin.setText("登         录");
        btLogin.setEnabled(true);
        ivLogo.clearAnimation();
    }

    private void changeToLoading() {
        btLogin.setText("正在登陆...");
        btLogin.setEnabled(false);
        ivLogo.startAnimation(rotate);
    }

    private void login() {
        String account = etAccount.getText().toString();
        // 将用户信息保存在内存中
        UserData.setUserNum(account);
        //记住账号
        SharedPreferences.Editor edit;
        edit = sp.edit();
        edit.putString(KEY_ACCOUNT, account);

        // 记住密码
        if (cbRmbPwd.isChecked()) {
            String pwd = etPassword.getText().toString();
            edit.putString(KEY_PASSWORD, pwd);
            edit.putBoolean(KEY_IS_REMEMBER_PASSWORD, true);
        } else {    //不记住密码
            edit.putBoolean(KEY_IS_REMEMBER_PASSWORD, false);
        }

        // 保存是否自动登陆
        edit.putBoolean(KEY_IS_AUTO_LOGIN, cbAutoLogin.isChecked());

        //提交
        edit.commit();

        ivLogo.clearAnimation();

        startActivity(MainActivity.class);
        finish();
    }

    public void checkLogin(String user_num, String password) {
//        Observable observable = ApiStores.

        // 界面变为登陆中
        changeToLoading();

        Subscriber subscriber = new Subscriber<HttpResult<String>>() {

            @Override
            public void onCompleted() {
                Log.d("login", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(LoginActivity.this, "登录失败" + e.getMessage(), Toast.LENGTH_LONG).show();
                loadFail();
                Log.d("login", "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<String> httpResult) {
//                Toast.makeText(LoginActivity.this, "httpResult.getCode() = " + httpResult.getCode(), Toast.LENGTH_LONG).show();
                Log.d("login", "onNext: " + "httpResult.getCode() = " + httpResult.getCode());
                switch (httpResult.getCode()) {
                    case HttpResult.CODE_FAILED:
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                        loadFail();
                        break;
                    case HttpResult.CODE_SUCCEED:
                        login();
                        String[] timeSet = httpResult.getData().split(",");
                        UserData.setStudentStart(timeSet[0]);
                        UserData.setStudentEnd(timeSet[1]);
                        break;
                }
            }
        };
        Subscription sn = apiStores.login(user_num, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        addSubscription(sn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
