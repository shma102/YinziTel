package com.yinsidh.bean;

/**
 * Created by User on 2016/12/13.
 */

public class GoodsBean {
    private String productId;
    private String productName;
    private String productPrice;
    private String productPic;
    private String volume;
    private String pic_h;
    private String pic_w;

    @Override
    public String toString() {
        return "GoodsBean{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", productPic='" + productPic + '\'' +
                ", volume='" + volume + '\'' +
                ", pic_h='" + pic_h + '\'' +
                ", pic_w='" + pic_w + '\'' +
                '}';
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getPic_h() {
        return pic_h;
    }

    public void setPic_h(String pic_h) {
        this.pic_h = pic_h;
    }

    public String getPic_w() {
        return pic_w;
    }

    public void setPic_w(String pic_w) {
        this.pic_w = pic_w;
    }
}
