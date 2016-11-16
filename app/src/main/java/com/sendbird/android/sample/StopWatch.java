package com.sendbird.android.sample;

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
        running = true;
    }
    public void stop(){
        stopTime = System.currentTimeMillis();
        running = false;
    }
    public long getElapsedTime(){
        if(running)
            return System.currentTimeMillis() - startTime;
        else
            return stopTime - startTime;
    }
    public void clear(){
        this.startTime = 0;
        this.stopTime = 0;
        this.running = false;
    }
}
