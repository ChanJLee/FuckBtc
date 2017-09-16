package com.chan.test;

import com.chan.fbtc.biz.ETHWatchDog;
import com.chan.fbtc.log.Logger;
import com.chan.fbtc.timeline.TimeLine;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by chan on 2017/9/10.
 */
public class TestMain {
    public static void main(String[] args) {
        try {
            TimeLine timeLine = new TimeLine();
            timeLine.addJob(new ETHWatchDog());
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
