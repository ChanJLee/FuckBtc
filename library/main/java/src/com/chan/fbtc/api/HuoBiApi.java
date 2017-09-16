package com.chan.fbtc.api;

import com.chan.fbtc.bean.BTCMarket;
import com.chan.fbtc.bean.ETHMarket;
import com.google.gson.JsonElement;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import retrofit.http.Url;
import rx.Observable;

import java.util.Map;

/**
 * Created by chan on 2017/9/8.
 */
public interface HuoBiApi {

    String ETH_2_CNY = "ethcny";
    String DEPTH_0 = "step0";
    String DEPTH_1 = "step1";
    String DEPTH_2 = "step2";
    String DEPTH_3 = "step3";
    String DEPTH_4 = "step4";
    String DEPTH_5 = "step5";
    String HOST = "be.huobi.com";

    @GET("/staticmarket/detail_btc_json.js")
    Observable<BTCMarket> fetchBTCMarket();

    @GET
    Observable<ETHMarket> fetchMarkDepth(@Url String url, @Query("symbol") String type, @Query("type") String depth,
                                           @QueryMap
            Map<String, String> query);
}
