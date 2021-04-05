package com.example.intelligentdrivingassistant.find.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by len_titude on 2017/5/4.
 */

public class NewsList {

    public int code;

    public String msg;

    @SerializedName("newslist")
    public List<News> newsList ;

}
