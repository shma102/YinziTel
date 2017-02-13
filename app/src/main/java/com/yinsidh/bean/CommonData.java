package com.yinsidh.bean;

/**
 * Created by User on 2016/10/27.
 */
public class CommonData {
    private String stats;
    private String msg;

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "CommonData{" +
                "stats='" + stats + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
