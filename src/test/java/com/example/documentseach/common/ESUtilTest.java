package com.example.documentseach.common;

import com.example.documentseach.DocumentApplicationTest;
import com.example.documentseach.common.util.ESUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Set;

public class ESUtilTest extends DocumentApplicationTest {
    @Autowired
    private ESUtil esUtil;

    @Test
    public void getAllIndexTest() throws IOException {
        Set<String> allIndex = esUtil.getAllIndex();
        Assertions.assertNotNull(allIndex);
    }
}
