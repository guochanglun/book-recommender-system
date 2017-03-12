package com.gcl.core.classify;

import com.gcl.bean.UserVector;
import com.gcl.util.MyVector;

import java.util.Arrays;
import java.util.List;

/**
 * KNN算法
 * Created by gcl on 2017/3/10.
 */
public class KNN {
    public int knn(List<UserVector> userVectorList, UserVector user, int k) {

        float[] dists = new float[k];
        for (int i = 0; i < k; i++) {
            dists[i] = Float.MAX_VALUE;
        }

        int[] label = new int[k];

        for (UserVector u : userVectorList) {
            Arrays.sort(dists);
            float dist = MyVector.dist(u, user);
            for (int i = 0; i < k; i++) {
                if (dist < dists[i]) {
                    dists[i] = dist;
                    label[i] = u.getLabel();
                }
            }
        }

        int[] maxLabel = new int[20];
        for (int i : label) {
            maxLabel[i]++;
        }

        int la = 0;

        for (int i : maxLabel) {
            if (i > la) {
                la = i;
            }
        }
        return la;
    }
}
