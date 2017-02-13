package com.yinsidh.bean;

/**
 * Created by User on 2016/11/2.
 */
public class AliOrderBean {
    private String stats;
    private String sign;

    @Override
    public String toString() {
        return "AliOrderBean{" +
                "stats='" + stats + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
