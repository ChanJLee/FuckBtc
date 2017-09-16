package com.chan.fbtc.biz;

import com.chan.fbtc.api.DingDingService;
import com.chan.fbtc.api.HuoBiApiService;
import com.chan.fbtc.bean.BTCMarket;
import com.chan.fbtc.bean.ETHMarket;
import com.chan.fbtc.bean.MarkdownMessage;
import com.chan.fbtc.log.Logger;
import com.chan.fbtc.markdown.MarkdownBuilder;
import com.chan.fbtc.markdown.Table;
import com.chan.fbtc.markdown.Title;
import com.chan.fbtc.preference.Preference;
import com.chan.fbtc.timeline.Job;
import com.google.gson.JsonElement;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by chan on 2017/9/8.
 */
public class ETHWatchDog implements Job {
    private final int mWarningThreshold;
    private final String mVersionInfo;
    private long mLastFocusTimestamp = 0;
    private final int mMaxInterval;

    public ETHWatchDog() {
        mMaxInterval = Preference.getInstance().getInt("ETH_MAX_DURATION", 20000);
        mWarningThreshold = Preference.getInstance().getInt("ETH_WARNING_THRESHOLD", 50);
        mVersionInfo = "version: " + Preference.getInstance().getString("VERSION");
    }

    @Override
    public boolean interceptAction(long timestamp) {
        if (timestamp - mLastFocusTimestamp <= mMaxInterval) {
            return false;
        }
        return true;
    }

    @Override
    public void action() {
        HuoBiApiService.getInstance().fetchETHMarket()
                .filter(new Func1<ETHMarket, Boolean>() {
                    @Override
                    public Boolean call(ETHMarket market) {
                        for (int i = 0; i < market.snapshot.buys.length; ++i) {
                            Float[] record = market.snapshot.buys[i];
                            if (record[1] >= mWarningThreshold) {
                                return true;
                            }
                        }

                        for (int i = 0; i < market.snapshot.sells.length; ++i) {
                            Float[] record = market.snapshot.sells[1];
                            if (record[1] >= mWarningThreshold) {
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .doOnNext(new Action1<ETHMarket>() {
                    @Override
                    public void call(ETHMarket market) {
                        mLastFocusTimestamp = System.currentTimeMillis();
                    }
                })
                .map(new Func1<ETHMarket, MarkdownMessage>() {
                    @Override
                    public MarkdownMessage call(ETHMarket market) {
                        MarkdownMessage message = new MarkdownMessage();
                        MarkdownBuilder builder = new MarkdownBuilder();
                        builder.addElement(new Title("报表 ETH-CNY"));
                        builder.addElement(new Title("发现大老板"));
                        builder.addElement(new Title(Title.Size.SIZE_3, mVersionInfo));

                        builder.addElement(new Title(Title.Size.SIZE_2, "买入情况"));
                        Table table = new Table("price", "amount");
                        for (int i = 0; i < market.snapshot.buys.length; ++i) {
                            Float[] record = market.snapshot.buys[i];
                            if (record[1] < mWarningThreshold) {
                                continue;
                            }
                            table.newRow().addCell(record[0]).addCell(record[1]);
                        }
                        builder.addElement(table);

                        builder.addElement(new Title(Title.Size.SIZE_2, "卖出情况"));
                        table = new Table("price", "amount");
                        for (int i = 0; i < market.snapshot.sells.length; ++i) {
                            Float[] record = market.snapshot.sells[i];
                            if (record[1] < mWarningThreshold) {
                                continue;
                            }
                            table.newRow().addCell(record[0]).addCell(record[1]);
                        }
                        builder.addElement(table);

                        message.markdown.text = builder.toMarkdown();
                        return message;
                    }
                })
                .doOnNext(new Action1<MarkdownMessage>() {
                    @Override
                    public void call(MarkdownMessage markdownMessage) {
                        Logger.log("ETHWatchDog", markdownMessage.markdown.text);
                    }
                })
                .flatMap(new Func1<MarkdownMessage, Observable<JsonElement>>() {
                    @Override
                    public Observable<JsonElement> call(MarkdownMessage message) {
                        return DingDingService.getInstance().sendMessage(message);
                    }
                })
                .subscribe(new Subscriber<JsonElement>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.log("ETHWatchDog", "error " + e.getMessage());
                    }

                    @Override
                    public void onNext(JsonElement jsonElement) {
                        Logger.log("ETHWatchDog", "response " + jsonElement);
                    }
                });
    }
}
