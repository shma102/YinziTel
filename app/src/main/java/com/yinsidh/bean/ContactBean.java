package com.yinsidh.bean;


import com.yinsidh.utils.PinYinUtils;

/**
 * Created by User on 2016/10/29.
 */
public class ContactBean implements Comparable<ContactBean> {
    public String name;
    public String number;
    public String pinyin;

    public ContactBean() {
    }

    public ContactBean(String name, String number) {
        this.name = name;
        this.number = number;
        this.pinyin = PinYinUtils.getPingYin(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String toString() {
        return "ContactBean{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }

    @Override
    public int compareTo(ContactBean another) {
        return this.pinyin.compareTo(another.pinyin);
    }
}
