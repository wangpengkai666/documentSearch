package com.example.documentseach.common.util.http;

import com.example.documentseach.common.util.container.StringUtil;
import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.xcontent.XContentHelper;

import javax.swing.text.html.parser.Entity;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author wangpengkai
 */
public class HttpUtil {

    private static final KLog LOGGER = LoggerFactory.getLog(HttpUtil.class);
    public static final String UTF8 = "UTF-8";
    public static final String GBK = "GBK";
    public static final String GB2312 = "GB2312";

    private static final String COOKIE_POLICY = "http.protocol.cookie-policy";
    private static final String COMPATIBILITY = "compatibility";
    private static final String EXCEPTION_1 = "UTF-8 is not surportted";
    private static final String EXCEPTION_2 = "error post data to ";
    private static final String RESPONSE = "response=";

    private static final HttpClient HTTP_CLIENT;

    private HttpUtil() {}

    public static String post(String url, Map<String, Object> params) {
        return post(url, params, (Map)null, (String)null, (String)null);
    }

    public static String postEncode(String url, Map<String, Object> params, String reqEncode, String resEncode) {
        return post(url, params, (Map)null, reqEncode, resEncode);
    }

    public static String post(String url, Map<String, Object> params, Map<String, String> headers, String reqEncode, String resEncode) {
        HttpPost post = new HttpPost(url);
        if(StringUtil.isBlank(reqEncode)) {
            reqEncode = UTF8;
        }

        if(StringUtil.isBlank(resEncode)) {
            resEncode = UTF8;
        }

        List<BasicNameValuePair> httpParams;
        Iterator var7;
        if(params != null && !params.isEmpty()) {
            httpParams = new ArrayList<>(params.size());
            var7 = params.entrySet().iterator();

            while(true) {
                Map.Entry e;
                while(var7.hasNext()) {
                    e = (Map.Entry)var7.next();
                    String k = (String)e.getKey();
                    Object v = e.getValue();
                    if(v == null) {
                        httpParams.add(new BasicNameValuePair(k, (String)null));
                    } else if(!v.getClass().isArray()) {
                        httpParams.add(new BasicNameValuePair(k, v.toString()));
                    } else {
                        int len = Array.getLength(v);

                        for(int i = 0; i < len; ++i) {
                            Object element = Array.get(v, i);
                            if(element != null) {
                                httpParams.add(new BasicNameValuePair(k, element.toString()));
                            } else {
                                httpParams.add(new BasicNameValuePair(k, (String)null));
                            }
                        }
                    }
                }

                if(headers != null) {
                    var7 = headers.entrySet().iterator();

                    while(var7.hasNext()) {
                        e = (Map.Entry)var7.next();
                        post.addHeader((String)e.getKey(), (String)e.getValue());
                    }
                }

                try {
                    post.setEntity(new UrlEncodedFormEntity(httpParams, reqEncode));
                    post.getParams().setParameter(COOKIE_POLICY, COMPATIBILITY);
                    break;
                } catch (UnsupportedEncodingException var20) {
                    throw new RuntimeException();
                }
            }
        }

        var7 = null;

        String response;
        try {
            HttpEntity entity = HTTP_CLIENT.execute(post).getEntity();
            response = EntityUtils.toString(entity, resEncode);
        } catch (Exception var18) {
            throw new RuntimeException();
        } finally {
            post.releaseConnection();
        }

        return response;
    }

    public static String postForString(String url, String content, Map<String, String> headers) {
        HttpPost post = new HttpPost(url);

        Iterator<Map.Entry<String, String>> var4;
        if(StringUtil.isNotBlank(content)) {
            if(headers != null) {
                var4 = headers.entrySet().iterator();

                while(var4.hasNext()) {
                    Map.Entry<String, String> e = var4.next();
                    post.addHeader(e.getKey(), e.getValue());
                }
            }

            try {
                BasicHttpEntity requestBody = new BasicHttpEntity();
                requestBody.setContent(new ByteArrayInputStream(content.getBytes(UTF8)));
                requestBody.setContentLength((long)content.getBytes(UTF8).length);
                post.setEntity(requestBody);
                post.getParams().setParameter(COOKIE_POLICY, COMPATIBILITY);
            } catch (UnsupportedEncodingException var12) {
                throw new RuntimeException();
            }
        }

        var4 = null;

        String response;
        try {
            HttpEntity entity = HTTP_CLIENT.execute(post).getEntity();
            response = EntityUtils.toString(entity, UTF8);
            EntityUtils.consume(entity);
        } catch (Exception var10) {
            throw new RuntimeException();
        } finally {
            post.releaseConnection();
        }


        return response;
    }
    public static String deleteForString(String url, String content, Map<String, String> headers) {
        HttpDelete post = new HttpDelete(url);

        Iterator<Map.Entry<String, String>> var4;
        if(StringUtil.isNotBlank(content)) {
            if(headers != null) {
                var4 = headers.entrySet().iterator();

                while(var4.hasNext()) {
                    Map.Entry<String, String> e = var4.next();
                    post.addHeader(e.getKey(), e.getValue());
                }
            }

            try {
                BasicHttpEntity requestBody = new BasicHttpEntity();
                requestBody.setContent(new ByteArrayInputStream(content.getBytes(UTF8)));
                requestBody.setContentLength((long)content.getBytes(UTF8).length);
                post.getParams().setParameter(COOKIE_POLICY, COMPATIBILITY);
            } catch (UnsupportedEncodingException var12) {
                throw new RuntimeException();
            }
        }

        var4 = null;

        String response;
        try {
            HttpEntity entity = HTTP_CLIENT.execute(post).getEntity();
            response = EntityUtils.toString(entity, UTF8);
            EntityUtils.consume(entity);
        } catch (Exception var10) {
            throw new RuntimeException();
        } finally {
            post.releaseConnection();
        }

        return response;
    }

