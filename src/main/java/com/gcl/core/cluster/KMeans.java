package com.gcl.core.cluster;

import com.gcl.bean.UserVector;
import com.gcl.bean.UserWithWordBag;
import com.gcl.util.MyVector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by gcl on 2017/3/10.
 */
public class KMeans {

    private Gson gson;

    public static void main(String[] args) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeSpecialFloatingPointValues();

        KMeans kMeans = new KMeans();
        kMeans.gson = gsonBuilder.create();

        System.out.println("读取用户词袋文件");
        List<UserWithWordBag> userWithWordBagList = new ArrayList<UserWithWordBag>();

        String userWordsBagPath = kMeans.getClass().getResource("/").getPath() + "/userWordsBag";
        BufferedReader userBufferedReader = null;

        try {
            userBufferedReader = new BufferedReader(new FileReader(userWordsBagPath));

            String line;
            // 除去开头的空对象
            userBufferedReader.readLine();
            while ((line = userBufferedReader.readLine()) != null) {
                UserWithWordBag userWithWordBag = kMeans.gson.fromJson(line, UserWithWordBag.class);
                userWithWordBagList.add(userWithWordBag);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (userBufferedReader != null) {
                try {
                    userBufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("读取用户词袋文件完成---------");

        kMeans.kMeans(userWithWordBagList, 15);

    }

    public void kMeans(List<UserWithWordBag> users, int k) {
        // 获取用户词向量
        List<UserVector> userVectors = getUserVector(users);
        System.out.println("获取用户词向量");

        List<UserVector> meanVectors = new ArrayList<UserVector>(k);
        int size = userVectors.size();

        Random random = new Random();
        // 初始化k个均值向量
        for (int i = 0; i < k; i++) {
            meanVectors.add(userVectors.get(random.nextInt(size)));
        }

        int loop = 1;

        boolean isChanged = true;
        while (isChanged) {

            System.out.println("循环--" + (loop++) + "--");

            // 做多进行4000轮学习
            if (loop == 4000) {
                break;
            }

            isChanged = false;

            // 计算所在分类
            for (UserVector user : userVectors) {
                int label = getClassLabel(user, meanVectors);
                user.setLabel(label);
            }

            // 重新计算均值向量
            for (int i = 0; i < k; i++) {
                float[] total = new float[26];
                for (UserVector user : userVectors) {
                    if (user.getLabel() == i) {
                        total = MyVector.vectorAdd(total, user.getVector());
                    }
                }
                float[] mean = MyVector.vectorMean(total);
                boolean equals = MyVector.isVectorEquals(mean, meanVectors.get(i).getVector());

                if (!equals) {
                    // 更新均值向量
                    meanVectors.get(i).setVector(mean);
                    isChanged = true;
                }
            }
        }

        // 计算完毕， 写入文件
        System.out.println("计算完毕，正在写入文件------");

        // 创建用户词袋文件
        String labelPath = getClass().getResource("/").getPath() + "/userLabel";
        System.out.println(labelPath);

        File userLabelFile = new File(labelPath);
        if (!userLabelFile.exists()) {
            try {
                userLabelFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedWriter userLabelWriter = null;

        try {
            userLabelWriter = new BufferedWriter(new FileWriter(userLabelFile));

            for (UserVector userVector : userVectors) {
                String id = userVector.getId();
                int label = userVector.getLabel();

                userLabelWriter.write(gson.toJson(userVector) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (userLabelWriter != null) {
                try {
                    userLabelWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取类标签
     */
    private int getClassLabel(UserVector user, List<UserVector> meanVectors) {

        float minDist = Float.MAX_VALUE;
        int label = 0;

        for (int i = 0; i < meanVectors.size(); i++) {
            UserVector userVector = meanVectors.get(i);
            float dist = MyVector.dist(user, userVector);
            if (dist < minDist) {
                minDist = dist;
                label = i;
            }
        }
        return label;
    }

    // 获得用户向量
    private List<UserVector> getUserVector(List<UserWithWordBag> users) {

        List<UserVector> userVectors = new ArrayList<UserVector>(users.size());

        for (UserWithWordBag user : users) {

            UserVector userVector = new UserVector();

            String id = user.getId();
            userVector.setId(id);

            Map<String, Float> map = user.getMap();

            float[] vector = userVector.getVector();

            for (Map.Entry<String, Float> entry : map.entrySet()) {
                String word = entry.getKey();
                Float weight = entry.getValue();

                float[] wordVector = MyVector.getWordVector(word);

                for (int i = 0; i < wordVector.length; i++) {
                    vector[i] += wordVector[i] * weight;
                }
            }

            userVectors.add(userVector);
        }

        return userVectors;
    }
}
