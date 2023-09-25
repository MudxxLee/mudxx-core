package com.mudxx.common.utils.http.okhttp;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * @author laiw
 * @date 2023/9/21 15:24
 */
public class OkHttpClientFactory {

    /**
     * 创建Builder对象
     *
     * @return Builder
     */
    public static OkHttpClient.Builder createDefaultBuilder() {
        return new OkHttpClient().newBuilder();
    }

    /**
     * 创建Builder对象
     *
     * @param connectTimeout 设置超时时间(秒)
     * @param readTimeout    设置读取超时时间(秒)
     * @param writeTimeout   设置写入超时时间(秒)
     * @return Builder
     */
    public static OkHttpClient.Builder createBuilder(long connectTimeout, long readTimeout, long writeTimeout) {
        return OkHttpClientFactory.createDefaultBuilder()
                // 设置超时时间
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                // 设置读取超时时间
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                // 设置写入超时时间
                .writeTimeout(writeTimeout, TimeUnit.SECONDS);
    }

    /**
     * 创建client对象 创建调用的工厂类 具备了访问http的能力
     *
     * @return OkHttpClient
     */
    public static OkHttpClient createDefaultClient() {
        return OkHttpClientFactory.createDefaultBuilder().build();
    }

}
