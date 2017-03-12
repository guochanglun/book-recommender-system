package com.gcl.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gcl on 2017/3/10.
 */
public class UserWithWordBag {

    // 用户ID
    private String id;

    // 保存用户词元和词权重
    private Map<String, Float> map = new HashMap<String, Float>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Float> getMap() {
        return map;
    }

    public void setMap(Map<String, Float> map) {
        this.map = map;
    }
}
