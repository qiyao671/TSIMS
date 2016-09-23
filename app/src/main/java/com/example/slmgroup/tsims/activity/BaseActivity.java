package com.example.slmgroup.tsims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.slmgroup.tsims.R;
import com.example.slmgroup.tsims.model.UserData;
import com.example.slmgroup.tsims.net.ApiClient;
import com.example.slmgroup.tsims.net.ApiStores;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends AppCompatActivity {
    protected static ApiStores apiStores;
    private TextView tvTitle;
    private Toolbar toolbar;
    private LinearLayout content;
    private Button btRight;
    private CoordinatorLayout container;
    private CompositeSubscription mCompositeSubscription;
    private ImageButton ibLogin;


    @Override
    public void setContentView(int layoutResID) {
        //super.setContentView(layoutResID);
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(layoutResID, content);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        apiStores = ApiClient.create(ApiStores.class);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        content = (LinearLayout) findViewById(R.id.content);
        btRight = (Button) findViewById(R.id.btRight);
        container=(CoordinatorLayout)findViewById(R.id.container);
        ibLogin = (ImageButton) findViewById(R.id.ibUser);
        ibLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasLogin = UserData.getUserNum() != null;
                new MaterialDialog.Builder(BaseActivity.this)
                            .content(hasLogin ? "当前登录的用户为：" + UserData.getUserNum() : "当前未登录，是否需要登录？")
                            .contentColorRes(R.color.gray_d)
                            .backgroundColorRes(R.color.gray_30)
                            .positiveColorRes(R.color.pink)
                            .positiveText(hasLogin ? "切换用户" : "登录")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    startActivity(LoginActivity.class);
                                }
                            })
                            .negativeText("取消")
                            .show();

            }
        });

        initToolbar();
        initData();
        initViews();
        initEvents();
    }

    protected abstract void initData();
    protected abstract void initViews();
    protected abstract void initEvents();

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void setTitle(String value) {
        tvTitle.setText(value);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mCompositeSubscription!=null)
            //页面stop()时将订阅关系取消，释放引用，防止内存泄露
            mCompositeSubscription.unsubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(BaseActivity.this);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    public void setRightBtn(boolean visibility, String value, View.OnClickListener onClickListener) {
        if (visibility) {
            if (value != null && onClickListener != null) {
                btRight.setVisibility(View.VISIBLE);
                btRight.setText(value);
                btRight.setOnClickListener(onClickListener);
            }
        } else {
            btRight.setVisibility(View.GONE);
            btRight.setText("");
            btRight.setOnClickListener(null);
        }
    }


    public ImageButton getIbLogin() {
        return ibLogin;
    }


    public CoordinatorLayout getCoordinatorLayout(){
        return container;
    }


}
