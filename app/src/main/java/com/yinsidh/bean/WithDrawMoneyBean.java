package com.yinsidh.bean;

/**
 * Created by User on 2016/11/7.
 */
public class WithDrawMoneyBean {
    private String id;
    private String cash_list;
    private String cash_name;
    private String cash_user;
    private String cash_type;
    private String cash_money;
    private String cash_number;
    private String cash_date;
    private String cash_ower;
    private String cash_stats;

    @Override
    public String toString() {
        return "WithDrawMoneyBean{" +
                "id='" + id + '\'' +
                ", cash_list='" + cash_list + '\'' +
                ", cash_name='" + cash_name + '\'' +
                ", cash_user='" + cash_user + '\'' +
                ", cash_type='" + cash_type + '\'' +
                ", cash_money='" + cash_money + '\'' +
                ", cash_number='" + cash_number + '\'' +
                ", cash_date='" + cash_date + '\'' +
                ", cash_ower='" + cash_ower + '\'' +
                ", cash_stats='" + cash_stats + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCash_list() {
        return cash_list;
    }

    public void setCash_list(String cash_list) {
        this.cash_list = cash_list;
    }

    public String getCash_name() {
        return cash_name;
    }

    public void setCash_name(String cash_name) {
        this.cash_name = cash_name;
    }

    public String getCash_user() {
        return cash_user;
    }

    public void setCash_user(String cash_user) {
        this.cash_user = cash_user;
    }

    public String getCash_type() {
        return cash_type;
    }

    public void setCash_type(String cash_type) {
        this.cash_type = cash_type;
    }

    public String getCash_money() {
        return cash_money;
    }

    public void setCash_money(String cash_money) {
        this.cash_money = cash_money;
    }

    public String getCash_number() {
        return cash_number;
    }

    public void setCash_number(String cash_number) {
        this.cash_number = cash_number;
    }

    public String getCash_date() {
        return cash_date;
    }

    public void setCash_date(String cash_date) {
        this.cash_date = cash_date;
    }

    public String getCash_ower() {
        return cash_ower;
    }

    public void setCash_ower(String cash_ower) {
        this.cash_ower = cash_ower;
    }

    public String getCash_stats() {
        return cash_stats;
    }

    public void setCash_stats(String cash_stats) {
        this.cash_stats = cash_stats;
    }
}
