package com.yinsidh.bean;

/**
 * Created by User on 2016/11/11.
 */
public class InvestmentBean {
    private String name;
    private String star;
    private String end;
    private String price;
    private String rate;
    private String days;
    private String safety;
    private String pid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "InvestmentBean{" +
                "name='" + name + '\'' +
                ", star='" + star + '\'' +
                ", end='" + end + '\'' +
                ", price='" + price + '\'' +
                ", rate='" + rate + '\'' +
                ", days='" + days + '\'' +
                ", safety='" + safety + '\'' +
                ", pid='" + pid + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getSafety() {
        return safety;
    }

    public void setSafety(String safety) {
        this.safety = safety;
    }
}
