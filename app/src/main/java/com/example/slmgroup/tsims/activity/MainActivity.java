package com.example.slmgroup.tsims.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.slmgroup.tsims.R;
import com.example.slmgroup.tsims.adapter.BookOrderListAdapter;
import com.example.slmgroup.tsims.model.BookOrderInfo;
import com.example.slmgroup.tsims.model.HttpResult;
import com.example.slmgroup.tsims.model.SubmitBookModel;
import com.example.slmgroup.tsims.model.UserData;
import com.example.slmgroup.tsims.utils.DateUtils;
import com.example.slmgroup.tsims.view.recyclerview.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.rvBookOrderList)
    RecyclerView rvBookOrderList;
    @Bind(R.id.btCancel)
    Button btCancel;
    @Bind(R.id.btOk)
    Button btOk;
    @Bind(R.id.llBottomBtn)
    LinearLayout llBottomBtn;
    @Bind(R.id.pbLoading)
    ProgressBar pbLoading;
    private BookOrderListAdapter bookOrderListAdapter;
    private List<BookOrderInfo> bookOrderList;

    // leaves test_1

    public void updateOrderList() {
        pbLoading.setVisibility(View.GONE);
        Subscriber<List<BookOrderInfo>> subscriber = new Subscriber<List<BookOrderInfo>>() {

            @Override
            public void onCompleted() {
                Log.d("login", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("login", "onError: " + e.getMessage());
                pbLoading.setVisibility(View.GONE);
                Snackbar.make(getCoordinatorLayout(), "加载数据失败", Snackbar.LENGTH_INDEFINITE)
                        .setAction("重新加载", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // 重新加载
                                updateOrderList();
                                Log.i("test", "onClick: " + view.getId());
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.primary))
                        .show();
            }

            @Override
            public void onNext(List<BookOrderInfo> bookOrderInfos) {
                bookOrderList.clear();
                bookOrderList.addAll(bookOrderInfos);
                bookOrderListAdapter.setCheckedList();
                bookOrderListAdapter.notifyDataSetChanged();
                pbLoading.setVisibility(View.GONE);

            }

        };
        Subscription sn = apiStores.getTextBookInfo("13110033140")
                .subscribeOn(Schedulers.io())
                .map(new Func1<HttpResult<List<BookOrderInfo>>, List<BookOrderInfo>>() {
                    @Override
                    public List<BookOrderInfo> call(HttpResult<List<BookOrderInfo>> listHttpResult) {
                        if (listHttpResult.getCode() == HttpResult.CODE_SUCCEED) {
                            return listHttpResult.getData();
                        } else {
                            throw new RuntimeException("获取数据失败");
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        addSubscription(sn); //???
    }

    @Override
    protected void initData() {
        bookOrderList = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        initOrder();
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_main);
        setTitle("我的订阅");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }


        //初始化适配器
        bookOrderListAdapter = new BookOrderListAdapter(this, bookOrderList);
        //设置适配器
        rvBookOrderList.setAdapter(bookOrderListAdapter);
        //设置布局管理
        rvBookOrderList.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rvBookOrderList.addItemDecoration(new DividerListItemDecoration(this, DividerListItemDecoration.VERTICAL_LIST));

    }

    @Override
    protected void initEvents() {
        //设置RecyclerView点击事件
        bookOrderListAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                // TODO: 2016/9/18 点击跳转详细信息
                BookOrderInfo bookOrderInfo = bookOrderList.get(i);
                if (bookOrderListAdapter.isEditMode()) {
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.cbIsOrdered);
                    boolean ordered = !checkBox.isChecked();
                    checkBox.setChecked(ordered);
                } else {
                    if (bookOrderInfo.getIsbn().isEmpty()) {
                        Toast.makeText(MainActivity.this, "该课程无教材", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, BookOrderInfoActivity.class);
                        intent.putExtra(BookOrderInfoActivity.KEY_BOOK_ISBN, bookOrderInfo.getIsbn());
                        startActivity(intent);
                    }
                }
            }
        });

        btCancel.setOnClickListener(this);
        btOk.setOnClickListener(this);


    }

    public void initOrder() {
        // 判断开始截止时间
        String now = DateUtils.getCurrentTime(DateUtils.FORMAT_YMDHMS);
        if (UserData.getStudentStart() == null && UserData.getStudentEnd() == null) {
            new MaterialDialog.Builder(this)
                    .content("订书系统未开放")
                    .contentColorRes(R.color.gray_d)
                    .backgroundColorRes(R.color.gray_30)
                    .positiveColorRes(R.color.pink)
                    .positiveText("确定")
                    .show();
        } else {
            if (DateUtils.isABeforeB(now, UserData.getStudentStart())) {
                new MaterialDialog.Builder(this)
                        .content("还未到订阅开始时间!")
                        .contentColorRes(R.color.gray_d)
                        .backgroundColorRes(R.color.gray_30)
                        .positiveColorRes(R.color.pink)
                        .positiveText("确定")
                        .show();
            } else {
                if (DateUtils.isABeforeB(UserData.getStudentEnd(), now)) {
                    new MaterialDialog.Builder(this)
                            .content("订阅已截止!")
                            .contentColorRes(R.color.gray_d)
                            .backgroundColorRes(R.color.gray_30)
                            .positiveColorRes(R.color.pink)
                            .positiveText("确定")
                            .show();
                } else {
                    initRightBtn();
                }
                updateOrderList();
            }
        }
    }

    public void initRightBtn() {
        setRightBtn(true, "修改", this);
    }

    private void endEdit() {
        bookOrderListAdapter.setEditMode(false);
        bookOrderListAdapter.notifyDataSetChanged();
        llBottomBtn.setVisibility(View.GONE);
        initRightBtn();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btRight:
                bookOrderListAdapter.setEditMode(true);
                bookOrderListAdapter.notifyDataSetChanged();
                llBottomBtn.setVisibility(View.VISIBLE);
                setRightBtn(false, null, null);
                break;
            case R.id.btCancel:
                //恢复保存checkbox的状态的列表至未更改前
                bookOrderListAdapter.setCheckedList();
                endEdit();
                break;
            case R.id.btOk:
                // 提交成功才更新数据
                submitBookOrderInfo();
                break;
        }
    }

    public void submitBookOrderInfo() {
        Subscriber subscriber = new Subscriber<HttpResult<String>>() {

            @Override
            public void onCompleted() {
                Log.d("submitBookOrderInfo", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "上传失败 " + e.getMessage(), Toast.LENGTH_LONG).show();
                //恢复保存checkbox的状态的列表至未更改前
                bookOrderListAdapter.setCheckedList();
                endEdit();
                Log.d("submitBookOrderInfo", "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<String> httpResult) {
                //将数据中的订阅状态更新
                pbLoading.setVisibility(View.GONE);
                bookOrderListAdapter.setDataListOrdered();
                endEdit();
                Log.d("submitBookOrderInfo", "onNext: " + "httpResult.getCode() = " + httpResult.getCode());
            }
        };
        pbLoading.setVisibility(View.VISIBLE);
        SubmitBookModel submitBookModel = new SubmitBookModel();
        submitBookModel.setUser_num(UserData.getUserNum());
        List<SubmitBookModel.TextBookInfo> textBookInfoListBeanList = new ArrayList<>();
        for (BookOrderInfo orderInfo : bookOrderList) {
            SubmitBookModel.TextBookInfo textBookInfo = new SubmitBookModel.TextBookInfo();
            textBookInfo.setIsbn(orderInfo.getIsbn());
            int index = bookOrderList.indexOf(orderInfo);
            textBookInfo.setIsSelect(Integer.parseInt(bookOrderListAdapter.getIsCheckedList().get(index)));
            textBookInfoListBeanList.add(textBookInfo);
        }
        submitBookModel.setTextBookInfoList(textBookInfoListBeanList);

        Subscription sn = apiStores.submitBookOrderInfo(submitBookModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        addSubscription(sn);
    }
}
