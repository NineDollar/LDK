package com.sys.ldk.http;

import com.sys.ldk.MainConfig;
import com.sys.ldk.accessibility.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;


public class JsonString implements Serializable {
    /**
     * code : 200
     * msg : success
     * newslist : [{"title":"真是桃花盛开的地方啊","content":"一男青年在公交车上看到一美女的衣领开得很低,春光外泄戏言道\u2018真是桃花盛开的地方啊\u2019美女听后,撩起裙子说:\u2018还有生你养你的地方\u2019！"}]
     */

    private int code;
    private String content;
    private String source;

    public JsonString(JSONObject jsonObject) {
        try {
            content = jsonObject.getString("content");
            source = jsonObject.getString("source");
            LogUtil.D("jsonObject:" + content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getContent() {
        return content;
    }

    public int getCode() {
        return code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
