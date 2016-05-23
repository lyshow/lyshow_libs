
package com.libs.my_libs.framework.http;


import android.text.TextUtils;

import com.libs.my_libs.framework.base.AppException;
import com.libs.my_libs.framework.base.LogUtil;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * HTTP请求客户端
 */
public class KGHttpClient {

    private final static String TAG = "KGHttpClient";

    // 最大重试次数
    private final static int MAX_RETRY_NUM = 1;

    private KGHttpClient() {
    }

    /**
     * 发起网络请求，并读取服务器返回的数据
     * 
     * @param requestPackage
     * @param responsePackage
     * @throws AppException
     */
    public static void request(RequestPackage requestPackage,
            ResponsePackage<Object> responsePackage) throws AppException {
        request(requestPackage, responsePackage, true);
    }

    /**
     * 发起网络请求，并读取服务器返回的数据
     * 
     * @param requestPackage
     * @param responsePackage
     * @param isRetry 是否重试
     * @throws AppException
     */
    public static void request(RequestPackage requestPackage,
            ResponsePackage<Object> responsePackage, boolean isRetry) throws AppException {
        HttpClient httpClient = null;
        HttpUriRequest uriReq = null;

        int tryNum = 0;
        do {
            try {
                httpClient = createHttpClient(requestPackage);
                uriReq = createHttpUriRequest(requestPackage);
                HttpResponse httpResponse = httpClient.execute(uriReq);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED
                        && statusCode != HttpStatus.SC_NO_CONTENT
                        && statusCode != HttpStatus.SC_PARTIAL_CONTENT) {
                    throw AppException.http(statusCode);
                }
                if (responsePackage != null) {
                    responsePackage.setContext(EntityUtils.toByteArray(httpResponse.getEntity()));
                }
                break;
            } catch (Exception e) {
                tryNum++;
                if (tryNum < MAX_RETRY_NUM) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // 发生网络异常
                e.printStackTrace();
                throw AppException.network(e);
            } finally {
                // 释放连接
                if (httpClient != null) {
                    // 释放连接
                    httpClient.getConnectionManager().shutdown();
                }
                httpClient = null;
            }
        } while (isRetry && tryNum < MAX_RETRY_NUM);
    }

    /**
     * 文件下载
     * 
     * @param requestPackage
     * @param downloadListener
     * @throws AppException
     */
    public static void download(RequestPackage requestPackage, IDownloadListener downloadListener)
            throws AppException {
        HttpClient httpClient = null;
        HttpUriRequest uriReq = null;

        long fileSize = 0;
        try {
            httpClient = createHttpClient(requestPackage);
            uriReq = createHttpUriRequest(requestPackage);
            HttpResponse httpResponse = httpClient.execute(uriReq);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED
                    && statusCode != HttpStatus.SC_NO_CONTENT
                    && statusCode != HttpStatus.SC_PARTIAL_CONTENT) {
                throw AppException.http(statusCode);
            }

            fileSize = httpResponse.getEntity().getContentLength();
            BufferedInputStream bis = new BufferedInputStream(httpResponse.getEntity().getContent());
            byte[] data = new byte[8 * 1024];
            long haveRead = 0;
            int read = 0;
            int progress = 0;
            while ((read = bis.read(data)) != -1) {
                haveRead += read;
                if (fileSize > 0) {
                    progress = (int) ((float) haveRead / fileSize * 100);
                }
                downloadListener.onProgressChanged(data, 0, read, progress);
            }
        } catch (IOException e) {
            // 发生网络异常
            // e.printStackTrace();
            throw AppException.network(e);
        } catch (Exception e) {
            // 保存数据时异常
            // e.printStackTrace();
            throw AppException.run(e);
        } finally {
            if (httpClient != null) {
                // 释放连接
                httpClient.getConnectionManager().shutdown();
            }
            httpClient = null;
        }
    }

    // 创建一个HttpClient对象
    private static HttpClient createHttpClient(RequestPackage requestPackage) {
        HttpClient client = new DefaultHttpClient();
        HttpParams httpParams = client.getParams();
        httpParams.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);

        Hashtable<String, Object> params = requestPackage.getSettings();
        if (params != null) {
            if (params.containsKey("conn-timeout")) {
                // 设置 连接超时时间
                HttpConnectionParams.setConnectionTimeout(httpParams,
                        (Integer) params.get("conn-timeout"));
            }
            if (params.containsKey("socket-timeout")) {
                // 设置 读数据超时时间
                HttpConnectionParams.setSoTimeout(httpParams,
                        (Integer) params.get("socket-timeout"));
            }
        }

        return client;
    }

    // 创建一个HttpUriRequest对象
    private static HttpUriRequest createHttpUriRequest(RequestPackage requestPackage)
            throws AppException {
        String prevUrl = requestPackage.getUrl();
        String params = requestPackage.getGetRequestParams();
        LogUtil.d(TAG, prevUrl + params);

        HttpUriRequest uriReq = null;
        if (requestPackage.getRequestType() == RequestPackage.TYPE_GET) {
            uriReq = new HttpGet(prevUrl + params);
        } else {
            uriReq = new HttpPost(prevUrl);
            // uriReq.setHeader("Content-Type", "multipart/form-data");
            ((HttpPost) uriReq).setEntity(requestPackage.getPostRequestEntity());
        }

        Hashtable<String, Object> settings = requestPackage.getSettings();
        if (settings != null) {
            if (settings.containsKey("socket-timeout")) {
                // 设置 读数据超时时间
                HttpConnectionParams.setSoTimeout(uriReq.getParams(),
                        (Integer) settings.get("socket-timeout"));
            }
        }

        // 设置头
        Hashtable<String, String> headers = requestPackage.getRequestHeaders();
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                if (!"Connection".equalsIgnoreCase(key) && !"User-Agent".equalsIgnoreCase(key)) {
                    // 屏弊自定义Connection和User-Agent
                    uriReq.setHeader(key, headers.get(key));
                }
            }
        }
        uriReq.setHeader("Connection", "Keep-Alive");
        uriReq.setHeader("User-Agent", getUserAgent());

        return uriReq;
    }

    private static String sUserAgent;

    public static String getUserAgent() {
        if (TextUtils.isEmpty(sUserAgent)) {
            StringBuilder ua = new StringBuilder();
            // 平台
            ua.append("Android");
            // 系统版本
            ua.append("/" + android.os.Build.VERSION.RELEASE);
            // 手机型号
            ua.append("/" + android.os.Build.MODEL);
            sUserAgent = ua.toString();
        }
        return sUserAgent;
    }

    /**
     * 下载回调
     */
    public interface IDownloadListener {

        void onProgressChanged(byte[] data, int offset, int length, int progress);
    }

}
