package com.anyonavinfo.bluetoothphone.bpclient.utils;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *Created by shijj on 2016/10/20
 */

public class HttpUtil {
    private OkHttpClient client;
    //超时时间
    public static final int TIMEOUT=1000*60;
    //json请求
    public static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");

    private Handler handler = new Handler(Looper.getMainLooper());

    public HttpUtil() {
        this.init();
    }

    /**
     * 初始化OKhttp
     */
    private void init() {
        client = new OkHttpClient();
        //设置超时
        client.newBuilder().connectTimeout(TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(TIMEOUT,TimeUnit.SECONDS).readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }
    /**
     * post请求  json数据为body
     *
     */
    public void postJson(String url,String json,final HttpCallBack callBack){
        RequestBody body = RequestBody.create(JSON,json);
        final Request request = new Request.Builder().url(url).post(body).build();

        OnStart(callBack);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    onSuccess(callBack, response.body().string());
                } else {
                    OnError(callBack, response.message());
                }
            }
        });
    }

    /**
     * post请求  map是body
     *
     * @param url
     * @param map
     * @param callBack
     */
    public void postMap(String url, Map<String,String> map, final HttpCallBack callBack){
        FormBody.Builder builder=new FormBody.Builder();

        //遍历map
        if(map!=null){
            for (Map.Entry<String,String> entry : map.entrySet()){
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        OnStart(callBack);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack,e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    onSuccess(callBack,response.body().string());
                }else{
                    OnError(callBack, response.message());
                }
            }
        });
    }

    /**
     * get 请求
     *
     * @param url
     * @param callBack
     */
    public void getJson(String url,String key,final HttpCallBack callBack){
        Request request = new Request.Builder().url(url).addHeader("apikey",key).build();
        OnStart(callBack);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack,e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    onSuccess(callBack,response.body().string());
                }else{
                    OnError(callBack,response.message());
                }
            }
        });
    }


    public void OnStart(HttpCallBack callBack){
        if(callBack!=null){
            callBack.onstart();
        }
    }
    public void onSuccess(final HttpCallBack callBack,final String data){
        if(callBack!=null){
            handler.post(new Runnable() {
                @Override
                public void run() {//在主线程操作
                    callBack.onSusscess(data);
                }
            });
        }
    }
    public void OnError(final HttpCallBack callBack,final String msg){
        if(callBack!=null){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onError(msg);
                }
            });
        }
    }


    public static abstract class HttpCallBack{
        //开始
        public void onstart(){};
        //成功回调
        public abstract void onSusscess(String data);
        //失败
        public void onError(String msg){};
    }

}
