package com.yinsidh.bean;

/**
 * Created by User on 2016/11/11.
 */
public class ZaiqiDetailsBean {
    private String earnings;
    private String name;
    private String deadline;
    private String rate;
    private String risk;
    private String safety;
    private String price;
    private String allprice;
    private String cashprice;

    @Override
    public String toString() {
        return "ZaiqiDetailsBean{" +
                "earnings='" + earnings + '\'' +
                ", name='" + name + '\'' +
                ", deadline='" + deadline + '\'' +
                ", rate='" + rate + '\'' +
                ", risk='" + risk + '\'' +
                ", safety='" + safety + '\'' +
                ", price='" + price + '\'' +
                ", allprice='" + allprice + '\'' +
                ", cashprice='" + cashprice + '\'' +
                '}';
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getSafety() {
        return safety;
    }

    public void setSafety(String safety) {
        this.safety = safety;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAllprice() {
        return allprice;
    }

    public void setAllprice(String allprice) {
        this.allprice = allprice;
    }

    public String getCashprice() {
        return cashprice;
    }

    public void setCashprice(String cashprice) {
        this.cashprice = cashprice;
    }
}
