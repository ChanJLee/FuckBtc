package com.chan.fbtc;


import com.chan.fbtc.biz.BTCWatchDog;
import com.chan.fbtc.biz.ETHWatchDog;
import com.chan.fbtc.biz.HeartBeat;
import com.chan.fbtc.log.Logger;
import com.chan.fbtc.timeline.TimeLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by chan on 2017/9/7.
 */
public class Main {
    public static void main(String[] args) {
        try {
            TimeLine timeLine = new TimeLine();
            timeLine.addJob(new BTCWatchDog());
            timeLine.addJob(new ETHWatchDog());
            timeLine.addJob(new HeartBeat());
            timeLine.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("type anything to exit");
            bufferedReader.readLine();
            timeLine.stop();
        } catch (Exception e) {
            Logger.log("main_time_line", e.getMessage());
        }
    }
}
