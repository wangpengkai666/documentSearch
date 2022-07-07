package com.example.documentseach.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.documentseach.DocumentApplicationTest;
import com.example.documentseach.common.util.http.HttpUtil;
import com.example.documentseach.persistent.dao.es.IndexESDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class IndexESDAOTest extends DocumentApplicationTest {

    @Autowired
    private IndexESDAO indexESDAO;

    @Test
    public void directHttpGetEsWithDslTest() {
        StringBuilder builder = new StringBuilder();
        builder.append("{").append("\"query\"").append(":").append("{").append("\"match_all\"").append(":{}").append("}").append("}");
        String response = indexESDAO.directHttpGetEsWithDsl("http://120.26.69.19:9200/_search", builder.toString());
        Assertions.assertNotNull(response);
    }
}
