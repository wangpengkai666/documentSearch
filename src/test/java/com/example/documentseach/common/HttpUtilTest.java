package com.example.documentseach.common;

import com.example.documentseach.DocumentApplicationTest;
import com.example.documentseach.common.util.http.HttpUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class HttpUtilTest extends DocumentApplicationTest {
    @Test
    public void getTest() {
        String response = HttpUtil.get("http://120.26.69.19:9200/_cat/templates", null, null);
        Assertions.assertNotNull(response);
    }
}
