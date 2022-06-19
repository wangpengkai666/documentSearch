package com.example.documentseach.persistent.client;


import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * 获取ES的client实例，目前暂时是只连接一个es集群，后续为了数据的扩展可以考虑多个es集群的接入
 *
 * @author wangpengkai
 */

@Configuration
@Data
public class ESOpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ESOpClient.class.getName());

    @Value("${es.url}")
    String esUrl;
    @Value("${es.port}")
    int esPort;

    /**
     * 连接的范式,http/https
     */
    private String schema = "http";

    /**
     * 设置连接时间
     */

    private int connectTimeOut = 2000;

    /**
     * socket连接时间
     */
    private int socketTimeOut = 30000;

    /**
     * 连接超时时间
     */
    private int connectionRequestTimeOut = 10000;

    /**
     * 一次最多接收请求
     */
    private int maxConnectNum = 100;

    /**
     * 某一个服务每次能并行接收的请求数量
     */
    private int maxConnectPerRoute = 100;

    /**
     * 是否异步延迟时间设置
     */
    private boolean uniqueConnectTimeConfig = true;

    /**
     * 是否设置连接线程数目
     */
    private boolean uniqueConnectNumConfig = true;

    private HttpHost httpHost;
    private RestClientBuilder builder;
    private RestHighLevelClient client;


    /**
     * es-client初始化连接
     */
    @PostConstruct
    public void init() {
        httpHost = new HttpHost(esUrl, esPort, schema);
        builder = RestClient.builder(httpHost);
        // 设置连接时间
        if (uniqueConnectTimeConfig) {
            setConnectTimeOutConfig();
        }
        // 设置连接数
        if (uniqueConnectNumConfig) {
            setMutiConnectConfig();
        }

        client = new RestHighLevelClient(builder);
    }

    /**
     * 异步httpclient的连接延迟配置
     * 设置修改默认请求配置的回调（例如：请求超时，认证，或者其他设置
     */
    public void setConnectTimeOutConfig() {
        builder.setRequestConfigCallback(builder -> {
            builder.setConnectTimeout(connectTimeOut);
            builder.setSocketTimeout(socketTimeOut);
            builder.setConnectionRequestTimeout(connectionRequestTimeOut);
            return builder;
        });
    }

    /**
     * 线程设置
     */
    public void setMutiConnectConfig() {
        builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
            httpAsyncClientBuilder.setMaxConnTotal(maxConnectNum);
            httpAsyncClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            return httpAsyncClientBuilder;
        });
    }

    /**
     * 关闭es连接
     */
    public void close() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                LOGGER.error("关闭es连接异常");
            }
        }
    }

}
