package com.yinsidh.bean;

/**
 * Created by User on 2016/11/18.
 */

public class AdvertisingBean {
    private String adv_url;
    private String img_url;
    private String isshow;
    private String img_w;
    private String img_h;
    private String is_adv;

    @Override
    public String toString() {
        return "AdvertisingBean{" +
                "adv_url='" + adv_url + '\'' +
                ", img_url='" + img_url + '\'' +
                ", isshow='" + isshow + '\'' +
                ", img_w='" + img_w + '\'' +
                ", img_h='" + img_h + '\'' +
                ", is_adv='" + is_adv + '\'' +
                '}';
    }

    public String getAdv_url() {
        return adv_url;
    }

    public void setAdv_url(String adv_url) {
        this.adv_url = adv_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getIsshow() {
        return isshow;
    }

    public void setIsshow(String isshow) {
        this.isshow = isshow;
    }

    public String getImg_w() {
        return img_w;
    }

    public void setImg_w(String img_w) {
        this.img_w = img_w;
    }

    public String getImg_h() {
        return img_h;
    }

    public void setImg_h(String img_h) {
        this.img_h = img_h;
    }

    public String getIs_adv() {
        return is_adv;
    }

    public void setIs_adv(String is_adv) {
        this.is_adv = is_adv;
    }
}
