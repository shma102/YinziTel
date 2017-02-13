package com.yinsidh.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 2016/11/8.
 */
public class FinancialBean {
    private String id;
    private String pro_num;
    private String pro_name;
    private String pro_price;
    private String pro_rate;
    private String ord_star;
    private String ord_end;
    private String pro_stats;
    private String pro_details;
    private String deadline;
    private String total;
    private String people;
    private String risk;
    private String pro_gift;
    private String safety;
    private String accrual_time;
    private String remit_type;
    private String expire;
    private String get_out;
    private String get_rule;
    private String hint;
    private String cash_type;
    private String FAQ;

    @SerializedName("class")
    private String classes;

    @Override
    public String toString() {
        return "FinancialBean{" +
                "id='" + id + '\'' +
                ", pro_num='" + pro_num + '\'' +
                ", pro_name='" + pro_name + '\'' +
                ", pro_price='" + pro_price + '\'' +
                ", pro_rate='" + pro_rate + '\'' +
                ", ord_star='" + ord_star + '\'' +
                ", ord_end='" + ord_end + '\'' +
                ", pro_stats='" + pro_stats + '\'' +
                ", pro_details='" + pro_details + '\'' +
                ", deadline='" + deadline + '\'' +
                ", total='" + total + '\'' +
                ", people='" + people + '\'' +
                ", risk='" + risk + '\'' +
                ", pro_gift='" + pro_gift + '\'' +
                ", safety='" + safety + '\'' +
                ", accrual_time='" + accrual_time + '\'' +
                ", remit_type='" + remit_type + '\'' +
                ", expire='" + expire + '\'' +
                ", get_out='" + get_out + '\'' +
                ", get_rule='" + get_rule + '\'' +
                ", hint='" + hint + '\'' +
                ", cash_type='" + cash_type + '\'' +
                ", FAQ='" + FAQ + '\'' +
                ", classes='" + classes + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPro_num() {
        return pro_num;
    }

    public void setPro_num(String pro_num) {
        this.pro_num = pro_num;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_price() {
        return pro_price;
    }

    public void setPro_price(String pro_price) {
        this.pro_price = pro_price;
    }

    public String getPro_rate() {
        return pro_rate;
    }

    public void setPro_rate(String pro_rate) {
        this.pro_rate = pro_rate;
    }

    public String getOrd_star() {
        return ord_star;
    }

    public void setOrd_star(String ord_star) {
        this.ord_star = ord_star;
    }

    public String getOrd_end() {
        return ord_end;
    }

    public void setOrd_end(String ord_end) {
        this.ord_end = ord_end;
    }

    public String getPro_stats() {
        return pro_stats;
    }

    public void setPro_stats(String pro_stats) {
        this.pro_stats = pro_stats;
    }

    public String getPro_details() {
        return pro_details;
    }

    public void setPro_details(String pro_details) {
        this.pro_details = pro_details;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getPro_gift() {
        return pro_gift;
    }

    public void setPro_gift(String pro_gift) {
        this.pro_gift = pro_gift;
    }

    public String getSafety() {
        return safety;
    }

    public void setSafety(String safety) {
        this.safety = safety;
    }

    public String getAccrual_time() {
        return accrual_time;
    }

    public void setAccrual_time(String accrual_time) {
        this.accrual_time = accrual_time;
    }

    public String getRemit_type() {
        return remit_type;
    }

    public void setRemit_type(String remit_type) {
        this.remit_type = remit_type;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getGet_out() {
        return get_out;
    }

    public void setGet_out(String get_out) {
        this.get_out = get_out;
    }

    public String getGet_rule() {
        return get_rule;
    }

    public void setGet_rule(String get_rule) {
        this.get_rule = get_rule;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getCash_type() {
        return cash_type;
    }

    public void setCash_type(String cash_type) {
        this.cash_type = cash_type;
    }

    public String getFAQ() {
        return FAQ;
    }

    public void setFAQ(String FAQ) {
        this.FAQ = FAQ;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }
}
