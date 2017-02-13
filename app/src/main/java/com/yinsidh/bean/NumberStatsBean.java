package com.yinsidh.bean;

/**
 * Created by User on 2016/11/8.
 */
public class NumberStatsBean {
    private String id;
    private String routeprefix;
    private String removeprefix;
    private String addtoprefix;
    private String gateware;
    private String startime;
    private String endtime;
    private String stardate;
    private String enddate;
    private String name;
    private String appname;
    private String servicesagent;
    private String owner;
    private String expdate;
    private String money;
    private String time;
    private String prior;

    @Override
    public String toString() {
        return "NumberStatsBean{" +
                "id='" + id + '\'' +
                ", routeprefix='" + routeprefix + '\'' +
                ", removeprefix='" + removeprefix + '\'' +
                ", addtoprefix='" + addtoprefix + '\'' +
                ", gateware='" + gateware + '\'' +
                ", startime='" + startime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", stardate='" + stardate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", name='" + name + '\'' +
                ", appname='" + appname + '\'' +
                ", servicesagent='" + servicesagent + '\'' +
                ", owner='" + owner + '\'' +
                ", expdate='" + expdate + '\'' +
                ", money='" + money + '\'' +
                ", time='" + time + '\'' +
                ", prior='" + prior + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRouteprefix() {
        return routeprefix;
    }

    public void setRouteprefix(String routeprefix) {
        this.routeprefix = routeprefix;
    }

    public String getRemoveprefix() {
        return removeprefix;
    }

    public void setRemoveprefix(String removeprefix) {
        this.removeprefix = removeprefix;
    }

    public String getAddtoprefix() {
        return addtoprefix;
    }

    public void setAddtoprefix(String addtoprefix) {
        this.addtoprefix = addtoprefix;
    }

    public String getGateware() {
        return gateware;
    }

    public void setGateware(String gateware) {
        this.gateware = gateware;
    }

    public String getStartime() {
        return startime;
    }

    public void setStartime(String startime) {
        this.startime = startime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStardate() {
        return stardate;
    }

    public void setStardate(String stardate) {
        this.stardate = stardate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getServicesagent() {
        return servicesagent;
    }

    public void setServicesagent(String servicesagent) {
        this.servicesagent = servicesagent;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrior() {
        return prior;
    }

    public void setPrior(String prior) {
        this.prior = prior;
    }
}
