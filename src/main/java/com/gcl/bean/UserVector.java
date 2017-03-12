package com.gcl.bean;

/**
 * Created by gcl on 2017/3/10.
 */
public class UserVector {

    private String id;
    private float[] vector = new float[26];

    private int label = -1;

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public UserVector() {
        for (float c : vector) {
            c = 0;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float[] getVector() {
        return vector;
    }

    public void setVector(float[] vector) {
        this.vector = vector;
    }
}
