package com.chan.fbtc.api;

import com.chan.fbtc.bean.BTCMarket;
import com.chan.fbtc.bean.ETHMarket;
import com.chan.fbtc.preference.Preference;
import com.chan.fbtc.utils.HuoBiUtils;
import com.squareup.okhttp.OkHttpClient;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;

import java.util.Map;

/**
 * Created by chan on 2017/9/8.
 */
public class HuoBiApiService {

    private static HuoBiApiService sHuoBiApiService;
    private HuoBiApi mHuoBiApi;

    private HuoBiApiService() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit dingDingRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://api.huobi.com")
                .build();
        mHuoBiApi = dingDingRetrofit.create(HuoBiApi.class);
    }

    public Observable<BTCMarket> fetchBTCMarket() {
        return mHuoBiApi.fetchBTCMarket();
    }

    public Observable<ETHMarket> fetchETHMarket() {
        Preference preference = Preference.getInstance();
        Map<String, String> query = HuoBiUtils.signature(
                preference.getString("HUO_BI_ACCESS_KEY"),
                preference.getString("HUO_BI_SECRET_KEY"),
                "GET",
                HuoBiApi.HOST,
                "market/depth"
        );

        return mHuoBiApi.fetchMarkDepth("https://be.huobi.com/market/depth", HuoBiApi.ETH_2_CNY, HuoBiApi.DEPTH_1, query);
    }

    public static HuoBiApiService getInstance() {
        if (sHuoBiApiService == null) {
            synchronized (HuoBiApiService.class) {
                if (sHuoBiApiService == null) {
                    sHuoBiApiService = new HuoBiApiService();
                }
            }
        }
        return sHuoBiApiService;
    }
}
