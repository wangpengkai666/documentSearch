package com.example.documentseach.common.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.documentseach.common.util.container.StringUtil;
import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class GetJSONFromTxt {

    private static KLog LOGGER = LoggerFactory.getLog(GetJSONFromTxt.class);

    public static JSONObject readDslFileInJarFile(String fileName) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("/Users/wangpengkai/Downloads/documentSeach/src/main/java/com/example/documentseach/default/template/article");

        if (inputStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            List<String> lines = new LinkedList<>();
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    lines.add(line);
                }
                return JSON.parseObject(StringUtil.join(lines, ""));
            } catch (Exception e) {
                LOGGER.error("class=GetJSONFromTxt||method=readDslFileInJarFile||errMsg={}", e.getMessage());
                return null;
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("class=GetJSONFromTxt||method=readDslFileInJarFile||errMsg={}", e.getMessage());
                }
            }
        }

        return null;
    }
}
