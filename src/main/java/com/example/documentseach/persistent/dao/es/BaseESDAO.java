package com.example.documentseach.persistent.dao.es;

import com.example.documentseach.persistent.client.ESOpClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author wangpengkai
 */
public class BaseESDAO {

    @Autowired
    private ESOpClient esOpClient;

    protected RestHighLevelClient client;

    @PostConstruct
    public void init() {
        client = esOpClient.getClient();
    }
}
