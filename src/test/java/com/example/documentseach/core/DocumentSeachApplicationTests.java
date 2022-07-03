package com.example.documentseach.core;

import com.example.documentseach.common.util.ESUtil;
import com.example.documentseach.persistent.client.ESOpClient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class DocumentSeachApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(ESOpClient.class.getName());

    @Autowired
    private ESUtil esUtil;

    @Test
    public void logLoads() {
        LOGGER.error("test");
    }

    @Test
    public void esTest() throws IOException {
        String indexName = "wpktest";
        esUtil.createIndex(indexName);
        esUtil.deleteIndex(indexName);
    }

}
