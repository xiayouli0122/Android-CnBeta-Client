package com.yuri.cnbeta.news;

/**
 * Created by Yuri on 2016/4/18.
 */
public enum NewsType {
    LATEST("latest"),
    DAILY("daily"),
    MONTHLY("monthly"),
    TOPIC("topic"),
    HOT("hot");

    private String value;

    NewsType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
