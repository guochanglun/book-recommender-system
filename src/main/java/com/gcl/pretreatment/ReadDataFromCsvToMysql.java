package com.gcl.pretreatment;

import com.gcl.util.JDBCTools;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by gcl on 2017/3/10.
 * <p>
 * 从Csv中读取原始数据到mysql
 */
public class ReadDataFromCsvToMysql {

    public static void main(String[] args) {
        //readUserCsvToMysql();

        readRatingCsvToMysql();

        //readBookCsvToMysql();
    }

    private static void readBookCsvToMysql() {
        String userCsvPath = ReadDataFromCsvToMysql.class.getResource("/BX-Books.csv").getPath();

        File file = new File(userCsvPath);

        if (!file.exists()) {
            System.out.println("文件不存在");
            return;
        }

        FileReader fileReader;
        BufferedReader bufferedReader = null;
        Connection connection = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            String line;
            bufferedReader.readLine();

            connection = JDBCTools.getConnection();
            connection.setAutoCommit(false);

            String prefix = "INSERT INTO book(isbn, title) VALUES ";

            PreparedStatement preparedStatement = connection.prepareStatement("");

            int i = 0;

            StringBuffer stringBuffer = new StringBuffer();

            while ((line = bufferedReader.readLine()) != null) {
                line = line.replace("\"", "").replace("\\", "").replace("'", "");
                String[] arr = line.split(";");

                if (arr.length < 3) continue;

                String isbn = arr[0];
                String title = arr[1];

                stringBuffer.append("('" + isbn + "', '" + title + "'),");

                i++;

                if (i % 10000 == 0) {
                    String sql = prefix + stringBuffer.substring(0, stringBuffer.length() - 1);
                    preparedStatement.addBatch(sql);
                    preparedStatement.executeBatch();
                    connection.commit();
                    stringBuffer = new StringBuffer();
                }
            }

            String sql = prefix + stringBuffer.substring(0, stringBuffer.length() - 1);
            preparedStatement.addBatch(sql);
            preparedStatement.executeBatch();
            connection.commit();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
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

    private static void readRatingCsvToMysql() {

        String userCsvPath = ReadDataFromCsvToMysql.class.getResource("/BX-Book-Ratings.csv").getPath();

        File file = new File(userCsvPath);

        if (!file.exists()) {
            System.out.println("文件不存在");
            return;
        }

        FileReader fileReader;
        BufferedReader bufferedReader = null;
        Connection connection = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            String line;
            bufferedReader.readLine();

            connection = JDBCTools.getConnection();
            connection.setAutoCommit(false);

            String prefix = "INSERT INTO rating(userid, isbn, rating) VALUES ";

            PreparedStatement preparedStatement = connection.prepareStatement("");

            int i = 0;

            StringBuffer stringBuffer = new StringBuffer();

            while ((line = bufferedReader.readLine()) != null) {
                line = line.replace("\"", "").replace(" ", "").replace("\\", "").replace("'", "");
                String[] arr = line.split(";");

                if (arr.length != 3) continue;

                int userid = Integer.parseInt(arr[0]);
                String isbn = arr[1];

                int rating = 0;
                if (!arr[2].equals("NULL")) {
                    try {
                        rating = Integer.parseInt(arr[2]);
                    } catch (NumberFormatException e) {
                    }
                }


                stringBuffer.append("(" + userid + ", '" + isbn + "', " + rating + "),");

                i++;

                if (i % 10000 == 0) {
                    String sql = prefix + stringBuffer.substring(0, stringBuffer.length() - 1);
                    preparedStatement.addBatch(sql);
                    preparedStatement.executeBatch();
                    connection.commit();
                    stringBuffer = new StringBuffer();
                }
            }

            String sql = prefix + stringBuffer.substring(0, stringBuffer.length() - 1);
            preparedStatement.addBatch(sql);
            preparedStatement.executeBatch();
            connection.commit();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
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

    private static void readUserCsvToMysql() {

        String ratingCsvPath = ReadDataFromCsvToMysql.class.getResource("/BX-Users.csv").getPath();

        File file = new File(ratingCsvPath);

        if (!file.exists()) {
            System.out.println("文件不存在");
            return;
        }

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Connection connection = null;

        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            String line = null;
            bufferedReader.readLine();

            connection = JDBCTools.getConnection();
            connection.setAutoCommit(false);

            String prefix = "INSERT INTO user(id, age) VALUES";

            StringBuffer suffix = new StringBuffer();

            PreparedStatement preparedStatement = connection.prepareStatement("");

            int i = 0;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.replace("\"", "");
                String[] arr = line.split(";");

                if (arr.length != 3) continue;

                int id = Integer.parseInt(arr[0]);

                int age = 0;
                if (!arr[2].equals("NULL")) {
                    try {
                        age = Integer.parseInt(arr[2]);
                    } catch (NumberFormatException e) {
                    }
                }

                suffix.append("(" + id + "," + age + "),");

                i++;

                if (i % 10000 == 0) {
                    String sql = prefix + suffix.substring(0, suffix.length() - 1);
                    preparedStatement.addBatch(sql);
                    preparedStatement.executeBatch();
                    connection.commit();
                    suffix = new StringBuffer();
                }
            }

            String sql = prefix + suffix.substring(0, suffix.length() - 1);
            preparedStatement.addBatch(sql);
            preparedStatement.executeBatch();
            connection.commit();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
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
