package com.luhong.locwithlibrary.net.request;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

public class HttpsUtils {

    public static OkHttpClient.Builder getHttpsOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.hostnameVerifier(getUnSafeHostnameVerifier());
        return builder;
    }

    private static class UnSafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static UnSafeHostnameVerifier getUnSafeHostnameVerifier() {
        return new UnSafeHostnameVerifier();
    }
}
