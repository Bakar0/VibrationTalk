package com.sendbird.android.sample;
import 	android.os.SystemClock;

/**
 * Created by Brkmo on 16/11/2016.
 */
public class StopWatch {
    private long startTime;
    private long stopTime;
    private boolean running;

    public StopWatch() {
        this.startTime = 0;
        this.stopTime = 0;
        this.running = false;
    }

    public void start(){
        startTime = System.currentTimeMillis();
        //startTime = SystemClock.elapsedRealtime();
        running = true;
    }
    public void stop(){
        stopTime = System.currentTimeMillis();
        //stopTime = SystemClock.elapsedRealtime();
        running = false;
    }
    public long getElapsedTime(){
        if(running)
            return System.currentTimeMillis() - startTime;
            //return  SystemClock.elapsedRealtime() - startTime;
        else
            return stopTime - startTime;
    }
    public void clear(){
        this.startTime = 0;
        this.stopTime = 0;
        this.running = false;
    }
}
