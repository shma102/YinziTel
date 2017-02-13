package com.yinsidh.user.alipay;

/**
 * Created by User on 2016/11/3.
 */
public class PayBackBean {

    private String order_money;
    private String order_id;
    private String order_date;
    private String order_type;
    private String order_user;

    @Override
    public String toString() {
        return "PayBackBean{" +
                "order_money='" + order_money + '\'' +
                ", order_id='" + order_id + '\'' +
                ", order_date='" + order_date + '\'' +
                ", order_type='" + order_type + '\'' +
                ", order_user='" + order_user + '\'' +
                '}';
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getOrder_user() {
        return order_user;
    }

    public void setOrder_user(String order_user) {
        this.order_user = order_user;
    }

    public String getOrder_money() {

        return order_money;
    }

    public void setOrder_money(String order_money) {
        this.order_money = order_money;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
