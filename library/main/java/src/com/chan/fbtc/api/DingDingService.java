package com.chan.fbtc.api;

import com.chan.fbtc.bean.MarkdownMessage;
import com.chan.fbtc.bean.TextMessage;
import com.chan.fbtc.preference.Preference;
import com.google.gson.JsonElement;
import com.squareup.okhttp.OkHttpClient;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;

/**
 * Created by chan on 2017/9/7.
 */
public class DingDingService {

    private static DingDingService sDingDingService;

    private DingDingApi mDingDingApi;

    private DingDingService() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit dingDingRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("https://oapi.dingtalk.com")
                .build();
        mDingDingApi = dingDingRetrofit.create(DingDingApi.class);
    }

    public Observable<JsonElement> sendMessage(TextMessage textMessage) {
        return mDingDingApi.sendMessage(Preference.getInstance().getString("DING_WEB_HOOK"), textMessage);
    }

    public  Observable<JsonElement> sendMessage(MarkdownMessage markdownMessage) {
        return mDingDingApi.sendMessage(Preference.getInstance().getString("DING_WEB_HOOK"), markdownMessage);
    }

    public static DingDingService getInstance() {
        if (sDingDingService == null) {
            synchronized (DingDingService.class) {
                if (sDingDingService == null) {
                    sDingDingService = new DingDingService();
                }
            }
        }
        return sDingDingService;
    }
}
