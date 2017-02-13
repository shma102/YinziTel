package com.yinsidh.bean;

import java.util.List;

/**
 * Created by User on 2016/11/8.
 */
public class FinacialListBean {
    private String img;
    private String rate;
    private List<FinancialBean> product;
    private List<FinancialBean> matters;
    private FinancialBean ty;

    public FinancialBean getTy() {
        return ty;
    }

    public void setTy(FinancialBean ty) {
        this.ty = ty;
    }

    @Override
    public String toString() {
        return "FinacialListBean{" +
                "img='" + img + '\'' +
                ", rate='" + rate + '\'' +
                ", product=" + product +
                ", matters=" + matters +
                ", ty=" + ty +
                '}';
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public List<FinancialBean> getProduct() {
        return product;
    }

    public void setProduct(List<FinancialBean> product) {
        this.product = product;
    }

    public List<FinancialBean> getMatters() {
        return matters;
    }

    public void setMatters(List<FinancialBean> matters) {
        this.matters = matters;
    }

}
