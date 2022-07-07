package com.example.documentseach.persistent.dao.es;

import com.example.documentseach.common.util.http.HttpUtil;
import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;
import com.example.documentseach.persistent.client.ESOpClient;
import com.google.common.collect.Maps;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author wangpengkai
 */
public class BaseESDAO {

    private static final KLog LOGGER = LoggerFactory.getLog(BaseESDAO.class);

    @Autowired
    private ESOpClient esOpClient;

    protected RestHighLevelClient client;

    @PostConstruct
    public void init() {
        client = esOpClient.getClient();
    }

    /**
     * 直接以http的形式发送get请求,没有走client封装接口
     * @param url
     * @return
     */
     public String directHttpGetEsWithOutDsl(String url) {
        try {
            return HttpUtil.get(url, null);
        } catch (Exception e) {
            LOGGER.error("class=BaseESDAO||method=directHttpGetES||errMsg={}", e.getMessage());
            return null;
        }
    }

    /**
     * 直接以http的形式发送请求,没有走client封装请求,主要是保留dsl使用的灵活性
     *
     * @param url
     * @param dsl
     * @return
     */
    public String directHttpGetEsWithDsl(String url, String dsl) {
        try {
            return HttpUtil.postForString(url, dsl, buildHeader());
        } catch (Exception e) {
            LOGGER.error("class=BaseESDAO||method=directHttpGetES||errMsg={}", e.getMessage());
            return null;
        }
    }

    private Map<String, String> buildHeader() {
        Map<String, String> headers = Maps.newHashMap();
        headers.put("Content-Type", "application/json");
        headers.put("charset", "utf-8");
        return headers;
    }
}
