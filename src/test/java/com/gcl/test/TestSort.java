package com.gcl.test;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by gcl on 2017/3/10.
 */
public class TestSort {

    @Test
    public void testSort() {
        float[] f = new float[10];

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            f[i] = random.nextFloat();
        }

        Arrays.sort(f);

        for (int i = 0; i < 10; i++) {
            System.out.println(f[i]);
        }
    }
}
