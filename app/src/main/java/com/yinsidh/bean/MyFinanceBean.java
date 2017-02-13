package com.yinsidh.bean;

import java.util.List;

/**
 * Created by User on 2016/11/10.
 */
public class MyFinanceBean {
    private String username;
    private String capital;
    private String wcapital;
    private String earnings;
    private List<MyFinanceZaiqiBean> zaiqi;
    private List<MyFinanceZaiqiBean> history;

    @Override
    public String toString() {
        return "MyFinanceBean{" +
                "username='" + username + '\'' +
                ", capital='" + capital + '\'' +
                ", wcapital='" + wcapital + '\'' +
                ", earnings='" + earnings + '\'' +
                ", zaiqi=" + zaiqi +
                ", history=" + history +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getWcapital() {
        return wcapital;
    }

    public void setWcapital(String wcapital) {
        this.wcapital = wcapital;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    public List<MyFinanceZaiqiBean> getZaiqi() {
        return zaiqi;
    }

    public void setZaiqi(List<MyFinanceZaiqiBean> zaiqi) {
        this.zaiqi = zaiqi;
    }

    public List<MyFinanceZaiqiBean> getHistory() {
        return history;
    }

    public void setHistory(List<MyFinanceZaiqiBean> history) {
        this.history = history;
    }
}
