package com.gcl.core.classify;

import com.gcl.bean.UserVector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcl on 2017/3/10.
 */
public class KNNRun {

    public static void main(String[] args) {
        KNN knn = new KNN();

        // 读取文件
        String userVectorPath = KNNRun.class.getResource("/userLabel").getPath();

        BufferedReader reader = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeSpecialFloatingPointValues();
        Gson gson = gsonBuilder.create();

        List<UserVector> list = new ArrayList<UserVector>();

        try {
            reader = new BufferedReader(new FileReader(userVectorPath));

            String line;
            while ((line = reader.readLine()) != null) {
                list.add(gson.fromJson(line, UserVector.class));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        UserVector user = getUserVector();
        int label = knn.knn(list, user, 10);
    }
    
    public static UserVector getUserVector() {
        return null;
    }
}
