package com.gcl.core.cluster;

import com.gcl.pretreatment.ReadDataFromCsvToMysql;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gcl on 2017/3/10.
 */
public class BookText {

    public static Map<String, String> getBookMap() {


        // 存储isbn和title
        Map<String, String> map = new HashMap<String, String>();


        /*
        读取isbn和title
         */
        String userCsvPath = ReadDataFromCsvToMysql.class.getResource("/BX-Books.csv").getPath();

        File file = new File(userCsvPath);

        if (!file.exists()) {
            System.out.println("文件不存在");
            return null;
        }

        FileReader fileReader;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            String line;
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                line = line.replace("\"", "").replace("\\", "").replace("'", "");
                String[] arr = line.split(";");

                if (arr.length < 3) continue;

                String isbn = arr[0];
                String title = arr[1];

                map.put(isbn, title);

            }

            return map;
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
        }
        return map;
    }
}
