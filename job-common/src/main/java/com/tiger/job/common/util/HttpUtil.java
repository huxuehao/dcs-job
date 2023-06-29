package com.tiger.job.common.util;

import com.alibaba.fastjson2.JSON;
import com.tiger.job.common.constant.HttpProperties;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http请求工具集
 *
 * @author HuXuehao (StudiousTiger)
 * @desc 这个类的作用是封装HttpClient
 * @date 2023/01/04
 */
@Component
public class HttpUtil {
    static HttpProperties commonProperties;

    /**
     * 连接管理
     */
    private static PoolingHttpClientConnectionManager connectionManager = null;

    /**
     * 请求配置
     */
    private static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(commonProperties.getSocketTimeout())
            .setConnectTimeout(commonProperties.getConnectTimeout())
            .setConnectionRequestTimeout(commonProperties.getConnectionRequestTimeout())
            .build();
    static {
        try {
            SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
            // 加载信任的材料，设置信任所有的策略
            sslContextBuilder.loadTrustMaterial(null, new TrustAllStrategy());
            // 构建sslcontext
            SSLContext sslcontext = sslContextBuilder.build();
            Registry<ConnectionSocketFactory> registry =
                    // 绕过 https 协议
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.INSTANCE)
                            .register("https",
                                    new SSLConnectionSocketFactory(
                                            sslcontext,
                                            new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"},
                                            null,
                                            NoopHostnameVerifier.INSTANCE
                                    )
                            ) // 支持的协议 "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"
                            .build();
            // 创建连接管理员
            connectionManager = new PoolingHttpClientConnectionManager(registry);
            // 设置最大连接数
            connectionManager.setMaxTotal(1000);
            // 每个路由最大的请求数量
            connectionManager.setDefaultMaxPerRoute(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HttpUtil(HttpProperties commonProperties) {
        this.commonProperties = commonProperties;
    }

    /**
     * 获取getHttpClient
     *
     * @return 可关闭的httpClient连接
     */
    private CloseableHttpClient getHttpClient() {
        return getHttpClientBuilder().build();
    }


    /**
     * 获取ssl类型的getHttpClient
     *
     * @param sslContext
     * @return 可关闭的httpClient连接
     */
    private CloseableHttpClient getHttpClient(SSLContext sslContext) {
        return getHttpClientBuilder(sslContext).build();
    }

    /**
     * HttpClient 构造器
     *
     * @param sslContext
     * @return httpClient构造器
     */
    private HttpClientBuilder getHttpClientBuilder(SSLContext sslContext) {
        if (sslContext != null) {
            return getHttpClientBuilder().setSSLContext(sslContext);
        } else {
            return getHttpClientBuilder();
        }
    }


    /**
     * HttpClient 构造器
     *
     * @return httpClient构造器
     */
    private HttpClientBuilder getHttpClientBuilder() {
        return HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig);
    }


    /**
     * 发送简单的get请求
     *
     * @param url 基路径
     * @return 请求结果
     */
    public Object sendGet(String url) throws IOException {
        return sendGet(url, null);
    }

    /**
     * 发送带有请求参数的get请求
     *
     * @param url    基路径
     * @param params 请求参数
     * @return 请求结果
     */
    public Object sendGet(String url, Map<String, String> params) throws IOException {
        return sendGet(url, params, false);

    }

    /**
     * 发送带有请求参数的get请求,并且参数进行urlEncode
     *
     * @param url       基路径
     * @param params    请求参数
     * @param urlEncode 是否urlEncode编码
     * @return 请求结果
     */
    public Object sendGet(String url,
                          Map<String, String> params,
                          Boolean urlEncode) throws IOException {
        return sendGet(url, params, null, urlEncode);
    }

    /**
     * 发送带有请求头和请求参数的get请求
     *
     * @param url    基路径
     * @param params 请求参数
     * @param heads  请求头
     * @return 请求结果
     */
    public Object sendGet(String url,
                          Map<String, String> params,
                          Map<String, String> heads) throws IOException {
        return sendGet(url, params, heads, false);
    }

    /**
     * 发送带有请求头和请求参数的get请求，并且参数进行urlEncode
     *
     * @param url       基路径
     * @param params    请求参数
     * @param headers   请求头
     * @param urlEncode 是否urlEncode编码
     * @return 请求结果
     */
    public Object sendGet(String url,
                          Map<String, String> params,
                          Map<String, String> headers,
                          Boolean urlEncode) throws IOException {
        // 添加参数
        try {
            url = setGetParams(url, params, urlEncode);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        // 可关闭的响应对象
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            // 可关闭的http客户端（浏览器）
            CloseableHttpClient closeableHttpClient = getHttpClient();
            // 构造httpGet请求对象
            HttpGet httpGet = new HttpGet(url);
            // 添加请求头
            httpGet = setGetHeaders(httpGet, headers);

            // 执行
            response = closeableHttpClient.execute(httpGet);
            // 获取响应实体
            entity = response.getEntity();
            return JSON.parseObject(EntityUtils.toString(entity, StandardCharsets.UTF_8));
        } finally {
            try {
                if (entity != null) {
                    // 会自动释放连接
                    EntityUtils.consumeQuietly(entity);
                }
                // 关闭响应对象
                if (response != null) {
                    response.close();
                }
                // 关闭客户端
                /*if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /***
     * 拼接Get请求参数,并做URLEncoder编码
     *
     * @param url 基路径
     * @param params 请求参数
     * @param urlEncoder 是否进行urlEncoder编码
     * @return 请求全路径
     */
    private String setGetParams(String url,
                                Map<String, String> params,
                                Boolean urlEncoder)
            throws UnsupportedEncodingException {
        if (isEmptyMap(params)) {
            return url;
        }
        StringBuffer paramsLink = new StringBuffer();
        paramsLink.append(url).append("?");

        if (urlEncoder) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramsLink.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()))
                        .append("&");
            }
        } else {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramsLink.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
        }
        return paramsLink.toString().substring(0, paramsLink.toString().length() - 1);
    }

