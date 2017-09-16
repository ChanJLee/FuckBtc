package com.chan.fbtc.api;

import com.chan.fbtc.bean.MarkdownMessage;
import com.chan.fbtc.bean.TextMessage;
import com.google.gson.JsonElement;
import retrofit.http.*;
import rx.Observable;

/**
 * Created by chan on 2017/9/7.
 */
public interface DingDingApi {

     @Headers("Content-Type: application/json; charset=utf-8")
     @POST("robot/send")
     Observable<JsonElement> sendMessage(@Query("access_token") String token, @Body TextMessage textMessage);

     @Headers("Content-Type: application/json; charset=utf-8")
     @POST("robot/send")
     Observable<JsonElement> sendMessage(@Query("access_token") String token, @Body MarkdownMessage markdownMessage);
}
