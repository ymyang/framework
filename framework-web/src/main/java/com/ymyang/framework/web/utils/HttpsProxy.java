//package cc.gigahome.common.utils;
//
//import java.io.IOException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.Header;
//import org.apache.http.HttpEntity;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.TrustStrategy;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.ssl.SSLContextBuilder;
//import org.apache.http.util.EntityUtils;
//
//@Slf4j
//public class HttpsProxy {
//
//    private static final String CHARSET_UTF8 = "UTF-8";
//    @SuppressWarnings("unused")
//    private static final String CHARSET_GBK = "GBK";
//
//    // cache开关，true则开启自身缓存
//    private static CloseableHttpClient httpClient;
//
//    public CloseableHttpClient getHttpClient() {
//        return httpClient;
//    }
//
//    private HttpsProxy() {
//        httpClient = getCloseableHttpClient();
//    }
//
//    private static HttpsProxy instance = new HttpsProxy();
//
//    private Header[] cookieHeaders = new Header[] {};
//    // 私有构造函数，单例
//
//    // 对外开放的获取类的单独实例的接口
//    public static HttpsProxy getProxy() {
//        return instance;
//    }
//
//
//    public String get(String url, Map<String, String> headers, Map<String, String> paramsMap, String charset) throws IOException {
//        if (url == null || url.isEmpty()) {
//            return null;
//        }
//        charset = (charset == null ? CHARSET_UTF8 : charset);
//
//        List<NameValuePair> params = getParamsList(paramsMap);
//        CloseableHttpResponse response = null;
//        String res = null;
//        try {
//            URIBuilder builder = new URIBuilder(url);
//            builder.setParameters(params);
//            HttpGet get = new HttpGet(builder.build());
//            if (cookieHeaders != null) {
//                for (Header header : cookieHeaders) {
//                    get.addHeader(header);
//                }
//            }
//
//            if (headers != null) {
//                for (String headerKey: headers.keySet()) {
//                    get.setHeader(headerKey, headers.get(headerKey));
//                }
//            }
//            response = httpClient.execute(get);
//            HttpEntity entity = response.getEntity();
//
//            log.info("GET: {}", url);
//            res = EntityUtils.toString(entity, charset);
//        } catch (Exception e) {
//            log.error("", e);
//        } finally {
//            if (response != null) {
//                response.close();
//            }
//
//        }
//        return res;
//    }
//
//    /**
//     * 对应cookie是单独设置的网站，通过某些办法把cookie的头信息提取出来，然后传到这里
//     *
//     * @param url
//     * @param charset
//     * @param cookie
//     * @return
//     * @throws IOException
//     */
//    public String get(String url, String charset, String cookie) throws IOException {
//        if (url == null || url.isEmpty()) {
//            return null;
//        }
//
//        charset = (charset == null ? CHARSET_UTF8 : charset);
//        HttpGet get = new HttpGet(url);
//        if (cookieHeaders != null && cookieHeaders.length > 0) {
//            for (Header header : cookieHeaders) {
//                get.addHeader(header);
//            }
//        }
//        get.addHeader("cookie", cookie);
//        CloseableHttpResponse response = null;
//        String res = null;
//        try {
//            response = httpClient.execute(get);
//            HttpEntity entity = response.getEntity();
//            log.info("GET: {}",url);
//            res = EntityUtils.toString(entity, charset);
//        } catch (IOException e) {
//            log.warn("", e);
//        } finally {
//            if (response != null) {
//                response.close();
//            }
//        }
//        return res;
//    }
//
//    /**
//     * 只传一个网址的get
//     *
//     * @param url
//     * @return
//     * @throws IOException
//     */
//    public String get(String url) throws IOException {
//        return get(url, CHARSET_UTF8, "");
//    }
//
//    public String post(String url, Map<String, String> map) throws IOException {
//        return post(url, map, null);
//    }
//
//    /**
//     * post方法，如果需要cookie则用getCookie方法设置
//     *
//     * @param url String
//     * @param paramsMap Map
//     * @param charset String
//     * @return String
//     * @throws IOException
//     */
//    public String post(String url, Map<String, String> paramsMap, String charset) throws IOException {
//        if (url == null || url.isEmpty()) {
//            return null;
//        }
//        CloseableHttpResponse response = null;
//        List<NameValuePair> params = getParamsList(paramsMap);
//        String res = null;
//        try {
//            charset = (charset == null ? CHARSET_UTF8 : charset);
//            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, charset);
//            HttpPost post = new HttpPost(url);
//            post.setEntity(formEntity);
//            response = httpClient.execute(post);
//            res = EntityUtils.toString(response.getEntity());
//
//        }
//        catch (IOException e) {
//            log.error("", e);
//        }
//        finally {
//            if (response != null) {
//                response.close();
//            }
//        }
//
//        return res;
//    }
//
//    /**
//     * @param url String
//     * @param paramsMap Map
//     * @param headers Map
//     * @param charset String
//     * @return String
//     * @throws IOException
//     */
//    public String post(String url, Map<String, String> paramsMap, Map<String, String> headers, String charset) throws IOException {
//        if (url == null || url.isEmpty()) {
//            return null;
//        }
//        List<NameValuePair> params = getParamsList(paramsMap);
//        CloseableHttpResponse response = null;
//        String res = null;
//        try {
//            charset = (charset == null ? CHARSET_UTF8 : charset);
//            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, charset);
//            HttpPost post = new HttpPost(url);
//
//            post.setEntity(formEntity);
//            if (cookieHeaders != null && cookieHeaders.length > 0) {
//                for (Header header : cookieHeaders) {
//                    post.addHeader(header);
//                }
//            }
//            // add Header authentication
//            for (String headerKey: headers.keySet()) {
//                post.setHeader(headerKey, headers.get(headerKey));
//            }
//
//            response = httpClient.execute(post);
//            res = EntityUtils.toString(response.getEntity());
//
//        } catch (IOException e) {
//            log.error("", e);
//        }
//        finally {
//            if (response != null) {
//                response.close();
//            }
//        }
//
//        return res;
//    }
//
//
//    /**
//     * @param url String
//     * @param data String
//     * @param charset String
//     * @return
//     * @throws IOException
//     */
//    public String postXml(String url, String data, String charset) throws IOException {
//        if (url == null || url.isEmpty()) {
//            return null;
//        }
//
//        charset = (charset == null ? CHARSET_UTF8 : charset);
//
//        HttpPost post = null;
//        CloseableHttpResponse response = null;
//        String res = null;
//        try {
//
//            post = new HttpPost(url);
//            post.addHeader("Content-Type", "text/xml");
//            StringEntity postEntity = new StringEntity(data, charset);
//            post.setEntity(postEntity);
//            response = httpClient.execute(post);
//            HttpEntity httpEntity = response.getEntity();
//            res = EntityUtils.toString(httpEntity, "UTF-8");
//
//        }
//        catch (IOException e) {
//            log.error("", e);
//        }
//        finally {
//            if (response != null) {
//                response.close();
//            }
//        }
//
//        return res;
//    }
//
//    /**
//     *
//     * @return CloseableHttpClient
//     */
//    private CloseableHttpClient getCloseableHttpClient() {
//        return HttpClients.createDefault();
//
//    }
//
//    private CloseableHttpClient getSSLCloseableHttpClient() throws Exception {
//        return HttpClients
//                .custom()
//                .setSSLSocketFactory(ssl())
//                .build();
//    }
//
//
//    private static SSLConnectionSocketFactory ssl() throws Exception {
//        return new SSLConnectionSocketFactory(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
//            @Override
//            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
//                return true;
//            }
//        }).build());
//    }
//
//    public void closeClient() throws IOException {
//        httpClient.close();
//    }
//
//    /**
//     * 将传入的键/值对参数转换为NameValuePair参数集
//     *
//     * @param paramsMap 参数集, 键/值对
//     * @return NameValuePair参数集
//     */
//    private List<NameValuePair> getParamsList(Map<String, String> paramsMap) {
//        if (paramsMap == null || paramsMap.size() == 0) {
//            return new ArrayList<NameValuePair>();
//        }
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        for (Map.Entry<String, String> map : paramsMap.entrySet()) {
//            params.add(new BasicNameValuePair(map.getKey(), map.getValue()));
//        }
//
//        return params;
//    }
//
//    /**
//     * 根据传入参数设置cookie
//     *
//     * @param url
//     * @param paramsMap
//     * @param charset
//     * @return
//     * @throws IOException
//     */
//    public void getCookie(String url, Map<String, String> paramsMap, String charset) throws IOException{
//
//        if (url == null || url.isEmpty()) {
//            return;
//        }
//        // 如果传入编码则使用传入的编码，否则utf8
//        charset = (charset == null ? CHARSET_UTF8 : charset);
//        // 将map转成List<NameValuePair>
//        List<NameValuePair> params = getParamsList(paramsMap);
//        UrlEncodedFormEntity entity = null;
//        HttpPost post = null;
//        CloseableHttpResponse response = null;
//
//        try {
//            entity = new UrlEncodedFormEntity(params, charset);
//            post = new HttpPost(url);
//            // 设置post的参数
//            post.setEntity(entity);
//            response = httpClient.execute(post);
//            // 保存response的名称为Set-Cookie的headers
//            cookieHeaders = response.getHeaders("Set-Cookie");
//            // cookie = set_cookie.substring(0, set_cookie.indexOf(";"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (response != null) {
//                response.close();
//            }
//        }
//    }
//
//    public void setCookies(Header[] headers) {
//        cookieHeaders = headers;
//    }
//}
