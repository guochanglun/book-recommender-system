package com.gcl.core.cluster;

import com.gcl.bean.UserWithWordBag;
import com.google.gson.Gson;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 数据
 * Created by gcl on 2017/3/10.
 */
public class ProcessData {

    private Set<String> stopWordSet = new HashSet<String>();
    private Map<String, String> bookMap;

    // main
    public static void main(String[] args) {
        ProcessData processData = new ProcessData();
        processData.getUserWordBag();
    }


    // 得到用户词组
    public void getUserWordBag() {

        // 加载停用词
        String stopWordPath = this.getClass().getResource("/StopWords").getPath();
        BufferedReader stopWordsBuffedReader = null;

        System.out.println("加载停用词------");
        try {
            stopWordsBuffedReader = new BufferedReader(new FileReader(stopWordPath));
            String stopWord;

            while ((stopWord = stopWordsBuffedReader.readLine()) != null) {
                stopWordSet.add(stopWord);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stopWordsBuffedReader != null) {
                try {
                    stopWordsBuffedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("停用词成功");

        // 获取book map
        System.out.println("获取bookMap------");
        bookMap = BookText.getBookMap();
        System.out.println("bookMap获取完毕");

        //遍历用户文件，获取用户词组
        String userPath = getClass().getResource("/BX-Book-Ratings.csv").getPath();

        // 创建用户词袋文件
        String userWordsBagPath = getClass().getResource("/").getPath() + "/userWordsBag";
        System.out.println(userWordsBagPath);

        File userWordsBagFile = new File(userWordsBagPath);
        if (!userWordsBagFile.exists()) {
            try {
                userWordsBagFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        BufferedWriter wordsBagWriter = null;
        BufferedReader userBufferedReader = null;

        Gson gson = new Gson();

        try {
            wordsBagWriter = new BufferedWriter(new FileWriter(userWordsBagFile));
            userBufferedReader = new BufferedReader(new FileReader(userPath));

            String user;

            // 记录UserID
            String curUserId = "";
            UserWithWordBag curUserObj = new UserWithWordBag();

            userBufferedReader.readLine();
            while ((user = userBufferedReader.readLine()) != null) {
                user = user.replace("\"", "");
                String[] users = user.split(";");
                if (users.length != 3) continue;

                String id = users[0];
                String isbn = users[1];
                int rating = Integer.parseInt(users[2]);

                if (curUserId.equals(id)) {
                    setWordsBag(curUserObj.getMap(), isbn, rating);
                } else {
                    // 如果user换了
                    // 保存上一个user信息
                    wordsBagWriter.write(gson.toJson(curUserObj) + "\n");
                    wordsBagWriter.flush();

                    // 初始化状态
                    curUserId = id;
                    curUserObj.setId(id);
                    curUserObj.getMap().clear();

                    // 处理这个user
                    setWordsBag(curUserObj.getMap(), isbn, rating);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stopWordsBuffedReader != null) {
                try {
                    stopWordsBuffedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (userBufferedReader != null) {
                try {
                    userBufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (wordsBagWriter != null) {
                try {
                    wordsBagWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 填充用户词袋
    private void setWordsBag(Map<String, Float> map, String isbn, int rating) {
        System.out.println(isbn);
        String sent = bookMap.get(isbn);
        if (sent == null) return;

        String[] sents = sent.toLowerCase().split(" ");
        for (String word : sents) {
            if (!stopWordSet.contains(word)) {
                map.put(word, (float) (rating * 0.10 + 0.001));
            }
        }
    }
    // 计算用户向量

}
