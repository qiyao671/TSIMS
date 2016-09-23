package com.example.slmgroup.tsims.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slmgroup.tsims.R;
import com.example.slmgroup.tsims.model.BookDetail;
import com.example.slmgroup.tsims.model.HttpResult;
import com.example.slmgroup.tsims.net.ApiClient;
import com.example.slmgroup.tsims.net.ApiStores;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class BookOrderInfoActivity extends AppCompatActivity {
    String isbn;
    public static final String KEY_BOOK_ISBN = "isbn";
    @Bind(R.id.tvIsbn)
    TextView tvIsbn;
    @Bind(R.id.tvBookName)
    TextView tvBookName;
    @Bind(R.id.tvAuthor)
    TextView tvAuthor;
    @Bind(R.id.tvPrice)
    TextView tvPrice;
    @Bind(R.id.tvPublisher)
    TextView tvPublisher;
    @Bind(R.id.tvEdition)
    TextView tvEdition;
    @Bind(R.id.pbLoading)
    ProgressBar pbLoading;
    private ApiStores apiStores = ApiClient.create(ApiStores.class);

    private void initData() {
        isbn = getIntent().getStringExtra(KEY_BOOK_ISBN);
    }

    private void initViews() {
        setContentView(R.layout.activity_book_info);
        ButterKnife.bind(this);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        pbLoading.setVisibility(View.VISIBLE);
        Subscriber subscriber = new Subscriber<BookDetail>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(BookOrderInfoActivity.this, "获取书本信息失败" + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("123", "onError: " + e.getMessage());
                BookOrderInfoActivity.this.finish();
            }

            @Override
            public void onNext(BookDetail bookDetail) {
                pbLoading.setVisibility(View.GONE);
                tvIsbn.setText(bookDetail.getIsbn());
                tvAuthor.setText(bookDetail.getAuthor());
                tvBookName.setText(bookDetail.getBook_name());
                tvEdition.setText(bookDetail.getEdition());
                tvPrice.setText(bookDetail.getPrice());
                tvPublisher.setText(bookDetail.getPublisher());
            }
        };

        String isbn = getIntent().getStringExtra(KEY_BOOK_ISBN);
        Subscription sn = apiStores.getBookDetail(isbn)
                .subscribeOn(Schedulers.io())
                .map(new Func1<HttpResult<BookDetail>, BookDetail>() {
                    @Override
                    public BookDetail call(HttpResult<BookDetail> bookDetailHttpResult) {
                        if (bookDetailHttpResult.getCode() == HttpResult.CODE_FAILED) {
                            throw new RuntimeException("获取数据失败");
                        } else {
                            return bookDetailHttpResult.getData();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
