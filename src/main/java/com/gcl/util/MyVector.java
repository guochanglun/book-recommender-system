package com.gcl.util;

import com.gcl.bean.UserVector;

/**
 * Created by gcl on 2017/3/10.
 */
public class MyVector {

    // 计算距离
    public static float dist(UserVector user1, UserVector user2) {

        float[] user1Vector = user1.getVector();
        float[] user2Vector = user2.getVector();

        float one = 0;
        float two = 0;
        float sum = 0;

        for (int i = 0; i < user1Vector.length; i++) {
            sum += user1Vector[i] * user2Vector[i];
            one += user1Vector[i] * user1Vector[i];
            two += user2Vector[i] * user2Vector[i];
        }
        float dist = (float) (sum / Math.sqrt(one * two));
        return dist;
    }

    // 获得词向量
    public static float[] getWordVector(String word) {

        float[] vector = new float[26];

        for (char c : word.toLowerCase().toCharArray()) {
            if (c - 'a' < 0 || c - 'a' >= 26) {
                continue;
            }
            vector[c - 'a'] = 1;
        }
        return vector;
    }

    // 向量均值
    public static float[] vectorMean(float[] total) {
        float sum = 0;
        for (float v : total) {
            sum += v;
        }
        for (int i = 0; i < total.length; i++) {
            total[i] /= sum;
        }
        return total;
    }

    // 向量加法
    public static float[] vectorAdd(float[] total, float[] vector) {
        for (int i = 0; i < total.length; i++) {
            total[i] = total[i] + vector[i];
        }
        return total;
    }

    // 比较向量是否相等
    public static boolean isVectorEquals(float[] mean, float[] vector) {
        for (int i = 0; i < mean.length; i++) {
            if (mean[i] != vector[i])
                return false;
        }
        return true;
    }
}
