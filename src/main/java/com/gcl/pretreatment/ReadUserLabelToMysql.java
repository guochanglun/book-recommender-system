package com.gcl.pretreatment;

import com.gcl.bean.UserVector;
import com.gcl.core.classify.KNNRun;
import com.gcl.util.JDBCTools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by gcl on 2017/3/10.
 */
public class ReadUserLabelToMysql {

    public static void main(String[] args) {
        readUserLabelToMysql();
    }

    private static void readUserLabelToMysql() {
        // 读取文件
        String userVectorPath = KNNRun.class.getResource("/userLabel").getPath();

        BufferedReader reader = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeSpecialFloatingPointValues();
        Gson gson = gsonBuilder.create();

        Connection connection = JDBCTools.getConnection();

        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("");

            String prefix = "insert into user_label(id, label) values";

            reader = new BufferedReader(new FileReader(userVectorPath));

            int i = 0;
            StringBuffer suffix = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                UserVector userVector = gson.fromJson(line, UserVector.class);
                suffix.append("('" + userVector.getId() + "'," + userVector.getLabel() + "),");

                i++;

                if (i % 10000 == 0) {
                    String sql = prefix + suffix.substring(0, suffix.length() - 1);
                    statement.addBatch(sql);
                    statement.executeBatch();
                    connection.commit();
                    suffix = new StringBuffer();
                }
            }
            String sql = prefix + suffix.substring(0, suffix.length() - 1);
            statement.addBatch(sql);
            statement.executeBatch();
            connection.commit();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
