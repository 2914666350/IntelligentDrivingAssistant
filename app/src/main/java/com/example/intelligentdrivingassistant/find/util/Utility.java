package com.example.intelligentdrivingassistant.find.util;

import com.example.intelligentdrivingassistant.find.gson.NewsList;
import com.google.gson.Gson;

/**
 * Created by len_titude on 2017/5/4.
 */

public class Utility {
    public static NewsList parseJsonWithGson(final String requestText){
        Gson gson = new Gson();
        return gson.fromJson(requestText, NewsList.class);
    }

}
