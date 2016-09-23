package com.example.slmgroup.tsims.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String PRODUCT_URL = "http://10.1.4.137:8002/api/tsims/";
    public static final String DEBUG_URL = "http://172.23.26.3/api/tsims/";
    private static final int DEFAULT_TIMEOUT = 10;
    // private Retrofit retrofit;

/*    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final ApiClient INSTANCE = new ApiClient();
    }

    //获取单例
    public static final ApiClient getInstance() {
        return SingletonHolder.INSTANCE;
    }*/

    public static <S> S create(Class<S> serviceClass){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .serializeNulls()
                .create();
        //OKhttp的日志系统
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //手动创建一个OkHttpClient并设置超时时间，网络判断拦截器、日志拦截器
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor);
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .baseUrl(PRODUCT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }
 /*   public Retrofit getRetrofit() {
        return retrofit;
    }*/
}