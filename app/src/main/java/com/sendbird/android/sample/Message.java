package com.sendbird.android.sample;

import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by Brkmo on 19/12/2016.
 */
public class Message implements Comparable {
    private Long seqNum;
    private String cmd;
    private Long time;

    public Message(Long seqNum, String cmd, Long time) {
        this.seqNum = seqNum;
        this.cmd = cmd;
        this.time = time;
    }

    public Long getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Long seqNum) {
        this.seqNum = seqNum;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int compareTo(Object o) {

        return Long.compare(this.seqNum,((Message)o).seqNum);
    }
}
