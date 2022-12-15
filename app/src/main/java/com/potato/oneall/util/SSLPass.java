package com.potato.oneall.util;

import com.potato.oneall.bean.SSLSocketManager;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class SSLPass {

    public static OkHttpClient sslPass(){
        return new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS) //连接超时
                .readTimeout(5000, TimeUnit.MILLISECONDS) //读取超时
                .writeTimeout(5000, TimeUnit.MILLISECONDS) //写入超时
                .sslSocketFactory(SSLSocketManager.getSSLSocketFactory(), new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                })//配置
                .hostnameVerifier(SSLSocketManager.getHostnameVerifier())//配置
                .build();
    }
}
