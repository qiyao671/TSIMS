package com.example.slmgroup.tsims.net;

import com.example.slmgroup.tsims.model.BookDetail;
import com.example.slmgroup.tsims.model.BookOrderInfo;
import com.example.slmgroup.tsims.model.HttpResult;
import com.example.slmgroup.tsims.model.SubmitBookModel;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lvqiyao (amorfatilay@163.com).
 * 2016/9/19 19:31.
 * 类描述：
 */
public interface ApiStores {
    @FormUrlEncoded
    @POST("login")
    Observable<HttpResult<String>> login(@Field("user_num") String account, @Field("password") String password);

    // 获取课程及教材信息
    @GET("getTextBookInfo")
    Observable<HttpResult<List<BookOrderInfo>>> getTextBookInfo(@Query("user_num") String user_num);

    // 获取教材详细信息
    @GET("getBookDetail")
    Observable<HttpResult<BookDetail>> getBookDetail(@Query("isbn") String isbn);

    // 提交教材选订信息
    @POST("submitBookOrderInfo")
    Observable<HttpResult<String>> submitBookOrderInfo(@Body SubmitBookModel submitBookModel);


}
