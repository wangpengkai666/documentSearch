package com.example.documentseach;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = App.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DocumentApplicationTest {

    protected HttpHeaders headers;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    public void setUp() {
        // 获取 springboot server 监听的端口号
        // port = applicationContext.getWebServer().getPort();
        System.out.println(String.format("port is : [%d]", port));

        headers = new HttpHeaders();
        headers.add("X-SSO-USER", "zhaoqingrong");
    }

    @Test
    public void test() {
        Assertions.assertNotNull(port);
    }
}
