package me.gurpreetsk.task_payu.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by Gurpreet on 11/08/17.
 * Model class to represent a kickstarter project
 */

@SimpleSQLTable(table = "projects", provider = "PayuProvider")
public class Project {

    @SerializedName("s.no")
    @SimpleSQLColumn("s_no")
    @Expose
    private int sNo;
    @SerializedName("amt.pledged")
    @SimpleSQLColumn("amt_pledged")
    @Expose
    private int amtPledged;
    @SerializedName("blurb")
    @SimpleSQLColumn("blurb")
    @Expose
    private String blurb;
    @SerializedName("by")
    @SimpleSQLColumn("by")
    @Expose
    private String by;
    @SerializedName("country")
    @SimpleSQLColumn("country")
    @Expose
    private String country;
    @SerializedName("currency")
    @SimpleSQLColumn("currency")
    @Expose
    private String currency;
    @SerializedName("end.time")
    @SimpleSQLColumn("end_time")
    @Expose
    private String endTime;
    @SerializedName("location")
    @SimpleSQLColumn("location")
    @Expose
    private String location;
    @SerializedName("percentage.funded")
    @SimpleSQLColumn("percentage_funded")
    @Expose
    private int percentageFunded;
    @SerializedName("num.backers")
    @SimpleSQLColumn("num_backers")
    @Expose
    private String numBackers;
    @SerializedName("state")
    @SimpleSQLColumn("state")
    @Expose
    private String state;
    @SerializedName("title")
    @SimpleSQLColumn(value = "title", primary = true)
    @Expose
    private String title;
    @SerializedName("type")
    @SimpleSQLColumn("type")
    @Expose
    private String type;
    @SerializedName("url")
    @SimpleSQLColumn("url")
    @Expose
    private String url;


    public Project() {
    }

    public Project(int sNo, int amtPledged, String blurb, String by, String country,
                   String currency, String endTime, String location, int percentageFunded,
                   String numBackers, String state, String title, String type, String url) {
        this.sNo = sNo;
        this.amtPledged = amtPledged;
        this.blurb = blurb;
        this.by = by;
        this.country = country;
        this.currency = currency;
        this.endTime = endTime;
        this.location = location;
        this.percentageFunded = percentageFunded;
        this.numBackers = numBackers;
        this.state = state;
        this.title = title;
        this.type = type;
        this.url = url;
    }

    public int getSNo() {
        return sNo;
    }

    public void setSNo(int sNo) {
        this.sNo = sNo;
    }

    public int getAmtPledged() {
        return amtPledged;
    }

    public void setAmtPledged(int amtPledged) {
        this.amtPledged = amtPledged;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPercentageFunded() {
        return percentageFunded;
    }

    public void setPercentageFunded(int percentageFunded) {
        this.percentageFunded = percentageFunded;
    }

    public String getNumBackers() {
        return numBackers;
    }

    public void setNumBackers(String numBackers) {
        this.numBackers = numBackers;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
