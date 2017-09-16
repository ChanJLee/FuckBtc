package com.chan.fbtc.biz;

import com.chan.fbtc.api.DingDingService;
import com.chan.fbtc.api.HuoBiApiService;
import com.chan.fbtc.bean.BTCMarket;
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
public class BTCWatchDog implements Job {

    private long mLastFocusTimestamp = 0;
    private final int mMaxInterval;
    private int mWarningThreshold;
    private String mVersionInfo;

    public BTCWatchDog() {
        mMaxInterval = Preference.getInstance().getInt("BTC_MAX_DURATION", 20000);
        mWarningThreshold = Preference.getInstance().getInt("BTC_WARNING_THRESHOLD", 10);
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
        HuoBiApiService.getInstance().fetchBTCMarket()
                .filter(new Func1<BTCMarket, Boolean>() {
                    @Override
                    public Boolean call(BTCMarket market) {
                        for (int i = 0; i < market.buys.size(); ++i) {
                            BTCMarket.TransactionRecord record = market.buys.get(i);
                            if (record.amount >= mWarningThreshold) {
                                return true;
                            }
                        }

                        for (int i = 0; i < market.sells.size(); ++i) {
                            BTCMarket.TransactionRecord record = market.sells.get(i);
                            if (record.amount >= mWarningThreshold) {
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .doOnNext(new Action1<BTCMarket>() {
                    @Override
                    public void call(BTCMarket market) {
                        mLastFocusTimestamp = System.currentTimeMillis();
                    }
                })
                .map(new Func1<BTCMarket, MarkdownMessage>() {
                    @Override
                    public MarkdownMessage call(BTCMarket market) {
                        MarkdownMessage message = new MarkdownMessage();
                        MarkdownBuilder builder = new MarkdownBuilder();
                        builder.addElement(new Title("报表 BTC-CNY"));
                        builder.addElement(new Title("发现大老板"));
                        builder.addElement(new Title(Title.Size.SIZE_3, mVersionInfo));

                        Table table = new Table("info", "price");
                        table.newRow().addCell("实时").addCell(market.lastPrice);
                        table.newRow().addCell("最低").addCell(market.lowestPrice);
                        table.newRow().addCell("最高").addCell(market.highestPrice);
                        builder.addElement(table);

                        builder.addElement(new Title(Title.Size.SIZE_2, "买入情况"));
                        table = new Table("price", "amount", "level");
                        for (int i = 0; i < market.buys.size(); ++i) {
                            BTCMarket.TransactionRecord record = market.buys.get(i);
                            if (record.amount < mWarningThreshold) {
                                continue;
                            }
                            table.newRow().addCell(record.price).addCell(record.amount).addCell(record.level);
                        }
                        builder.addElement(table);

                        builder.addElement(new Title(Title.Size.SIZE_2, "卖出情况"));
                        table = new Table("price", "amount", "level");
                        for (int i = 0; i < market.sells.size(); ++i) {
                            BTCMarket.TransactionRecord record = market.sells.get(i);
                            if (record.amount < mWarningThreshold) {
                                continue;
                            }
                            table.newRow().addCell(record.price).addCell(record.amount).addCell(record.level);
                        }
                        builder.addElement(table);

                        message.markdown.text = builder.toMarkdown();
                        return message;
                    }
                })
                .doOnNext(new Action1<MarkdownMessage>() {
                    @Override
                    public void call(MarkdownMessage markdownMessage) {
                        Logger.log("BTCWatchDog", markdownMessage.markdown.text);
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
                        Logger.log("BTCWatchDog", "error " + e.getMessage());
                    }

                    @Override
                    public void onNext(JsonElement jsonElement) {
                        Logger.log("BTCWatchDog", "response " + jsonElement);
                    }
                });
    }
}
