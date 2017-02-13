package com.yinsidh.bean;

/**
 * Created by User on 2016/11/5.
 */
public class InvitaionBean {
    private String numb;
    private String count;

    @Override
    public String toString() {
        return "InvitaionBean{" +
                "numb='" + numb + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

    public String getNumb() {
        return numb;
    }

    public void setNumb(String numb) {
        this.numb = numb;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
