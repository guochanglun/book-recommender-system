package com.gcl.bean;

/**
 * Created by gcl on 2017/3/10.
 */
public class User {

    // ID
    private int id;

    // 地点
    private String locacltion;

    // 年龄
    private int age;

    // 标签
    private String label;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocacltion() {
        return locacltion;
    }

    public void setLocacltion(String locacltion) {
        this.locacltion = locacltion;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