    public static String putForString(String url, String content, Map<String, String> headers) {
        HttpPut post = new HttpPut(url);

        Iterator<Map.Entry<String, String>> var4;
        if(StringUtil.isNotBlank(content)) {
            if(headers != null) {
                var4 = headers.entrySet().iterator();

                while(var4.hasNext()) {
                    Map.Entry<String, String> e = var4.next();
                    post.addHeader(e.getKey(), e.getValue());
                }
            }

            try {
                BasicHttpEntity requestBody = new BasicHttpEntity();
                requestBody.setContent(new ByteArrayInputStream(content.getBytes(UTF8)));
                requestBody.setContentLength((long)content.getBytes(UTF8).length);
                post.setEntity(requestBody);
                post.getParams().setParameter(COOKIE_POLICY, COMPATIBILITY);
            } catch (UnsupportedEncodingException var12) {
                throw new RuntimeException();
            }
        }

        var4 = null;

        String response;
        try {
            HttpEntity entity = HTTP_CLIENT.execute(post).getEntity();
            response = EntityUtils.toString(entity, UTF8);
            EntityUtils.consume(entity);
        } catch (Exception var10) {
            throw new RuntimeException();
        } finally {
            post.releaseConnection();
        }

        return response;
    }

    public static String get(String url, Map<String, String> params) {
        Iterator<Map.Entry<String, String>> var3;
        if(params != null) {
            StringBuilder builder = (new StringBuilder(url)).append('?');
            var3 = params.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry<String, String> e = (Map.Entry)var3.next();
                builder.append((String)e.getKey()).append('=').append((String)e.getValue()).append('&');
            }

            url = builder.toString();
        }

        HttpGet get = new HttpGet(url);

        String response;
        try {
            HttpEntity entity = HTTP_CLIENT.execute(get).getEntity();
            response = EntityUtils.toString(entity, UTF8);
        } catch (Exception var8) {
            throw new RuntimeException();
        } finally {
            get.releaseConnection();
        }


        return response;
    }

    public static String get(String url, Map<String, String> params, Map<String, String> headers) {
        if (params != null) {
            StringBuilder builder = new StringBuilder(url).append('?');
            for (Map.Entry<String, String> e : params.entrySet()) {
                builder.append(e.getKey()).append('=').append(e.getValue()).append('&');
            }
            url = builder.toString();
        }

        HttpGet get = new HttpGet(url);
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                get.addHeader(e.getKey(), e.getValue());
            }
        }

        String response = null;
        try {
            HttpEntity entity = HTTP_CLIENT.execute(get).getEntity();
            response = EntityUtils.toString(entity, UTF8);
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            get.releaseConnection();
        }

        return response;
    }

    public static Header buildHttpHeader(String appid, String passWord) {
        // ?????????????????????header
        Header header = null;
        try {
            header = new BasicHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(String.format("%s:%s", appid, passWord).getBytes(UTF8)));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("class=BaseHttpUtil||method=buildHttpHeader||appid={}||passWord={}||errMsg=encoding error",
                    appid, passWord, e);
        }

        return header;
    }

    static {
        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(50);
        HTTP_CLIENT = new DefaultHttpClient(connectionManager);
        HTTP_CLIENT.getParams().setParameter("http.connection.timeout", Integer.valueOf(20000));
        HTTP_CLIENT.getParams().setParameter("http.socket.timeout", Integer.valueOf(20000));
    }
}