    /**
     * 添加Get请求头
     *
     * @param httpGet HttpGet
     * @param map     请求头
     * @return 添加请求头的 HttpGet
     */
    private HttpGet setGetHeaders(HttpGet httpGet,
                                  Map<String, String> map) {
        if (!isEmptyMap(map)) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return httpGet;
    }

    /**
     * 发送简单的post请求
     *
     * @param url 请求基路径
     * @return 请求结果
     */
    public Object sendJsonPost(String url) throws IOException {
        return sendJsonPost(url, null, null);
    }

    /**
     * 发送带有请求体的post请求
     *
     * @param url  请求基路径
     * @param body 请求体
     * @return 请求结果
     */
    public Object sendJsonPost(String url,
                               Object body) throws IOException {
        return sendJsonPost(url, body, null);
    }

    /**
     * 发送带有请求头和请求体post请求
     *
     * @param url   基路径
     * @param body  请求体
     * @param heads 请求头
     * @return 请求结果
     */
    public Object sendJsonPost(String url,
                               Object body,
                               Map<String, String> heads) throws IOException {
        // 可关闭的响应对象
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            // 可关闭的http客户端（浏览器）
            CloseableHttpClient closeableHttpClient = getHttpClient();
            // 构造httpPost请求对象
            HttpPost httpPost = new HttpPost(url);
            // 添加请求体
            httpPost = setPostBody(httpPost, body);
            // 添加请求头
            httpPost = setPostHeaders(httpPost, heads);
            // 执行
            response = closeableHttpClient.execute(httpPost);
            // 获取响应实体
            entity = response.getEntity();
            return JSON.parseObject(EntityUtils.toString(entity, StandardCharsets.UTF_8));
        }
        finally {
            try {
                if (entity != null) {
                    // 会自动释放连接
                    EntityUtils.consumeQuietly(entity);
                }
                // 关闭响应对象
                if (response != null) {
                    response.close();
                }
                // 关闭客户端
                /*if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }*/
            } catch (IOException e) {
                return null;
            }
        }
    }

    /**
     * 设置post请求体
     *
     * @param httpPost httpPost对象
     * @param body     请求体
     * @return 封装后的HttpPost对象
     */
    private HttpPost setPostBody(HttpPost httpPost,
                                 Object body) {
        if (body != null) {
            httpPost.addHeader("Content-Type", "application/json;charset=utf8");
            StringEntity jsonEntity = new StringEntity(JSON.toJSONString(body), StandardCharsets.UTF_8);
            /*jsonEntity.setContentType(new BasicHeader("Content-Type", "application/json; charset=utf-8"));
            jsonEntity.setContentEncoding(StandardCharsets.UTF_8.name());*/
            httpPost.setEntity(jsonEntity);
        }
        return httpPost;
    }

    /**
     * 发送简单的post请求
     *
     * @param url 基路径
     * @return 请求结果
     */
    public Object sendFormPost(String url) throws IOException {
        return sendFormPost(url, null);
    }

    /**
     * 发送带有请求体post请求
     *
     * @param url    基路径
     * @param params 请求参数
     * @return 请求结果
     */
    public Object sendFormPost(String url,
                               Map<String, String> params) throws IOException {
        return sendFormPost(url, params, null);
    }

    /**
     * 发送带有请求头和请求体post请求
     *
     * @param url    基路径
     * @param params 请求参数
     * @param heads  请求头
     * @return 请求结果
     */
    public Object sendFormPost(String url,
                               Map<String, String> params,
                               Map<String, String> heads) throws IOException {
        // 可关闭的响应对象
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            // 可关闭的http客户端（浏览器）
            CloseableHttpClient closeableHttpClient = getHttpClient();
            // 构造httpPost请求对象
            HttpPost httpPost = new HttpPost(url);
            // 添加请求体
            httpPost = setPostParams(httpPost, params);
            // 添加请求头
            httpPost = setPostHeaders(httpPost, heads);
            // 执行
            response = closeableHttpClient.execute(httpPost);
            // 获取响应实体
            entity = response.getEntity();
            return JSON.parseObject(EntityUtils.toString(entity, StandardCharsets.UTF_8));
        }
        finally {
            try {
                if (entity != null) {
                    // 会自动释放连接
                    EntityUtils.consumeQuietly(entity);
                }
                // 关闭响应对象
                if (response != null) {
                    response.close();
                }
                // 关闭客户端
                /*if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }*/
            } catch (IOException e) {
                return null;
            }
        }
    }

    /**
     * 设置post请求参数
     *
     * @param httpPost httpPost请求对象
     * @param map      参数map
     * @return 封装好的httpPost请求对象
     */
    private HttpPost setPostParams(HttpPost httpPost,
                                   Map<String, String> map) {
        if (!isEmptyMap(map)) {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            for (Map.Entry<String, String> m : map.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(m.getKey(), m.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8));
        }
        return httpPost;
    }

    /**
     * 添加Get请求头
     *
     * @param httpPost HttpPost
     * @param map      请求头
     * @return 添加请求头的 HttpGet
     */
    private HttpPost setPostHeaders(HttpPost httpPost,
                                    Map<String, String> map) {
        if (!isEmptyMap(map)) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return httpPost;
    }

    /**
     * Get请求实现文件下载
     *
     * @param url 请求基路径
     * @return
     */
    public byte[] fileDownload(String url) throws IOException {
        return fileDownload(url, null);
    }

    /**
     * Get请求实现文件下载
     *
     * @param url     请求基路径
     * @param headers 请求头
     * @return byte[]
     */
    public byte[] fileDownload(String url,
                               Map<String, String> headers) throws IOException {
        // 可关闭的响应对象
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            CloseableHttpClient closeableHttpClient = getHttpClient();
            HttpGet httpGet = new HttpGet(url);
            // 添加请求头
            httpGet = setGetHeaders(httpGet, headers);
            // 执行
            response = closeableHttpClient.execute(httpGet);
            // 获取响应实体
            entity = response.getEntity();
            return EntityUtils.toByteArray(entity);
        }
        finally {
            try {
                if (entity != null) {
                    // 会自动释放连接
                    EntityUtils.consumeQuietly(entity);
                }
                // 关闭响应对象
                if (response != null) {
                    response.close();
                }
                // 关闭客户端
                /*if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }*/
            } catch (IOException e) {
                return null;
            }
        }
    }

    /**
     * 发送带有文件请求体post请求
     *
     * @param url     请求基路径
     * @param fileMap 文件map，key为 RequestParam，value 为 FileInputStream
     * @return 请求结果
     */
    public String fileUpload(String url,
                             Map<String, FileInputStream> fileMap) throws IOException {
        return fileUpload(url, fileMap, null);
    }

    /**
     * 发送带有请求头和文件请求体post请求
     *
     * @param url     基路径
     * @param fileMap 请求体
     * @param heads   请求头
     * @return 请求结果
     */
    public String fileUpload(String url,
                             Map<String, FileInputStream> fileMap,
                             Map<String, String> heads) throws IOException {
        // 可关闭的响应对象
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            // 可关闭的http客户端（浏览器）
            CloseableHttpClient closeableHttpClient = getHttpClient();
            // 构造httpPost请求对象
            HttpPost httpPost = new HttpPost(url);
            // 设置文件请求体
            httpPost = setFileEntity(httpPost, fileMap);
            // 添加请求头
            httpPost = setPostHeaders(httpPost, heads);
            // 执行
            response = closeableHttpClient.execute(httpPost);
            // 获取响应实体
            entity = response.getEntity();
            // 获取字符串
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        }
        finally {
            try {
                if (entity != null) {
                    // 会自动释放连接
                    EntityUtils.consumeQuietly(entity);
                }
                // 关闭响应对象
                if (response != null) {
                    response.close();
                }
                // 关闭客户端
                /*if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }*/
            } catch (IOException e) {
                return null;
            }
        }
    }

    /**
     * 设置fileEntity
     *
     * @param httpPost httpPost对象
     * @param fileMap  fileMap集合
     * @return 封装后的httpPost对象
     */
    private HttpPost setFileEntity(HttpPost httpPost,
                                   Map<String, FileInputStream> fileMap) {
        if (fileMap != null && fileMap.size() > 0) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Consts.UTF_8);
            builder.setContentType(ContentType.create("multipart/form-data", Consts.UTF_8));
            // 设置浏览器模式
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            // 循环设置文件请求体
            for (Map.Entry<String, FileInputStream> map : fileMap.entrySet()) {
                builder = builder.addBinaryBody(map.getKey(), map.getValue());
            }
            httpPost.setEntity(builder.build());
        }
        return httpPost;
    }

    /**
     * 判断 Map<String,String> 是否为空
     *
     * @param map
     * @return true/false
     */
    private Boolean isEmptyMap(Map<String, String> map) {
        return map == null || map.size() == 0;
    }

}
